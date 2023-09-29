package com.example.simplechat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Features:
 * - Real-time Chat:
 *   Enables users to have real-time chat conversations with other users.
 *   Messages are stored in Firebase Realtime Database.

 * - Message List Display:
 *   Displays a list of chat messages in a RecyclerView.
 *   Messages are retrieved from the database and updated in real-time.

 * - Message Sending:
 *   Allows users to send messages to the selected recipient.
 *   Messages are sent and received via Firebase Realtime Database.

 * - Dynamic Chat Rooms:
 *   Creates dynamic chat rooms for each chat pair by combining user UIDs.
 *   Ensures private conversations between users.

 * - UI Interaction:
 *   Provides an interface for users to type and send messages.
 *   Automatically scrolls to the latest message for a better chat experience.

 * - Code Organization:
 *   Utilizes a custom adapter (MessageAdapter) for displaying chat messages.
 */

class ChatActivity : AppCompatActivity() {
    // Define variables
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mRef: DatabaseReference
    private var receiverRoom: String? = null
    private var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Retrieve user details and initialize Firebase references
        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mRef = FirebaseDatabase.getInstance().reference

        // initialize senderRoom and receiverRoom be the combination of receiverUid and senderUid
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        // add receiver name as title of supportActionBar
        supportActionBar?.title = name

        // initialize views
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        sendButton = findViewById(R.id.sendBtn)
        messageBox = findViewById(R.id.messageBox)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        // Configure the RecyclerView for displaying messages
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        // Retrieve and display chat messages in real-time
        mRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // avoid duplication by clearing messages that are already there in the list
                    messageList.clear()
                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    //notify data set changed
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    showToast(error.message, this@ChatActivity)
                }
            })

        // Send messages when the send button is clicked
        sendButton.setOnClickListener {
            Toast.makeText(this@ChatActivity, "check", Toast.LENGTH_SHORT).show()
            val message = messageBox.text.toString()
            val messageObject = Message(message, senderUid)
            // add message object in sender room then in receiver room
            mRef
                .child("chats")
                .child(senderRoom!!)
                .child("messages")
                .push().setValue(messageObject).addOnSuccessListener {
                    mRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            //scroll to show the latest message
            chatRecyclerView.scrollToPosition(messageList.size-1 )
            //clear message box
            messageBox.setText("")
        }
    }
}