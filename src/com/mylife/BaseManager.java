package com.mylife;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

/**
 * Created by szymon on 03.11.14.
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

    public void saveLocation(Location location, int activityIndex)
    {
        
    }
}
