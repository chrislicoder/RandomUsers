package com.urgentx.randomusers.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.urgentx.randomusers.model.UserDatabase
import com.urgentx.randomusers.repository.IUsersRepository
import com.urgentx.randomusers.repository.UsersRepository
import com.urgentx.randomusers.service.UserService
import com.urgentx.randomusers.viewmodel.UsersViewModel
import com.urgentx.randomusers.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideUserDao() = Room.databaseBuilder(
        app.applicationContext,
        UserDatabase::class.java,
        "user-database"
    ).build().userDao()

    @Provides
    @Singleton
    fun provideUserService() = Retrofit.Builder()
        .baseUrl("https://randomuser.me/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(UserService::class.java)

    @Provides
    @Singleton
    fun provideUserRepository(): IUsersRepository =
        UsersRepository(provideUserService(), provideUserDao(), CompositeDisposable())
}

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(UsersViewModel::class)
    abstract fun usersViewModel(viewModel: UsersViewModel): ViewModel
}