package mylife.org.mylife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class SportList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_list);

        BaseManager bm = new BaseManager(getApplicationContext());

        ListView lv = (ListView)findViewById(R.id.sport_list);
        lv.setAdapter(new SportListAdapter(this, R.layout.sport_list_item, bm.getActivitiesInformation()));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                ActivityModel am = (ActivityModel)parent.getAdapter().getItem(position);

                Intent i = new Intent(getApplicationContext(), Sport.class);
                i.putExtra("id", am.getId());
                i.putExtra("device", am.getDevice());
                i.putExtra("name", am.getName());
                startActivity(i);
            }
        });
    }

    /*@Override
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
    }*/
}
