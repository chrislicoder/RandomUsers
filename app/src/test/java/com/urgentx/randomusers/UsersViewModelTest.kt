package com.urgentx.randomusers

import com.nhaarman.mockitokotlin2.*
import com.urgentx.randomusers.model.Name
import com.urgentx.randomusers.model.User
import com.urgentx.randomusers.model.UserDao
import com.urgentx.randomusers.repository.IUsersRepository
import com.urgentx.randomusers.service.UserService
import com.urgentx.randomusers.viewmodel.UsersViewModel
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.*

class UsersViewModelTest {

    @Mock
    lateinit var usersRepository: IUsersRepository

    @Mock
    lateinit var userService: UserService
    @Mock
    lateinit var userDao: UserDao

    val dummyUsers = listOf(
            User(name = Name("Mr", "Bob", "Ross")),
            User(name = Name("Mrs", "Alice", "Ross")),
            User(name = Name("Ms", "Jane", "Smith")),
            User(name = Name("Mr", "Jack", "Thompson")),
            User(name = Name("Mr", "Dane", "Smith"))
    )

    @Captor
    lateinit var updateCaptor: ArgumentCaptor<String>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val mockedList = mockPagedList(dummyUsers)
        whenever(usersRepository.users()).thenReturn(Observable.just(mockedList))
    }

    @Test
    fun noValueIfNoQuery() {
        val viewModel = UsersViewModel(userService, userDao, usersRepository)
        val test = viewModel.usersList.test()
        test.assertNoValues()
        test.assertNoErrors()
    }

    @Test
    fun displayInitialUsers() {
        val viewModel = UsersViewModel(userService, userDao, usersRepository)
        val test = viewModel.usersList.test()
        viewModel.filter("")
        test.assertValue { it.size == dummyUsers.size }
        test.assertNoErrors()
    }

    @Test
    fun displayFilteredUsers() {
        val viewModel = UsersViewModel(userService, userDao, usersRepository)
        val dummyFiltered = mockPagedList(dummyUsers.filter { it.name.contains("a") })
        whenever(usersRepository.usersByName("a")).thenReturn(Observable.just(dummyFiltered))
        val test = viewModel.usersList.test()
        viewModel.filter("a")
        verify(usersRepository, times(1)).usersByName(capture(updateCaptor))
        assert(updateCaptor.value == "a")
        test.assertValue {
            it.size == 3 && it[0] == dummyUsers[2] && it[1] == dummyUsers[3] && it[2] == dummyUsers[4]
        }
        test.assertNoErrors()
    }
}