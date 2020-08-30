package monoxide.carbon.Helpith

import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import kotlinx.android.synthetic.main.activity_config.*
import monoxide.carbon.Helpith.API.UserAPI
import monoxide.carbon.Helpith.API.UserRequest
import org.json.JSONArray
import org.json.JSONObject
import kotlin.concurrent.thread

class ConfigActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        val usersJsonString = intent.getStringExtra("USERS_JSON")
        val usersJson = JSONArray(usersJsonString)
        val familyId = intent.getIntExtra("FAMILY_ID", -1)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "家族設定"

        val userAPI = UserAPI()

        val userLinearLayout: LinearLayout = findViewById(R.id.user_linear_layout)

        val addUserTextEdit: EditText = findViewById(R.id.add_user_text_edit)
        val addUserButton: Button = findViewById(R.id.add_user_button)
        addUserButton.setOnClickListener {
            val newName = addUserTextEdit.text.toString()
            val userRequest = UserRequest(
                name = newName,
                family_id = familyId
            )
            thread {
                userAPI.create(userRequest)
            }
            Toast.makeText(applicationContext, "「$newName」さんを追加しました", Toast.LENGTH_LONG).show()
            finish()
        }

        for (i in 0 until usersJson.length()) {
            val user = usersJson[i] as JSONObject
            val id = user.optInt("id")
            val name = user.optString("name")
            layoutInflater.inflate(R.layout.config_form, userLinearLayout)
            val configForm = userLinearLayout.getChildAt(i) as ViewGroup
            val horizontalLinearLayout = configForm.getChildAt(0) as ViewGroup
            val nameTextField: EditText = horizontalLinearLayout.getChildAt(1) as EditText
            nameTextField.setText(name)
            val changeButton = horizontalLinearLayout.getChildAt(2) as Button
            changeButton.setOnClickListener {
                val newName = nameTextField.text.toString()
                thread {
                    userAPI.putName(id, newName)
                }
                Toast.makeText(applicationContext, "名前を「$newName」に変更しました", Toast.LENGTH_LONG).show()
            }
            val deleteButton = horizontalLinearLayout.getChildAt(3) as Button
            deleteButton.setOnClickListener {
                thread {
                    userAPI.delete(id)
                }
                Toast.makeText(applicationContext, "「$name」さんを削除しました", Toast.LENGTH_LONG).show()
                userLinearLayout.removeView(configForm)
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