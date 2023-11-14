package com.demo.diploma.activity.teacher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ListView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.demo.diploma.R
import com.demo.diploma.activity.TeacherActivity
import com.demo.diploma.api.ExerciseAPI
import com.demo.diploma.api.TestAPI
import com.demo.diploma.callback.LoadAllGroupsCallback
import com.demo.diploma.configuration.RetrofitConfiguration
import com.demo.diploma.configuration.TokenHolder
import com.demo.diploma.fragment.AssignGroupToTest
import com.demo.diploma.fragment.ExerciseDetailsFragment
import com.demo.diploma.fragment.PopUpUpdateGroupFragment
import com.demo.diploma.fragment.PopUpUpdateTest
import com.demo.diploma.model.response.ExerciseResponse
import com.demo.diploma.model.response.GroupResponse
import com.demo.diploma.model.response.TestResponse
import com.demo.diploma.usecase.test.AssignTestToGroupUseCase
import com.demo.diploma.usecase.test.GetGroupsByTestIdUseCase
import com.demo.diploma.util.BottomNavigationUtil
import com.demo.diploma.util.ShowPopupNotificationUtil
import com.demo.diploma.util.ViewUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestDetailsActivity : AppCompatActivity() {

    private val testAPI: TestAPI = RetrofitConfiguration.getInstance()
        .create(TestAPI::class.java)

    private val exerciseAPI: ExerciseAPI = RetrofitConfiguration.getInstance()
        .create(ExerciseAPI::class.java)

    private val getGroupsByTestIdUseCase: GetGroupsByTestIdUseCase = GetGroupsByTestIdUseCase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_exercise_layout)

        val testId: Long = intent.extras?.getLong("testId") ?: 0
        val testNameView: TextView = findViewById(R.id.test_name)
        val testThemeView: TextView = findViewById(R.id.theme_name)
        val enabled: CheckBox = findViewById(R.id.enable_test)
        val updateView: ImageView = findViewById(R.id.update_test)
        val deleteView: ImageView = findViewById(R.id.delete_test)
        val tableLayout: TableLayout = findViewById(R.id.tablelayout)
        val addExercise: Button = findViewById(R.id.add_exercise)
        val assignGroupToTest: ImageView = findViewById(R.id.assign_group)
        val groupList: ListView = findViewById(R.id.groups_list)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationBar)

        BottomNavigationUtil.setBottomNavigationMenu(
            this@TestDetailsActivity,
            bottomNavigationView,
            TeacherActivity::class.java
        )

        assignGroupToTest.setOnClickListener {
            val showPopUp = AssignGroupToTest()
            val bundle = Bundle()
            bundle.putLong("testId", testId)
            showPopUp.arguments = bundle
            showPopUp.show(
                this@TestDetailsActivity.supportFragmentManager,
                "showPopUp"
            )
        }

        getGroupsByTestIdUseCase.execute(
            object : LoadAllGroupsCallback {
                override fun onSuccess(body: List<GroupResponse>) {
                    val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(this@TestDetailsActivity, android.R.layout.simple_list_item_1, body.map { it.name }.toList())
                    groupList.adapter = arrayAdapter
                }
            }, testId
        )


        addExercise.setOnClickListener {
            val intent = Intent(applicationContext, CreateExerciseActivity::class.java)
            intent.putExtra("id", testId)
            startActivity(intent)
        }

        val callExercise: Call<List<ExerciseResponse>> =
            exerciseAPI.getAllByTestId("token=${TokenHolder.getToken()}", testId)

        callExercise.enqueue(
            object : Callback<List<ExerciseResponse>> {
                override fun onResponse(
                    call: Call<List<ExerciseResponse>>,
                    response: Response<List<ExerciseResponse>>
                ) {
                    if (response.isSuccessful) {
                        Log.i("Exercises ", response.body().orEmpty().toString())
                        // TODO replace to use callback
                        response.body().orEmpty().forEach { exercise ->
                            val tableRow: TableRow = prepareRows(exercise)
                            tableLayout.addView(tableRow)
                            tableRow.setOnClickListener {
                                val showPopUp = ExerciseDetailsFragment()
                                val bundle = Bundle()
                                bundle.putLong("exerciseId", exercise.id)
                                showPopUp.arguments = bundle
                                showPopUp.show(
                                    this@TestDetailsActivity.supportFragmentManager,
                                    "showPopUp"
                                )
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<List<ExerciseResponse>>, t: Throwable) {
                    Log.v("Retrofit", "call failed ${t.message}")
                }
            }
        )

        getTest(testAPI, testId, testNameView, testThemeView, enabled)
        updateTest(updateView, testId)
        deleteTest(deleteView, testAPI, testId)
    }

    private fun getTest(
        testAPI: TestAPI,
        testId: Long,
        testNameView: TextView,
        testThemeView: TextView,
        enabled: CheckBox
    ) {
        val call: Call<TestResponse> =
            testAPI.getTestById("token=${TokenHolder.getToken()}", testId)

        call.enqueue(object : Callback<TestResponse> {
            override fun onResponse(
                call: Call<TestResponse>,
                response: Response<TestResponse>
            ) {
                if (response.isSuccessful) {
                    val testResponse: TestResponse? = response.body()
                    testNameView.text = testResponse?.subject ?: ""
                    testThemeView.text = testResponse?.theme ?: ""
                    enabled.isChecked = testResponse?.enabled ?: false

                }
            }

            override fun onFailure(call: Call<TestResponse>, t: Throwable) {
                Log.v("Retrofit", "call failed ${t.message}")
            }
        })
    }

    private fun updateTest(updateView: ImageView, testId: Long) {
        updateView.setOnClickListener {
            val showPopUp = PopUpUpdateTest()
            val bundle = Bundle()
            bundle.putLong("id", testId)
            showPopUp.arguments = bundle
            showPopUp.show(
                this@TestDetailsActivity.supportFragmentManager,
                "showPopUp"
            )
        }
    }

    private fun deleteTest(
        deleteView: ImageView,
        testAPI: TestAPI,
        testId: Long
    ) {
        deleteView.setOnClickListener {
            val call: Call<ResponseBody> =
                testAPI.deleteTest("token=${TokenHolder.getToken()}", testId)

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        ShowPopupNotificationUtil.showPopup(
                            "Test successfully deleted",
                            this@TestDetailsActivity
                        )
                        val intent = Intent(applicationContext, TeacherTestActivity::class.java)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.v("Retrofit", "call failed ${t.message}")
                }
            })
        }
    }

    private fun prepareRows(
        it: ExerciseResponse
    ): TableRow {
        val tableRow = TableRow(this@TestDetailsActivity)
        tableRow.layoutParams =
            TableRow.LayoutParams(
                200,
                TableRow.LayoutParams.WRAP_CONTENT
            )
        val id = ViewUtil.prepareTextView(it.id.toString(), 1, this@TestDetailsActivity)
        val question = ViewUtil.prepareTextView(
            if (!it.exercise.question.isNullOrEmpty()) it.exercise.question else "",
            2,
            this@TestDetailsActivity
        )

        tableRow.addView(id, 0)
        tableRow.addView(question, 1)
        return tableRow
    }
}