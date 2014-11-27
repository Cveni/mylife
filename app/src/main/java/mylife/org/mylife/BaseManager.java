package mylife.org.mylife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;

import java.util.ArrayList;

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

    public long createNewActivity(String type)
    {
        ContentValues values = new ContentValues();
        values.put(BaseHelper.ACTIVITIES_COLUMN_DATE, Long.valueOf(System.currentTimeMillis()).toString());
        values.put(BaseHelper.ACTIVITIES_COLUMN_TYPE, type);

        open();
        long id = database.insert(BaseHelper.TABLE_NAME_ACTIVITIES, null, values);
        close();

        return id;
    }

    public Bundle getActivityInformation(long id)
    {
        Bundle b = new Bundle();

        String[] allColumns = {BaseHelper.ACTIVITIES_COLUMN_ID, BaseHelper.ACTIVITIES_COLUMN_DATE,
                BaseHelper.ACTIVITIES_COLUMN_TYPE};

        open();
        String selection = BaseHelper.ACTIVITIES_COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor item = database.query(BaseHelper.TABLE_NAME_ACTIVITIES, allColumns, selection, selectionArgs,
                null, null, null);

        if(item.moveToFirst())
        {
            b.putLong("id", item.getLong(item.getColumnIndex(BaseHelper.ACTIVITIES_COLUMN_ID)));
            b.putLong("date", item.getLong(item.getColumnIndex(BaseHelper.ACTIVITIES_COLUMN_DATE)));
            b.putString("type", item.getString(item.getColumnIndex(BaseHelper.ACTIVITIES_COLUMN_TYPE)));
        }
        item.close();
        close();

        return b;
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

    public void saveSteps(double steps)
    {
        ContentValues values = new ContentValues();
        values.put(BaseHelper.STEPS_COLUMN_DATE, Long.valueOf(System.currentTimeMillis()).toString());
        values.put(BaseHelper.STEPS_COLUMN_VALUE, Double.valueOf(steps).toString());

        open();

        database.insert(BaseHelper.TABLE_NAME_STEPS, null, values);

        close();
    }
}
