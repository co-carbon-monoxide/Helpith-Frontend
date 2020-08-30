package monoxide.carbon.Helpith

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import monoxide.carbon.Helpith.API.HouseWorkAPI
import monoxide.carbon.Helpith.API.HouseWorkRequest
import org.json.JSONArray
import org.json.JSONObject
import kotlin.concurrent.thread

class NewHouseWorkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_house_work)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "家事を追加する"

        val listId = intent.getIntExtra("LISTID", 0)
        val userNames = intent.getStringArrayExtra("USER_NAME")

        val usersJsonStr = intent.getStringExtra("USERS_JSON")
        val usersJsonArr = JSONArray(usersJsonStr)
        var usersHash = mutableMapOf<String, Int>()
        for (i in 0 until usersJsonArr.length()) {
            val user = usersJsonArr[i] as JSONObject
            val name = user.optString("name")
            val id = user.optInt("id")
            usersHash[name] = id
        }

        val spinner: Spinner = findViewById(R.id.spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userNames)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val nameTextField: EditText = findViewById(R.id.house_work_name_text_field)
        val timeTextField: EditText = findViewById(R.id.house_work_time_text_field)
        val registerButton: Button = findViewById(R.id.house_work_register_button)
        registerButton.setOnClickListener {
            println(spinner.selectedItem)
            if (TextUtils.isEmpty(nameTextField.text)) {
                Toast.makeText(applicationContext,"家事名が未入力です", Toast.LENGTH_LONG).show()
            } else if (TextUtils.isEmpty(timeTextField.text)) {
                Toast.makeText(applicationContext, "時間が未入力です", Toast.LENGTH_LONG).show()
            } else {
                val houseWorkAPI = HouseWorkAPI()
                val houseWorkRequest = HouseWorkRequest(
                    name = nameTextField.text.toString(),
                    time = timeTextField.text.toString().toInt(),
                    list_id = listId,
                    done = false,
                    user_id = usersHash[spinner.selectedItem] as Int
                )
                Toast.makeText(applicationContext, "家事が作成されました", Toast.LENGTH_LONG).show()

                thread {
                    val res = houseWorkAPI.create(houseWorkRequest)
                    val resJson = JSONObject(res)
                    println(resJson)
                    val intentSub = Intent()
                    intentSub.putExtra("NEW_HOUSE_WORK_RES", res)
                    intentSub.putExtra("NEW_HOUSE_WORK_NAME", nameTextField.text.toString())
                    setResult(RESULT_OK, intentSub)
                    finish()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}