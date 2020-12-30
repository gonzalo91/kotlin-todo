package com.example.todo2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class AddItemActivity : AppCompatActivity() {

    private lateinit var itemNameEditTextView: EditText
    private lateinit var itemDateEditTextView: EditText
    private lateinit var isUrgentCheckbox : Switch
    private lateinit var titleTextView : TextView

    private var isNewItem : Boolean = true
    private lateinit var oldItem : TodoItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val button = findViewById<Button>(R.id.saveButton)
        button.setOnClickListener {
            saveItemAction()
        }

        val cancelButton = findViewById<Button>(R.id.cancelButton)
        cancelButton.setOnClickListener {
            goHome()
        }

        itemNameEditTextView = findViewById(R.id.editTextName)
        isUrgentCheckbox = findViewById(R.id.switch1)
        titleTextView    = findViewById(R.id.titleTextView)

        itemDateEditTextView = findViewById(R.id.editTextDate)

        val textIntent = intent.getStringExtra("name")
        val dateIntent = intent.getStringExtra("date")
        val isUrgentIntent   = intent.getBooleanExtra("urgent", false)

        if(textIntent != null){
            oldItem = TodoItem(textIntent, isUrgentIntent, dateIntent)
            itemNameEditTextView.setText(textIntent)
            itemDateEditTextView.setText(dateIntent)
            titleTextView.setText("Edit Item")
            isNewItem = false
        }

        isUrgentCheckbox.isChecked = isUrgentIntent

    }

    public fun saveItemAction(){
        val itemName = itemNameEditTextView.text.toString()
        val itemDate = itemDateEditTextView.text.toString()
        val isItemUrgent = isUrgentCheckbox.isChecked
        val newTodoItem = TodoItem(itemName, isItemUrgent, itemDate)

        val dbo = DataBaseOperations(this)

        if(isNewItem){
            dbo.addItem(dbo, newTodoItem)
        }else{
            dbo.updateItem(dbo, oldItem, newTodoItem)
        }
        goHome()
    }

    public fun goHome(){
        val intent : Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}