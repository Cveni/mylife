package mylife.org.mylife;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by szymon on 25.11.14.
 */
public class StepService extends IntentService {

    public StepService()
    {
        super("StepService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        StepCounter stepCounter = new StepCounter();
        BaseManager base = new BaseManager(getApplicationContext());
        base.saveSteps(stepCounter.getSteps());
    }
}
