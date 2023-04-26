//package ie.wit.shoppingapp.ui.auth
//
//import android.content.Intent
//import android.os.Bundle
//import android.text.TextUtils
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.ViewModelProvider
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import ie.wit.shoppingapp.R
//import ie.wit.shoppingapp.activities.Home
//import ie.wit.shoppingapp.databinding.FragmentProductsBinding
//import ie.wit.shoppingapp.databinding.RegisterBinding
//import timber.log.Timber
//
//class RegisterActivity : AppCompatActivity() {
//
//    private lateinit var registerViewModel: LoginRegisterViewModel
//    private lateinit var registerBinding: RegisterBinding
//    private lateinit var auth: FirebaseAuth
//    private lateinit var database: FirebaseDatabase
//    private lateinit var usersRef: DatabaseReference
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        registerBinding = RegisterBinding.inflate(layoutInflater)
//        setContentView(registerBinding.root)
//
//        registerViewModel = ViewModelProvider(this).get(LoginRegisterViewModel::class.java)
//
//        auth = FirebaseAuth.getInstance()
//        database = FirebaseDatabase.getInstance()
//        usersRef = database.reference.child("users")
//
//        registerBinding.registerButton.setOnClickListener {
//            signUp(
//                registerBinding.registerEmail.text.toString(),
//                registerBinding.registerPassword.text.toString(),
//                registerBinding.registerName.text.toString(),
//                registerBinding.registerAddress.text.toString()
//            )
//        }
//
//        registerBinding.registerButton.setOnClickListener {
//            startActivity(Intent(this, Home::class.java))
//        }
//    }
//
//    private fun signUp(email: String, password: String, name: String, phoneNumber: String) {
//        Timber.d("signUp:$email")
//        if (!validateForm()) {
//            return
//        }
//
//        registerViewModel.register(email, password)
//
//        registerViewModel.liveFirebaseUser.observe(this) { user ->
//            if (user != null) {
//                val userId = user.uid
//                val userInfo = HashMap<String, Any>()
//                userInfo["name"] = name
//                userInfo["phone_number"] = phoneNumber
//
//                // Save user info to database
//                usersRef.child(userId).setValue(userInfo)
//
//                startActivity(Intent(this, Home::class.java))
//                finish()
//            } else {
//                Toast.makeText(
//                    baseContext, "Authentication failed.",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }
//
//    private fun validateForm(): Boolean {
//        var valid = true
//
//        val email = registerBinding.registerEmail.text.toString()
//        if (TextUtils.isEmpty(email)) {
//            registerBinding.registerEmail.error = "Required."
//            valid = false
//        } else {
//            registerBinding.registerEmail.error = null
//        }
//
//        val password = registerBinding.registerPassword.text.toString()
//        if (TextUtils.isEmpty(password)) {
//            registerBinding.registerPassword.error = "Required."
//            valid = false
//        } else {
//            registerBinding.registerPassword.error = null
//        }
//
//        val name = registerBinding.registerName.text.toString()
//        if (TextUtils.isEmpty(name)) {
//            registerBinding.registerName.error = "Required."
//            valid = false
//        } else {
//            registerBinding.registerName.error = null
//        }
//
//        val address = registerBinding.registerAddress.text.toString()
//        if (TextUtils.isEmpty(address)) {
//            registerBinding.registerAddress.error = "Required."
//            valid = false
//        } else {
//            registerBinding.registerAddress.error = null
//        }
//
//        return valid
//    }
//}
