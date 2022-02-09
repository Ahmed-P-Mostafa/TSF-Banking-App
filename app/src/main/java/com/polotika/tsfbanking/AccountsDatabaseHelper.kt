package com.polotika.tsfbanking

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val ACCOUNTS_TABLE_NAME = "ACCOUNTS"
private const val TRANSACTIONS_TABLE_NAME = "TRANSACTIONS"
private const val ACC_COL_NAME = "NAME"
private const val ACC_COL_EMAIL = "EMAIL"
private const val ACC_COL_BALANCE = "BALANCE"

private const val TRA_COL_SENDER_NAME = "SENDER_NAME"
private const val TRA_COL_RECEIVER_NAME = "RECEIVER_NAME"
private const val DB_VERSION = 1

class AccountsDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "Accounts_Database", null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createAccountsTableQuery =
            "CREATE TABLE $ACCOUNTS_TABLE_NAME (ID INTEGER PRIMARY KEY AUTOINCREMENT, $ACC_COL_NAME TEXT, $ACC_COL_EMAIL TEXT, $ACC_COL_BALANCE INTEGER)"
        val createTransactionTableQuery =
            "CREATE TABLE $TRANSACTIONS_TABLE_NAME (ID INTEGER PRIMARY KEY AUTOINCREMENT, $TRA_COL_SENDER_NAME TEXT, $TRA_COL_RECEIVER_NAME TEXT, $ACC_COL_BALANCE INTEGER)"
        db?.execSQL(createAccountsTableQuery)
        db?.execSQL(createTransactionTableQuery)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val upgradeAccountsQuery = "DROP TABLE IF EXISTS $ACCOUNTS_TABLE_NAME"
        val upgradeTransactionsQuery = "DROP TABLE IF EXISTS $TRANSACTIONS_TABLE_NAME"
        db?.execSQL(upgradeAccountsQuery)
        db?.execSQL(upgradeTransactionsQuery)

        onCreate(db)
    }

    fun insertAccount(account: Account): Boolean {
        val db: SQLiteDatabase = writableDatabase
        val values = ContentValues()
        values.put(ACC_COL_NAME, account.name)
        values.put(ACC_COL_EMAIL, account.email)
        values.put(ACC_COL_BALANCE, account.balance)

        val result = db.insert(ACCOUNTS_TABLE_NAME, null, values)
        return result != -1L
    }

    fun updateAccount(account: Account): Boolean {
        val db: SQLiteDatabase = writableDatabase
        val values = ContentValues()

        values.put(ACC_COL_NAME, account.name)
        values.put(ACC_COL_EMAIL, account.email)
        values.put(ACC_COL_BALANCE, account.balance)

        val args = arrayOf("${account.id}")
        val result = db.update(ACCOUNTS_TABLE_NAME, values, "ID=?", args)

        return result != 0
    }

    fun getAllAccounts(): List<Account> {
        val accounts = arrayListOf<Account>()
        val db: SQLiteDatabase = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $ACCOUNTS_TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(ACC_COL_NAME))
                val email = cursor.getString(cursor.getColumnIndexOrThrow(ACC_COL_EMAIL))
                val balance = cursor.getInt(cursor.getColumnIndexOrThrow(ACC_COL_BALANCE))
                val account = Account(id, name, email, balance)
                accounts.add(account)
            } while (cursor.moveToNext())
            cursor.close()
        }

        return accounts
    }

    fun getAccount(id: Int): Account? {
        val query = "SELECT * FROM $ACCOUNTS_TABLE_NAME WHERE ID =?"
        val db: SQLiteDatabase = readableDatabase
        val cursor = db.rawQuery(query, arrayOf(id.toString()))

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(ACC_COL_NAME))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(ACC_COL_EMAIL))
            val balance = cursor.getInt(cursor.getColumnIndexOrThrow(ACC_COL_BALANCE))
            val account = Account(id, name, email, balance)
            cursor.close()
            return account

        }

        return null
    }


}