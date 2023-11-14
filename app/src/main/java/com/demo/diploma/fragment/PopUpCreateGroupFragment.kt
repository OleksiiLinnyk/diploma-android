package com.demo.diploma.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.demo.diploma.R
import com.demo.diploma.activity.AdminGroupsActivity
import com.demo.diploma.usecase.user.CreateGroupUseCase

class PopUpCreateGroupFragment : DialogFragment() {

    private val createGroupUseCase = CreateGroupUseCase()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pop_up_create_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val createBtn = view.findViewById<Button>(R.id.create_group_btn)
        val groupInput = view.findViewById<EditText>(R.id.input_group_name)

        createBtn.setOnClickListener {
            val groupName = groupInput.text.toString()
            if (groupName.isNullOrEmpty()) {
                Toast.makeText(view.context, "Please specify the group name", Toast.LENGTH_LONG).show()
            } else {
                createGroupUseCase.createGroup(groupName, view)
                val intent = Intent(view.context, AdminGroupsActivity::class.java)
                startActivity(intent)
            }
        }
    }
}