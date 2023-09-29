package com.example.simplechat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
/**
 * Adapter class for the RecyclerView in the MainActivity.
 *
 * This adapter is responsible for binding user data to individual items in the user list.
 * It inflates the user layout and sets click listeners to start a chat with a selected user.
 *
 * @param context The context in which the adapter is used.
 * @param userList The list of users to be displayed in the RecyclerView.
 */
class UserAdapter(private val context: Context, private val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    /**
     * Create a new ViewHolder by inflating the user layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    /**
     * Return the number of items in the user list.
     */
    override fun getItemCount(): Int {
        return userList.size
    }

    /**
     * Bind user data to the ViewHolder and set click listeners to start a chat with the user.
     */
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        // add on click listener to items
        val currentUser = userList[position]
        holder.txtName.text = currentUser.name
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.uid)
            context.startActivity(intent)
        }
    }

    /**
     * ViewHolder class to hold references to UI elements in the user item layout.
     */
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtName)
    }
}

