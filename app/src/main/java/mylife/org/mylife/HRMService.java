package mylife.org.mylife;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

public class HRMService extends Service
{
    private PowerManager.WakeLock wl;
    private BaseManager base;
    private long activityIndex;

    HRMScan scanner;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        acquireWakeLock();
        base = new BaseManager(getApplicationContext());
        activityIndex = intent.getExtras().getLong("activityIndex");
        setupHRMListener();

        return Service.START_NOT_STICKY;
    }
    private void acquireWakeLock() {
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MYLIFE");
        wl.acquire();
    }
    private void setupHRMListener() {
        scanner = new HRMScan(this, base, activityIndex);
        scanner.handleReset();
    }
    @Override
    public void onDestroy() {
        wl.release();
        scanner.release();
        scanner.kill();
        super.onDestroy();
    }

}