package com.urgentx.randomusers.di


import android.app.Application
import com.urgentx.randomusers.repository.UsersRepository
import com.urgentx.randomusers.view.MainActivity
import com.urgentx.randomusers.viewmodel.ViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(repository: UsersRepository)
    fun inject(mainActivity: MainActivity)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        fun appModule(appModule: AppModule): Builder
    }
}

object DependencyInjector {

    var appComponent: AppComponent? = null

    fun initialize(app: Application) {
        this.appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .build()
    }
}