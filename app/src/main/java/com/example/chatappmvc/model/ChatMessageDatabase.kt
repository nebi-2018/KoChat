package com.example.chatappmvc.model

import com.example.chatappmvc.Observable
import com.example.chatappmvc.ChatMessageObserver

/**
 * local repository for for chatMessage
 * it get the data from the chatServer and store it in the list
 * when a new chatMessage is arrived it calls the update method of each observers
 * to notify them
 */
object ChatMessageDatabase : Observable {

    private val databaseChatMessageList = mutableListOf<ChatMessage>()
    //list of observers
    private val memberList = mutableListOf<ChatMessageObserver>()

    fun newMessage(message: ChatMessage) {
        updateChatHistory(message)
    }
    private fun updateChatHistory(message: ChatMessage) {
        databaseChatMessageList.add(message)
        notifyClients(databaseChatMessageList)
    }
    fun getChatHistory(): MutableList<ChatMessage> {
        return databaseChatMessageList
    }
    override fun registerUser(client: ChatMessageObserver) {
        memberList.add(client)
    }
    override fun unregisterUser(client: ChatMessageObserver) {}
    // observer Like MainActivity get notified when a new chatMessage received
    override fun notifyClients(newChatMessage: MutableList<ChatMessage>) {
        memberList.forEach {
            it.update()
        }
    }

}
