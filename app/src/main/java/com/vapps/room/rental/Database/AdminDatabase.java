package com.vapps.room.rental.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vapps.room.rental.Model.Admin;
import com.vapps.room.rental.Model.Room;

import java.util.ArrayList;

public class AdminDatabase extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="AdminDB";
    private static final String TABLE_ADMIN="AdminTable";
    private static final String KEY_ID="Id";
    private static final String KEY_NAME="Name";
    private static final String KEY_STATUS="Status";



    //new table for rooms

    private static final String TABLE_ROOM="RoomTable";
    private static final String KEY_ROOM_NO="RoomNo";
    private static final String KEY_BOOK="Book";
    private static final String KEY_INTERIOR="Interior";
    private static final String KEY_SIZE="Size";
    private static final String KEY_FURNITURE="Furniture";

    private static final String KEY_RENT="Rent";

    private static final String KEY_BUILDING_NAME="BuildingName";


    public AdminDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table AdminTable " +
                        "(Id text unique primary key,Name text,Status text)"
        );


        db.execSQL(
                "create table RoomTable " +
                        "(RoomNo text,Book integer,Interior integer,Size text,Furniture integer,Rent REAL,BuildingName text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS AdminTable");

        db.execSQL("DROP TABLE IF EXISTS RoomTable");
    }



    public boolean addRoom(Room room){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ROOM_NO, room.getRoomNo());
        contentValues.put(KEY_BOOK,room.getBook());
        contentValues.put(KEY_INTERIOR,room.getInterior());
        contentValues.put(KEY_SIZE,room.getSize());
        contentValues.put(KEY_FURNITURE,room.getFurniture());
        contentValues.put(KEY_RENT,room.getRent());
        contentValues.put(KEY_BUILDING_NAME,room.getBuildingName());
        db.insert(TABLE_ROOM, null, contentValues);
        return true;
    }

    public ArrayList<Room> getRoomDetail(String name){
        ArrayList<Room> array_list = new ArrayList<Room>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from RoomTable where BuildingName = '"+name+"'  ";
        Cursor res = db.rawQuery(selectQuery, null);
        while(res.moveToNext()) {

            String roomno = res.getString(0);
            int book = res.getInt(1);
            int interior = res.getInt(2);
            String size = res.getString(3);
            int furniture = res.getInt(4);
            float rent  = res.getFloat(5);
            String name1 = res.getString(6);

            Room room = new Room();
            room.setRoomNo(roomno);
            room.setBook(book);
            room.setInterior(interior);
            room.setSize(size);
            room.setFurniture(furniture);
            room.setRent(rent);
            room.setBuildingName(name1);
            array_list.add(room);



        }
        return array_list;
    }


    public int getRoom(String name){
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select RoomNo  from RoomTable where BuildingName = '"+name+"' ";

        //String query = "SELECT RoomNo from RoomTable ORDER BY column DESC LIMIT 1 where BuildingName = '"+name+"'";


        Cursor res = db.rawQuery(selectQuery, null);
        while(res.moveToNext()) {


            //count = res.getInt(0);
            count++;

        }
        Log.e("CCC","count :"+count);
        return count;
    }

    public boolean addBuilding(Admin admin){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, admin.getId());
        contentValues.put(KEY_NAME,admin.getName());
        contentValues.put(KEY_STATUS,admin.getStatus());
        db.insert(TABLE_ADMIN, null, contentValues);
        return true;
    }


 /*   public ArrayList<String> getbuilding(){
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select Name from AdminTable  ";
        Cursor res = db.rawQuery(selectQuery, null);
        while(res.moveToNext()) {

            //String id = res.getString(0);
            String name = res.getString(0);


            array_list.add(name);



        }
        return array_list;
    }*/



    public ArrayList<Admin> getbuilding(){
        ArrayList<Admin> array_list = new ArrayList<Admin>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from AdminTable  ";
        Cursor res = db.rawQuery(selectQuery, null);
        while(res.moveToNext()) {

            String id = res.getString(0);
            String name = res.getString(1);

            String status = res.getString(2);

            Admin admin = new Admin();
            admin.setId(id);
            admin.setName(name);
            admin.setStatus(status);

            array_list.add(admin);



        }
        return array_list;
    }


    public void updateBuilding(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_STATUS,"Yes");

        db.update(TABLE_ADMIN, cv, KEY_ID+"= '"+ id+"'   " , null);



    }

    public Float getrent(String room,String building){
        float rent = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select Rent from RoomTable where RoomNo = '"+room+"' AND BuildingName = '"+building+"' ";
        Cursor res = db.rawQuery(selectQuery, null);
        while(res.moveToNext()) {

            rent = res.getFloat(0);




        }
        return rent;
    }

    public String getstatus(String id){
        String status="";
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select Status from AdminTable where Id = '"+id+"' ";
        Cursor res = db.rawQuery(selectQuery, null);
        while(res.moveToNext()) {

            status = res.getString(0);




        }
        return status;
    }



    public void updateRoom(Room rm,String roomno,String building){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ROOM_NO,rm.getRoomNo());

        cv.put(KEY_INTERIOR,rm.getInterior());
        cv.put(KEY_FURNITURE,rm.getFurniture());
        cv.put(KEY_SIZE,rm.getSize());
        cv.put(KEY_RENT,rm.getRent());

        db.update(TABLE_ROOM, cv, KEY_ROOM_NO+"= '"+ roomno+"'  AND "+KEY_BUILDING_NAME+"= '"+ building+"' " , null);



    }

    public void updateRoomRent(float rent,String roomno,String building){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_RENT,rent);
        cv.put(KEY_BOOK,1);

        db.update(TABLE_ROOM, cv, KEY_ROOM_NO+"= '"+ roomno+"'  AND "+KEY_BUILDING_NAME+"= '"+ building+"'   " , null);



    }


    public void updateRoom(String room,String building){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_BOOK,0);
        cv.put(KEY_INTERIOR,0);
        cv.put(KEY_SIZE,"");
        cv.put(KEY_FURNITURE,0);
        cv.put(KEY_RENT,0.0);
        db.update(TABLE_ROOM, cv, KEY_ROOM_NO+"= '"+ room+"' And "+KEY_BUILDING_NAME+"='"+building+"'" , null);



    }

}
