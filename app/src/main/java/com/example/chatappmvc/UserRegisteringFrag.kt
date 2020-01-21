package com.example.chatappmvc

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment

/**
 * a custom dialog fragment for taking user input so setting username
 * it has an observer interface for delivering the user input to the mainActivity
 */
class UserRegisteringFrag : DialogFragment() {

    private var input = ""
    var userNameObserver: UserNameObserver? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.register_frag_layout, container, true)

        val cancel = view.findViewById<View>(R.id.cancelButtonFragment)
        val register = view.findViewById<View>(R.id.registerButtonFragment)
        val editText: EditText = view.findViewById(R.id.editTextFragment)
        cancel.setOnClickListener {
            dialog?.dismiss()
        }
        register.setOnClickListener {
            input = editText.text.toString()
            userNameObserver?.registerFragmentObserver(input)
            dialog?.dismiss()
        }
        return view
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is UserNameObserver) {
            userNameObserver = context
        }
    }
    /**
     * an interface for delivering the userName from the dialog fragment to the main activity
      */
    interface UserNameObserver {
        fun registerFragmentObserver(userNameInput: String)
    }


}
