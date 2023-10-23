package com.demo.helloworld.activity.teacher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.demo.helloworld.R
import com.demo.helloworld.activity.TeacherActivity
import com.demo.helloworld.api.GroupAPI
import com.demo.helloworld.api.TestAPI
import com.demo.helloworld.configuration.RetrofitConfiguration
import com.demo.helloworld.configuration.TokenHolder
import com.demo.helloworld.model.request.TestUpsertRequest
import com.demo.helloworld.model.response.GroupResponse
import com.demo.helloworld.model.response.TestResponse
import com.demo.helloworld.util.BottomNavigationUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_test_layout)

        val nextBtn: Button = findViewById(R.id.next_btn)
        val backBtn: Button = findViewById(R.id.back_btn)
        val themeView: EditText = findViewById(R.id.theme)
        val subjectView: EditText = findViewById(R.id.subject)
        val enableCheckBoxView: CheckBox = findViewById(R.id.enableCheckBox)
        val groupView: AutoCompleteTextView = findViewById(R.id.groupTestView)

        val testAPI: TestAPI = RetrofitConfiguration.getInstance()
            .create(TestAPI::class.java)
        val groupAPI: GroupAPI = RetrofitConfiguration.getInstance()
            .create(GroupAPI::class.java)
        val groups: MutableList<GroupResponse> = mutableListOf()

        setupGroupsToSelector(
            groupAPI.getAllGroups("token=${TokenHolder.getToken()}"),
            groups,
            groupView
        )

        backBtn.setOnClickListener {
            val intent = Intent(applicationContext, TeacherTestActivity::class.java)
            startActivity(intent)
        }

        nextBtn.setOnClickListener {
            val theme: String = themeView.text.toString()
            val subject: String = subjectView.text.toString()
            val groupView: AutoCompleteTextView = findViewById(R.id.groupTestView)
            val group: String = groupView.text.toString()

            val call: Call<TestResponse> = testAPI.createTest(
                "token=${TokenHolder.getToken()}",
                TestUpsertRequest(subject, theme)
            )

            call.enqueue(object : Callback<TestResponse> {
                override fun onResponse(
                    call: Call<TestResponse>,
                    response: Response<TestResponse>
                ) {
                    if (response.isSuccessful) {
                        val body: TestResponse? = response.body()
                        if (body != null) {
                            if (enableCheckBoxView.isChecked) {
                                val checkBoxCall: Call<TestResponse> = testAPI.enableTest(
                                    "token=${TokenHolder.getToken()}",
                                    body.id,
                                    true
                                )
                                checkBoxCall.enqueue(object : Callback<TestResponse> {
                                    override fun onResponse(
                                        call: Call<TestResponse>,
                                        response: Response<TestResponse>
                                    ) {
                                        if (response.isSuccessful) {
                                            Log.v("[Create Test]", "Activated Test")
                                        }
                                    }

                                    override fun onFailure(call: Call<TestResponse>, t: Throwable) {
                                        Log.v("Retrofit", "call failed ${t.message}")
                                    }
                                })
                            }
                            if (!group.isNullOrEmpty()) {
//TODO implements groups saving
                            }
                            val intent =
                                Intent(applicationContext, CreateExerciseActivity::class.java)
                            intent.putExtra("id", body.id)
                            startActivity(intent)
                        }
                    }
                }

                override fun onFailure(call: Call<TestResponse>, t: Throwable) {
                    Log.v("Retrofit", "call failed ${t.message}")
                }
            })
        }


        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationBar)

        BottomNavigationUtil.setBottomNavigationMenu(
            this@CreateTestActivity,
            bottomNavigationView,
            TeacherActivity::class.java
        )
    }

    private fun setupGroupsToSelector(
        callGroup: Call<List<GroupResponse>>,
        groups: MutableList<GroupResponse>,
        autoCompleteTextView: AutoCompleteTextView
    ) {
        val groupNames: MutableList<String> = mutableListOf()
        callGroup.enqueue(object : Callback<List<GroupResponse>> {
            override fun onResponse(
                call: Call<List<GroupResponse>>,
                response: Response<List<GroupResponse>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body().orEmpty()
                    groups.addAll(body)
                    groupNames.addAll(body.map { it.name }.toList())
                }
            }

            override fun onFailure(call: Call<List<GroupResponse>>, t: Throwable) {
                Log.v("Retrofit", "call failed ${t.message}")
            }
        })
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.groups, groupNames)
        autoCompleteTextView.setAdapter(arrayAdapter)
    }
}