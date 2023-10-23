package com.demo.diploma.activity.teacher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.TableLayout
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import com.demo.diploma.R
import com.demo.diploma.activity.TeacherActivity
import com.demo.diploma.api.TestAPI
import com.demo.diploma.configuration.RetrofitConfiguration
import com.demo.diploma.configuration.TokenHolder
import com.demo.diploma.model.response.TestResponse
import com.demo.diploma.util.BottomNavigationUtil
import com.demo.diploma.util.ShowPopupNotificationUtil
import com.demo.diploma.util.ViewUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeacherTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.teacher_tests_layout)

        val testAPI: TestAPI = RetrofitConfiguration.getInstance()
            .create(TestAPI::class.java)
        val tableLayout: TableLayout = findViewById(R.id.tablelayout)
        val createTestBtn: Button = findViewById(R.id.create_test_btn)


        loadAllTest(testAPI, tableLayout)

        createTestBtn.setOnClickListener {
            val intent = Intent(applicationContext, CreateTestActivity::class.java)
            startActivity(intent)
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationBar)

        BottomNavigationUtil.setBottomNavigationMenu(this@TeacherTestActivity, bottomNavigationView, TeacherActivity::class.java)
    }

    private fun loadAllTest(
        testAPI: TestAPI,
        tableLayout: TableLayout
    ) {
        val call: Call<List<TestResponse>> =
            testAPI.getTeacherTest("token=${TokenHolder.getToken()}")

        call.enqueue(object : Callback<List<TestResponse>> {
            override fun onResponse(
                call: Call<List<TestResponse>>,
                response: Response<List<TestResponse>>
            ) {
                if (response.isSuccessful) {
                    tableLayout.removeAllViews()
                    val body: List<TestResponse> = response.body().orEmpty()
                    body.forEach { res ->
                        prepareRows(res, tableLayout, testAPI)
                    }
                }
            }

            override fun onFailure(call: Call<List<TestResponse>>, t: Throwable) {
                Log.v("Retrofit", "call failed ${t.message}")
            }
        })
    }

    private fun prepareRows(
        it: TestResponse,
        tableLayout: TableLayout,
        testAPI: TestAPI
    ) {
        val tableRow = TableRow(this@TeacherTestActivity)
        tableRow.layoutParams =
            TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
        val id = ViewUtil.prepareTextView(it.id.toString(), 1, this@TeacherTestActivity)
        val theme = ViewUtil.prepareTextView(
            if (!it.theme.isNullOrEmpty()) it.theme else "",
            3,
            this@TeacherTestActivity
        )
        val subject = ViewUtil.prepareTextView(
            if (!it.subject.isNullOrEmpty()) it.subject else "",
            2,
            this@TeacherTestActivity
        )
        val enabled = ViewUtil.prepareCheckBoxView(it.enabled, 4, this@TeacherTestActivity)
        val trashBtn = ViewUtil.prepareImageView(5, this@TeacherTestActivity)

        tableRow.addView(id)
        tableRow.addView(subject)
        tableRow.addView(theme)
        tableRow.addView(enabled)
        tableRow.addView(trashBtn)

        enabled.setOnClickListener { view ->
            val checkValue = view as CheckBox
            val call: Call<TestResponse> = if (checkValue.isChecked) {
                testAPI.enableTest("token=${TokenHolder.getToken()}", it.id, true)
            } else {
                testAPI.enableTest("token=${TokenHolder.getToken()}", it.id, false)
            }
            call.enqueue(object : Callback<TestResponse> {
                override fun onResponse(
                    call: Call<TestResponse>,
                    response: Response<TestResponse>
                ) {
                    if (response.isSuccessful) {
                        val body: TestResponse? = response.body()
                        if (body != null) {
                            tableRow.removeView(enabled)
                            tableRow.addView(ViewUtil.prepareCheckBoxView(body.enabled, 4, this@TeacherTestActivity), 3)
                        }
                    }
                }

                override fun onFailure(call: Call<TestResponse>, t: Throwable) {
                    Log.v("Retrofit", "call failed ${t.message}")
                }
            })
        }

        tableLayout.addView(tableRow)

        trashBtn.setOnClickListener {
            val call: Call<ResponseBody> = testAPI.deleteTest("token=${TokenHolder.getToken()}", id.text.toString().toLong())

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        ShowPopupNotificationUtil.showPopup("Test successfully deleted", this@TeacherTestActivity)
                        tableLayout.removeView(tableRow)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.v("Retrofit", "call failed ${t.message}")
                }
            })
        }
    }
}