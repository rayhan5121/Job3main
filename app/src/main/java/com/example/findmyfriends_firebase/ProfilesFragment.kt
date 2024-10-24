package com.example.findmyfriends_firebase

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.findmyfriends_firebase.databinding.FragmentProfilesBinding
import com.example.findmyfriends_firebase.view.LoginActivity
import com.example.findmyfriends_firebase.view.MainActivity
import com.example.findmyfriends_firebase.viewmodel.AuthenticationViewModel
import com.example.findmyfriends_firebase.viewmodel.FirestoreViewModel
import com.example.findmyfriends_firebase.viewmodel.LocationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth


class ProfilesFragment : Fragment() {

    private lateinit var binding: FragmentProfilesBinding
    private lateinit var firestoreViewModel: FirestoreViewModel
    private lateinit var authenticationViewModel: AuthenticationViewModel
    private lateinit var loactionViewModel: LocationViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val firebaseAuth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentProfilesBinding.inflate(inflater, container, false)
        authenticationViewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)
        firestoreViewModel = ViewModelProvider(this).get(FirestoreViewModel::class.java)
        loactionViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)


        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))

        }

        binding.homeBtn.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }
        loadUserInfo()


        binding.updateBtn.setOnClickListener {
            val Newname = binding.nameEt.text.toString()
            val NewLocation = binding.locationEt.text.toString()

            updateUserInfo(Newname, NewLocation)
        }
        return binding.root
    }

    private fun updateUserInfo(newname: String, newLocation: String) {
        val currentUser = authenticationViewModel.getCurrentUser()
        if (currentUser != null) {
            val userId = currentUser.uid
            firestoreViewModel.updateUser(requireContext(), userId, newname, newLocation)
            Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
        } else {


            Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
        }
    }


    private fun loadUserInfo() {
        val currentUser = authenticationViewModel.getCurrentUser()
        if (currentUser != null) {
          binding.emailEt .setText(currentUser.email)


            firestoreViewModel.getUser(requireContext(), currentUser.uid) {
                if (it != null) {
                    binding.nameEt.setText(it.displayName)


                    firestoreViewModel.getUserLocation (requireContext(), currentUser.uid) {
                        if (it.isNotEmpty()) {
                            binding.locationEt.setText(it)
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()

                }
            }
        } else {
            Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()

        }
    }

}
