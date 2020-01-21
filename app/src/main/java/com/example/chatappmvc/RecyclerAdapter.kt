package com.example.chatappmvc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappmvc.model.ChatMessage
import com.example.chatappmvc.model.ChatMessageDatabase
import kotlinx.android.synthetic.main.recyler_layout.view.*

/**
 * a custom adapter class for the recyclerView in the main Activity
 */
class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>()  {
    private var adapterChatMessageList:MutableList<ChatMessage> = ChatMessageDatabase.getChatHistory().asReversed()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val cell =inflater.inflate(R.layout.recyler_layout,parent,false)
        return ViewHolder(cell)
    }
    override fun getItemCount(): Int {
        return adapterChatMessageList.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // populate the view holder
        holder.view.chatMessageTextView.text= adapterChatMessageList[position].chatMessage
        holder.view.userNameTextView.text= adapterChatMessageList[position].userName
        holder.view.timeTextView.text=adapterChatMessageList[position].time
        holder.view.setOnClickListener {  }
    }
    class ViewHolder(val view: View): RecyclerView.ViewHolder(view),View.OnClickListener{
        override fun onClick(p0: View?) {
        view.setOnClickListener {
            print(adapterPosition)
         }
        }
     }

}
