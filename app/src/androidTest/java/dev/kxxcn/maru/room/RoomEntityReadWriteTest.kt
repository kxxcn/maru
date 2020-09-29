package dev.kxxcn.maru.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.kxxcn.maru.data.User
import dev.kxxcn.maru.data.source.local.MaruDatabase
import dev.kxxcn.maru.data.source.local.dao.UserDao
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoomEntityReadWriteTest {

    private lateinit var userDao: UserDao
    private lateinit var database: MaruDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        Room.inMemoryDatabaseBuilder(
            context,
            MaruDatabase::class.java
        ).build().also {
            database = it
            userDao = it.userDao()
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        val user: User = User().apply { name = "kxxcn" }
        runBlocking {
            userDao.insertUser(user)
            val users = userDao.getUsers()
            assertThat(users[0], equalTo(user))
        }
    }
}
