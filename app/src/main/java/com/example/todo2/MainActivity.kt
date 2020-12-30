package com.example.todo2

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var todoItemRecyclerView : RecyclerView
    private lateinit var recyclerAdapter: TodoItemAdapter
    private lateinit var recyclerLayoutManager: RecyclerView.LayoutManager

    private lateinit var todayButton : Button;
    private lateinit var urgentButton : Button;
    private lateinit var allItemsButton : Button;

    var todoItemLists = mutableListOf<TodoItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        todoItemRecyclerView = findViewById(R.id.my_recycler)

        val button = findViewById<FloatingActionButton>(R.id.add_new_event_button)
        button.setOnClickListener {
            val homepage = Intent(this@MainActivity, AddItemActivity::class.java)
            startActivity(homepage)
        }

        todayButton = findViewById<Button>(R.id.todayButton)
        todayButton.setOnClickListener {
            showTodayActivities()
        }

        urgentButton = findViewById<Button>(R.id.urgentButton)
        urgentButton.setOnClickListener {
            showUrgentActivities()
        }

        allItemsButton = findViewById<Button>(R.id.allItemsButton)
        allItemsButton.setOnClickListener {
            showAllActivities()
        }

        showAllActivities()

    }

    fun showAllActivities(){
        todoItemLists.clear()
        __whiteButtons()
        allItemsButton.setTextColor(Color.parseColor("#B00020"))

        val dbo = DataBaseOperations(this)
        val cursor = dbo.getAllItem(dbo)

        with(cursor){
            while (moveToNext()){
                val urgency = getInt(getColumnIndex( DatabaseInfo.TableInfo.COLUMN_ITEM_URGENCY))
                val isUrgent = if( urgency  == 0) false else true
                todoItemLists.add(
                    TodoItem(
                        getString(getColumnIndex( DatabaseInfo.TableInfo.COLUMN_ITEM_NAME)),
                        isUrgent,
                        getString(getColumnIndex( DatabaseInfo.TableInfo.COLUMN_DATE)),
                    )
                )
            }
        }

        applyChangesToRV()
    }

    fun showTodayActivities(){
        todoItemLists.clear()
        __whiteButtons()
        todayButton.setTextColor(Color.parseColor("#B00020"))
        val calendar = Calendar.getInstance()

        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)

        val today = "$day/$month/$year"

        val dbo = DataBaseOperations(this)
        val cursor = dbo.getItemsByDate(dbo, today)

        with(cursor){
            while (moveToNext()){
                val urgency = getInt(getColumnIndex( DatabaseInfo.TableInfo.COLUMN_ITEM_URGENCY))
                val isUrgent = if( urgency  == 0) false else true
                todoItemLists.add(
                    TodoItem(
                        getString(getColumnIndex( DatabaseInfo.TableInfo.COLUMN_ITEM_NAME)),
                        isUrgent,
                        getString(getColumnIndex( DatabaseInfo.TableInfo.COLUMN_DATE)),
                    )
                )
            }
        }

        applyChangesToRV()

    }

    fun showUrgentActivities(){
        todoItemLists.clear()
        __whiteButtons()
        urgentButton.setTextColor(Color.parseColor("#B00020"))
        val dbo = DataBaseOperations(this)
        val cursor = dbo.getUrgentItems(dbo)

        with(cursor){
            while (moveToNext()){
                val urgency = getInt(getColumnIndex( DatabaseInfo.TableInfo.COLUMN_ITEM_URGENCY))
                val isUrgent = if( urgency  == 0) false else true
                todoItemLists.add(
                    TodoItem(
                        getString(getColumnIndex( DatabaseInfo.TableInfo.COLUMN_ITEM_NAME)),
                        isUrgent,
                        getString(getColumnIndex( DatabaseInfo.TableInfo.COLUMN_DATE)),
                    )
                )
            }
        }

        applyChangesToRV()
    }

    fun applyChangesToRV(){
        recyclerLayoutManager = LinearLayoutManager(this)

        recyclerAdapter = TodoItemAdapter(todoItemLists, this)

        todoItemRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = recyclerLayoutManager
            adapter = recyclerAdapter
        }
    }
    

    fun __whiteButtons(){
        todayButton.setTextColor(Color.parseColor("#ffffff"))
        urgentButton.setTextColor(Color.parseColor("#ffffff"))
        allItemsButton.setTextColor(Color.parseColor("#ffffff"))
    }


}