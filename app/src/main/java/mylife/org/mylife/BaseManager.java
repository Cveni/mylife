package mylife.org.mylife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Szymon on 2014-11-03.
 */

public class BaseManager
{
    private SQLiteDatabase database;
    private BaseHelper baseHelper;

    public BaseManager(Context context)
    {
        baseHelper = new BaseHelper(context);
        database = null;
    }

    public void open()
    {
        if(database == null || !database.isOpen())
            database = baseHelper.getWritableDatabase();
    }

    public void close()
    {
        baseHelper.close();
    }

    public long createNewActivity(String name, String type, int device)
    {
        ContentValues values = new ContentValues();
        values.put(BaseHelper.ACTIVITIES_COLUMN_NAME, name);
        values.put(BaseHelper.ACTIVITIES_COLUMN_DATE, Long.valueOf(System.currentTimeMillis()).toString());
        values.put(BaseHelper.ACTIVITIES_COLUMN_DEVICE, device);
        values.put(BaseHelper.ACTIVITIES_COLUMN_TYPE, type);

        open();
        long id = database.insert(BaseHelper.TABLE_NAME_ACTIVITIES, null, values);
        close();

        return id;
    }

    public ActivityModel getActivityInformation(long id)
    {
        ActivityModel activity = null;

        String[] allColumns = {BaseHelper.ACTIVITIES_COLUMN_ID, BaseHelper.ACTIVITIES_COLUMN_NAME,
                BaseHelper.ACTIVITIES_COLUMN_DATE, BaseHelper.ACTIVITIES_COLUMN_DEVICE, BaseHelper.ACTIVITIES_COLUMN_TYPE};

        open();
        String selection = BaseHelper.ACTIVITIES_COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor item = database.query(BaseHelper.TABLE_NAME_ACTIVITIES, allColumns, selection, selectionArgs,
                null, null, null);

        if(item.moveToFirst())
        {
            activity = new ActivityModel(
                    item.getLong(item.getColumnIndex(BaseHelper.ACTIVITIES_COLUMN_ID)),
                    item.getString(item.getColumnIndex(BaseHelper.ACTIVITIES_COLUMN_NAME)),
                    item.getLong(item.getColumnIndex(BaseHelper.ACTIVITIES_COLUMN_DATE)),
                    item.getLong(item.getColumnIndex(BaseHelper.ACTIVITIES_COLUMN_DEVICE)),
                    item.getString(item.getColumnIndex(BaseHelper.ACTIVITIES_COLUMN_TYPE))
            );
        }
        item.close();
        close();

        return activity;
    }

    public ArrayList<ActivityModel> getActivitiesInformation()
    {
        ArrayList<ActivityModel> activities = new ArrayList<ActivityModel>();

        String[] allColumns = {BaseHelper.ACTIVITIES_COLUMN_ID, BaseHelper.ACTIVITIES_COLUMN_NAME,
                BaseHelper.ACTIVITIES_COLUMN_DATE, BaseHelper.ACTIVITIES_COLUMN_DEVICE, BaseHelper.ACTIVITIES_COLUMN_TYPE};

        open();

        Cursor activitiesCursor = database.query(BaseHelper.TABLE_NAME_ACTIVITIES, allColumns,
                null, null,
                null, null, BaseHelper.ACTIVITIES_COLUMN_ID+" DESC", null);

        if(activitiesCursor.moveToFirst())
        {
            do
            {
                activities.add(new ActivityModel(
                        activitiesCursor.getLong(activitiesCursor.getColumnIndex(BaseHelper.ACTIVITIES_COLUMN_ID)),
                        activitiesCursor.getString(activitiesCursor.getColumnIndex(BaseHelper.ACTIVITIES_COLUMN_NAME)),
                        activitiesCursor.getLong(activitiesCursor.getColumnIndex(BaseHelper.ACTIVITIES_COLUMN_DATE)),
                        activitiesCursor.getLong(activitiesCursor.getColumnIndex(BaseHelper.ACTIVITIES_COLUMN_DEVICE)),
                        activitiesCursor.getString(activitiesCursor.getColumnIndex(BaseHelper.ACTIVITIES_COLUMN_TYPE))
                ));
            } while(activitiesCursor.moveToNext());
        }

        activitiesCursor.close();
        close();

        return activities;
    }

