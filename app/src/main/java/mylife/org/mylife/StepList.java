package mylife.org.mylife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
//import android.view.Menu;
//import android.view.MenuItem;

import java.util.Calendar;

public class StepList extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_list);

        BaseManager bm = new BaseManager(getApplicationContext());

        ListView lv = (ListView)findViewById(R.id.step_list);
        lv.setAdapter(new StepListAdapter(this, R.layout.step_list_item, bm.getStepDays()));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Calendar day = (Calendar)parent.getAdapter().getItem(position);

                Intent i = new Intent(getApplicationContext(), Step.class);
                i.putExtra("date", day.getTimeInMillis());
                startActivity(i);
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.step_list_menu, menu);
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
    }*/
}