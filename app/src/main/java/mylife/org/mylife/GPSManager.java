package mylife.org.mylife;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by Szymon on 2014-11-03.
 */

public class GPSManager
{
    private static int activityIndex = 0;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private BaseManager base;

    public GPSManager(Context context, final int activityIndexArg)
    {
        activityIndex = activityIndexArg;

        base = new BaseManager(context);

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                base.saveLocation(location, activityIndex);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle)
            {

            }

            @Override
            public void onProviderEnabled(String s)
            {

            }

            @Override
            public void onProviderDisabled(String s)
            {

            }
        };
    }

    public void start()
    {
        if(locationManager != null && locationListener != null)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public void stop()
    {
        if(locationManager != null)
            locationManager.removeUpdates(locationListener);
    }
}
