package mylife.org.mylife;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Cveni on 2014-11-03.
 */

public class Main extends Activity
{
    public static GPSManager gps;
    public static HRMManager pm;
    public static long id;

    public String name;
    public String type;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if(isFirstStart()) alertFirstStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if(isGPSServiceWorking() || isPulseServiceWorking()) changeButton(1);
        else changeButton(0);
    }

    public void btn1(View v)
    {
        if(isGPSServiceWorking() || isPulseServiceWorking())
        {
            stopActivity();
        }
        else
        {
            if(!checkGPS())
            {
                alertGPS();
                return;
            }
            else if(!checkPulse1())
            {
                alertPulse1();
                return;
            }
            else if(!checkPulse2())
            {
                alertPulse2();
                return;
            }
            else
            {
                name = "";
                type = "";

                nameDialog();
            }
        }
    }

    public void btn2(View v)
    {
        Intent i = new Intent(getApplicationContext(), SportList.class);
        startActivity(i);
    }

    public void btn3(View v)
    {
        if(!hasStepCounter())
        {
            alertStepCounter();
        }
        else if(!enoughStepsToday())
        {
            alertNoStepsToday();
        }
        else
        {
            Calendar today = Calendar.getInstance();
            long time = System.currentTimeMillis();
            today.setTimeInMillis(time);

            Intent i = new Intent(getApplicationContext(), Step.class);
            i.putExtra("date", time);
            i.putExtra("name", today.get(Calendar.DAY_OF_MONTH) + "." + String.format("%02d", (today.get(Calendar.MONTH) + 1)) + "." + today.get(Calendar.YEAR));
            startActivity(i);
        }
    }

    public void btn4(View v)
    {
        if(!hasStepCounter())
        {
            alertStepCounter();
        }
        else
        {
            Intent i = new Intent(getApplicationContext(), StepList.class);
            startActivity(i);
        }
    }

    public void btn5(View v)
    {
        if(isGPSServiceWorking() || isPulseServiceWorking())
        {
            alertWorking();
        }
        else
        {
            Intent i = new Intent(getApplicationContext(), Settings.class);
            startActivity(i);
        }
    }

    public boolean checkGPS()
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean gps = settings.getBoolean(getResources().getString(R.string.settings_gps_use_key), true);
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        if(gps && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) return false;
        else return true;
    }

    public void alertGPS()
    {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(getResources().getString(R.string.main_alert_gps_title));
        alert.setMessage(getResources().getString(R.string.main_alert_gps_msg));
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.alert_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.alert_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent i = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        });

        alert.show();
    }

    public boolean checkPulse1()
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean pulse = settings.getBoolean(getResources().getString(R.string.settings_pulse_use_key), false);

        PackageManager pm = getApplicationContext().getPackageManager();

        try
        {
            pm.getPackageInfo("com.dsi.ant.service.socket", PackageManager.GET_ACTIVITIES);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return !pulse;
        }
    }

    public boolean checkPulse2()
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean pulse = settings.getBoolean(getResources().getString(R.string.settings_pulse_use_key), false);

        PackageManager pm = getApplicationContext().getPackageManager();

        try
        {
            pm.getPackageInfo("com.dsi.ant.plugins.antplus", PackageManager.GET_ACTIVITIES);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return !pulse;
        }
    }

    public void alertPulse1()
    {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(getResources().getString(R.string.main_alert_pulse1_title));
        alert.setMessage(getResources().getString(R.string.main_alert_pulse1_msg));
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.alert_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.alert_store), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.dsi.ant.service.socket"));
                startActivity(i);
            }
        });

        alert.show();
    }

    public void alertPulse2()
    {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(getResources().getString(R.string.main_alert_pulse2_title));
        alert.setMessage(getResources().getString(R.string.main_alert_pulse2_msg));
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.alert_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.alert_store), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.dsi.ant.plugins.antplus"));
                startActivity(i);
            }
        });

        alert.show();
    }

    public boolean isGPSServiceWorking()
    {
        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if(GPSService.class.getName().equals(service.service.getClassName())) return true;
        }

        return false;
    }

    public boolean isPulseServiceWorking()
    {
        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if(HRMService.class.getName().equals(service.service.getClassName())) return true;
        }

        return false;
    }

    public void alertWorking()
    {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(getResources().getString(R.string.main_alert_working_title));
        alert.setMessage(getResources().getString(R.string.main_alert_working_msg));
        alert.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.alert_positive), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            { }
        });

        alert.show();
    }

    public boolean hasStepCounter()
    {
        SensorManager sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if(countSensor == null) return false;
        else return true;
    }

    public void alertStepCounter()
    {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(getResources().getString(R.string.settings_alert_step_title));
        alert.setMessage(getResources().getString(R.string.settings_alert_step_msg));
        alert.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.alert_positive), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            { }
        });

        alert.show();
    }

    public boolean isFirstStart()
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        return !settings.contains(getResources().getString(R.string.settings_gps_use_key));
    }

    public void alertFirstStart()
    {
        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.settings, true);

        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(getResources().getString(R.string.main_alert_first_start_title));
        alert.setMessage(getResources().getString(R.string.main_alert_first_start_msg));
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.alert_negative), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        alert.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.alert_settings), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent i = new Intent(getApplicationContext(), Settings.class);
                startActivity(i);
            }
        });

        alert.show();
    }

    public void startActivity()
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean gps = settings.getBoolean(getResources().getString(R.string.settings_gps_use_key), true);
        boolean pulse = settings.getBoolean(getResources().getString(R.string.settings_pulse_use_key), false);

        int device = 0;

        if(gps && !pulse) device = 0;
        else if (!gps && pulse) device = 1;
        else if (gps && pulse) device = 2;

        BaseManager bm = new BaseManager(getApplicationContext());
        id = bm.createNewActivity(name, type, device);

        if(gps && !pulse)
        {
            this.gps = new GPSManager(getApplicationContext());
            this.gps.start(id);
        }
        else if (!gps && pulse)
        {
            this.pm = new HRMManager(getApplicationContext());
            this.pm.start(id);
        }
        else if (gps && pulse)
        {
            this.gps = new GPSManager(getApplicationContext());
            this.gps.start(id);
            this.pm = new HRMManager(getApplicationContext());
            this.pm.start(id);
        }

        changeButton(1);
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.main_record_start_toast), Toast.LENGTH_LONG).show();
    }

    public void stopActivity()
    {
        if (isGPSServiceWorking())
        {
            gps.stop();
            gps = null;
        }
        if (isPulseServiceWorking())
        {
            pm.stop();
            pm = null;
        }

        changeButton(0);
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.main_record_stop_toast), Toast.LENGTH_LONG).show();
    }

    public void changeButton(int choice)
    {
        TextView tv = (TextView)findViewById(R.id.main_btn1);

        if(choice == 0) tv.setText(getResources().getString(R.string.str_main_btn1));
        else if(choice == 1) tv.setText(getResources().getString(R.string.str_main_btn1_alt));
    }

    public void nameDialog()
    {
        final EditText et = new EditText(this);
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(getResources().getString(R.string.main_record_start_name_title));
        alert.setView(et);
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.alert_negative), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        alert.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.alert_positive), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                name = et.getText().toString();

                typeDialog();
            }
        });

        alert.show();
    }

    public void typeDialog()
    {
        ArrayAdapter<String> types = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, getResources().getStringArray(R.array.sport_activity_types));
        final AlertDialog alert = new AlertDialog.Builder(this).setSingleChoiceItems(types, 0, null).create();
        alert.setTitle(getResources().getString(R.string.main_record_start_type_title));
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.alert_negative), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        alert.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.alert_positive), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                type = getResources().getStringArray(R.array.sport_activity_types_db)[alert.getListView().getCheckedItemPosition()];

                startActivity();
            }
        });

        alert.show();
    }

    public boolean enoughStepsToday()
    {
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(System.currentTimeMillis());

        BaseManager bm = new BaseManager(getApplicationContext());
        int count = bm.getStepsByDay(today).size();

        if(count > 1) return true;
        else return false;
    }

    public void alertNoStepsToday()
    {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(getResources().getString(R.string.main_alert_enough_steps_title));
        alert.setMessage(getResources().getString(R.string.main_alert_enough_steps_msg));
        alert.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.alert_positive), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            { }
        });

        alert.show();
    }
}