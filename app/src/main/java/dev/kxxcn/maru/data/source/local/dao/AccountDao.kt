package dev.kxxcn.maru.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.kxxcn.maru.data.Account

@Dao
interface AccountDao {

    @Query("SELECT * FROM Accounts")
    fun observeAccounts(): LiveData<List<Account>>

    @Query("SELECT * FROM Accounts")
    suspend fun getAccounts(): List<Account>

    @Query("SELECT * FROM Accounts WHERE taskId = :taskId")
    suspend fun getAccount(taskId: String): Account

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccountAll(accounts: List<Account>)

    @Update
    suspend fun updateAccount(account: Account): Int
}
