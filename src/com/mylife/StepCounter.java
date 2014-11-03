package com.mylife;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by szymon on 03.11.14.
 */
public class StepCounter implements SensorEventListener
{
    private SensorManager sensorManager;
    private double steps;

    public void StepCounter(Context conext)
    {
        sensorManager = (SensorManager) conext.getSystemService(Context.SENSOR_SERVICE);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null)
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        steps = sensorEvent.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {
    }

    public double getSteps()
    {
        return steps;
    }
}
