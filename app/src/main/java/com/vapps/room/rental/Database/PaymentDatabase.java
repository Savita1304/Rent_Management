package com.vapps.room.rental.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vapps.room.rental.Model.HistoryData;
import com.vapps.room.rental.Model.TenantRecord;
import com.vapps.room.rental.Model.TenantRentRecord;

import java.util.ArrayList;

public class PaymentDatabase extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="TenantDB";
    private static final String TABLE_TENANT="TenantTable";
    private static final String KEY_TENANT_ID="TenantId";
    private static final String KEY_TENANT_NAME="TenantName";
    private static final String KEY_TENANT_MOBILE="TenantMobile";

    private static final String KEY_TENANT_IMAGE="TenantImage";
    private static final String KEY_TENANT_RENT="TenantRent";
    private static final String KEY_TENANT_SDATE="TenantSdate";

    private static final String KEY_TENANT_ADDRESS="TenantAddress";
    private static final String KEY_TENANT_EDATE="TenantEdate";

    private static final String KEY_TENANT_ELEC="TenantElect";


    private static final String KEY_TENANT_WATER="TenantWater";

    private static final String KEY_ROOM_NO="RoomNo";

    private static final String KEY_BUILDING_NAME="BuildingName";



    //create another table here for rent management


    private static final String TABLE_RENT="TenantRentTable";
    private static final String KEY_TENANT_RENT_ID="TenantId";
    private static final String KEY_TENANT_RENT_NAME="TenantName";
    private static final String KEY_TENANT_RENT_SDATE="TenantSdate";
    private static final String KEY_TENANT_RENT_AMOUNT="TenantRent";

    private static final String KEY_TENANT_RENT_MONTH="TenantMonth";
    private static final String KEY_TENANT_RENT_YEAR="TenantYear";

    private static final String KEY_TENANT_RENT_PAID="TenantRentPaid";
    private static final String KEY_TENANT_RENT_REMAINING="TenantRentRemaining";
    private static final String KEY_TENANT_RENT_STATE="TenantState";
    private static final String KEY_TENANT_RENT_PDATE="TenantPdate";


    private static final String KEY_TENANT_RENT_MODE="TenantMode";


    private static final String KEY_TENANT_RENT_LDATE="TenantLdate";








    private static final String TABLE_RENT_HISTORY="TenantRentTableHistory";
    private static final String KEY_TENANT_RENT_HISTORY_ID="TenantId";
    private static final String KEY_TENANT_RENT_HISTORY_PAID="TenantPaid";
    private static final String KEY_TENANT_RENT_HISTORY_MODE="TenantMode";
    private static final String KEY_TENANT_RENT_HISTORY_DATE="TenantDate";

    private static final String KEY_TENANT_RENT_HISTORY_MONTH="TenantMonth";
    private static final String KEY_TENANT_RENT_HISTORY_YEAR="TenantYear";





    public PaymentDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table TenantTable " +
                        "(TenantId text unique primary key,TenantName text,TenantMobile text,TenantImage text, TenantRent REAL ,TenantSdate text,TenantAddress text,TenantEdate text,TenantElect REAL,TenantWater REAL,RoomNo text,BuildingName text)"
        );


        db.execSQL(
                "create table TenantRentTable " +
                        "(TenantId text ,TenantName text, TenantSdate text,TenantRent REAL ,TenantMonth text,TenantYear text,TenantRentPaid REAL,TenantRentRemaining REAL,TenantState INTEGER,TenantPdate text,TenantMode text,TenantLdate text,RoomNo text,BuildingName text)"
        );


        db.execSQL(
                "create table TenantRentTableHistory " +
                        "(TenantId text,TenantPaid REAL,TenantMode text,TenantDate text, TenantMonth text ,TenantYear text,RoomNo text,BuildingName text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TenantTable");
        db.execSQL("DROP TABLE IF EXISTS TenantRentTable");
        db.execSQL("DROP TABLE IF EXISTS TenantRentTableHistory");
    }

    public boolean addTenantRentHistory(HistoryData data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TENANT_RENT_HISTORY_ID, data.getId());
        contentValues.put(KEY_TENANT_RENT_HISTORY_PAID, data.getAmt());
        contentValues.put(KEY_TENANT_RENT_HISTORY_MODE, data.getType());
        contentValues.put(KEY_TENANT_RENT_HISTORY_DATE, data.getDate());
        contentValues.put(KEY_TENANT_RENT_HISTORY_MONTH, data.getMonth());
        contentValues.put(KEY_TENANT_RENT_HISTORY_YEAR,data.getYear());

        contentValues.put(KEY_ROOM_NO,data.getRoom());
        contentValues.put(KEY_BUILDING_NAME,data.getBuilding());
        db.insert(TABLE_RENT_HISTORY, null, contentValues);
        return true;
    }

    public boolean addTenantRent(TenantRentRecord trecord){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TENANT_RENT_ID, trecord.getId());
        contentValues.put(KEY_TENANT_RENT_NAME, trecord.getName());
        contentValues.put(KEY_TENANT_RENT_SDATE, trecord.getDate());
        contentValues.put(KEY_TENANT_RENT_AMOUNT, trecord.getAmount());
        contentValues.put(KEY_TENANT_RENT_MONTH, trecord.getMonth());
        contentValues.put(KEY_TENANT_RENT_YEAR,trecord.getYear());
        contentValues.put(KEY_TENANT_RENT_PAID,trecord.getPaid());
        contentValues.put(KEY_TENANT_RENT_REMAINING,trecord.getRemaining());
        contentValues.put(KEY_TENANT_RENT_STATE,trecord.getState());
        contentValues.put(KEY_TENANT_RENT_PDATE,trecord.getPaiddate());

        contentValues.put(KEY_TENANT_RENT_MODE,trecord.getMode());
        contentValues.put(KEY_TENANT_RENT_LDATE,trecord.getLdate());


        contentValues.put(KEY_ROOM_NO,trecord.getRoom());
        contentValues.put(KEY_BUILDING_NAME,trecord.getBuilding());
        db.insert(TABLE_RENT, null, contentValues);
        return true;
    }


    public float getRentRemaining(String mm,String yy,String roomno,String buildingname){
        float list = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select TenantRentRemaining from TenantRentTable where TenantMonth == '"+mm+"' AND TenantYear =='"+yy+"' And RoomNo = '"+roomno+"' And BuildingName = '"+buildingname+"' ";

        Cursor res = db.rawQuery(selectQuery, null);
        while(res.moveToNext()) {


             list = res.getFloat(0);


           

        }
        return list;
    }




    //get individual tenant rent month
    public TenantRentRecord getSingleTenantRent(String mm,String yy,String roomno,String buildingname){
        TenantRentRecord tr = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from TenantRentTable where TenantMonth == '"+mm+"' AND TenantYear =='"+yy+"' And RoomNo = '"+roomno+"' And BuildingName = '"+buildingname+"' ";
       // String selectQuery = "select * from TenantRentTable where  RoomNo = '"+roomno+"' And BuildingName = '"+buildingname+"' ";

        Cursor res = db.rawQuery(selectQuery, null);
        while(res.moveToNext()) {

            String id = res.getString(0);
            String name = res.getString(1);
            String sdate = res.getString(2);
            float amt = res.getFloat(3);
            String mm1  = res.getString(4);
            String yy1 = res.getString(5);
            float paid = res.getFloat(6);
            float rem = res.getFloat(7);
            int state = res.getInt(8);
            String pdate = res.getString(9);

            String mode = res.getString(10);
            String ldate = res.getString(11);
            String room = res.getString(12);
            String building = res.getString(13);




            tr = new TenantRentRecord();
            tr.setId(id);
            tr.setName(name);
            tr.setDate(sdate);
            tr.setAmount(amt);
            tr.setMonth(mm1);
            tr.setYear(yy1);
            tr.setPaid(paid);
            tr.setRemaining(rem);
            tr.setState(state);
            tr.setPaiddate(pdate);
            tr.setMode(mode);
            tr.setLdate(ldate);
            tr.setRoom(room);
            tr.setBuilding(building);


         //   array_list.add(tr);



        }
        return tr;
    }


    //param



    public TenantRentRecord getSingleTenantRent11(String id1){
        TenantRentRecord tr = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from TenantRentTable where TenantId = '"+id1+"'  ";
        // String selectQuery = "select * from TenantRentTable where  RoomNo = '"+roomno+"' And BuildingName = '"+buildingname+"' ";

        Cursor res = db.rawQuery(selectQuery, null);
        while(res.moveToNext()) {

            String id = res.getString(0);
            String name = res.getString(1);
            String sdate = res.getString(2);
            float amt = res.getFloat(3);
            String mm1  = res.getString(4);
            String yy1 = res.getString(5);
            float paid = res.getFloat(6);
            float rem = res.getFloat(7);
            int state = res.getInt(8);
            String pdate = res.getString(9);

            String mode = res.getString(10);
            String ldate = res.getString(11);
            String room = res.getString(12);
            String building = res.getString(13);




            tr = new TenantRentRecord();
            tr.setId(id);
            tr.setName(name);
            tr.setDate(sdate);
            tr.setAmount(amt);
            tr.setMonth(mm1);
            tr.setYear(yy1);
            tr.setPaid(paid);
            tr.setRemaining(rem);
            tr.setState(state);
            tr.setPaiddate(pdate);
            tr.setMode(mode);
            tr.setLdate(ldate);
            tr.setRoom(room);
            tr.setBuilding(building);


            //   array_list.add(tr);



        }
        return tr;
    }

    public ArrayList<TenantRentRecord> getAllTenantRent(String mm,String yy){
        ArrayList<TenantRentRecord> array_list = new ArrayList<TenantRentRecord>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from TenantRentTable where TenantMonth == '"+mm+"' AND TenantYear =='"+yy+"' ";
        Cursor res = db.rawQuery(selectQuery, null);
        while(res.moveToNext()) {

            String id = res.getString(0);
            String name = res.getString(1);
            String sdate = res.getString(2);
            float amt = res.getFloat(3);
            String mm1  = res.getString(4);
            String yy1 = res.getString(5);
            float paid = res.getFloat(6);
            float rem = res.getFloat(7);
            int state = res.getInt(8);
            String pdate = res.getString(9);

            String mode = res.getString(10);
            String ldate = res.getString(11);
            String room = res.getString(12);
            String building = res.getString(13);




            if (ldate == null || ldate.isEmpty()) {

                TenantRentRecord tr = new TenantRentRecord();
                tr.setId(id);
                tr.setName(name);
                tr.setDate(sdate);
                tr.setAmount(amt);
                tr.setMonth(mm1);
                tr.setYear(yy1);
                tr.setPaid(paid);
                tr.setRemaining(rem);
                tr.setState(state);
                tr.setPaiddate(pdate);
                tr.setMode(mode);
                tr.setLdate(ldate);
                tr.setRoom(room);
                tr.setBuilding(building);


                array_list.add(tr);
            }


        }
        return array_list;
    }


    public String getmonthyear(String id){
        String my = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select TenantMonth,TenantYear from TenantRentTable where TenantId == '"+id+"'  ";
        Cursor res = db.rawQuery(selectQuery, null);
        while(res.moveToNext()) {

            String mm1  = res.getString(0);
            String yy1 = res.getString(1);



            my = mm1+"_"+yy1;





        }
        return my;
    }

    public void resetTenantRent(String id,float tr,String mm,String yy,int state){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();


        cv.put(KEY_TENANT_RENT_MONTH,mm);
        cv.put(KEY_TENANT_RENT_YEAR,yy);
        cv.put(KEY_TENANT_RENT_REMAINING,tr);
        cv.put(KEY_TENANT_RENT_STATE,state);
        db.update(TABLE_RENT, cv, KEY_TENANT_RENT_ID+"= '"+ id+"' " , null);



    }

    public ArrayList<TenantRentRecord> getAllTenantRent11(String mm,String yy){
        ArrayList<TenantRentRecord> array_list = new ArrayList<TenantRentRecord>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from TenantRentTable where TenantMonth == '"+mm+"' AND TenantYear =='"+yy+"' And TenantRentRemaining > 0";
        Cursor res = db.rawQuery(selectQuery, null);
        while(res.moveToNext()) {

            String id = res.getString(0);
            String name = res.getString(1);
            String sdate = res.getString(2);
            float amt = res.getFloat(3);
            String mm1  = res.getString(4);
            String yy1 = res.getString(5);
            float paid = res.getFloat(6);
            float rem = res.getFloat(7);
            int state = res.getInt(8);
            String pdate = res.getString(9);

            String mode = res.getString(10);
            String ldate = res.getString(11);
            String room = res.getString(12);
            String building = res.getString(13);




            if (ldate == null ) {


                TenantRentRecord tr = new TenantRentRecord();
                tr.setId(id);
                tr.setName(name);
                tr.setDate(sdate);
                tr.setAmount(amt);
                tr.setMonth(mm1);
                tr.setYear(yy1);
                tr.setPaid(paid);
                tr.setRemaining(rem);
                tr.setState(state);
                tr.setPaiddate(pdate);
                tr.setMode(mode);
                tr.setLdate(ldate);
                tr.setRoom(room);
                tr.setBuilding(building);


                array_list.add(tr);
            }


        }
        return array_list;
    }

    public ArrayList<String> exploreYear(String id){
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select TenantYear from TenantRentTableHistory where TenantId == '"+id+"' ";



        Cursor res = db.rawQuery(selectQuery, null);
        while(res.moveToNext()) {

            String year = res.getString(0);







            array_list.add(year);
        }
        return array_list;

    }



    public ArrayList<TenantRentRecord> getTenantRentRecord(String id1,String year){
        ArrayList<TenantRentRecord> array_list = new ArrayList<TenantRentRecord>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from TenantRentTable where TenantId == '"+id1+"'  And TenantYear == '"+year+"'  ";
        Cursor res = db.rawQuery(selectQuery, null);
        while(res.moveToNext()) {

            String id = res.getString(0);
            String name = res.getString(1);
            String sdate = res.getString(2);
            float amt = res.getFloat(3);
            String mm1  = res.getString(4);
            String yy1 = res.getString(5);
            float paid = res.getFloat(6);
            float rem = res.getFloat(7);
            int state = res.getInt(8);
            String pdate = res.getString(9);

            String mode = res.getString(10);
            String ldate = res.getString(11);

            String room = res.getString(12);
            String building = res.getString(13);


            TenantRentRecord tr = new TenantRentRecord();
            tr.setId(id);
            tr.setName(name);
            tr.setDate(sdate);
            tr.setAmount(amt);
            tr.setMonth(mm1);
            tr.setYear(yy1);
            tr.setPaid(paid);
            tr.setRemaining(rem);
            tr.setState(state);
            tr.setPaiddate(pdate);
            tr.setMode(mode);
            tr.setLdate(ldate);

            tr.setRoom(room);
            tr.setBuilding(building);


            array_list.add(tr);



        }
        return array_list;
    }

    public ArrayList<TenantRentRecord> getTenantRentRecord11(String id1,String year){
        ArrayList<TenantRentRecord> array_list = new ArrayList<TenantRentRecord>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select TenantId,TenantPaid,TenantMode,TenantDate from TenantRentTablehistory where TenantId == '"+id1+"'  And TenantYear == '"+year+"'  ";
        Cursor res = db.rawQuery(selectQuery, null);
        while(res.moveToNext()) {

            String id = res.getString(0);

            float amt = res.getFloat(1);
            String mode  = res.getString(2);
            String pdate = res.getString(3);




            TenantRentRecord tr = new TenantRentRecord();
            tr.setId(id);
            tr.setPaid(amt);
            tr.setMode(mode);
            tr.setPaiddate(pdate);



            array_list.add(tr);



        }
        return array_list;
    }
    public ArrayList<HistoryData> getTenantRentRecordMonth(String id1, String year, String month){
        ArrayList<HistoryData> array_list = new ArrayList<HistoryData>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select TenantPaid,TenantMode,TenantDate ,RoomNo,BuildingName from TenantRentTableHistory where TenantId == '"+id1+"' And TenantYear == '"+year+"' And TenantMonth == '"+month+"'  ";
        Cursor res = db.rawQuery(selectQuery, null);
        while(res.moveToNext()) {


            float paid = res.getFloat(0);
            String mode = res.getString(1);
            String pdate = res.getString(2);

            String room = res.getString(3);
            String building = res.getString(4);




            HistoryData hd = new HistoryData();
            hd.setAmt(String.valueOf(paid));
            hd.setType(mode);
            hd.setDate(pdate);
            hd.setRoom(room);
            hd.setBuilding(building);


            array_list.add(hd);



        }
        return array_list;
    }

    public boolean addTenant(TenantRecord trecord){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TENANT_ID, trecord.getTid());
        contentValues.put(KEY_TENANT_NAME, trecord.getTname());
        contentValues.put(KEY_TENANT_MOBILE, trecord.getTmob());
        contentValues.put(KEY_TENANT_IMAGE, trecord.getTimg());
        contentValues.put(KEY_TENANT_RENT, trecord.getTrent());
        contentValues.put(KEY_TENANT_SDATE, trecord.getTsdate());
        contentValues.put(KEY_TENANT_ADDRESS, trecord.getTadd());
        contentValues.put(KEY_TENANT_ELEC,trecord.getTele());
        contentValues.put(KEY_TENANT_WATER,trecord.getTwater());
        contentValues.put(KEY_ROOM_NO,trecord.getRoomno());
        contentValues.put(KEY_BUILDING_NAME,trecord.getBuildingname());
        db.insert(TABLE_TENANT, null, contentValues);
        return true;
    }


    public ArrayList<TenantRecord> getAllTenant(){
        ArrayList<TenantRecord> array_list = new ArrayList<TenantRecord>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from TenantTable  ";
        Cursor res = db.rawQuery(selectQuery, null);
        while(res.moveToNext()) {

            String id = res.getString(0);
            String name = res.getString(1);
            String mob = res.getString(2);
            String img = res.getString(3);
            float rent = res.getFloat(4);
            String sdate = res.getString(5);
            String add  = res.getString(6);
            String edate = res.getString(7);

            float tele = res.getFloat(8);

            float twater = res.getFloat(9);


            String room  = res.getString(10);
            String building = res.getString(11);





                TenantRecord tr = new TenantRecord();
                tr.setTid(id);
                tr.setTname(name);
                tr.setTmob(mob);
                tr.setTimg(img);
                tr.setTrent(rent);
                tr.setTsdate(sdate);
                tr.setTadd(add);
                tr.setTedate(edate);
                tr.setTele(tele);
                tr.setTwater(twater);
                tr.setRoomno(room);
                tr.setBuildingname(building);
                array_list.add(tr);






        }
        return array_list;
    }


    public TenantRecord getTenant(String id){
        TenantRecord tr = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select TenantMobile,TenantImage,TenantAddress from TenantTable where TenantId = '"+id+"'  ";
        Cursor res = db.rawQuery(selectQuery, null);
        while(res.moveToNext()) {




            String mob = res.getString(0);
            String img = res.getString(1);
            String add  = res.getString(2);






            tr = new TenantRecord();

            tr.setTimg(img);
            tr.setTmob(mob);

            tr.setTadd(add);





        }
        return tr;
    }

    public String getTenantImagePath(String id){
        String path = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select TenantImage from TenantTable where TenantId = '"+id+"' ";
        Cursor res = db.rawQuery(selectQuery, null);
        while(res.moveToNext()) {

            path = res.getString(0);

        }
        return path;
    }


    public String getTenantImagePath1(String room,String building){
        String path = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select TenantImage from TenantTable where RoomNo = '"+room+"' And BuildingName = '"+building+"' ";
        Cursor res = db.rawQuery(selectQuery, null);
        while(res.moveToNext()) {

            path = res.getString(0);

        }
        return path;
    }
    public void updateTenantRent(String id,TenantRentRecord tr,String mm,String yy){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_TENANT_RENT_PAID,tr.getPaid());
        cv.put(KEY_TENANT_RENT_STATE, tr.getState());
        cv.put(KEY_TENANT_RENT_PDATE, tr.getPaiddate());
        cv.put(KEY_TENANT_RENT_REMAINING,tr.getRemaining());
        db.update(TABLE_RENT, cv, KEY_TENANT_RENT_ID+"= '"+ id+"' AND "+KEY_TENANT_RENT_MONTH+"='"+mm+"' AND "+KEY_TENANT_RENT_YEAR+"='"+yy+"'" , null);



    }

    public String getTenantMobileNumber(String id){
        String  num ="";
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select TenantMobile  from TenantTable where TenantId = '"+id+"'  ";
        Cursor res = db.rawQuery(selectQuery, null);
        while(res.moveToNext()) {

            num = res.getString(0);

        }
        return num;
    }

    public void updateTenant(String id,TenantRecord tr){
       /* SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(data,value);
        cv.put(Mamt, amt);
        db.update(Milk, cv, ""+Myear+"= '"+ year+"' AND "+Mmonth+"='"+month+"'  " , null);*/



    }

    public void updateTenantLeft(String id,String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_TENANT_RENT_LDATE,date);
        db.update(TABLE_RENT, cv, KEY_TENANT_RENT_ID+"= '"+ id+"' " , null);



    }

    public void updateTenantLeft1(String id,String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_TENANT_EDATE,date);
        db.update(TABLE_TENANT, cv, KEY_TENANT_ID+"= '"+ id+"' " , null);



    }

}
