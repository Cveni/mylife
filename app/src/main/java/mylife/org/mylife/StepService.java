package mylife.org.mylife;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by szymon on 25.11.14.
 */

public class StepService extends IntentService implements ServiceConnection
{
    public StepService()
    {
        super("StepService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        getApplicationContext().bindService(new Intent(getApplicationContext(), StepCounter.class), this, 0);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder)
    {
        StepCounter.StepCounterBinder binder = (StepCounter.StepCounterBinder)iBinder;
        StepCounter stepCounter = binder.getService();

        BaseManager bm = new BaseManager(getApplicationContext());
        bm.saveSteps(stepCounter.getSteps());

        getApplicationContext().unbindService(this);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) { }
}