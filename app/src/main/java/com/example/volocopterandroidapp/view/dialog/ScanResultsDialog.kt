package com.example.volocopterandroidapp.view.dialog

import android.app.Dialog
import android.net.wifi.ScanResult
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.volocopterandroidapp.R
import com.example.volocopterandroidapp.view.adapters.ScanResultsAdapter
import com.example.volocopterandroidapp.interfaces.OnWifiItemClickListener

class ScanResultsDialog(
    private val scanResults: List<ScanResult>,
    private val onItemClickListener: OnWifiItemClickListener?
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(activity)
        val dialogView = inflater.inflate(R.layout.popup_layout, null)

        val recyclerView: RecyclerView = dialogView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = ScanResultsAdapter(scanResults, onItemClickListener)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(dialogView)
        builder.setTitle("Connect with Drone")

        return builder.create()
    }
}
