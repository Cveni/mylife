package mylife.org.mylife;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Szymon on 2014-11-03.
 */

public class StepCounter extends Service implements SensorEventListener
{
    private SensorManager sensorManager;
    private double steps;
    private IBinder binder;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if(countSensor != null)
        {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        return Service.START_STICKY;
    }

    public double getSteps()
    {
        return steps;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        binder = new StepCounterBinder();

        return binder;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        steps = sensorEvent.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    { }

    public class StepCounterBinder extends Binder
    {
        StepCounter getService()
        {
            return StepCounter.this;
        }
    }
}