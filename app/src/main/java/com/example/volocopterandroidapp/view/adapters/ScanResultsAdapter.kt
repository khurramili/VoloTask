package com.example.volocopterandroidapp.view.adapters

import android.net.wifi.ScanResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.volocopterandroidapp.R
import com.example.volocopterandroidapp.interfaces.OnWifiItemClickListener

class ScanResultsAdapter(
    private val scanResults: List<ScanResult>,
    private val onItemClickListener: OnWifiItemClickListener?) :
    RecyclerView.Adapter<ScanResultsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ssidTextView: TextView = itemView.findViewById(R.id.ssidTextView)
        val connectButton: Button = itemView.findViewById(R.id.connectButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scan_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scanResult = scanResults[position]
        holder.ssidTextView.text = scanResult.SSID ?: scanResult.BSSID
        holder.connectButton.setOnClickListener {
            // Handle click event for the specific WiFi item
            // You can implement your desired behavior here
            onItemClickListener?.onItemClick(scanResult)
        }
    }

    override fun getItemCount(): Int = scanResults.size
}