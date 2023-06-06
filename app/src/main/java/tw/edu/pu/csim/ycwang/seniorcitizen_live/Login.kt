package tw.edu.pu.csim.ycwang.seniorcitizen_live

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

class Login : AppCompatActivity() {

    lateinit var txv2: TextView
    lateinit var actionBar: ActionBar
    lateinit var editTextEmail: EditText
    lateinit var editTextPassword: EditText
    lateinit var btnIn: Button
    lateinit var firebaseAuth: FirebaseAuth

    private val errorTranslations = mapOf(
        "The email address is badly formatted." to "Email格式不正確",
        "The password is invalid or the user does not have a password." to "密碼錯誤",
        "There is no user record corresponding to this identifier. The user may have been deleted." to "此帳號尚未註冊，請進行註冊"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        actionBar = supportActionBar!!
        actionBar.hide()

        txv2 = findViewById(R.id.txv2)
        txv2.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        }

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        btnIn = findViewById(R.id.btnIn)

        // 初始化 Firebase 身分驗證
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser != null) {
            val userUid = firebaseAuth.currentUser?.uid
            // 用戶已經登錄，直接導航到主畫面
            navigateToHomeScreen(userUid)
        }

        // 處理登陸按鈕的點擊事件
        btnIn.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            }
            else{
                Toast.makeText(this, "請輸入帳號和密碼", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        // 使用 Firebase 身分驗證進行登錄
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                task -> if (task.isSuccessful) {
                // 登錄成功，處理下一步操作，例如導航到主畫面
                val user: FirebaseUser? = firebaseAuth.currentUser
                if (user != null) {
                    // 使用者已登錄
                    Toast.makeText(this, "登入成功", Toast.LENGTH_SHORT).show()
                    val userUid = firebaseAuth.currentUser?.uid
                    // 保存登錄狀態到本地
                    saveUserLoggedInState(userUid)
                    // 導航到主畫面
                    navigateToHomeScreen(userUid)
                }

            }
            else {
                val errorMessage = task.exception?.message
                val translatedError = errorTranslations[errorMessage]
                val displayMessage = translatedError ?: "登入失敗:$errorMessage"

                Toast.makeText(this, displayMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserLoggedInState(userUid: String?) {
        val userPreferences = UserPreferences(this)
        userPreferences.userLoggedIn = true
        userPreferences.userUid = userUid
    }


    private fun navigateToHomeScreen(userUid: String?) {
        // 登錄成功，轉到 HomeScreen
        val intent1 = Intent(this, HomeScreen::class.java)
        intent1.putExtra("userUid", userUid)
        startActivity(intent1)
        finish() // 結束當前的 Activity，以便用戶無法返回登錄頁面
    }

}