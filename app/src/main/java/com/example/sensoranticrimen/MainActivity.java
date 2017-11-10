package com.example.sensoranticrimen;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private TextView temperaturelabel;
    private TextView distanceLabel;
    private SensorManager mSensorManager;
    private Sensor mTemperature;
    private Sensor mProximity;
    private static final int SENSOR_SENSITIVITY = 4;


    private final static String NOT_SUPPORTED_MESSAGE = "Este celular no es tan privilegiado como para tener un termometro integrado.";
    private final static String NOT_SUPPORTED_MESSAGE2 = "Ni el de distancia tienes?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temperaturelabel = (TextView) findViewById(R.id.myTemp);
        distanceLabel = (TextView) findViewById(R.id.myDistance);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            mTemperature= mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            mSensorManager.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mTemperature == null) {
            temperaturelabel.setText(NOT_SUPPORTED_MESSAGE);
        }

        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (mProximity != null) {
            distanceLabel.setText(NOT_SUPPORTED_MESSAGE2);
            mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mTemperature != null) {
            mSensorManager.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(mProximity != null) {
            mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float ambient_temperature = event.values[0];
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE && mTemperature != null) {
            temperaturelabel.setText("Temperatura: \n " + String.valueOf(ambient_temperature) + " °C");
        }
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                distanceLabel.setText("Te van a multar");
            } else {
                distanceLabel.setText("Muy bien, manten esa distancia");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
