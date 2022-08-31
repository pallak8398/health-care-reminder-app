package com.example.dell.healthcarereminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String database_name = "HRCSystem.db";
    public static final String table1 = "hcr_table";
    public static final String table2 = "medicine_table";
    public static final String table3 = "exercise_table";
    public static final String table4 = "water_table";
    public static final String table5 = "general_table";

    public DatabaseHelper(Context context) {
        super(context,database_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " +table1+ "(id integer primary key autoincrement,type text,rem_id integer,date date,time integer)");
        db.execSQL("create table " +table2+ "(id integer primary key autoincrement,name text,date date,time integer,freq integer,day_freq integer,interval text)");//done
        db.execSQL("create table " +table3+ "(id integer primary key autoincrement,name text,date date,time integer,freq integer)"); //done
        db.execSQL("create table " +table4+ "(id integer primary key autoincrement,start_time text,end_time integer,interval integer)");//done
        db.execSQL("create table " +table5+ "(id integer primary key autoincrement,name text,date date,time integer,freq integer)"); //done
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+table1);
        db.execSQL("DROP TABLE IF EXISTS "+table2);
        db.execSQL("DROP TABLE IF EXISTS "+table3);
        db.execSQL("DROP TABLE IF EXISTS "+table4);
        db.execSQL("DROP TABLE IF EXISTS "+table5);
        onCreate(db);
    }

    public boolean insert_data_general(String nname, String ddate, String ttime, String ffreq)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name",nname);
        cv.put("date",ddate);
        cv.put("time",ttime);
        cv.put("freq",ffreq);
        long result = db.insert(table5,null,cv);
        if(result==-1) {
            return false;
        }
        else
            return true;
    }

    public boolean insert_data_exercise(String nname, String ddate, String ttime, String ffreq)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name",nname);
        cv.put("date",ddate);
        cv.put("time",ttime);
        cv.put("freq",ffreq);
        long result = db.insert(table3,null,cv);
        if(result==-1) {
            return false;
        }
        else
            return true;
    }

    public boolean insert_data_water(String start_time,String end_time,int interval)
    {
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("start_time",start_time);
        contentValues.put("end_time",end_time);
        contentValues.put("interval",interval);
        long result = db.insert(table4,null,contentValues);
        if(result==-1)
            return false;
        else
            return true;
    }

    public boolean insert_med_data(String name, String date, String time, String freq , String freq2, String interval) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String date2 = date;
        cv.put("name",name);
        cv.put("date",date2);
        cv.put("time",time);
        cv.put("freq",freq);
        cv.put("day_freq",freq2);
        cv.put("interval",interval);
        long result = db.insert(table2,null,cv);
        if(result==-1) {
            return false;
        }
        else
            return true;
    }


    public boolean insert_data(String type ,int rem_id ,String date,String time)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("type",type);
        cv.put("rem_id",rem_id);
        cv.put("date",date);
        cv.put("time",time);
        long result = db.insert(table1,null,cv);
        if(result==-1) {
            return false;
        }
        else
            return true;
    }

    public void delete(String table_name , int id,int main_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+table_name+" where id='"+id+"'");
        SQLiteDatabase db1 = this.getWritableDatabase();
        db1.execSQL("delete from "+table1+" where id='"+main_id+"'");
    }

    public void updateonalarm(String table_name , int id,int main_id) {
        Log.d("FREQUENCY"," HELLO f");
        int freq = getfreq(table_name,id);
        Log.d("FREQUENCY"," FREq=> "+freq);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+table_name+" where id='"+id+"'");
        SQLiteDatabase db1 = this.getWritableDatabase();
        db1.execSQL("delete from "+table1+" where id='"+main_id+"'");
    }

    public int getid(String table_name)
    {
        String selectQuery = "SELECT * FROM " + table_name + " ORDER BY " + "id" + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int id=0 ;

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        return id;
    }
    public int getfreq(String table_name,int id)
    {
        String selectQuery = "SELECT * FROM " + table_name +" where id="+id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int freq=0 ;

        if (cursor.moveToFirst() && table_name!="water_table") {
            freq = cursor.getInt(4);
        }
        if (table_name.equals("water_table")) {
            if(cursor.moveToFirst())
            freq = cursor.getInt(3);
        }
        else
        {
            if (cursor.moveToFirst()) {
                freq = cursor.getInt(4);
            }
        }
        return freq;
    }



    public String getname(String table_name,int id)
    {
        String name="";
        if(table_name.compareTo("water_table") == 0)
        {
            name = "Drink Water";
            return name;
        }
        String selectQuery = "SELECT * FROM " + table_name +" WHERE id= "+ id;
        Log.i("ALARMIN",selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

                    name = cursor.getString(1);
                    return name;

        }
        return name;
    }

    public ArrayList fetch_records()
    {
        ArrayList reminderList = new ArrayList();
        String selectQuery = "SELECT * FROM " + table1+ " ORDER BY " + "date , time ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Reminderclass reminderDetails = new Reminderclass();
                int id = cursor.getInt(2);
                String table_name = cursor.getString(1);
                reminderDetails.setName(getname(table_name,id));
                reminderDetails.setDate(cursor.getString(3));
                reminderDetails.setTime(cursor.getString(4));
                reminderDetails.setTableName(cursor.getString(1));
                reminderDetails.setid(cursor.getInt(2));
                reminderDetails.setMainid(cursor.getInt(0));
                reminderList.add(reminderDetails);
            } while (cursor.moveToNext());
        }
        db.close();
        return reminderList;
    }
}
