package com.urgentx.randomusers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.urgentx.randomusers.model.User
import com.urgentx.randomusers.model.UserDao
import com.urgentx.randomusers.repository.IUsersRepository
import com.urgentx.randomusers.repository.UsersRepository
import com.urgentx.randomusers.service.UserService
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class UsersViewModel @Inject constructor(
    userService: UserService,
    userDao: UserDao,
    usersRepository: IUsersRepository? = null
) : ViewModel() {

    val usersList: Observable<PagedList<User>>

    private val compositeDisposable = CompositeDisposable()

    private val filters = PublishSubject.create<String>()
    fun filter(query: String) = filters.onNext(query)

    init {
        val repository = usersRepository ?: UsersRepository(userService, userDao, compositeDisposable)
        val source: Observable<Observable<PagedList<User>>> =
            Observable.combineLatest(repository.users(), filters, BiFunction { users, query ->
                if (query.isBlank()) repository.users() else repository.usersByName(query)
            })
        usersList = source.switchMap { it }
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}