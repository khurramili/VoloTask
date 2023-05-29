package com.example.volocopterandroidapp.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import androidx.core.content.ContextCompat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class WifiScannerTest {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var wifiManager: WifiManager

    private lateinit var wifiScanner: WifiScanner

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        `when`(context.applicationContext).thenReturn(context)
        `when`(context.getSystemService(Context.WIFI_SERVICE)).thenReturn(wifiManager)
        wifiScanner = WifiScanner(context)
    }

    @Test
    fun testScanWifiNetworks_permissionGranted() {
        // Prepare
        val scanResults = listOf(ScanResult(), ScanResult(), ScanResult())
        `when`(wifiManager.startScan()).thenReturn(true)
        `when`(wifiManager.scanResults).thenReturn(scanResults)
        `when`(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ).thenReturn(PackageManager.PERMISSION_GRANTED)

        // Act
        val result = wifiScanner.scanWifiNetworks()

        // Assert
        assertEquals(scanResults, result)
    }

    @Test
    fun testScanWifiNetworks_permissionNotGranted() {
        // Prepare
        `when`(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ).thenReturn(PackageManager.PERMISSION_DENIED)

        // Act
        val result = wifiScanner.scanWifiNetworks()

        // Assert
        assertEquals(emptyList<ScanResult>(), result)
    }
}
