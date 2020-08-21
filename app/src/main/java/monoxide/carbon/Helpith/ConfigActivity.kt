package monoxide.carbon.Helpith

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConfigActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        val returnButton: Button = findViewById(R.id.config_return_button)
        returnButton.setOnClickListener {
            finish()
        }
    }

}