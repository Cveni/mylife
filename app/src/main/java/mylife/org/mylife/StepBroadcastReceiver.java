package mylife.org.mylife;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by szymon on 25.11.14.
 */
public class StepBroadcastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent stepService = new Intent(context, StepService.class);
        context.startService(stepService);
    }
}
