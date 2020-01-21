package com.example.chatappmvc

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappmvc.controller.Controller
import com.example.chatappmvc.model.ChatMessageDatabase
import com.example.chatappmvc.model.ChatClientConnector
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * this is the main activity class, which  contains a recyclerView for displaying user chat and  username
 * and  one textView for chatMessage input
 * MVC is the architecture used to build the app
 */
class MainActivity : AppCompatActivity(), ChatMessageObserver, UserRegisteringFrag.UserNameObserver {
    private val NOTIFICATION_CHANNEL_ID = "1"
    private val NOTIFICATION_ID = 1
    private lateinit var controller: Controller
    private var chatClientConnector = ChatClientConnector()
    private lateinit var adapterRecycler: RecyclerAdapter
    private lateinit var touchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ChatMessageDatabase.registerUser(this)
        adapterRecycler = RecyclerAdapter()
        mainRecyclerView.layoutManager = LinearLayoutManager(this)
        mainRecyclerView.adapter = adapterRecycler
        mainRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))//horizontal recyclerView layout divider
        controller = Controller(chatClientConnector)
        recyclerDragAndDrop() // a function which contain recyclerView  TouchHelper callback for drag and drop animation
        touchHelper.attachToRecyclerView(mainRecyclerView)

        sendButton.setOnClickListener {
            var usetInput = editTextMessage.text.toString()
            // if the user is connected accept the input otherwise notify the user to connect
            when {
                controller.clientConnected() && usetInput != "" -> {
                    controller.sendMessage(usetInput)
                    editTextMessage.text.clear()
                }
                !controller.clientConnected() -> makeSnackbar("Please connect first")
            }
        }
    }
    /**
     *  observer method for notifying the recyclerView when chatmessage list is updated
     */
    override fun update() {
        createNotificationChannel() // call this fun to create a notification
        runOnUiThread {
            adapterRecycler.notifyDataSetChanged()
        }
    }

    /**
     *     userName  registering fragment observer for delivering username to this activity
     */
    override fun registerFragmentObserver(userNameInput: String) {
        if (userNameInput != "") controller.register(userNameInput)
    }
    //menu icon onCreate
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    /**
     * user connection request sent only if the user is not connected
     * user registration request send only when it is connected and not registered before or is is connected and th user name it has submitted
     * already exist
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.connect -> {
                when {
                    !controller.connectionRequestSent() -> controller.startServer()
                     controller.connectionRequestSent() && !controller.clientConnected() -> makeSnackbar(getString(R.string.c_not_Succefull))
                     controller.connectionRequestSent() && controller.clientConnected() -> makeSnackbar(getString(R.string.alread_connected))
                }
                return true
            }
            R.id.register -> {
                when {
                    controller.clientConnected() && !controller.userIsRegisterd() -> {
                        cheaterRegisteringDialog() }
                    controller.clientConnected() && controller.userNameAlreadyExist() -> {
                        cheaterRegisteringDialog() }
                    controller.clientConnected() && controller.userIsRegisterd() -> {
                        makeSnackbar(getString(R.string.alread_connected)) }
                    !controller.clientConnected() -> makeSnackbar(getString(R.string.connect_first))

                    else -> makeSnackbar(getString(R.string.unable_to_register))
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    // create snackBar for user interaction
    private fun makeSnackbar(text: String) {
        var snack = Snackbar.make(mainActivity, text, Snackbar.LENGTH_LONG)
        snack.show()
    }
    private fun cheaterRegisteringDialog() {
        val customFragment = UserRegisteringFrag()
        customFragment.show(supportFragmentManager, "CustomFragment")
    }

    /**
     * create a drag and drop animation for the recyclerView
      */
    private fun recyclerDragAndDrop() {
        touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val sourcePosition = viewHolder.adapterPosition
                val targetPosition = target.adapterPosition
                Collections.swap(ChatMessageDatabase.getChatHistory(), sourcePosition, targetPosition)
                adapterRecycler.notifyDataSetChanged()
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // no onSwiped animation implementation
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    /**
     * create a notification
     */
    private fun createNotificationChannel() {

        val builder = NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("New Message")
            .setContentText("please press to see the message")
            .setStyle(NotificationCompat.BigTextStyle().bigText("please press to see the message"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }


}
