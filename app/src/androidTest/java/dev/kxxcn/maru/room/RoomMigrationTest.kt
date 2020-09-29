package dev.kxxcn.maru.room

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.kxxcn.maru.data.source.local.MaruDatabase
import dev.kxxcn.maru.di.ApplicationModule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoomMigrationTest {

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        MaruDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrateAll() {
        val name = "Maru"
        helper.createDatabase(name, 1).apply { close() }
        Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            MaruDatabase::class.java,
            name
        ).addMigrations(*ApplicationModule.ALL_MIGRATIONS).build().apply {
            openHelper.writableDatabase
            close()
        }
    }
}
