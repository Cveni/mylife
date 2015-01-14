package mylife.org.mylife;

/**
 * Created by Mateusz on 2015-01-12.
 */

import android.content.Context;
import android.content.Intent;

public class HRMManager
{
    private Intent serviceIntent;
    private Context context;

    public HRMManager(Context context)
    {
        this.context = context;
        serviceIntent = new Intent(context, HRMService.class);
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

