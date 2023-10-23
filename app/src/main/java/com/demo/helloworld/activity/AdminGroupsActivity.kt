package com.demo.helloworld.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.demo.helloworld.R
import com.demo.helloworld.api.GroupAPI
import com.demo.helloworld.configuration.RetrofitConfiguration
import com.demo.helloworld.configuration.TokenHolder
import com.demo.helloworld.fragment.PopUpCreateGroupFragment
import com.demo.helloworld.fragment.PopUpUpdateGroupFragment
import com.demo.helloworld.model.response.GroupResponse
import com.demo.helloworld.util.BottomNavigationUtil
import com.demo.helloworld.util.ShowPopupNotificationUtil.showPopup
import com.demo.helloworld.util.ViewUtil.prepareImageView
import com.demo.helloworld.util.ViewUtil.prepareTextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminGroupsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_groups_layout)

        val groupAPI: GroupAPI = RetrofitConfiguration.getInstance()
            .create(GroupAPI::class.java)
        val tableLayout: TableLayout = findViewById(R.id.tablelayout)
        val searchBtn: Button = findViewById(R.id.search_group_btn)
        val createBtn: Button = findViewById(R.id.create_group_btn)

        loadAllGroups(groupAPI, tableLayout)

        createBtn.setOnClickListener {
            val showPopUp = PopUpCreateGroupFragment()
            showPopUp.show(this@AdminGroupsActivity.supportFragmentManager, "showPopUp")
        }

        searchButtonBehavior(searchBtn, groupAPI, tableLayout)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationBar)

        BottomNavigationUtil.setBottomNavigationMenu(this@AdminGroupsActivity, bottomNavigationView, AdminActivity::class.java)
    }

    private fun loadAllGroups(
        groupAPI: GroupAPI,
        tableLayout: TableLayout
    ) {
        val call: Call<List<GroupResponse>> =
            groupAPI.getAllGroups("token=${TokenHolder.getToken()}")


        call.enqueue(object : Callback<List<GroupResponse>> {
            override fun onResponse(
                call: Call<List<GroupResponse>>,
                response: Response<List<GroupResponse>>
            ) {
                if (response.isSuccessful) {
                    tableLayout.removeAllViews()
                    val body: List<GroupResponse> = response.body().orEmpty()
                    body.sortedBy { it.id }.forEach { res ->
                        val tableRow = TableRow(this@AdminGroupsActivity)
                        tableRow.layoutParams =
                            TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                            )
                        val id = prepareTextView(res.id.toString(), 1, this@AdminGroupsActivity)
                        val name = prepareTextView(res.name, 2, this@AdminGroupsActivity)
                        val btn = prepareImageView(3, this@AdminGroupsActivity)

                        tableRow.addView(id)
                        tableRow.addView(name)
                        tableRow.addView(btn)

                        tableLayout.addView(tableRow)

                        tableRow.setOnClickListener {
                            val showPopUp = PopUpUpdateGroupFragment()
                            val bundle = Bundle()
                            bundle.putLong("id", res.id)
                            showPopUp.arguments = bundle
                            showPopUp.show(
                                this@AdminGroupsActivity.supportFragmentManager,
                                "showPopUp"
                            )
                        }

                        deleteGroupBtnBehavior(btn, groupAPI, res, name, tableRow, id)
                    }
                }
            }

            override fun onFailure(call: Call<List<GroupResponse>>, t: Throwable) {
                Log.v("Retrofit", "call failed ${t.message}")
            }
        })
    }

    private fun deleteGroupBtnBehavior(
        btn: ImageView,
        groupAPI: GroupAPI,
        res: GroupResponse,
        name: TextView,
        tableRow: TableRow,
        id: TextView
    ) {
        btn.setOnClickListener {
            val removeGroupCall = groupAPI.deleteGroup("token=${TokenHolder.getToken()}", res.id)
            removeGroupCall.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    showPopup(
                        "Group ${name.text} was successfully removed",
                        this@AdminGroupsActivity
                    )
                    tableRow.removeView(id)
                    tableRow.removeView(name)
                    tableRow.removeView(btn)
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.v("Retrofit", "call failed ${t.message}")
                }
            })
        }
    }

    private fun searchButtonBehavior(
        searchBtn: Button,
        groupAPI: GroupAPI,
        tableLayout: TableLayout
    ) {
        searchBtn.setOnClickListener {
            val searchInput: EditText = findViewById(R.id.search_group_input)
            if (searchInput.text.toString().isNullOrEmpty()) {
                loadAllGroups(groupAPI, tableLayout)
            } else {
                val groupByNameCall: Call<GroupResponse> = groupAPI.getGroupByName(
                    "token=${TokenHolder.getToken()}",
                    searchInput.text.toString()
                )
                groupByNameCall.enqueue(object : Callback<GroupResponse> {
                    override fun onResponse(
                        call: Call<GroupResponse>,
                        response: Response<GroupResponse>
                    ) {
                        if (response.isSuccessful) {
                            val body: GroupResponse? = response.body()
                            if (body != null) {
                                val tableRow = TableRow(this@AdminGroupsActivity)
                                tableRow.layoutParams =
                                    TableRow.LayoutParams(
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT
                                    )
                                tableRow.id
                                val id =
                                    prepareTextView(body.id.toString(), 1, this@AdminGroupsActivity)
                                val name = prepareTextView(body.name, 2, this@AdminGroupsActivity)
                                val btn = prepareImageView(3, this@AdminGroupsActivity)

                                tableRow.addView(id)
                                tableRow.addView(name)
                                tableRow.addView(btn)

                                tableLayout.removeAllViews()
                                tableLayout.addView(tableRow)

                                deleteGroupBtnBehavior(btn, groupAPI, body, name, tableRow, id)
                            } else {
                                showPopup(
                                    "Group has not been found",
                                    this@AdminGroupsActivity
                                )
                            }
                        }
                    }

                    override fun onFailure(call: Call<GroupResponse>, t: Throwable) {
                        Log.v("Retrofit", "call failed ${t.message}")
                    }
                })
            }
        }
    }
}