package com.example.chatappmvc

/**
 * an observer which will be implemented by the main activity
 * any observer will be notified through the update method of this class
 */
interface ChatMessageObserver {
    fun  update()

}