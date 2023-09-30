package com.example.simplechat

import UserAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
/**
 * MainActivity for the chat application.
 *
 * Features:
 * - User List Display:
 *   Displays a list of users from the Firebase Realtime Database.
 *   Users can be clicked to start a chat with them.

 * - User Authentication:
 *   Uses Firebase Authentication to manage user login/logout.

 * - Logout Option:
 *   Provides a logout option in the menu for users to sign out.

 * - Code Organization:
 *   Utilizes the UserAdapter to display a list of users in a RecyclerView.
 */
class MainActivity : AppCompatActivity() {
    // Define variables
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialize auth and ref to users
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference("users")

        // initialize variables and recycler view
        userList = ArrayList()
        adapter = UserAdapter(this@MainActivity,userList)
        userRecyclerView = findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter

        // Add Value Event Listener to Fetch Users
        mDbRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(userSnapShot in snapshot.children ){
                    // get current user
                    val currentUser = userSnapShot.getValue(User::class.java)
                    // Exclude the currently logged-in user from the list
                    if(mAuth.currentUser?.uid!=currentUser?.uid) {
                        userList.add(currentUser!!)
                        // Notify adapter of data change
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    // inflate the  menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    // handle click events on menu items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.logout){
            mAuth.signOut()
            finish()
            moveTo(this@MainActivity,Login::class.java)
            return true
        }
        if(item.itemId==R.id.search){
            moveTo(this@MainActivity,SearchUsers::class.java)
            return true
        }
        return true

    }
}