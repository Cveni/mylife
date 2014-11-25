package mylife.org.mylife;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by szymon on 25.11.14.
 */
public class StartupBroadcastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equalsIgnoreCase("android.intent.action.BOOT_COMPLETED"))
        {
            StepCounter stepCounter = new StepCounter();
            stepCounter.start(context);

            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent stepIntent = new Intent(context, StepBroadcastReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(context, 0, stepIntent, 0);

            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 60, sender);
        }
    }
}
