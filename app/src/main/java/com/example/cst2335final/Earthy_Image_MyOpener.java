/**Course Name: CST2335
 * Class Name: MyOpener
 * Date: 03/16/2020
 *
 * Class extends SQLiteOpenHelper and will create or update SQLite database
 *
 * @author Karl Rezansoff 040955782
 * @version 1.0
 */
package com.example.cst2335final;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Earthy_Image_MyOpener extends SQLiteOpenHelper {
    /**
     * {@value} Constant for name of the database
     */
    protected final static String DATABASE_NAME = "EarthyImageDB";
    /**
     * {@value} Constant for version number of database. If database version differs from this value, a new database will be created.
     */
    protected final static int VERSION_NUM = 2;
    /**
     * {@value} Constant for name of the database table.
     */
    public final static String TABLE_NAME = "Images";
    /**
     * {@value} Constant for the name of the date column.
     */
    public final static String DATE = "date";
    /**
     * {@value} Constant for the name of the name column.
     */
    public final static String NAME = "name";
    /**
     * {@value} Constant for the name of the latitude column.
     */
    public final static String LATITUDE = "latitude";
    /**
     * {@value} Constant for the name of the longitude column.
     */
    public final static String LONGITUDE = "longitude";
    /**
     * {@value} Constant for the name of the id column
     */
    public final static String COL_ID = "_id";

    /**
     * Class contructor
     * @param ctx Context of current activity
     */
    public Earthy_Image_MyOpener(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * This method gets called if no database file exists. Creates a new SQLite database.
     * @param db SQLiteDatabase object
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " text,"
                + DATE + " text,"
                + LATITUDE  + " text,"
                + LONGITUDE + " text);");  // add or remove columns
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
