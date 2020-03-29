package com.example.cst2335final;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NASADailyOpener extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "MessagesDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "Images";
    public final static String COL_ID = "_id";
    public final static String COL_TITLE = "title";
    public final static String COL_DATE = "date";
    public final static String COL_DESCRIPTION = "description";

    NASADailyOpener(Context ctx) {super(ctx, DATABASE_NAME, null, VERSION_NUM);}

    @Override
    //This function gets called if no database file exists.
    //Look on your device in the /data/data/package-name/database directory.
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " text,"
                + COL_DATE  + " text,"
                + COL_DESCRIPTION + " text);");  // add or remove columns
    }

    //this function gets called if the database version on your device is lower than VERSION_NUM
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    //this function gets called if the database version on your device is higher than VERSION_NUM
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    public void printCursor(Cursor c){
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("Database Version:", Integer.toString(db.getVersion()));
        Log.d("Number of columns: ", Integer.toString(c.getColumnCount()));
        for (int i = 0; i < c.getColumnCount(); i++){
            Log.d("Column "+(i+1)+": ", c.getColumnName(i));
        }
        Log.d("Number of rows:", Integer.toString(c.getCount()));
        Log.d("Cursor Object", DatabaseUtils.dumpCursorToString(c));
    }
}
