package com.example.volocopterandroidapp.viewmodel

import androidx.lifecycle.ViewModel
import android.net.wifi.ScanResult
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.volocopterandroidapp.services.WifiScanner
import com.example.volocopterandroidapp.interfaces.DroneOperations
import com.example.volocopterandroidapp.interfaces.MovementListener
import com.example.volocopterandroidapp.interfaces.SwitchController

class HomeViewModel : ViewModel() {

    lateinit var wifiScanner: WifiScanner
    lateinit var switchController: SwitchController
    lateinit var movementListener: MovementListener

    var isDroneStarted = false
    lateinit var droneOperations: DroneOperations

    fun getWifiNetworks(): List<ScanResult> {
        return wifiScanner.scanWifiNetworks()
    }

    fun droneOnAndOff() {
        if (!isDroneStarted) {
            droneOperations.droneStarted()
            isDroneStarted = true
        } else {
            isDroneStarted = false
            droneOperations.droneStopped()
        }

    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun updateListByAddingMockDroneWifi(scanResults: List<ScanResult>): List<ScanResult> {
        // Create a mutable copy of the scanResults list
        val modifiedScanResults = scanResults.toMutableList()

        // Create a fake Drone item assuming
        val fakeScanResult = ScanResult()
        fakeScanResult.BSSID = "Drone"
        fakeScanResult.level = -80

        // Add the fake item to the modifiedScanResults list at the beginning
        modifiedScanResults.add(0, fakeScanResult)
        return modifiedScanResults
    }

    fun switchController() {
        switchController.switchController()
    }

    fun moveUp() {
        movementListener.moveDrone(0f, -0.1f, 0f)
    }

    fun moveDown() {
        movementListener.moveDrone(0f, 0.1f, 0f)
    }

    fun moveLeft() {
        movementListener.moveDrone(-0.1f, 0f, 0f)
    }

    fun moveRight() {
        movementListener.moveDrone(0.1f, 0f, 0f)
    }
}