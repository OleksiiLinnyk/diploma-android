package com.demo.diploma.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.DialogFragment
import com.demo.diploma.R
import com.demo.diploma.callback.LoadAllGroupsCallback
import com.demo.diploma.model.response.GroupResponse
import com.demo.diploma.usecase.test.AssignTestToGroupUseCase
import com.demo.diploma.usecase.test.GetAvailableGroupsForTestUseCase

class AssignGroupToTest : DialogFragment() {

    private val getAvailableGroupsForTestUseCase: GetAvailableGroupsForTestUseCase = GetAvailableGroupsForTestUseCase()
    private val assignTestToGroupUseCase: AssignTestToGroupUseCase = AssignTestToGroupUseCase()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val assignButton: Button = view.findViewById(R.id.assign_group_btn)
        val id: Long = arguments?.getLong("testId")!!
        val autoCompleteTextView: ListView = view.findViewById(R.id.groupAutoTextView)
        var selectedGroup: String? = null

        getAvailableGroupsForTestUseCase.execute(object : LoadAllGroupsCallback {
            override fun onSuccess(body: List<GroupResponse>) {
                val map: Map<String, GroupResponse> = body.associateBy { it.name }
                val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_single_choice, map.keys.toList())
                autoCompleteTextView.adapter = arrayAdapter

                assignButton.setOnClickListener {
                    if (!selectedGroup.isNullOrEmpty()) {
                        assignTestToGroupUseCase.execute(id, map[selectedGroup]!!.id, it.context)
                    }
                }
            }
        }, id)

        autoCompleteTextView.setOnItemClickListener { adapterView, view, i, l ->
            selectedGroup = adapterView.getItemAtPosition(i).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_assign_group_to_test, container, false)
    }
}