package mylife.org.mylife;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Settings extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setupSimplePreferencesScreen();
    }

    private void setupSimplePreferencesScreen()
    {
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_reset)
        {
            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle(getResources().getString(R.string.settings_alert_default_title));
            alert.setMessage(getResources().getString(R.string.settings_alert_default_msg));
            alert.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.alert_negative), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            });
            alert.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.alert_positive), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    settings.edit().clear().commit();

                    setPreferenceScreen(null);
                    setupSimplePreferencesScreen();

                    Toast.makeText(getApplicationContext(), R.string.settings_alert_default_toast, Toast.LENGTH_SHORT).show();
                }
            });
            alert.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}