    public void saveLocation(Location location, long activityIndex)
    {
        ContentValues values = new ContentValues();
        values.put(BaseHelper.LOCATIONS_COLUMN_ACTIVITY_ID, Long.valueOf(activityIndex));
        values.put(BaseHelper.LOCATIONS_COLUMN_ALTITUDE, Double.valueOf(location.getAltitude()));
        values.put(BaseHelper.LOCATIONS_COLUMN_LATITUDE, String.valueOf(location.getLatitude()));
        values.put(BaseHelper.LOCATIONS_COLUMN_LONGITUDE, String.valueOf(location.getLongitude()));
        values.put(BaseHelper.LOCATIONS_COLUMN_DATE, Long.valueOf(System.currentTimeMillis()));

        open();
        database.insert(BaseHelper.TABLE_NAME_LOCATIONS, null, values);
        close();
    }

    public ArrayList<LocationModel> getActivityLocations(long activityIndex)
    {
        ArrayList<LocationModel> locations = new ArrayList<LocationModel>();
        String[] cursorColumns = {BaseHelper.LOCATIONS_COLUMN_ALTITUDE, BaseHelper.LOCATIONS_COLUMN_LATITUDE,
                BaseHelper.LOCATIONS_COLUMN_LONGITUDE, BaseHelper.LOCATIONS_COLUMN_DATE};
        String[] cursorWhereArgs = {String.valueOf(activityIndex)};

        open();

        Cursor locationsCursor = database.query(BaseHelper.TABLE_NAME_LOCATIONS, cursorColumns,
                BaseHelper.LOCATIONS_COLUMN_ACTIVITY_ID + " = ?", cursorWhereArgs,
                null, null, null, null);

        if(locationsCursor.moveToFirst())
        {
            do
            {
                locations.add(new LocationModel(locationsCursor.getDouble(locationsCursor.getColumnIndex(BaseHelper.LOCATIONS_COLUMN_LATITUDE)),
                        locationsCursor.getDouble(locationsCursor.getColumnIndex(BaseHelper.LOCATIONS_COLUMN_LONGITUDE)),
                        locationsCursor.getDouble(locationsCursor.getColumnIndex(BaseHelper.LOCATIONS_COLUMN_ALTITUDE)),
                        locationsCursor.getDouble(locationsCursor.getColumnIndex(BaseHelper.LOCATIONS_COLUMN_DATE))));
            } while(locationsCursor.moveToNext());
        }

        locationsCursor.close();
        close();

        return locations;
    }

    public void savePulse(int value, long activityIndex)
    {
        ContentValues values = new ContentValues();
        values.put(BaseHelper.PULSE_COLUMN_ACTIVITY_ID, Long.valueOf(activityIndex));
        values.put(BaseHelper.PULSE_COLUMN_VALUE, Integer.valueOf(value));
        values.put(BaseHelper.PULSE_COLUMN_DATE, Long.valueOf(System.currentTimeMillis()));

        open();
        database.insert(BaseHelper.TABLE_NAME_PULSE, null, values);
        close();
    }

