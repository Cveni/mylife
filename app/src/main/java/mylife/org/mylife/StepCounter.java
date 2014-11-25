package mylife.org.mylife;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Szymon on 2014-11-03.
 */

public class StepCounter
{
    private SensorManager sensorManager;
    private static double steps;

    public void start(Context context)
    {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null)
            sensorManager.registerListener(new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    steps = sensorEvent.values[0];
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            }, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public double getSteps()
    {
        return steps;
    }
}
