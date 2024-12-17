package com.example.sensorexperimentapp;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener { // Implement SensorEventListener
    private Sensor accelerometer, proximitySensor, lightSensor, rotationVectorSensor;
    private SensorManager sensorManager;
    private TextView sensorListTextView, accelerometerData, proximityData, lightData, orientationData; // Added orientationData TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Initialize TextViews (You need to add these IDs in activity_main.xml)
        accelerometerData = findViewById(R.id.accelerometerData);
        proximityData = findViewById(R.id.proximityData);
        lightData = findViewById(R.id.lightData);
        orientationData = findViewById(R.id.orientationData); // Initialize orientationData TextView

        Button detectSensorsButton = findViewById(R.id.detectSensorButton);
        sensorListTextView = findViewById(R.id.sensorListTextView);

        detectSensorsButton.setOnClickListener(v -> ListAvailableSensors());

        // Initialize sensors
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR); // Initialize rotation vector sensor

        // Register listeners
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (proximitySensor != null) {
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (rotationVectorSensor != null) {
            sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL); // Register rotation vector sensor
        }
    }

    private void ListAvailableSensors() {
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuilder sensorInfo = new StringBuilder("Available Sensors: \n");
        for (Sensor sensor : sensorList) {
            sensorInfo.append(sensor.getName()).append(" (").append(sensor.getType()).append(")\n");
        }
        sensorListTextView.setText(sensorInfo.toString());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            accelerometerData.setText("Accelerometer Data: X=" + x + ", Y=" + y + ", Z=" + z);
        } else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            proximityData.setText("Proximity Data: " + event.values[0]);
        } else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            lightData.setText("Light Sensor Data: " + event.values[0]);
        } else if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            float[] rotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

            float[] orientation = new float[3];
            SensorManager.getOrientation(rotationMatrix, orientation);

            orientationData.setText("Orientation: " +
                    "Azimuth=" + Math.toDegrees(orientation[0]) +
                    ", Pitch=" + Math.toDegrees(orientation[1]) +
                    ", Roll=" + Math.toDegrees(orientation[2]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Leave empty if not needed
    }
}
