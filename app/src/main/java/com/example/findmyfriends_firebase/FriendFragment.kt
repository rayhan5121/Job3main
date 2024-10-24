package com.example.findmyfriends_firebase

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findmyfriends_firebase.adapter.UserAdapter
import com.example.findmyfriends_firebase.databinding.FragmentFriendBinding
import com.example.findmyfriends_firebase.view.MapsActivity
import com.example.findmyfriends_firebase.viewmodel.AuthenticationViewModel
import com.example.findmyfriends_firebase.viewmodel.FirestoreViewModel
import com.example.findmyfriends_firebase.viewmodel.LocationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class FriendFragment : Fragment() {


   private  lateinit var  binding: FragmentFriendBinding
   private  lateinit var  authenticationViewModel: AuthenticationViewModel
    private  lateinit var  userAdapter: UserAdapter
    private  lateinit var  firestoreViewModel: FirestoreViewModel
    private lateinit var  locationViewModel: LocationViewModel
    private  lateinit var  fusedLocationClient: FusedLocationProviderClient


      val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getLocation()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Location Permission denied",
                    Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFriendBinding.inflate(inflater, container, false)

        firestoreViewModel = ViewModelProvider(this).get(FirestoreViewModel::class.java)
        authenticationViewModel= ViewModelProvider(this).get(AuthenticationViewModel::class.java)
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        locationViewModel.initializeFusedLocationClient(fusedLocationClient)
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        } else {

            getLocation()
        }

        userAdapter = UserAdapter(emptyList())
        binding.userRV.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(requireContext())

        }

        fetchUsers()

        binding.locationBtn.setOnClickListener{
            startActivity(Intent(requireContext(),  MapsActivity::class.java))
        }
        return binding.root
    }
    private fun fetchUsers() {
        firestoreViewModel.getAllUsers(requireContext()){
            userAdapter.updateData(it)
        }
    }
    private fun getLocation() {
        locationViewModel.getLastLocation {
            authenticationViewModel.getCurrentUserId().let { userId ->
                firestoreViewModel.updateUserLocation(requireContext(),userId, it)
            }
        }
    }

}





