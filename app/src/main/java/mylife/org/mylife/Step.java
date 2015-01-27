package mylife.org.mylife;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
            View page = inflater.inflate(R.layout.step_page1, container, false);

            return page;
        }

        public View page2(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View page = inflater.inflate(R.layout.step_page2, container, false);

            return page;
        }
    }
}
