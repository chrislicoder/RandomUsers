package com.urgentx.randomusers.repository

import androidx.annotation.MainThread
import androidx.paging.PagedList
import com.urgentx.randomusers.ioThenMain
import com.urgentx.randomusers.model.User
import com.urgentx.randomusers.service.UserService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class UsersBoundaryCallback(
        private val webservice: UserService,
        private val handleResponse: (List<User>) -> Unit,
        private val compositeDisposable: CompositeDisposable
) : PagedList.BoundaryCallback<User>() {

    var currentPage = 2L

    /**
     * Database returned 0 items. We should query the backend for more items.
     */
    @MainThread
    override fun onZeroItemsLoaded() {
        webservice.getUsers(1)
                .ioThenMain()
                .subscribe({ handleResponse(it.results) }, { })
                .addTo(compositeDisposable)
    }

    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: User) {
        webservice.getUsers(currentPage)
                .ioThenMain()
                .subscribe({ handleResponse(it.results) }, { })
                .addTo(compositeDisposable)
        currentPage += 1
    }

    override fun onItemAtFrontLoaded(itemAtFront: User) {
        // ignored, since we only ever append to what's in the DB
    }
}