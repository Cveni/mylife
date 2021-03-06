package mylife.org.mylife;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLngBounds;

public class Sport extends FragmentActivity implements ActionBar.TabListener
{
    final static double speedRatio = 18/5;

    ViewPager mViewPager;
    SectionsPagerAdapter mSectionsPagerAdapter;
    long id;
    int device;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        id = getIntent().getLongExtra("id", 1);
        device = (int)getIntent().getLongExtra("device", 0);

        setContentView(R.layout.sport);

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setTitle(getIntent().getStringExtra("name"));

        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.sport_pager);
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

    /*@Override
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

        SportPage sp = (SportPage)mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem());
        sp.update("Here comes update!");
        mSectionsPagerAdapter.notifyDataSetChanged();

        //noinspection SimplifiableIfStatement
        if (ids == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

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
            return SportPage.newInstance(id, device, position);
        }

        @Override
        public int getCount()
        {
            switch(device)
            {
                case 0:
                    return 4;
                case 1:
                    return 3;
                case 2:
                    return 5;
            }
            return 4;
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
                    switch(device)
                    {
                        case 0:
                            return getString(R.string.sport_tab3);
                        case 1:
                            return getString(R.string.sport_tab5);
                        case 2:
                            return getString(R.string.sport_tab3);
                    }
                case 3:
                    return getString(R.string.sport_tab4);
                case 4:
                    return getString(R.string.sport_tab5);
            }
            return null;
        }
    }

    public static class SportPage extends Fragment /*implements Updatable*/
    {
        public static View map;

        public static SportPage newInstance(long id, int device, int page)
        {
            SportPage fragment = new SportPage();
            Bundle args = new Bundle();
            args.putLong("id", id);
            args.putInt("device", device);
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
                    switch(getArguments().getInt("device"))
                    {
                        case 0:
                            return page3(inflater, container, savedInstanceState);
                        case 1:
                            return page5(inflater, container, savedInstanceState);
                        case 2:
                            return page3(inflater, container, savedInstanceState);
                    }
                case 3:
                    return page4(inflater, container, savedInstanceState);
                case 4:
                    return page5(inflater, container, savedInstanceState);
            }
            return null;
        }

        public View page1(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            final View page = inflater.inflate(R.layout.sport_page1, container, false);

            BaseManager bm = new BaseManager(getActivity().getApplicationContext());
            ArrayList<LocationModel> locs = bm.getActivityLocations(getArguments().getLong("id"));
            ArrayList<PulseModel> pulse = bm.getActivityPulses(getArguments().getLong("id"));
            ActivityModel acti = bm.getActivityInformation(getArguments().getLong("id"));
            int device = getArguments().getInt("device");

            final ArrayList<GridItem> gi = new ArrayList<GridItem>();
            String[] devices = getResources().getStringArray(R.array.sport_info_device);
            String[] types = getResources().getStringArray(R.array.sport_activity_types);
            String[] typesdb = getResources().getStringArray(R.array.sport_activity_types_db);
            String[] titles = getResources().getStringArray(R.array.sport_info_titles);
            String[] data = {"", "", "", ""};

            TextView tvName = (TextView)page.findViewById(R.id.sport_name_value);
            tvName.setText(acti.getName());

            TextView tvSpace = (TextView)page.findViewById(R.id.sport_name_space);
            tvSpace.setTextSize((float)(tvSpace.getTextSize()/GridAdapter.spaceScale));

            if(device == 2) titles[0] = titles[4];
            data[0] = devices[device];

            String type = acti.getType();
            for(int i = 0; i < types.length; i++)
            {
                if(type.equals(typesdb[i])) data[1] = types[i];
            }

            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(acti.getDate());
            data[2] = time.get(Calendar.DAY_OF_MONTH)+"."+String.format("%02d", (time.get(Calendar.MONTH)+1))+"."+time.get(Calendar.YEAR)
                      +" - "+time.get(Calendar.HOUR_OF_DAY)+":"+String.format("%02d", time.get(Calendar.MINUTE));

            long gpstime = 0;
            long pulsetime = 0;
            long delta = 0;

            if((device == 0 || device == 2) && locs.size() > 1) gpstime = (long)locs.get(locs.size()-1).getDateTimestamp();
            if((device == 1 || device == 2) && pulse.size() > 1) pulsetime = (long)pulse.get(pulse.size()-1).getDateTimestamp();

            if(device == 0 && locs.size() > 1) delta = (gpstime-acti.getDate())/1000;
            else if(device == 1 && pulse.size() > 1) delta = (pulsetime-acti.getDate())/1000;
            else if(device == 2 && (locs.size() > 1 || pulse.size() > 1))
            {
                if(gpstime > pulsetime && locs.size() > 1) delta = (gpstime-acti.getDate())/1000;
                else if(pulsetime > gpstime && pulse.size() > 1) delta = (pulsetime-acti.getDate())/1000;
            }

            if(delta != 0) data[3] = delta/(60 * 60)+":"+String.format("%02d", ((delta%(60 * 60))/60))+":"+String.format("%02d", (delta%60));

            for(int i = 0; i < 2; i++)
            {
                gi.add(new GridItem(titles[i], data[i], false));
            }
            for(int i = 2; i < 4; i++)
            {
                gi.add(new GridItem(titles[i], data[i], true));
            }

            ViewTreeObserver vto = page.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    RelativeLayout cell = (RelativeLayout)page.findViewById(R.id.sport_name_cell);
                    GridAdapter ga = new GridAdapter(getActivity(), R.layout.grid_item, gi);
                    GridView gv = (GridView)page.findViewById(R.id.sport_info_grid);

                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                    {
                        cell.setMinimumHeight(page.getMeasuredHeight()/4);
                        ga.setCellHeight(page.getMeasuredHeight()/4);
                        gv.setNumColumns(2);
                    }
                    else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                    {
                        cell.setMinimumHeight(page.getMeasuredHeight()/2);
                        ga.setCellHeight(page.getMeasuredHeight()/2);
                        gv.setNumColumns(4);
                    }

                    gv.setAdapter(ga);

                    page.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });

            return page;
        }

        public View page2(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            final View page = inflater.inflate(R.layout.sport_page2, container, false);

            BaseManager bm = new BaseManager(getActivity().getApplicationContext());
            ArrayList<LocationModel> locs = bm.getActivityLocations(getArguments().getLong("id"));
            ArrayList<PulseModel> pulse = bm.getActivityPulses(getArguments().getLong("id"));
            ActivityModel acti = bm.getActivityInformation(getArguments().getLong("id"));

            final ArrayList<GridItem> gi = new ArrayList<GridItem>();
            String[] typesdb = getResources().getStringArray(R.array.sport_activity_types_db);
            String[] titles = getResources().getStringArray(R.array.sport_stats_titles);
            String[] data = {"", "", "", "", "", "", "", ""};

            int device = getArguments().getInt("device");
            int type = 0;
            String typeS = acti.getType();
            for(int i = 0; i < typesdb.length; i++)
            {
                if(typeS.equals(typesdb[i])) type = i;
            }

            double avgSpeed = 0;
            double avgPulse = 0;

            if((device == 0 || device == 2) && !locs.isEmpty())
            {
                int n = locs.size();
                float[] result = new float[3];
                float dist = 0;
                ArrayList<Double> speeds = new ArrayList<Double>();

                LocationModel last = locs.get(0);

                for (int i = 1; i < n; i++)
                {
                    LocationModel curr = locs.get(i);

                    Location.distanceBetween(last.getLatitude(), last.getLongitude(), curr.getLatitude(), curr.getLongitude(), result);
                    dist += result[0];
                    double speed = ((double) result[0] / (curr.getDateTimestamp() - last.getDateTimestamp())) * 1000;
                    speeds.add(speed);

                    last = curr;
                }

                if(!speeds.isEmpty())
                {
                    double distTotal = (double)dist;
                    data[3] = (double)(Math.round(distTotal/10)) / 100+" "+getResources().getString(R.string.unit_km);

                    avgSpeed = (distTotal/(last.getDateTimestamp()-locs.get(0).getDateTimestamp())) * 1000;
                    data[2] = (double)(Math.round(avgSpeed*100*speedRatio)) / 100+" "+getResources().getString(R.string.unit_kmh);

                    double minSpeed = speeds.get(0);
                    double maxSpeed = speeds.get(0);

                    int o = speeds.size();

                    for(int i = 1; i < o; i++)
                    {
                        if(speeds.get(i) < minSpeed) minSpeed = speeds.get(i);
                        if(speeds.get(i) > maxSpeed) maxSpeed = speeds.get(i);
                    }

                    data[0] = (double)(Math.round(minSpeed*100*speedRatio)) / 100+" "+getResources().getString(R.string.unit_kmh);
                    data[1] = (double)(Math.round(maxSpeed*100*speedRatio)) / 100+" "+getResources().getString(R.string.unit_kmh);
                }
            }

            if((device == 1 || device == 2) && !pulse.isEmpty())
            {
                int n = pulse.size();
                PulseModel last = pulse.get(0);

                int minPulse = last.getValue();
                int maxPulse = last.getValue();

                double all = 0;

                for(int i = 1; i < n; i++)
                {
                    PulseModel curr = pulse.get(i);

                    if(curr.getValue() < minPulse) minPulse = curr.getValue();
                    if(curr.getValue() > maxPulse) maxPulse = curr.getValue();

                    all += last.getValue()*(curr.getDateTimestamp()-last.getDateTimestamp());

                    last = curr;
                }

                if(n > 1)
                {
                    avgPulse = (all + (1.0 / (double)(n-1)) * last.getValue() * (last.getDateTimestamp() - pulse.get(0).getDateTimestamp()))
                            / (((double)n / (double)(n-1)) * (last.getDateTimestamp() - pulse.get(0).getDateTimestamp()));

                    data[6] = (double)Math.round(avgPulse*10) / 10+" "+getResources().getString(R.string.unit_bpm);
                }

                data[4] = minPulse+" "+getResources().getString(R.string.unit_bpm);
                data[5] = maxPulse+" "+getResources().getString(R.string.unit_bpm);
            }

            long gpstime = 0;
            long pulsetime = 0;
            long delta = 0;

            if((device == 0 || device == 2) && locs.size() > 1) gpstime = (long)locs.get(locs.size()-1).getDateTimestamp();
            if((device == 1 || device == 2) && pulse.size() > 1) pulsetime = (long)pulse.get(pulse.size()-1).getDateTimestamp();

            if(device == 0 && locs.size() > 1) delta = (gpstime-acti.getDate())/1000;
            else if(device == 1 && pulse.size() > 1) delta = (pulsetime-acti.getDate())/1000;
            else if(device == 2 && (locs.size() > 1 || pulse.size() > 1))
            {
                if(gpstime > pulsetime && locs.size() > 1) delta = (gpstime-acti.getDate())/1000;
                else if(pulsetime > gpstime && pulse.size() > 1) delta = (pulsetime-acti.getDate())/1000;
            }

            if(delta != 0)
            {
                data[7] = calorieCount(device, type, delta, avgSpeed*speedRatio, avgPulse)+" "+getResources().getString(R.string.unit_kcal);
            }

            if(device == 0 || device == 2)
            {
                for(int i = 0; i < 4; i++)
                {
                    gi.add(new GridItem(titles[i], data[i], false));
                }
            }

            if(device == 1 || device == 2)
            {
                for(int i = 4; i < 7; i++)
                {
                    gi.add(new GridItem(titles[i], data[i], false));
                }
            }

            gi.add(new GridItem(titles[7], data[7], false));

            ViewTreeObserver vto = page.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    GridAdapter ga = new GridAdapter(getActivity(), R.layout.grid_item, gi);
                    GridView gv = (GridView)page.findViewById(R.id.sport_stats_grid);

                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                    {
                        ga.setCellHeight(page.getMeasuredHeight()/4);
                        gv.setNumColumns(2);
                    }
                    else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                    {
                        ga.setCellHeight(page.getMeasuredHeight()/2);
                        gv.setNumColumns(4);
                    }

                    gv.setAdapter(ga);

                    page.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });

            return page;
        }

        public View page3(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            if (map != null)
            {
                ViewGroup parent = (ViewGroup)map.getParent();
                if (parent != null) parent.removeView(map);
            }

            try
            {
                map = inflater.inflate(R.layout.sport_page3, container, false);

                final GoogleMap mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.gps_map))
                        .getMap();

                BaseManager bm = new BaseManager(getActivity().getApplicationContext());
                ArrayList<LocationModel> locs = bm.getActivityLocations(getArguments().getLong("id"));

                if(locs.size() > 1)
                {
                    final LatLngBounds.Builder bounds = new LatLngBounds.Builder();
                    ArrayList<LatLng> routePoints = new ArrayList<LatLng>();
                    for(LocationModel location: locs)
                    {
                        routePoints.add(new LatLng(location.getLatitude(), location.getLongitude()));
                        bounds.include(new LatLng(location.getLatitude(), location.getLongitude()));
                    }

                    Polyline route = mapFragment.addPolyline(new PolylineOptions().color(PlotExtended.fillColor));
                    route.setPoints(routePoints);

                    LatLng startPosition = new LatLng(locs.get(0).getLatitude(), locs.get(0).getLongitude());
                    mapFragment.addMarker(new MarkerOptions().position(startPosition)
                        .draggable(false).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(getResources().getString(R.string.map_start)));

                    LatLng finishPosition = new LatLng(locs.get(locs.size()-1).getLatitude(), locs.get(locs.size()-1).getLongitude());
                    mapFragment.addMarker(new MarkerOptions().position(finishPosition)
                        .draggable(false).icon(BitmapDescriptorFactory.defaultMarker(196)).title(getResources().getString(R.string.map_finish)));

                    ViewTreeObserver vto = map.getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
                    {
                        @Override
                        public void onGlobalLayout()
                        {
                            mapFragment.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 75), 2000, null);

                            map.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    });
                }
            }
            catch (Exception e) { }

            return map;
        }

        public View page4(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View page = inflater.inflate(R.layout.sport_page4, container, false);

            BaseManager bm = new BaseManager(getActivity().getApplicationContext());

            ArrayList<LocationModel> locs = bm.getActivityLocations(getArguments().getLong("id"));

            final PlotExtended plot = (PlotExtended) page.findViewById(R.id.gps_plot);
            plot.setLabels(getResources().getString(R.string.gps_plot_xlabel),
                           getResources().getString(R.string.gps_plot_ylabel));

            if(locs.size() > 1)
            {
                plot.addSeries(getLocationSeries(locs), getSeriesFormatter());
            }

            return page;
        }

        public View page5(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View page = inflater.inflate(R.layout.sport_page5, container, false);

            BaseManager bm = new BaseManager(getActivity().getApplicationContext());

            ArrayList<PulseModel> pulse = bm.getActivityPulses(getArguments().getLong("id"));

            final PlotExtended pulsePlot = (PlotExtended)page.findViewById(R.id.pulse_plot);
            pulsePlot.setLabels(getResources().getString(R.string.pulse_plot_xlabel),
                                getResources().getString(R.string.pulse_plot_ylabel));

            pulsePlot.setDomainValueFormat(new DecimalFormat("#"));
            pulsePlot.setRangeValueFormat(new DecimalFormat("#"));

            if(pulse.size() > 1)
            {
                pulsePlot.addSeries(getPulseSeries(pulse), getSeriesFormatter());
            }

            return page;
        }

        public LineAndPointFormatter getSeriesFormatter()
        {
            LineAndPointFormatter seriesFormat = new LineAndPointFormatter();
            seriesFormat.getLinePaint().setColor(PlotExtended.lineColor);
            seriesFormat.getLinePaint().setStrokeWidth(seriesFormat.getLinePaint().getStrokeWidth()*PlotExtended.lineScale);
            seriesFormat.getFillPaint().setColor(PlotExtended.fillColor);
            seriesFormat.getVertexPaint().setColor(Color.TRANSPARENT);

            return seriesFormat;
        }

        public SimpleXYSeries getLocationSeries(ArrayList<LocationModel> locs)
        {
            ArrayList<Double> xAxis = new ArrayList<Double>();
            ArrayList<Double> yAxis = new ArrayList<Double>();

            int n = locs.size();
            float[] result = new float[3];
            float dist = 0;

            xAxis.add(0.0);
            yAxis.add(0.0);
            LocationModel last = locs.get(0);

            for (int i = 1; i < n; i++)
            {
                LocationModel curr = locs.get(i);

                Location.distanceBetween(last.getLatitude(), last.getLongitude(), curr.getLatitude(), curr.getLongitude(), result);
                dist += result[0];

                double speed = ((double) result[0] / (curr.getDateTimestamp() - last.getDateTimestamp())) * 1000 * speedRatio;
                xAxis.add((double) dist / 1000);
                yAxis.add(speed);

                last = curr;
            }

            return new SimpleXYSeries(xAxis, yAxis, null);
        }

        public SimpleXYSeries getPulseSeries(ArrayList<PulseModel> pulse)
        {
            ArrayList<Double> xAxis = new ArrayList<Double>();
            ArrayList<Integer> yAxis = new ArrayList<Integer>();

            int n = pulse.size();

            PulseModel first = pulse.get(0);
            xAxis.add(0.0);
            yAxis.add(first.getValue());

            for(int i = 1; i < n; i++)
            {
                PulseModel curr = pulse.get(i);

                xAxis.add((curr.getDateTimestamp()-first.getDateTimestamp()) / 1000);
                yAxis.add(curr.getValue());
            }

            return new SimpleXYSeries(xAxis, yAxis, null);
        }

        public int calorieCount(int device, int type, long time, double speed, double pulse)
        {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            Resources res = getResources();

            double calories = 0;
            double timeInMinutes = ((double)time)/60;

            boolean isMale = settings.getString(res.getString(R.string.settings_user_gender_key), "M").equals("M");
            double weight = Integer.parseInt(settings.getString(res.getString(R.string.settings_user_weight_key), "80"));
            double age = Integer.parseInt(settings.getString(res.getString(R.string.settings_user_age_key), "25"));

            if (type == 0)
            {
                if(device==0)
                {
                    calories=(timeInMinutes*5.83);
                }
                else
                {
                    if(isMale)
                    {
                        calories=((-55.0969 + (0.6309 *pulse) + (0.1988 *weight) + (0.2017 *age))/4.184) *timeInMinutes;
                    }
                    else
                    {
                        calories=((-20.4022 + (0.4472 *pulse) - (0.1263 *weight) + (0.074 *age))/4.184) *timeInMinutes;
                    }
                }
            }
            else if (type == 1)
            {
                if(device==0)
                {
                    calories=speed*weight*timeInMinutes/60.857;
                }
                else if(device==1)
                {
                    if(isMale)
                    {
                        calories=((-55.0969 + (0.6309 *pulse) + (0.1988 *weight) + (0.2017 *age))/4.184) *timeInMinutes;
                    }
                    else
                    {
                        calories=((-20.4022 + (0.4472 *pulse) - (0.1263 *weight) + (0.074 *age))/4.184) *timeInMinutes;
                    }
                }
                else
                {
                    if(isMale)
                    {
                        calories=((-55.0969 + (0.6309 *pulse) + (0.1988 *weight) + (0.2017 *age))/4.184) *timeInMinutes;
                    }
                    else
                    {
                        calories=((-20.4022 + (0.4472 *pulse) - (0.1263 *weight) + (0.074 *age))/4.184) *timeInMinutes;
                    }
                    calories+=speed*weight*timeInMinutes/60.857;
                    calories=calories/2;
                }
            }
            else if (type == 2)
            {
                if(device==1)
                {
                    calories=(timeInMinutes*8.28);
                }
                else
                {
                    calories=6.0848*weight-78.2673+96.07*(speed/1.609-8);
                }
            }
            else if (type == 3)
            {
                calories=timeInMinutes*8.52;
            }
            else if (type == 4)
            {
                if(device==1)
                {
                    calories=(timeInMinutes*6.97);
                }
                else
                {
                    calories=5.48532*weight-70.4403+86.4*(speed/1.609-8);
                }
            }
            else if (type == 5)
            {
                if(device==0)
                {
                    calories=((0.6345*speed*speed+0.7563*speed+36.725)/30)*timeInMinutes;
                }
                else if(device==1)
                {
                    if(isMale)
                    {
                        calories=((-55.0969 + (0.6309 *pulse) + (0.1988 *weight) + (0.2017 *age))/4.184) *timeInMinutes;
                    }
                    else
                    {
                        calories=((-20.4022 + (0.4472 *pulse) - (0.1263 *weight) + (0.074 *age))/4.184) *timeInMinutes;
                    }
                }
                else
                {
                    if(isMale)
                    {
                        calories=((-55.0969 + (0.6309 *pulse) + (0.1988 *weight) + (0.2017 *age))/4.184) *timeInMinutes;
                    }
                    else
                    {
                        calories=((-20.4022 + (0.4472 *pulse) - (0.1263 *weight) + (0.074 *age))/4.184) *timeInMinutes;
                    }
                    calories=(0.6345*speed*speed+0.7563*speed+36.725)/3600;
                    calories=calories/2;
                }
            }
            else if (type == 6)
            {
                if(device==1)
                {
                    calories=timeInMinutes*4.08;
                }
                else
                {
                    calories=(0.0215*speed*speed*speed-0.1765*speed*speed+0.871*speed+1.4577)*weight*timeInMinutes/60;
                }
            }

            return (int) calories;
        }

        /*public void update(String data)
        {

        }*/
    }

    /*public interface Updatable
    {
        public void update(String data);
    }*/
}
