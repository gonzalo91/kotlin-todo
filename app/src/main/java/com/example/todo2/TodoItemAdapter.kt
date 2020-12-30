package com.example.todo2

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class TodoItemAdapter(private val todoItemsList: MutableList<TodoItem>, val activity : MainActivity)
    : RecyclerView.Adapter<TodoItemAdapter.ViewHolder>() {


    class ViewHolder(val constraintLayout: ConstraintLayout)
        : RecyclerView.ViewHolder( constraintLayout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val constraintLayout = LayoutInflater
                                .from(parent.context)
                                .inflate(R.layout.to_do_item_layout, parent, false)
                                as ConstraintLayout

        constraintLayout.setOnClickListener(View.OnClickListener {
            val nameTextView = constraintLayout.getChildAt(0) as TextView
            val urgencyView = constraintLayout.getChildAt(1) as TextView
            val dateView = constraintLayout.getChildAt(2) as TextView
            val nameText = nameTextView.text
            val urgencyText = urgencyView.text
            val dateText = dateView.text

            val isItemUrgent = if( urgencyText == "!!") true else false

            val intent = Intent(parent.context, AddItemActivity::class.java)
            intent.putExtra("name", nameText)
            intent.putExtra("urgent", isItemUrgent)
            intent.putExtra("date", dateText)

            activity.startActivity(intent)
        })

        constraintLayout.setOnLongClickListener( View.OnLongClickListener {
            val position : Int = parent.indexOfChild(it)

            val todoItemToRemove = activity.todoItemLists[position]
            val dbo = DataBaseOperations(parent.context)
            dbo.deleteItem(dbo, todoItemToRemove)

            activity.todoItemLists.removeAt(position)
            notifyItemRemoved(position)

            true
        })

        return ViewHolder(constraintLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val constraintLayout = holder.constraintLayout
        val nameTextView = constraintLayout.getChildAt(0) as TextView
        nameTextView.text = todoItemsList[position].name

        val urgencyView = constraintLayout.getChildAt(1) as TextView
        urgencyView.text = if(todoItemsList[position].isUrgent) "!!" else ""

        val dateView = constraintLayout.getChildAt(2) as TextView
        dateView.text = todoItemsList[position].taskDate

    }

    override fun getItemCount(): Int {
        return todoItemsList.size
    }


}