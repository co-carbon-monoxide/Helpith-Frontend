package monoxide.carbon.Helpith

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import monoxide.carbon.Helpith.API.HouseWorkAPI
import monoxide.carbon.Helpith.API.HouseWorkRequest
import kotlin.concurrent.thread

class NewHouseWorkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_house_work)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "家事を追加する"

        val listId = intent.getIntExtra("LISTID", 0)

        val nameTextField: EditText = findViewById(R.id.house_work_name_text_field)
        val timeTextField: EditText = findViewById(R.id.house_work_time_text_field)
        val registerButton: Button = findViewById(R.id.house_work_register_button)
        registerButton.setOnClickListener {
            if (TextUtils.isEmpty(nameTextField.text)) {
                Toast.makeText(applicationContext,"家事名が未入力です", Toast.LENGTH_LONG).show()
            } else if (TextUtils.isEmpty(timeTextField.text)) {
                Toast.makeText(applicationContext, "時間が未入力です", Toast.LENGTH_LONG).show()
            } else {
                val houseWorkAPI = HouseWorkAPI()
                val houseWorkRequest = HouseWorkRequest(
                    name = nameTextField.text.toString(),
                    time = timeTextField.text.toString().toInt(),
                    list_id = listId
                )
                thread {
                    houseWorkAPI.create(houseWorkRequest)
                }
                Toast.makeText(applicationContext, "家事が作成されました", Toast.LENGTH_LONG).show()
                val intentSub = Intent()
                intentSub.putExtra("NEW_HOUSE_WORK_NAME", nameTextField.text.toString())
                setResult(RESULT_OK, intentSub)
                finish()
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