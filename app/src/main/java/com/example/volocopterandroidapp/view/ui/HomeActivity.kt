package com.example.volocopterandroidapp.view.ui

import android.net.wifi.ScanResult
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.volocopterandroidapp.R
import com.example.volocopterandroidapp.databinding.ActivityHomeBinding
import com.example.volocopterandroidapp.utils.DroneSound
import com.example.volocopterandroidapp.services.GyroscopeManager
import com.example.volocopterandroidapp.services.WifiScanner
import com.example.volocopterandroidapp.utils.CollisionAlert
import com.example.volocopterandroidapp.utils.PermissionHelper
import com.example.volocopterandroidapp.view.dialog.ScanResultsDialog
import com.example.volocopterandroidapp.interfaces.DroneOperations
import com.example.volocopterandroidapp.interfaces.MovementListener
import com.example.volocopterandroidapp.interfaces.OnWifiItemClickListener
import com.example.volocopterandroidapp.interfaces.SwitchController
import com.example.volocopterandroidapp.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity(), OnWifiItemClickListener, DroneOperations,
    MovementListener, SwitchController {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var scanResultsDialog: ScanResultsDialog
    private lateinit var permissionHelper: PermissionHelper
    private lateinit var gyroscopeManager: GyroscopeManager
    private lateinit var droneSound: DroneSound
    private lateinit var collisionAlert: CollisionAlert


    private var posX: Float = 0f
    private var posY: Float = 0f
    private var posZ: Float = 0f

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Permission Helper class to handle permission
        permissionHelper = PermissionHelper(this)

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.homeViewModel = homeViewModel
        homeViewModel.droneOperations = this
        homeViewModel.switchController = this
        homeViewModel.movementListener = this

        if (permissionHelper.checkAndRequestLocationPermission()) {
            homeViewModel.wifiScanner = WifiScanner(this)
            val scanResults = homeViewModel.getWifiNetworks()
            processScanResults(scanResults)
        }

    }

    override fun onItemClick(scanResult: ScanResult) {
        Toast.makeText(this, "Connected with " + scanResult.BSSID, Toast.LENGTH_SHORT).show()
        scanResultsDialog.dismiss()
    }


    @RequiresApi(Build.VERSION_CODES.R)
    private fun processScanResults(scanResults: List<ScanResult>) {

        val modifiedScanResults = homeViewModel.updateListByAddingMockDroneWifi(scanResults)

        // Process the list of Wi-Fi networks and show in DialogBox
        scanResultsDialog = ScanResultsDialog(modifiedScanResults, this)
        scanResultsDialog.show(supportFragmentManager, "ScanResultsDialog")

    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper.handlePermissionResult(
            requestCode,
            grantResults,
            {
                // Location permission granted
                // Proceed with scanning or any other logic
                homeViewModel.wifiScanner = WifiScanner(this)
                val scanResults = homeViewModel.getWifiNetworks()
                processScanResults(scanResults)
            },
            {
                // Location permission denied
                // Handle the denial case, show a message, or disable location-related functionality
            }
        )
    }

    override fun droneStarted() {

        binding.startStopDrone.text = this.getString(R.string.stop_drone)

        // Create an instance of GyroscopeManager and start listening
        gyroscopeManager = GyroscopeManager(this, this)
        droneSound = DroneSound(this, R.raw.drone_sound)
        collisionAlert = CollisionAlert(this,R.raw.alert_sound)
        droneSound.start()
        gyroscopeManager.startListening()

    }

    override fun droneStopped() {
        binding.droneIV.clearAnimation()
        binding.startStopDrone.text = this.getString(R.string.start_drone)
        gyroscopeManager.stopListening()
        droneSound.stop()
        collisionAlert.stop()
    }

    override fun moveDrone(x: Float, y: Float, z: Float) {

        // Update the position
        posX = binding.droneIV.x - (x * 100) //
        posY = binding.droneIV.y + (y * 220)
        posZ = binding.droneIV.z + (z * 100)

        // Check collision with screen boundaries
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels

        if (posX < 0) {
            posX = 0f
            // Handle collision with left screen boundary
            collisionAlert()
        } else if (posX > screenWidth - binding.droneIV.width) {
            posX = (screenWidth - binding.droneIV.width).toFloat()
            // Handle collision with right screen boundary
            collisionAlert()
        }

        if (posY < 0) {
            posY = 0f
            // Handle collision with top screen boundary
            collisionAlert()
        } else if (posY > screenHeight - binding.droneIV.height) {
            posY = (screenHeight - binding.droneIV.height).toFloat()
            // Handle collision with bottom screen boundary
            collisionAlert()
        }

        // Update the ImageView position
        binding.droneIV.x = posX
        binding.droneIV.y = posY
        binding.droneIV.z = posZ
    }

    private fun collisionAlert() {
        collisionAlert.start()
    }

    override fun onPause() {
        super.onPause()
        if (::gyroscopeManager.isInitialized)
            gyroscopeManager.stopListening()
    }

    override fun onResume() {
        super.onResume()
        if (::gyroscopeManager.isInitialized)
            gyroscopeManager.startListening()
    }

    override fun switchController() {
        if (binding.switchController.isChecked) {
            if (::gyroscopeManager.isInitialized)
                gyroscopeManager.stopListening()
            binding.manualControllerLayout.visibility = View.VISIBLE
        } else {
            if (::gyroscopeManager.isInitialized)
                gyroscopeManager.startListening()
            binding.manualControllerLayout.visibility = View.GONE
        }
    }
}