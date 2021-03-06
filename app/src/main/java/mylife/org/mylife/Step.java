package mylife.org.mylife;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v13.app.FragmentPagerAdapter;
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
import com.androidplot.xy.XYStepMode;

import java.util.ArrayList;
import java.util.Calendar;

public class Step extends FragmentActivity implements ActionBar.TabListener
{
    ViewPager mViewPager;
    SectionsPagerAdapter mSectionsPagerAdapter;
    long date;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        date = getIntent().getLongExtra("date", 0);

        setContentView(R.layout.step);

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setTitle(getIntent().getStringExtra("name"));

        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.step_pager);
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
        getMenuInflater().inflate(R.menu.step_menu, menu);
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft)
    {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft)
    { }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft)
    { }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter
    {
        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            return StepPage.newInstance(date, position);
        }

        @Override
        public int getCount()
        {
            return 2;
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
                    return getString(R.string.step_tab1);
                case 1:
                    return getString(R.string.step_tab2);
            }
            return null;
        }
    }

    public static class StepPage extends Fragment
    {
        public static StepPage newInstance(long date, int page)
        {
            StepPage fragment = new StepPage();
            Bundle args = new Bundle();
            args.putLong("date", date);
            args.putInt("page", page);
            fragment.setArguments(args);

            return fragment;
        }

        public StepPage()
        { }

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
            }
            return null;
        }

        public View page1(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            final View page = inflater.inflate(R.layout.step_page1, container, false);

            final ArrayList<GridItem> gi = new ArrayList<GridItem>();
            String[] titles = getResources().getStringArray(R.array.step_info_titles);
            String[] data = {"", "", ""};

            TextView tvDate = (TextView)page.findViewById(R.id.step_date_value);
            tvDate.setText(getActivity().getIntent().getStringExtra("name"));
            TextView tvSpace = (TextView)page.findViewById(R.id.step_date_space);
            tvSpace.setTextSize((float)(tvSpace.getTextSize()/GridAdapter.spaceScale));

            BaseManager bm = new BaseManager(getActivity().getApplicationContext());

            Calendar day = Calendar.getInstance();
            day.setTimeInMillis(getArguments().getLong("date"));
            ArrayList<StepModel> steps = bm.getStepsByDay(day);

            int stepSum = getStepSum(steps);

            data[0] = stepSum+"";
            data[1] = (double)(Math.round(10*((double)stepSum)/24)) / 10 + " "+getResources().getString(R.string.unit_per_hour);
            data[2] = stepsCalories(stepSum)+" "+getResources().getString(R.string.unit_kcal);

            for(int i = 0; i < 3; i++)
            {
                gi.add(new GridItem(titles[i], data[i], false));
            }

            ViewTreeObserver vto = page.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @Override
                public void onGlobalLayout()
                {
                    RelativeLayout cell = (RelativeLayout)page.findViewById(R.id.step_date_cell);
                    GridAdapter ga = new GridAdapter(getActivity(), R.layout.grid_item, gi);
                    GridView gv = (GridView)page.findViewById(R.id.step_info_grid);

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
            View page = inflater.inflate(R.layout.step_page2, container, false);

            BaseManager bm = new BaseManager(getActivity().getApplicationContext());

            Calendar day = Calendar.getInstance();
            day.setTimeInMillis(getArguments().getLong("date"));
            ArrayList<StepModel> steps = bm.getStepsByDay(day);

            PlotExtended plot = (PlotExtended) page.findViewById(R.id.step_plot);
            plot.setDomainStep(XYStepMode.SUBDIVIDE, 13);
            plot.setLabels(getResources().getString(R.string.step_plot_xlabel),
                    getResources().getString(R.string.step_plot_ylabel));

            plot.addSeries(getStepSeries(steps), getSeriesFormatter());

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

        public static ArrayList<Integer> getStepValues(ArrayList<StepModel> steps)
        {
            ArrayList<Integer> intervals = new ArrayList<Integer>();

            int first = -(int)steps.get(0).getValue();
            int last = 0;
            intervals.add(0);

            int n = steps.size();

            for(int i = 1; i < n; i++)
            {
                int curr = (int)steps.get(i).getValue();

                if(curr < last)
                {
                    first = last+first;
                }

                intervals.add(curr+first);

                last = curr;
            }

            return intervals;
        }

        public static ArrayList<Integer> getStepIntervals(ArrayList<StepModel> steps)
        {
            ArrayList<Integer> values = getStepValues(steps);

            ArrayList<Integer> intervals = new ArrayList<Integer>();
            intervals.add(0);

            int n = steps.size();

            for(int i = 1; i < n; i++)
            {
                intervals.add(values.get(i)-values.get(i-1));
            }

            return intervals;
        }

        public static int getStepSum(ArrayList<StepModel> steps)
        {
            ArrayList<Integer> intervals = getStepIntervals(steps);

            int sum = 0;

            int n = steps.size();

            for(int i = 0; i < n; i++)
            {
                sum += intervals.get(i);
            }

            return sum;
        }

        public static SimpleXYSeries getStepSeries(ArrayList<StepModel> steps)
        {
            ArrayList<Integer> values = getStepValues(steps);

            ArrayList<Double> xAxis = new ArrayList<Double>();
            ArrayList<Double> yAxis = new ArrayList<Double>();

            int n = steps.size();

            for(int i = 0; i < n; i++)
            {
                StepModel curr = steps.get(i);

                Calendar time = curr.getDate();
                xAxis.add(time.get(Calendar.HOUR_OF_DAY)+(double)time.get(Calendar.MINUTE)/60.0);

                yAxis.add((double)(values.get(i))/1000);
            }

            return new SimpleXYSeries(xAxis, yAxis, null);
        }

        public int stepsCalories(int dayCount)
        {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            Resources res = getResources();

            boolean isMale = settings.getString(res.getString(R.string.settings_user_gender_key), "M").equals("M");
            double weight = Integer.parseInt(settings.getString(res.getString(R.string.settings_user_weight_key), "80"));
            double height = Integer.parseInt(settings.getString(res.getString(R.string.settings_user_height_key), "180"));
            double age = Integer.parseInt(settings.getString(res.getString(R.string.settings_user_age_key), "25"));
            String intense = settings.getString(res.getString(R.string.settings_user_acti_key), "S");

            double burned = dayCount * 0.045;
            double brm;

            if(isMale)
            {
                brm = (9.99*weight) + (6.25*height) - (4.92*age) + 5;
            }
            else
            {
                brm = (9.99*weight) + (6.25*height) - (4.92*age) - 161;
            }

            if(intense.equals("M"))
            {
                brm += 200;
            }
            else if(intense.equals("S"))
            {
                brm += 400;
            }
            else
            {
                brm += 600;
            }

            return (int) (brm+burned);
        }
    }
}
