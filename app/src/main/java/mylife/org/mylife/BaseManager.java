package mylife.org.mylife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

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

    public long createNewActivity()
    {
        ContentValues values = new ContentValues();
        values.put(BaseHelper.ACTIVITIES_COLUMN_LOCATIONS, "");

        open();
        long id = database.insert(BaseHelper.TABLE_NAME_ACTIVITIES, null, values);
        close();

        return id;
    }

    public void saveLocation(Location location, int activityIndex)
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
                    + ";" + location.getLatitude() + ":" + location.getLongitude();

            ContentValues values = new ContentValues();
            values.put(BaseHelper.ACTIVITIES_COLUMN_LOCATIONS, locations);

            database.update(BaseHelper.TABLE_NAME_ACTIVITIES, values, whereClause, whereArgs);
        }

        activityCursor.close();
        close();
    }

    public String getActivityLocations(int activityIndex)
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
