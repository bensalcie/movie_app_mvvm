package app.bensalcie.movieapp.ui.notifications

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import app.bensalcie.movieapp.R
import com.google.firebase.database.*

class NotificationsFragment : Fragment() {
    private lateinit var tvNotifications:TextView
    private lateinit var ctx:Context
    private lateinit var root:View
    private lateinit var notificationsDatabase:DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ctx = container!!.context
        root = inflater.inflate(R.layout.fragment_notifications, container, false)
        tvNotifications = root.findViewById(R.id.tv_notification_text)
        notificationsDatabase = FirebaseDatabase.getInstance().reference.child("MOVIES2021/NOTIFICATIONS")

        notificationsDatabase.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
              val notification = snapshot.child("1").value.toString()
                tvNotifications.text = notification
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        return  root
    }
}