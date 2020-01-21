package com.example.chatappmvc.controller

import android.util.Log
import com.example.chatappmvc.model.ChatClientConnector

/**
 * this class play a mediator role between the model and the view.
 * view and the model class communicate through this class
 */
class Controller(private val chatClientConnector: ChatClientConnector) {
    private var connectionRequestIsSent = false

    fun startServer() {
        connectionRequestIsSent = true
        Thread(chatClientConnector).start()
    }
    fun register(userName: String) {
        Log.d("registerme","controller pass ")
        chatClientConnector.registerUser(userName)
    }
    fun sendMessage(text: String) {
        chatClientConnector.createChatMessageObject(text)
    }
    fun clientConnected() = chatClientConnector.clientIsConnected() // check client is connected if connected return true
    fun connectionRequestSent() = connectionRequestIsSent
    fun userNameAlreadyExist() = chatClientConnector.userNameNotaccepted()
    fun userIsRegisterd() = chatClientConnector.userRegisterd()


}