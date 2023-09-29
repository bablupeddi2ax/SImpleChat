package com.example.simplechat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(private val context: Context, private val messageList: ArrayList<Message>):RecyclerView.Adapter<ViewHolder>() {

    private val ITEM_RECEIVE = 1;
    private val ITEM_SENT = 2;
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType==1){
            val view:View = LayoutInflater.from(context).inflate(R.layout.receive,parent,false)
            return ReceiveViewHolder(view)
        }
        else{
            val view:View = LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
            return SentViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
      return messageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if(holder.javaClass==SentViewHolder::class.java){
            val viewHolder = holder as SentViewHolder
            viewHolder.sentMessage.text = currentMessage.message.toString()
        }else{
            val viewHolder = holder as ReceiveViewHolder
            viewHolder.receiveMessage.text = currentMessage.message.toString()
        }
//        holder.itemView.setOnLongClickListener{
//
//        }
    }

    override fun getItemViewType(position: Int): Int {
       val currentMessage   = messageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid==currentMessage.senderId){
            return ITEM_SENT
        }
        else{
            return ITEM_RECEIVE
        }
    }

    class SentViewHolder(itemView: View):ViewHolder(itemView)
    {
        val sentMessage: TextView = itemView.findViewById(R.id.txtSentMessage)
    }
    class ReceiveViewHolder(itemView: View):ViewHolder(itemView){
        val receiveMessage: TextView = itemView.findViewById(R.id.txtreceiveMessage)
    }
}