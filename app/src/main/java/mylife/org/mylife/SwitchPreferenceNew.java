package mylife.org.mylife;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Cveni on 2015-01-06.
 */

public class SwitchPreferenceNew extends SwitchPreference
{
    public SwitchPreferenceNew(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        if(getKey().equals(context.getResources().getString(R.string.settings_step_use_key))) setStepSettingsListener();
        else if(getKey().equals(context.getResources().getString(R.string.settings_gps_use_key))
             || getKey().equals(context.getResources().getString(R.string.settings_pulse_use_key))) setGPSPulseSettingsListener();
    }

    public SwitchPreferenceNew(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        if(getKey().equals(context.getResources().getString(R.string.settings_step_use_key))) setStepSettingsListener();
        else if(getKey().equals(context.getResources().getString(R.string.settings_gps_use_key))
             || getKey().equals(context.getResources().getString(R.string.settings_pulse_use_key))) setGPSPulseSettingsListener();
    }

    public SwitchPreferenceNew(Context context)
    {
        super(context);
        if(getKey().equals(context.getResources().getString(R.string.settings_step_use_key))) setStepSettingsListener();
        else if(getKey().equals(context.getResources().getString(R.string.settings_gps_use_key))
             || getKey().equals(context.getResources().getString(R.string.settings_pulse_use_key))) setGPSPulseSettingsListener();
    }

    public void setStepSettingsListener()
    {
        setOnPreferenceChangeListener(new OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
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

                    alert.show();

                    return false;
                }
                else
                {
                    Toast.makeText(getContext(), R.string.settings_alert_step_toast, Toast.LENGTH_LONG).show();

                    return true;
                }
            }
        });
    }

    public void setGPSPulseSettingsListener()
    {
        setOnPreferenceChangeListener(new OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
                Resources res = getContext().getResources();
                String key = preference.getKey();

                if((key.equals(res.getString(R.string.settings_gps_use_key))
                    && !settings.getBoolean(res.getString(R.string.settings_pulse_use_key), false) && !(Boolean)newValue)
                    || (key.equals(res.getString(R.string.settings_pulse_use_key))
                    && !settings.getBoolean(res.getString(R.string.settings_gps_use_key), true) && !(Boolean)newValue))
                {
                    AlertDialog alert = new AlertDialog.Builder(getContext()).create();
                    alert.setTitle(getContext().getResources().getString(R.string.settings_alert_gpspulse_title));
                    alert.setMessage(getContext().getResources().getString(R.string.settings_alert_gpspulse_msg));
                    alert.setButton(DialogInterface.BUTTON_POSITIVE, getContext().getResources().getString(R.string.alert_positive), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        { }
                    });

                    alert.show();

                    return false;
                }
                else
                {
                    return true;
                }
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
