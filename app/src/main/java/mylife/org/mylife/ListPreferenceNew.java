package mylife.org.mylife;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Cveni on 2015-01-14.
 */

public class ListPreferenceNew extends ListPreference
{
    public ListPreferenceNew(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        if(getKey().equals(context.getResources().getString(R.string.settings_step_freq_key))) setStepSettingsListener();
    }

    public ListPreferenceNew(Context context)
    {
        super(context);
        if(getKey().equals(context.getResources().getString(R.string.settings_step_freq_key))) setStepSettingsListener();
    }

    public void setStepSettingsListener()
    {
        setOnPreferenceClickListener(new OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                SensorManager sensorManager = (SensorManager)getContext().getSystemService(Context.SENSOR_SERVICE);
                Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

                if(countSensor == null)
                {
                    AlertDialog alert = new AlertDialog.Builder(getContext()).create();
                    alert.setTitle(getContext().getResources().getString(R.string.settings_alert_step_title));
                    alert.setMessage(getContext().getResources().getString(R.string.settings_alert_step_msg));
                    alert.setButton(DialogInterface.BUTTON_POSITIVE, getContext().getResources().getString(R.string.alert_positive), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        { }
                    });

                    getDialog().dismiss();
                    alert.show();

                    return true;
                }

                return true;
            }
        });

        setOnPreferenceChangeListener(new OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                Toast.makeText(getContext(), R.string.settings_alert_step_toast, Toast.LENGTH_LONG).show();

                return true;
            }
        });
    }

    protected void onBindView(View v)
    {
        super.onBindView(v);
        adjustViews(v);
    }

    protected void adjustViews(View v)
    {
        if(v instanceof TextView)
        {
            TextView tv = (TextView)v;
            tv.setSingleLine(false);
        }
        else if(v instanceof ViewGroup)
        {
            ViewGroup vg = (ViewGroup)v;

            for(int i = 0; i < vg.getChildCount(); i++)
            {
                adjustViews(vg.getChildAt(i));
            }
        }
    }
}
