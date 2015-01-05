package mylife.org.mylife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        gps = new GPSManager(getApplicationContext());
        bm = new BaseManager(getApplicationContext());
        sc = new StepCounter();
        sc.start(getApplicationContext());
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void btn1(View v)
    {
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
        gps.start(bm.createNewActivity(""));

        Toast.makeText(getApplicationContext(), "Rozpoczęto rejestrację aktywności", Toast.LENGTH_LONG).show();
    }

    public void akcja2(View v)
    {
        gps.stop();

        Toast.makeText(getApplicationContext(), "Zakończono rejestrację aktywności", Toast.LENGTH_LONG).show();
    }

    public void akcja3(View v)
    {
        Toast.makeText(getApplicationContext(), "Pokonano "+sc.getSteps()+" kroków", Toast.LENGTH_LONG).show();
    }
}