    public ArrayList<PulseModel> getActivityPulses(long activityIndex)
    {
        ArrayList<PulseModel> pulses = new ArrayList<PulseModel>();
        String[] cursorColumns = {BaseHelper.PULSE_COLUMN_VALUE, BaseHelper.PULSE_COLUMN_DATE};
        String[] cursorWhereArgs = {String.valueOf(activityIndex)};

        open();

        Cursor pulsesCursor = database.query(BaseHelper.TABLE_NAME_PULSE, cursorColumns,
                BaseHelper.PULSE_COLUMN_ACTIVITY_ID + " = ?", cursorWhereArgs,
                null, null, null, null);

        if(pulsesCursor.moveToFirst())
        {
            do
            {
                pulses.add(new PulseModel(pulsesCursor.getInt(pulsesCursor.getColumnIndex(BaseHelper.PULSE_COLUMN_VALUE)),
                        pulsesCursor.getDouble(pulsesCursor.getColumnIndex(BaseHelper.PULSE_COLUMN_DATE))));
            } while(pulsesCursor.moveToNext());
        }

        pulsesCursor.close();
        close();

        return pulses;
    }

    public void saveSteps(double steps)
    {
        ContentValues values = new ContentValues();
        values.put(BaseHelper.STEPS_COLUMN_DATE, Long.valueOf(System.currentTimeMillis()).toString());
        values.put(BaseHelper.STEPS_COLUMN_VALUE, Double.valueOf(steps).toString());

        open();

        database.insert(BaseHelper.TABLE_NAME_STEPS, null, values);

        close();
    }

    public ArrayList<Calendar> getStepDays()
    {
        ArrayList<Calendar> firstDays = new ArrayList<Calendar>();
        ArrayList<Calendar> finalDays = new ArrayList<Calendar>();

        String[] cursorColumns = {BaseHelper.STEPS_COLUMN_DATE};

        open();

        Cursor daysCursor = database.query(BaseHelper.TABLE_NAME_STEPS, cursorColumns,
                null, null, null, null, BaseHelper.STEPS_COLUMN_ID+" DESC", null);

        if(daysCursor.moveToFirst())
        {
            do
            {
                Calendar day = Calendar.getInstance();
                day.setTimeInMillis(daysCursor.getLong(daysCursor.getColumnIndex(BaseHelper.STEPS_COLUMN_DATE)));
                day.set(Calendar.HOUR_OF_DAY, 0);
                day.set(Calendar.MINUTE, 0);
                day.set(Calendar.SECOND, 0);
                day.set(Calendar.MILLISECOND, 0);

                if(!firstDays.contains(day)) firstDays.add(day);
                else if(!finalDays.contains(day)) finalDays.add(day);

            } while(daysCursor.moveToNext());
        }

        daysCursor.close();
        close();

        return finalDays;
    }

    public ArrayList<StepModel> getStepsByDay(Calendar day)
    {
        day.set(Calendar.HOUR_OF_DAY, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 0);
        long dayStartTimestamp = day.getTimeInMillis();

        day.set(Calendar.HOUR_OF_DAY, 23);
        day.set(Calendar.MINUTE, 59);
        day.set(Calendar.SECOND, 59);
        day.set(Calendar.MILLISECOND, 499);
        long dayEndTimestamp = day.getTimeInMillis();

        ArrayList<StepModel> steps = new ArrayList<StepModel>();

        String[] cursorColumns = {BaseHelper.STEPS_COLUMN_DATE, BaseHelper.STEPS_COLUMN_VALUE};
        String[] cursorWhereArgs = {String.valueOf(dayStartTimestamp), String.valueOf(dayEndTimestamp)};

        open();

        Cursor stepsCursor = database.query(BaseHelper.TABLE_NAME_STEPS, cursorColumns,
                BaseHelper.STEPS_COLUMN_DATE + " BETWEEN ? AND ?", cursorWhereArgs, null, null, null, null);

        if(stepsCursor.moveToFirst())
        {
            do
            {
                steps.add(new StepModel(stepsCursor.getLong(stepsCursor.getColumnIndex(BaseHelper.STEPS_COLUMN_DATE)),
                        stepsCursor.getDouble(stepsCursor.getColumnIndex(BaseHelper.STEPS_COLUMN_VALUE))));
            } while(stepsCursor.moveToNext());
        }

        stepsCursor.close();
        close();

        return steps;
    }
}
