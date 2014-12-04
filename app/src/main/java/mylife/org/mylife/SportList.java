package mylife.org.mylife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;


public class SportList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_list);

        BaseManager bm = new BaseManager(getApplicationContext());
        LinearLayout ll = (LinearLayout)findViewById(R.id.lista);

        for(int i = 0; i < 15; i++)
        {
            final long id = bm.getActivityInformation(i).getLong("id", -1);
            final long date = bm.getActivityInformation(i).getLong("date", -1);

            if(id != -1)
            {
                Date d = new Date();
                d.setTime(date);
                Button but = new Button(getApplicationContext());
                but.setText(d.toString());
                but.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Intent i = new Intent(getApplicationContext(), Sport.class);
                        i.putExtra("id", id);
                        startActivity(i);
                    }
                });
                ll.addView(but);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sport_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
