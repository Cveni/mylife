package mylife.org.mylife;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Cveni on 2014-11-03.
 */

public class Main extends Activity
{
    GPSManager gps;
    BaseManager bm;
    StepCounter sc;
    HRMManager pm;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        gps = new GPSManager(getApplicationContext());
        bm = new BaseManager(getApplicationContext());
        sc = new StepCounter();
        sc.start(getApplicationContext());
        pm = new HRMManager(getApplicationContext());
        id = 1;
    }

    public void btn1(View v)
    {
        if(!checkGPS()) alertGPS();

        //Intent i = new Intent(getApplicationContext(), Sport.class);
        //startActivity(i);
    }

    public void btn2(View v)
    {
        Intent i = new Intent(getApplicationContext(), SportList.class);
        startActivity(i);
    }

    public void btn5(View v)
    {
        Intent i = new Intent(getApplicationContext(), Settings.class);
        startActivity(i);
    }

    public void akcja(View v)
    {
        id = bm.createNewActivity("", "");
        pm.start(id);

        Toast.makeText(getApplicationContext(), "Rozpoczęto rejestrację aktywności", Toast.LENGTH_LONG).show();
    }

    public void akcja2(View v)
    {
        pm.stop();

        Toast.makeText(getApplicationContext(), "Zakończono rejestrację aktywności (wykonano "+bm.getActivityLocations(id).size()+" pomiarów)", Toast.LENGTH_LONG).show();
    }

    public void akcja3(View v)
    {
        Toast.makeText(getApplicationContext(), "Pokonano "+sc.getSteps()+" kroków", Toast.LENGTH_LONG).show();
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
}
