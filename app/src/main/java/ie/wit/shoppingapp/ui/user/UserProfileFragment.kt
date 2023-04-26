package ie.wit.shoppingapp.ui.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import ie.wit.shoppingapp.R
import ie.wit.shoppingapp.models.User

class UserFragment : Fragment() {

    private lateinit var firstNameTextView: TextView
    private lateinit var secondNameTextView: TextView
    private lateinit var genderTextView: TextView
    private lateinit var ageTextView: TextView
    private lateinit var dateOfBirthTextView: TextView
    private lateinit var addressTextView: TextView

    private lateinit var database: FirebaseDatabase
    private lateinit var userRef: DatabaseReference
    private lateinit var userListener: ValueEventListener


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        firstNameTextView = view.findViewById(R.id.firstName)
        secondNameTextView = view.findViewById(R.id.secondName)
        genderTextView = view.findViewById(R.id.gender)
        ageTextView = view.findViewById(R.id.age)
        dateOfBirthTextView = view.findViewById(R.id.DoB)
        addressTextView = view.findViewById(R.id.address)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = FirebaseDatabase.getInstance("https://shoppingapp-496b2-default-rtdb.europe-west1.firebasedatabase.app/")
        userRef = database.getReference("users").child(FirebaseAuth.getInstance().currentUser!!.uid)
        userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    firstNameTextView.text = user.firstName
                    secondNameTextView.text = user.secondName
                    genderTextView.text = user.gender
                    ageTextView.text = user.age
                    dateOfBirthTextView.text = user.DoB
                    addressTextView.text = user.address
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        userRef.addValueEventListener(userListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userRef.removeEventListener(userListener)
    }
}