package tw.edu.pu.csim.ycwang.seniorcitizen_live

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userPreferences = UserPreferences(this)

        if (userPreferences.userLoggedIn) {
            val userUid = userPreferences.userUid
            // 使用者已登入，導航到主畫面並傳遞使用者的 UID
            navigateToHomeScreen(userUid)
        }
        else {
            // 使用者未登入，導航到登入頁面
            navigateToLogin()
        }

    }

    private fun navigateToLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToHomeScreen(userUid: String?) {
        val intent = Intent(this, HomeScreen::class.java)
        intent.putExtra("userUid", userUid)
        startActivity(intent)
        finish()
    }
}