package com.urgentx.randomusers.model

import androidx.paging.DataSource
import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable

@Database(entities = [User::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(users: List<User>): Completable

    @Query("SELECT * FROM user")
    fun getAll(): DataSource.Factory<Int, User>

    @Query("SELECT * FROM user WHERE first LIKE :query OR last LIKE :query")
    fun getAllWithQuery(query: String): DataSource.Factory<Int, User>
}