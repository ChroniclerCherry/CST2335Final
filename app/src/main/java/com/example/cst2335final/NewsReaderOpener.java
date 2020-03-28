package com.example.cst2335final;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NewsReaderOpener extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "BBCDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "NEWSREADER";
    public final static String TITLE = "TITLE";
    public final static String DESC = "DESC";
    public final static String DATE = "DATE";
    public final static String LINK = "LINK";
    public final static String COL_ID = "_id";

    public NewsReaderOpener(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    //This function gets called if no database file exists.
    //Look on your device in the /data/data/package-name/database directory.
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE + " text,"
                + DESC + " text,"
                + DATE  + " text,"
                + LINK + " text);");
    }

    /**
     * This function gets called if the database version on your device is lower than VERSION_NUM.
     * @param db SQLiteDatabase object
     * @param oldVersion int value for version number
     * @param newVersion int value for version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    /**
     * This function gets called if the database version on your device is higher than VERSION_NUM.
     * @param db SQLiteDatabase object
     * @param oldVersion int value for version number
     * @param newVersion int value for version number
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }
}
