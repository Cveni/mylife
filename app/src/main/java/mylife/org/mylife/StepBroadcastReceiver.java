package mylife.org.mylife;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by szymon on 25.11.14.
 */

public class StepBroadcastReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent stepService = new Intent(context, StepService.class);
        context.startService(stepService);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent stepIntent = new Intent(context, StepBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, stepIntent, 0);

        int freq = Integer.parseInt(settings.getString(context.getResources().getString(R.string.settings_step_freq_key), "3600"));
        long time = System.currentTimeMillis() - (System.currentTimeMillis() % (freq * 1000)) + (freq * 1000);

        am.setExact(AlarmManager.RTC_WAKEUP, time, sender);
    }
}
