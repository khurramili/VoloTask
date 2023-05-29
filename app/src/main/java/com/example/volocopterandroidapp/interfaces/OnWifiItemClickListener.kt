package com.example.volocopterandroidapp.interfaces

import android.net.wifi.ScanResult

interface OnWifiItemClickListener {
    fun onItemClick(scanResult: ScanResult)
}