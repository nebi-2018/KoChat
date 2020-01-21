package com.example.chatappmvc.model

import kotlinx.serialization.Serializable
// data class for chatMessage
@Serializable
data class ChatMessage(val userName:String, val chatMessage:String, val command:String, var time:String,var id:Int) {
    override fun toString(): String {
        return "$userName $chatMessage $time  $id"
    }
}