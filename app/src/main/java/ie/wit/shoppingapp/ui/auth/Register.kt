package ie.wit.shoppingapp.ui.auth

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ie.wit.shoppingapp.activities.Home
import ie.wit.shoppingapp.databinding.RegisterBinding
import ie.wit.shoppingapp.models.User

class RegisterActivity : AppCompatActivity() {
    var database: DatabaseReference = FirebaseDatabase.getInstance("https://shoppingapp-496b2-default-rtdb.europe-west1.firebasedatabase.app/").reference

    private lateinit var registerBinding: RegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var usersDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = RegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        usersDatabaseReference = firebaseDatabase.getReference("users")

        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")

        registerBinding.registerButton.setOnClickListener {
            registerUser(email, password)
        }
    }

    private fun registerUser(email: String?, password: String?) {
        if (!validateForm()) { return }

        if (password != null) {
            if (email != null) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            val userId = user!!.uid

                            val fname = registerBinding.registerFirstname.text.toString()
                            val sname = registerBinding.registerSecondname.text.toString()
                            val gender = registerBinding.registerGender.text.toString()
                            val age = registerBinding.registerAge.text.toString()
                            val address = registerBinding.registerAddress.text.toString()
                            val dob = registerBinding.registerDoB.text.toString()
                            val newUser = User(fname, age, address,gender,sname,dob)
                            database.child("users").child(userId).setValue(newUser)


                            startActivity(Intent(this, Home::class.java))
                        } else {
                            Toast.makeText(this, "Registration failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val firstName = registerBinding.registerFirstname.text.toString()
        if (TextUtils.isEmpty(firstName)) {
            registerBinding.registerFirstname.error = "Required."
            valid = false
        } else {
            registerBinding.registerFirstname.error = null
        }

        val secondName = registerBinding.registerSecondname.text.toString()
        if (TextUtils.isEmpty(secondName)) {
            registerBinding.registerSecondname.error = "Required."
            valid = false
        } else {
            registerBinding.registerSecondname.error = null
        }

        val gender = registerBinding.registerGender.text.toString()
        if (TextUtils.isEmpty(gender)) {
            registerBinding.registerGender.error = "Required."
            valid = false
        } else {
            registerBinding.registerGender.error = null
        }

        val age = registerBinding.registerAge.text.toString()
        if (TextUtils.isEmpty(age)) {
            registerBinding.registerAge.error = "Required."
            valid = false
        } else {
            registerBinding.registerAge.error = null
        }

        val address = registerBinding.registerAddress.text.toString()
        if (TextUtils.isEmpty(address)) {
            registerBinding.registerAddress.error = "Required."
            valid = false
        } else {
            registerBinding.registerAddress.error = null
        }

        val dob = registerBinding.registerDoB.text.toString()
        if (TextUtils.isEmpty(dob)) {
            registerBinding.registerDoB.error = "Required."
            valid = false
        } else {
            registerBinding.registerDoB.error = null
        }

        return valid
    }

}