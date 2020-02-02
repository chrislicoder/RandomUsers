package com.urgentx.randomusers.repository

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.urgentx.randomusers.ioThenMain
import com.urgentx.randomusers.model.User
import com.urgentx.randomusers.model.UserDao
import com.urgentx.randomusers.service.UserService
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

interface IUsersRepository {
    fun users(): Observable<PagedList<User>>
    fun usersByName(query: String): Observable<PagedList<User>>
}

class UsersRepository(
    userService: UserService,
    private val userDao: UserDao,
    private val compositeDisposable: CompositeDisposable
) : IUsersRepository {

    private val boundaryCallback = UsersBoundaryCallback(
        webservice = userService,
        handleResponse = this::insertResultIntoDb,
        compositeDisposable = compositeDisposable
    )

    override fun users(): Observable<PagedList<User>> {
        return RxPagedListBuilder(userDao.getAll(), 100)
            .setBoundaryCallback(boundaryCallback)
            .buildObservable()
    }

    override fun usersByName(query: String): Observable<PagedList<User>> {
        return RxPagedListBuilder(userDao.getAllWithQuery("%$query%"), 100)
            .setBoundaryCallback(boundaryCallback)
            .buildObservable()
    }

    private fun insertResultIntoDb(users: List<User>) {
        val usersFixed = users.map {
            // Workaround for bug in GSON :(
            if (it.id.value == null) it.copy(id = it.id.copy(value = "")) else it
        }
        userDao.insert(usersFixed)
            .ioThenMain()
            .subscribe { }.addTo(compositeDisposable)
    }

    companion object {
        const val PAGE_SIZE = 100
    }
}

