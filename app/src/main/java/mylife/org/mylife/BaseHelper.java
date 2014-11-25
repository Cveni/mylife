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
    public static final String TABLE_NAME_STEPS = "steps";

    //activities columns
    public static final String ACTIVITIES_COLUMN_ID = "_id";
    public static final String ACTIVITIES_COLUMN_DATE = "date";
    public static final String ACTIVITIES_COLUMN_TYPE = "type";
    public static final String ACTIVITIES_COLUMN_LOCATIONS = "locations";
    public static final String ACTIVITIES_COLUMN_PULSE = "pulse";

    public static final String STEPS_COLUMN_ID = "_id";
    public static final String STEPS_COLUMN_DATE = "date";
    public static final String STEPS_COLUMN_VALUE = "value";


    private static final String DATABASE_NAME = "mylifedb.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String ACTIVITIES_DATABASE_CREATE_SQL = "create table " + TABLE_NAME_ACTIVITIES +
            "(" + ACTIVITIES_COLUMN_ID + " integer primary key autoincrement, " +
            ACTIVITIES_COLUMN_DATE + "integer, " +
            ACTIVITIES_COLUMN_TYPE + "text, " +
            ACTIVITIES_COLUMN_LOCATIONS + "text, " +
            ACTIVITIES_COLUMN_PULSE + "text);";

    private static final String STEPS_DATABASE_CREATE_SQL = "create table " + TABLE_NAME_STEPS +
            "(" + STEPS_COLUMN_ID + " integer primary key autoincrement, " +
            STEPS_COLUMN_DATE + "integer, " +
            STEPS_COLUMN_VALUE + "text);";

    public BaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(ACTIVITIES_DATABASE_CREATE_SQL);
        sqLiteDatabase.execSQL(STEPS_DATABASE_CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ACTIVITIES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STEPS);
        onCreate(sqLiteDatabase);
    }
}
