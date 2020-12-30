package com.example.todo2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log

class DataBaseOperations(context : Context) : SQLiteOpenHelper(
        context,
        DATABASE_NAME, null, DATABASE_VERSION,){

        companion object{
            const val DATABASE_NAME = "TodoItems.db"
            const val DATABASE_VERSION = 2
        }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DatabaseInfo.SQL_CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DatabaseInfo.SQL_DROP_TABLE_QUERY)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun addItem(dbo: DataBaseOperations, todoItem: TodoItem){

        val db = dbo.writableDatabase
        val itemName = todoItem.name
        val itemDate = todoItem.taskDate
        val itemUrgency = todoItem.isUrgent()

        val contentValues = ContentValues().apply {
            put(DatabaseInfo.TableInfo.COLUMN_ITEM_NAME, itemName)
            put(DatabaseInfo.TableInfo.COLUMN_ITEM_URGENCY, itemUrgency)
            put(DatabaseInfo.TableInfo.COLUMN_DATE, itemDate)
        }

        val rowId = db.insert(
                        DatabaseInfo.TableInfo.TABLE_NAME,
                null,
                    contentValues)
    }

    fun getAllItem(dbo: DataBaseOperations) : Cursor{
        val db = dbo.readableDatabase

        val projection = arrayOf(
                BaseColumns._ID,
                DatabaseInfo.TableInfo.COLUMN_DATE,
                DatabaseInfo.TableInfo.COLUMN_ITEM_NAME,
                DatabaseInfo.TableInfo.COLUMN_ITEM_URGENCY,
        )

        val cursor = db.query(
                DatabaseInfo.TableInfo.TABLE_NAME,
                projection,
                "",
                null, null, null, null
        )

        return cursor
    }

    fun getItemsByDate(dbo: DataBaseOperations, date : String) : Cursor{
        val db = dbo.readableDatabase

        val projection = arrayOf(
            BaseColumns._ID,
            DatabaseInfo.TableInfo.COLUMN_DATE,
            DatabaseInfo.TableInfo.COLUMN_ITEM_NAME,
            DatabaseInfo.TableInfo.COLUMN_ITEM_URGENCY,
        )

        val selection = "${DatabaseInfo.TableInfo.COLUMN_DATE} = ?"
        val selectionArgs = arrayOf(date)
        Log.e("date" , date)
        val cursor = db.query(
            DatabaseInfo.TableInfo.TABLE_NAME,
            projection,
            selection,
            selectionArgs, null, null, null
        )
        return cursor
    }

    fun getUrgentItems(dbo: DataBaseOperations) : Cursor{
        val db = dbo.readableDatabase

        val projection = arrayOf(
            BaseColumns._ID,
            DatabaseInfo.TableInfo.COLUMN_DATE,
            DatabaseInfo.TableInfo.COLUMN_ITEM_NAME,
            DatabaseInfo.TableInfo.COLUMN_ITEM_URGENCY,
        )

        val selection = "${DatabaseInfo.TableInfo.COLUMN_ITEM_URGENCY} = 1"

        val cursor = db.query(
            DatabaseInfo.TableInfo.TABLE_NAME,
            projection,
            selection,
            null, null, null, null
        )
        return cursor
    }

    fun updateItem(dbo : DataBaseOperations, oldItem : TodoItem, newItem: TodoItem){
        var db = dbo.writableDatabase
        val itemName = newItem.name
        val itemDate = newItem.taskDate
        val itemUrgency = newItem.isUrgent()
        Log.e("aarre", newItem.taskDate)
        val contentValues = ContentValues().apply {
            put(DatabaseInfo.TableInfo.COLUMN_ITEM_NAME, itemName)
            put(DatabaseInfo.TableInfo.COLUMN_ITEM_URGENCY, itemUrgency)
            put(DatabaseInfo.TableInfo.COLUMN_DATE, itemDate)
        }

        val selection = "${DatabaseInfo.TableInfo.COLUMN_ITEM_NAME} LIKE ?"
        val selectionArgs = arrayOf(oldItem.name)

        db.update(
                DatabaseInfo.TableInfo.TABLE_NAME,
                contentValues,
                selection,
                selectionArgs
        )
    }

    fun deleteItem(dbo : DataBaseOperations, todoItem : TodoItem){
        val db = dbo.writableDatabase

        val selection = "${DatabaseInfo.TableInfo.COLUMN_ITEM_NAME} LIKE ?"
        val selectionArgs = arrayOf(todoItem.name)

        db.delete(
                DatabaseInfo.TableInfo.TABLE_NAME,
                selection, selectionArgs
        )
    }
}