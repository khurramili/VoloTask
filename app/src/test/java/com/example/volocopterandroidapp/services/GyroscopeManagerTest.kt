package com.example.volocopterandroidapp.services

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import com.example.volocopterandroidapp.interfaces.MovementListener
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
class GyroscopeManagerTest {

    private lateinit var gyroscopeManager: GyroscopeManager
    private lateinit var context: Context
    private lateinit var sensorManager: SensorManager
    private lateinit var gyroscope: Sensor
    private lateinit var movementListener: MovementListener

    @Before
    fun setup() {
        context = mock(Context::class.java)
        sensorManager = mock(SensorManager::class.java)
        gyroscope = mock(Sensor::class.java)
        movementListener = mock(MovementListener::class.java)

        `when`(context.getSystemService(Context.SENSOR_SERVICE)).thenReturn(sensorManager)
        `when`(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)).thenReturn(gyroscope)

        gyroscopeManager = GyroscopeManager(context, movementListener)
        gyroscopeManager.startListening()
    }


    @Test
    fun testStartListening() {
        // Prepare
        `when`(gyroscopeManager.isListening).thenReturn(false)

        // Act
        gyroscopeManager.startListening()

        // Assert
        assert(gyroscopeManager.isListening)
    }

    @Test
    fun testStopListening() {
        // Prepare
        `when`(gyroscopeManager.isListening).thenReturn(true)

        // Act
        gyroscopeManager.stopListening()

        // Assert
        assert(!gyroscopeManager.isListening)
    }

}
