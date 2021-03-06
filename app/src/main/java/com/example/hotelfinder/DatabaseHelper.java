package com.example.hotelfinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "hotels.db";
    public static final String TABLE_NAME = "room_table";
    public static final String COL_1 = "room_id";
    public static final String COL_2 = "type";
    public static final String COL_3 = "size";
    public static final String COL_4 = "price";
    public static final String COL_5 = "status";

    public static final String DESCRIPTION = "description";
    public static final String DES_col1 = "Room_type";
    public static final String DES_col2 = "no_of_beds";
    public static final String DES_col3 = "no_of_washrooms";
    public static final String DES_col4 = "Service_charge";
    public static final String DES_col5 = "Wifi";

    public static final String USER = "user";
    public static final String USER_col1 = "user_id";
    public static final String USER_col2 = "user_name";
    public static final String USER_col3 = "arrival_date";
    public static final String USER_col4 = "departure_date";
    public static final String USER_col5 = "mode_of_payment";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DESCRIPTION + "(room_type TEXT PRIMARY KEY , no_of_beds INT , no_of_washrooms TEXT, service_charge TEXT, wifi TEXT)");
        db.execSQL("create table " + TABLE_NAME + "(room_id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT, size INT, price INT, status TEXT" +
                ",FOREIGN KEY(type) references description (room_type)" +
                ")");

        db.execSQL("create table " + USER + "(user_id INTEGER PRIMARY KEY AUTOINCREMENT, user_name TEXT , arrival_date TEXT, departure_date TEXT, mode_of_payment TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " +DESCRIPTION);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " +USER);
        onCreate(db);


    }

    public boolean insertData(String type, String size, int price, String status)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, type);
        contentValues.put(COL_3, size);
        contentValues.put(COL_4, price);
        contentValues.put(COL_5, status);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result==-1)
            return false;
        else
            return true;
    }


    public boolean add_data_for_roomdescription(String type,int beds,int washrooms,int service,String wifi){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DES_col1, type);
        contentValues.put(DES_col2, beds);
        contentValues.put(DES_col3, washrooms);
        contentValues.put(DES_col4, service);
        contentValues.put(DES_col5, wifi);
        long result = db.insert(DESCRIPTION, null, contentValues);
        if(result==-1)
            return false;
        else
            return true;

    }

    public boolean insert_user(String name, String arrival, String departure, String mode)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_col2,name);
        cv.put(USER_col3,arrival);
        cv.put(USER_col4,departure);
        cv.put(USER_col5,mode);
        long result = db.insert(USER, null, cv);
        if(result==-1)
            return false;
        else
            return true;

    }

    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAME, null);
        return res;
    }

    public Cursor getAllData_description()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ DESCRIPTION, null);
        return res;
    }

    public int print_revenue()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c;
        int amount;
        c = db.rawQuery(" select SUM ( "+ COL_4 +") from " + TABLE_NAME , null);
        if (c.getCount() == 0) {
           return 1;
        }
        if(c.moveToFirst())
            amount = c.getInt(0);
        else
            amount = -1;
        c.close();

        return amount;
    }

    public int print_booked() {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c;
        int amount;
        c = db.rawQuery(" select COUNT(*) from " + TABLE_NAME + " where " + COL_5 + "='Unavailable' ", null);
        if (c.getCount() == 0) {
            return 1;
        }
        if(c.moveToFirst())
            amount = c.getInt(0);
        else
            amount = -1;
        c.close();

        return amount;
    }

    public Cursor print_unbooked() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res;
        res = db.rawQuery(" select * from " + TABLE_NAME + " where " + COL_5 + "='Available' ", null);
        return res;
    }

    public Cursor print_advanced(String type,int budget) {

        SQLiteDatabase db = this.getWritableDatabase();


        Cursor res;
        res = db.rawQuery(" select * from " + TABLE_NAME + " where " + COL_4 + " <= " + budget + " and type = '" + type + "' and status = 'Available'"  , null);

        return res;

    }

    public int booking(int parseInt) {

        SQLiteDatabase db = this.getWritableDatabase();

        String strSQL = "UPDATE " + TABLE_NAME + " SET status = 'Unvailable' WHERE room_id = "+ parseInt;
            db.execSQL(strSQL);

        Cursor c;
        int amount;
        c = db.rawQuery(" select SUM ( "+ COL_4 +") from " + TABLE_NAME + " WHERE room_id = " + parseInt, null);
        if (c.getCount() == 0) {
            return 1;
        }
        if(c.moveToFirst())
            amount = c.getInt(0);
        else
            amount = -1;
        c.close();

        return amount;


    }
}

