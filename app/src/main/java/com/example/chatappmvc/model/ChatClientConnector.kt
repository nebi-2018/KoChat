package com.example.chatappmvc.model

import android.util.Log
import kotlinx.io.PrintWriter
import kotlinx.serialization.json.Json
import java.lang.Exception
import java.net.Socket
import java.util.*

/**
 * in this class the client create a socket and send  connection request to the chat server
 * if the connection is stabilised input and output stream will be created
 */
class ChatClientConnector : Runnable {
    private lateinit var input: Scanner
    private lateinit var output: PrintWriter
    private var userName = ""
    private var clientIsConnected = false //the state of client connetion to the chatServer
    private var userNameUnaccepted = false //  true if the user name already exist
    private var userRegisterd = false // true is the user is registered to the chatserver

    override fun run() {
        try {
            var socket = Socket("10.0.2.2", 40000)
            clientIsConnected = true
            input = Scanner(socket.getInputStream())
            output = PrintWriter(socket.getOutputStream(), true)
            // the thread continue to run as long as the client is connected to the server
            while (true) {
                val message: String = input.nextLine()
                val chatMessage = Json.parse(ChatMessage.serializer(), message)
                ChatMessageDatabase.newMessage(chatMessage)
                //each chatmessage has its own id and registering request is handled by the server with an id number of
                // 4 or 5 , if id is 4 the username is already exist and if it is 5 the registration is successful
                if ((chatMessage.id == 4) or (chatMessage.id == 5)) {
                    if (chatMessage.id == 4) userNameUnaccepted = true
                    else {
                        userRegisterd = true
                        userNameUnaccepted = false
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("servermessage", "server closed")
        }
    }
    fun createChatMessageObject(userInput: String) {
        var message = ChatMessage(userName, userInput, "send", "", 0)
        serverOutput(message)
    }
    fun serverOutput(chatMessage: ChatMessage) {
      try {
          Thread {
            val newChatMessage = Json.stringify(ChatMessage.serializer(), chatMessage)
            output.println(newChatMessage)
        }.start()
      }
      catch (e:Exception){
          Log.d("chatClient","error trowed from the chatClientConnector serverOutPut method")
      }
    }
    fun registerUser(userNameInput: String) {
        userName = userNameInput
        val message = ChatMessage(userName, "", "register", "", 0)
        serverOutput(message)
    }
    fun clientIsConnected() = clientIsConnected
    fun userNameNotaccepted() = userNameUnaccepted
    fun userRegisterd() = userRegisterd


}