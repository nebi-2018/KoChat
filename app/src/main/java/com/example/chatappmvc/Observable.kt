package com.example.chatappmvc

import com.example.chatappmvc.model.ChatMessage
/**
 * un observable interface for the chatMessageDatabase class
 */
interface Observable {
    fun registerUser(client:ChatMessageObserver)
    fun unregisterUser(client:ChatMessageObserver)
    fun notifyClients(newChatMessage: MutableList<ChatMessage>)
}