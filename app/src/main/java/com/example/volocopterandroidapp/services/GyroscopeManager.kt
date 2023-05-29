package com.example.volocopterandroidapp.services

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.volocopterandroidapp.interfaces.MovementListener

class GyroscopeManager(context: Context, private var movementListener: MovementListener) : SensorEventListener {
    private var sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var gyroscope: Sensor? = null
    var isListening: Boolean = false

    init {
        // Initialize gyroscope and accelerometer sensors
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    }

    fun startListening() {
        if (!isListening) {
            // Register gyroscope and accelerometer listeners
            gyroscope?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            }
            isListening = true
        }
    }

    fun stopListening() {
        if (isListening) {
            // Unregister gyroscope and accelerometer listeners
            sensorManager.unregisterListener(this)
            isListening = false
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            // Handle gyroscope data
            handleGyroscopeData(event.values[0], event.values[1], event.values[2])
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }

    private fun handleGyroscopeData(y: Float, x: Float, z: Float) {
        // Determine the movement based on gyroscope data and invoke corresponding methods in the main activity
        // Example implementation assuming the main activity is named MainActivity
        movementListener.moveDrone(x,y,z)
    }
}
