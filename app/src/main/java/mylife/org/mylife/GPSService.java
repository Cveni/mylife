package mylife.org.mylife;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;

/**
 * Created by szymon on 26.11.14.
 */
public class GPSService extends Service{
    private LocationManager locationManager;
    private PowerManager.WakeLock wl;
    private BaseManager base;
    private long activityIndex;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        acquireWakeLock();
        base = new BaseManager(getApplicationContext());
        activityIndex = intent.getExtras().getLong("activityIndex");
        setupLocationListener();

        return Service.START_NOT_STICKY;
    }

    private void acquireWakeLock()
    {
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MYLIFE");
        wl.acquire();
    }

    private void setupLocationListener()
    {
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        base.saveLocation(location, activityIndex);
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                });
    }

    @Override
    public void onDestroy()
    {
        wl.release();
        super.onDestroy();
    }
}
