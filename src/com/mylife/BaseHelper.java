package com.mylife;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by szymon on 03.11.14.
 */
public class BaseHelper extends SQLiteOpenHelper
{
    //tables name
    public static final String TABLE_NAME_ACTIVITIES = "activities";

    //activities columns
    public static final String ACTIVITIES_COLUMN_ID = "_id";
    public static final String ACTIVITIES_LOCATIONS = "locations";


    private static final String DATABASE_NAME = "mylifedb.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String ACTIVITIES_DATABASE_CREATE_SQL = "create table " + TABLE_NAME_ACTIVITIES +
            "(" + ACTIVITIES_COLUMN_ID + " integer primary key autoincrement, " +
            ACTIVITIES_LOCATIONS + "text);";

    public BaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(ACTIVITIES_DATABASE_CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ACTIVITIES);
        onCreate(sqLiteDatabase);
    }
}
