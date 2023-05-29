package com.example.volocopterandroidapp.viewmodel

import android.net.wifi.ScanResult
import com.example.volocopterandroidapp.services.WifiScanner
import com.example.volocopterandroidapp.interfaces.DroneOperations
import com.example.volocopterandroidapp.interfaces.MovementListener
import com.example.volocopterandroidapp.interfaces.SwitchController
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class HomeViewModelTest {

    @Mock
    private lateinit var wifiScanner: WifiScanner

    @Mock
    private lateinit var switchController: SwitchController

    @Mock
    private lateinit var movementListener: MovementListener

    @Mock
    private lateinit var droneOperations: DroneOperations

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        homeViewModel = HomeViewModel().apply {
            this.wifiScanner = this@HomeViewModelTest.wifiScanner
            this.switchController = this@HomeViewModelTest.switchController
            this.movementListener = this@HomeViewModelTest.movementListener
            this.droneOperations = this@HomeViewModelTest.droneOperations
        }
    }

    @Test
    fun testGetWifiNetworks() {
        // Prepare
        val expectedScanResults = listOf(ScanResult(), ScanResult(), ScanResult())
        `when`(wifiScanner.scanWifiNetworks()).thenReturn(expectedScanResults)

        // Act
        val scanResults = homeViewModel.getWifiNetworks()

        // Assert
        assertEquals(expectedScanResults, scanResults)
    }

    @Test
    fun testDroneOnAndOff() {
        // Prepare
        doNothing().`when`(droneOperations).droneStarted()
        doNothing().`when`(droneOperations).droneStopped()

        // Act
        homeViewModel.droneOnAndOff() // Initial state, turn on drone
        homeViewModel.droneOnAndOff() // Drone already started, turn off drone

        // Assert
        assertEquals(false, homeViewModel.isDroneStarted)
    }

    @Test
    fun testUpdateListByAddingMockDroneWifi() {
        // Prepare
        val scanResults = listOf(ScanResult(), ScanResult(), ScanResult())

        // Act
        val modifiedScanResults = homeViewModel.updateListByAddingMockDroneWifi(scanResults)

        // Assert
        assertEquals(scanResults.size + 1, modifiedScanResults.size)
        assertEquals("Drone", modifiedScanResults[0].BSSID)
        assertEquals(-80, modifiedScanResults[0].level)
    }

}
