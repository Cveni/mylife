package mylife.org.mylife;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.location.Location;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

public class Sport extends FragmentActivity implements ActionBar.TabListener
{
    ViewPager mViewPager;
    SectionsPagerAdapter mSectionsPagerAdapter;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        id = getIntent().getLongExtra("id", -1);

        setContentView(R.layout.sport);

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        mViewPager = (ViewPager)findViewById(R.id.sportPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++)
        {
            actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sport_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int ids = item.getItemId();

        /*SportPage sp = (SportPage)mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem());
        sp.update("Here comes update!");
        mSectionsPagerAdapter.notifyDataSetChanged();*/

        //noinspection SimplifiableIfStatement
        if (ids == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter
    {
        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            return SportPage.newInstance(id, position);
        }

        @Override
        public int getCount()
        {
            return 6;
        }

        @Override
        public int getItemPosition(Object object)
        {
            return FragmentPagerAdapter.POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position)
            {
                case 0:
                    return getString(R.string.sport_tab1);
                case 1:
                    return getString(R.string.sport_tab2);
                case 2:
                    return getString(R.string.sport_tab3);
                case 3:
                    return getString(R.string.sport_tab4);
                case 4:
                    return getString(R.string.sport_tab5);
                case 5:
                    return getString(R.string.sport_tab6);
            }
            return null;
        }
    }

    public static class SportPage extends Fragment implements Updatable
    {
        public static SportPage newInstance(long id, int page)
        {
            SportPage fragment = new SportPage();
            Bundle args = new Bundle();
            args.putLong("id", id);
            args.putInt("page", page);
            fragment.setArguments(args);

            return fragment;
        }

        public SportPage() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            int page = getArguments().getInt("page");

            switch (page)
            {
                case 0:
                    return page1(inflater, container, savedInstanceState);
                case 1:
                    return page2(inflater, container, savedInstanceState);
                case 2:
                    return page3(inflater, container, savedInstanceState);
                case 3:
                    return page4(inflater, container, savedInstanceState);
                case 4:
                    return page5(inflater, container, savedInstanceState);
                case 5:
                    return page6(inflater, container, savedInstanceState);
            }

            return null;
        }

        public View page1(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View page = inflater.inflate(R.layout.sport_page1, container, false);

            return page;
        }

        public View page2(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View page = inflater.inflate(R.layout.sport_page2, container, false);

            return page;
        }

        public View page3(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View page = inflater.inflate(R.layout.sport_page3, container, false);

            BaseManager bm = new BaseManager(getActivity().getApplicationContext());

            ArrayList<LocationModel> wynik = bm.getActivityLocations(getArguments().getLong("id"));

            if(!wynik.isEmpty()) {
                int n = wynik.size();
                float[] result = new float[3];
                float all = 0;

                XYPlot plot = (XYPlot) page.findViewById(R.id.gpsPlot);
                ArrayList<Double> xaxis = new ArrayList<Double>();
                ArrayList<Double> yaxis = new ArrayList<Double>();

                LocationModel last = wynik.get(0);
                xaxis.add(0.0);
                yaxis.add(0.0);

                for (int i = 1; i < n; i++) {
                    LocationModel curr = wynik.get(i);

                    Location.distanceBetween(last.getLatitude(), last.getLongitude(), curr.getLatitude(), curr.getLongitude(), result);
                    all += result[0];

                    xaxis.add((double) all);
                    yaxis.add(((double) result[0] / (curr.getDateTimestamp() - last.getDateTimestamp())) * 1000);

                    last = curr;
                }

                SimpleXYSeries series = new SimpleXYSeries(xaxis, yaxis, "Predkosc");
                LineAndPointFormatter seriesFormat = new LineAndPointFormatter();
                plot.addSeries(series, seriesFormat);
            }

            return page;
        }

        public View page4(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View page = inflater.inflate(R.layout.sport_page4, container, false);

            return page;
        }

        public View page5(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View page = inflater.inflate(R.layout.sport_page5, container, false);

            BaseManager bm = new BaseManager(getActivity().getApplicationContext());

            ArrayList<LocationModel> wynik = bm.getActivityLocations(getArguments().getLong("id"));

            if(!wynik.isEmpty()) {
                int n = wynik.size();
                float[] result = new float[3];
                float all = 0;

                ArrayList<Double> xaxis = new ArrayList<Double>();
                ArrayList<Double> yaxis = new ArrayList<Double>();

                LocationModel last = wynik.get(0);
                xaxis.add(0.0);
                yaxis.add(0.0);

                for (int i = 1; i < n; i++) {
                    LocationModel curr = wynik.get(i);

                    Location.distanceBetween(last.getLatitude(), last.getLongitude(), curr.getLatitude(), curr.getLongitude(), result);
                    all += result[0];

                    last = curr;
                }

                TextView tv = (TextView) page.findViewById(R.id.label1);
                tv.setText("Wykonano " + n + " pomiarów");
                TextView tv2 = (TextView) page.findViewById(R.id.label2);
                tv2.setText("Pokonano " + Math.round(all) + " metrów");
            }

            return page;
        }

        public View page6(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View page = inflater.inflate(R.layout.sport_page6, container, false);

            return page;
        }

        public void update(String data)
        {
            // soon
        }
    }

    public interface Updatable
    {
        public void update(String data);
    }
}
