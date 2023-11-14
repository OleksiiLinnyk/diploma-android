package com.demo.diploma.activity.teacher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.demo.diploma.R
import com.demo.diploma.activity.TeacherActivity
import com.demo.diploma.api.TestAPI
import com.demo.diploma.configuration.RetrofitConfiguration
import com.demo.diploma.configuration.TokenHolder
import com.demo.diploma.callback.LoadTestCallback
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

        loadAllTest(testAPI, tableLayout,
            object : LoadTestCallback {
                override fun onSuccess(body: List<TestResponse>) {
                    body.forEach { res ->
                        val tableRow: TableRow = prepareRows(res)
                        tableLayout.addView(tableRow)

                        deleteRowAction(testAPI, tableRow)
                        enableAction(tableRow, testAPI)

                        tableRow.setOnClickListener {
                            val id: TextView = tableRow[0] as TextView
                            val intent = Intent(applicationContext, TestDetailsActivity::class.java)
                            intent.putExtra("testId", id.text.toString().toLong())
                            startActivity(intent)
                        }
                    }
                }
            })

        createTestBtn.setOnClickListener {
            val intent = Intent(applicationContext, CreateTestActivity::class.java)
            startActivity(intent)
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationBar)

        BottomNavigationUtil.setBottomNavigationMenu(
            this@TeacherTestActivity,
            bottomNavigationView,
            TeacherActivity::class.java
        )
    }

    private fun enableAction(tableRow: TableRow, testAPI: TestAPI) {
        val id: TextView = tableRow[0] as TextView
        val enabled: CheckBox = tableRow[3] as CheckBox

        enabled.setOnClickListener { view ->
            val checkValue = view as CheckBox
            val call: Call<TestResponse> = if (checkValue.isChecked) {
                testAPI.enableTest(
                    "token=${TokenHolder.getToken()}",
                    id.text.toString().toLong(),
                    true
                )
            } else {
                testAPI.enableTest(
                    "token=${TokenHolder.getToken()}",
                    id.text.toString().toLong(),
                    false
                )
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
                            tableRow.addView(
                                ViewUtil.prepareCheckBoxView(
                                    body.enabled,
                                    4,
                                    this@TeacherTestActivity
                                ), 3
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<TestResponse>, t: Throwable) {
                    Log.v("Retrofit", "call failed ${t.message}")
                }
            })
        }
    }

    private fun deleteRowAction(
        testAPI: TestAPI,
        tableRow: TableRow
    ) {
        val trashBtn: ImageView = tableRow[4] as ImageView
        val id: TextView = tableRow[0] as TextView
        trashBtn.setOnClickListener {
            val call: Call<ResponseBody> =
                testAPI.deleteTest("token=${TokenHolder.getToken()}", id.text.toString().toLong())

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        ShowPopupNotificationUtil.showPopup(
                            "Test successfully deleted",
                            this@TeacherTestActivity
                        )
                        tableRow.removeAllViews()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.v("Retrofit", "call failed ${t.message}")
                }
            })
        }
    }

    private fun loadAllTest(
        testAPI: TestAPI,
        tableLayout: TableLayout,
        callBack: LoadTestCallback
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
                    callBack.onSuccess(body)
                }
            }

            override fun onFailure(call: Call<List<TestResponse>>, t: Throwable) {
                Log.v("Retrofit", "call failed ${t.message}")
            }
        })
    }

    private fun prepareRows(
        it: TestResponse
    ): TableRow {
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

        tableRow.addView(id, 0)
        tableRow.addView(subject, 1)
        tableRow.addView(theme, 2)
        tableRow.addView(enabled, 3)
        tableRow.addView(trashBtn, 4)
        return tableRow
    }
}