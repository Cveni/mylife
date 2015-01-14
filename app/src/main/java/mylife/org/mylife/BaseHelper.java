package mylife.org.mylife;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Szymon on 2014-11-03.
 */

public class BaseHelper extends SQLiteOpenHelper
{
    //tables name
    public static final String TABLE_NAME_ACTIVITIES = "activities";
    public static final String TABLE_NAME_LOCATIONS = "locations";
    public static final String TABLE_NAME_PULSE = "pulse";
    public static final String TABLE_NAME_STEPS = "steps";

    //activities columns
    public static final String ACTIVITIES_COLUMN_ID = "_id";
    public static final String ACTIVITIES_COLUMN_NAME = "name";
    public static final String ACTIVITIES_COLUMN_DATE = "date";
    public static final String ACTIVITIES_COLUMN_DEVICE = "device";
    public static final String ACTIVITIES_COLUMN_TYPE = "type";

    public static final String LOCATIONS_COLUMN_ID = "_id";
    public static final String LOCATIONS_COLUMN_LATITUDE = "latitude";
    public static final String LOCATIONS_COLUMN_LONGITUDE = "longitude";
    public static final String LOCATIONS_COLUMN_ALTITUDE = "altitude";
    public static final String LOCATIONS_COLUMN_DATE = "date";
    public static final String LOCATIONS_COLUMN_ACTIVITY_ID = "activity_id";

    public static final String PULSE_COLUMN_ID = "_id";
    public static final String PULSE_COLUMN_VALUE = "value";
    public static final String PULSE_COLUMN_DATE = "date";
    public static final String PULSE_COLUMN_ACTIVITY_ID = "activity_id";

    public static final String STEPS_COLUMN_ID = "_id";
    public static final String STEPS_COLUMN_DATE = "date";
    public static final String STEPS_COLUMN_VALUE = "value";


    private static final String DATABASE_NAME = "mylifedb.db";
    private static final int DATABASE_VERSION = 3;

    // Database creation sql statement
    private static final String ACTIVITIES_DATABASE_CREATE_SQL = "create table " + TABLE_NAME_ACTIVITIES +
            "(" + ACTIVITIES_COLUMN_ID + " integer primary key autoincrement, " +
            ACTIVITIES_COLUMN_NAME + " text, " +
            ACTIVITIES_COLUMN_DATE + " integer, " +
            ACTIVITIES_COLUMN_DEVICE + " integer, " +
            ACTIVITIES_COLUMN_TYPE + " text);";

    private static final String LOCATIONS_DATABASE_CREATE_SQL = "create table " + TABLE_NAME_LOCATIONS +
            "(" + LOCATIONS_COLUMN_ID + " integer primary key autoincrement, " +
            LOCATIONS_COLUMN_LATITUDE + " text, " +
            LOCATIONS_COLUMN_LONGITUDE + " text, " +
            LOCATIONS_COLUMN_ALTITUDE + " text, " +
            LOCATIONS_COLUMN_DATE + " integer, " +
            LOCATIONS_COLUMN_ACTIVITY_ID + " integer);";

    private static final String PULSE_DATABASE_CREATE_SQL = "create table " + TABLE_NAME_PULSE +
            "(" + PULSE_COLUMN_ID + " integer primary key autoincrement, " +
            PULSE_COLUMN_VALUE + " integer, " +
            PULSE_COLUMN_DATE + " integer, " +
            PULSE_COLUMN_ACTIVITY_ID + " integer);";

    private static final String STEPS_DATABASE_CREATE_SQL = "create table " + TABLE_NAME_STEPS +
            "(" + STEPS_COLUMN_ID + " integer primary key autoincrement, " +
            STEPS_COLUMN_DATE + " integer, " +
            STEPS_COLUMN_VALUE + " text);";

    public BaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(ACTIVITIES_DATABASE_CREATE_SQL);
        sqLiteDatabase.execSQL(LOCATIONS_DATABASE_CREATE_SQL);
        sqLiteDatabase.execSQL(PULSE_DATABASE_CREATE_SQL);
        sqLiteDatabase.execSQL(STEPS_DATABASE_CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ACTIVITIES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOCATIONS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PULSE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STEPS);
        onCreate(sqLiteDatabase);
    }
}
