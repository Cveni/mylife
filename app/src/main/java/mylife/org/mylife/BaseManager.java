package mylife.org.mylife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;

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
        values.put(BaseHelper.ACTIVITIES_COLUMN_LOCATIONS, "");
        values.put(BaseHelper.ACTIVITIES_COLUMN_PULSE, "");

        open();
        long id = database.insert(BaseHelper.TABLE_NAME_ACTIVITIES, null, values);
        close();

        return id;
    }

    public Bundle getActivityInformation(long id)
    {
        Bundle b = new Bundle();

        String[] allColumns = {BaseHelper.ACTIVITIES_COLUMN_ID, BaseHelper.ACTIVITIES_COLUMN_DATE,
                BaseHelper.ACTIVITIES_COLUMN_TYPE, BaseHelper.ACTIVITIES_COLUMN_LOCATIONS,
                BaseHelper.ACTIVITIES_COLUMN_PULSE};

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
            b.putString("locations", item.getString(item.getColumnIndex(BaseHelper.ACTIVITIES_COLUMN_LOCATIONS)));
            b.putString("pulse", item.getString(item.getColumnIndex(BaseHelper.ACTIVITIES_COLUMN_PULSE)));
        }
        item.close();
        close();

        return b;
    }

    public void saveLocation(Location location, long activityIndex)
    {
        String whereClause = BaseHelper.ACTIVITIES_COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(activityIndex)};

        String[] cursorColumns = {BaseHelper.ACTIVITIES_COLUMN_LOCATIONS};
        String[] cursorWhereArgs = {String.valueOf(activityIndex)};

        open();

        Cursor activityCursor = database.query(BaseHelper.TABLE_NAME_ACTIVITIES, cursorColumns,
                BaseHelper.ACTIVITIES_COLUMN_ID + " = ?", cursorWhereArgs,
                null, null, null, null);

        if(activityCursor.moveToFirst())
        {
            String locations = activityCursor.getString(activityCursor.getColumnIndex(BaseHelper.ACTIVITIES_COLUMN_LOCATIONS))
                    + ";" + String.valueOf(location.getLatitude()) +
                    ":" + String.valueOf(location.getLongitude()) +
                    ":" + String.valueOf(location.getAltitude()) +
                    ":" + String.valueOf(System.currentTimeMillis());

            ContentValues values = new ContentValues();
            values.put(BaseHelper.ACTIVITIES_COLUMN_LOCATIONS, locations);

            database.update(BaseHelper.TABLE_NAME_ACTIVITIES, values, whereClause, whereArgs);
        }

        activityCursor.close();
        close();
    }

    public String getActivityLocations(long activityIndex)
    {
        String locations = null;
        String[] cursorColumns = {BaseHelper.ACTIVITIES_COLUMN_LOCATIONS};
        String[] cursorWhereArgs = {String.valueOf(activityIndex)};

        open();

        Cursor activityCursor = database.query(BaseHelper.TABLE_NAME_ACTIVITIES, cursorColumns,
                BaseHelper.ACTIVITIES_COLUMN_ID + " = ?", cursorWhereArgs,
                null, null, null, null);

        if(activityCursor.moveToFirst())
        {
            locations = activityCursor.getString(activityCursor.getColumnIndex(BaseHelper.ACTIVITIES_COLUMN_LOCATIONS));
        }

        activityCursor.close();
        close();

        return locations;
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
