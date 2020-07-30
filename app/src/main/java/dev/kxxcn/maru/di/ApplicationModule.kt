package dev.kxxcn.maru.di

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dev.kxxcn.maru.MaruApplication
import dev.kxxcn.maru.MaruCoroutineScope
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.data.source.DataSource
import dev.kxxcn.maru.data.source.MaruRepository
import dev.kxxcn.maru.data.source.api.RetrofitBuilder
import dev.kxxcn.maru.data.source.firebase.FirebaseDataSource
import dev.kxxcn.maru.data.source.local.LocalDataSource
import dev.kxxcn.maru.data.source.local.MaruDatabase
import dev.kxxcn.maru.data.source.remote.RemoteDataSource
import dev.kxxcn.maru.view.base.BaseCoroutineScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.annotation.AnnotationRetention.RUNTIME

@Module(includes = [ApplicationModuleBinds::class])
object ApplicationModule {

    @Qualifier
    @Retention(RUNTIME)
    annotation class RemoteDataSource

    @Qualifier
    @Retention(RUNTIME)
    annotation class LocalDataSource

    @Qualifier
    @Retention(RUNTIME)
    annotation class FirebaseDataSource

    @Singleton
    @RemoteDataSource
    @Provides
    fun provideRemoteDataSource(ioDispatcher: CoroutineDispatcher): DataSource {
        return RemoteDataSource(
            RetrofitBuilder.createNaverService(),
            ioDispatcher
        )
    }

    @Singleton
    @LocalDataSource
    @Provides
    fun provideLocalDataSource(
        database: MaruDatabase,
        ioDispatcher: CoroutineDispatcher
    ): DataSource {
        return LocalDataSource(
            database.userDao(),
            database.taskDao(),
            database.accountDao(),
            database.mappingDao(),
            database.directionDao(),
            database.dayDao(),
            ioDispatcher
        )
    }

    @Singleton
    @FirebaseDataSource
    @Provides
    fun provideFirebaseDataSource(): DataSource {
        return FirebaseDataSource(Firebase.firestore)
    }

    @Singleton
    @Provides
    fun provideDataBase(application: MaruApplication): MaruDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            MaruDatabase::class.java,
            "Maru.db"
        ).addMigrations(*ALL_MIGRATIONS).fallbackToDestructiveMigrationOnDowngrade().build()
    }

    @Singleton
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideCoroutineScope(): BaseCoroutineScope = MaruCoroutineScope()

    val ALL_MIGRATIONS = arrayOf<Migration>(
        object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE users ADD COLUMN premium INTEGER default 0 NOT NULL")
            }
        }
    )
}

@Module
abstract class ApplicationModuleBinds {

    @Singleton
    @Binds
    abstract fun bindRepository(repo: MaruRepository): DataRepository
}
