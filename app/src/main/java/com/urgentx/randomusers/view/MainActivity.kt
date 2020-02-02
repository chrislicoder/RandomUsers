package com.urgentx.randomusers.view

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import com.jakewharton.rxbinding3.widget.textChanges
import com.urgentx.randomusers.R
import com.urgentx.randomusers.di.DependencyInjector
import com.urgentx.randomusers.viewmodel.UsersViewModel
import com.urgentx.randomusers.viewmodel.ViewModelFactory
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var vmFactory: ViewModelFactory

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        DependencyInjector.appComponent?.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(LayoutInflater.from(this).inflate(R.layout.activity_main, null))
        val viewModel = ViewModelProviders.of(this, vmFactory)[UsersViewModel::class.java]
        val adapter = UsersAdapter()
        users_recycler_view.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        users_recycler_view.adapter = adapter
        viewModel.usersList.subscribe {
            adapter.submitList(it)
        }.addTo(compositeDisposable)
        users_search_view.queryTextChanges().subscribe {
            viewModel.filter(it.toString())
        }.addTo(compositeDisposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) compositeDisposable.dispose()
    }
}