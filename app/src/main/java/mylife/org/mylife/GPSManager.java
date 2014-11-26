package mylife.org.mylife;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by Szymon on 2014-11-03.
 */

public class GPSManager
{
    private Intent serviceIntent;
    private Context context;

    public GPSManager(Context context)
    {
        this.context = context;
        serviceIntent = new Intent(context, GPSService.class);
    }

    public void start(long activityIndexArg)
    {
        serviceIntent.putExtra("activityIndex", activityIndexArg);
        context.startService(serviceIntent);
    }

    public void stop()
    {
        context.stopService(serviceIntent);
    }
}
