package tw.edu.pu.csim.ycwang.seniorcitizen_live
import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    companion object {
        private const val PREF_NAME = "UserPreferences"
        private const val KEY_USER_LOGGED_IN = "userLoggedIn"
        private const val KEY_USER_UID = "userUid"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var userLoggedIn: Boolean
        get() = sharedPreferences.getBoolean(KEY_USER_LOGGED_IN, false)
        set(value) = sharedPreferences.edit().putBoolean(KEY_USER_LOGGED_IN, value).apply()

    var userUid: String?
        get() = sharedPreferences.getString(KEY_USER_UID, null)
        set(value) = sharedPreferences.edit().putString(KEY_USER_UID, value).apply()
}
