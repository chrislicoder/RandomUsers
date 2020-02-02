package com.urgentx.randomusers.service

import com.urgentx.randomusers.model.User
import com.urgentx.randomusers.repository.UsersRepository.Companion.PAGE_SIZE
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {

    @GET(".")
    fun getUsers(
        @Query("page") page: Long,
        @Query("results") results: Int = PAGE_SIZE,
        @Query("seed") seed: String = "abc"
    ): Single<Response>

}

data class Response(val results: List<User>)