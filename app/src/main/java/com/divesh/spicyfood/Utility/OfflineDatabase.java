package com.divesh.spicyfood.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.divesh.spicyfood.Model.CartData;

import java.math.BigInteger;
import java.util.ArrayList;
//this class is used for accessing sqlite database for storing cart items.
public class OfflineDatabase extends SQLiteOpenHelper {
    //for cart table
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SpicyFood";
    private static final String TABLE_NAME = "foodItems";
    private static final String KEY_ID = " cart_id ";
    private static final String ITEM_NAME = " itemname ";
    private static final String RATE = " price ";
    private static final String QTY = " qty ";
    private static final String IMAGE = " image ";
    private static final String DESC = " description ";

    //for userTable
    private static final String TABLE_NAME1 = "users";
    private static final String USER_ID = " id ";
    private static final String USER_NAME = " name ";
    private static final String USER_EMAIL = " email ";
    private static final String USER_ADD = " address ";
    private static final String USER_IMAGE = " image ";
    private static final String USER_PASS = " password ";


    //constructor for accessing database
    public OfflineDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

        //creating database and table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CART = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + ITEM_NAME + " TEXT," + RATE + " TEXT," + QTY + " TEXT, " + IMAGE + " BLOB ," + DESC + " TEXT"   +" )";
        String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_NAME1 + "(" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_NAME + " TEXT," + USER_EMAIL + " TEXT," + USER_ADD + " TEXT, " + USER_IMAGE + " BLOB ," + USER_PASS + " TEXT"   +" )";

        db.execSQL(CREATE_TABLE_CART);
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE " + TABLE_NAME);
        db.execSQL(" DROP TABLE " + TABLE_NAME1);
        onCreate(db);

    }
        //checking the item whether it exists in database or not.
    public boolean CheckIfNameExist(String fieldValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ITEM_NAME + " like '" + fieldValue + "' ";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    //adding item to sqlite database.
    public boolean addtoCart(String aitemname, String price,
                             String itemqty, byte[] pimage,
                             String description) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEM_NAME, aitemname);
        values.put(RATE, price);
        values.put(QTY, itemqty);
        values.put(IMAGE, pimage);
        values.put(DESC, description);

        long id = db.insert(TABLE_NAME, null, values);
        if (id == -1) {
            return false;
        }
        return true;
    }

        //updating the values stored in database
    public boolean UpdateData(String itemname, String mqty) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(QTY, mqty);

        int n = db.update(TABLE_NAME,values, "ITEMNAME = ?",new String[] {itemname});

        boolean value = false;

        if (n==1)
            value = true;

        return  value;
    }
        //getting all the data stored in foodItems table
    public ArrayList<CartData> getAllData() {
        ArrayList<CartData> d = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String select = "SELECT * FROM " + TABLE_NAME;
        cursor  = db.rawQuery(select, null);

        try{
            if (cursor != null && cursor.getCount() > 0){
                if (cursor.moveToFirst()) {
                    do {
                        CartData dta = new CartData(cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getBlob(4),
                                cursor.getString(5));
                        d.add(dta);
                    } while (cursor.moveToNext());
                }
            }
            db.close();
        }catch (Exception e){
            Log.e("Log",e.toString());
        }

        return d;
    }
    public boolean DeleteData(String name){
        SQLiteDatabase db = this.getReadableDatabase();

        int n = db.delete(TABLE_NAME,"ITEMNAME = ?",new String[] {name});

        boolean value = false;

        if (n==1)
            value = true;

        return value;
    }

    public long getItemsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        Log.e("ffde", cnt + "");
        db.close();
        return cnt;
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }

    public Double getTotalAmount(){

        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        double amount = 0;

        if (cursor!=null) {
            if (cursor.moveToFirst()) {

                do {
                    amount = amount + Double.valueOf(cursor.getString(2)) * Double.valueOf(cursor.getString(3));

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return amount;
    }

    public boolean saveUserToDatabase(String name,String email
                                        ,String address,String password)
    {
     SQLiteDatabase database = this.getWritableDatabase();
     ContentValues values = new ContentValues();
        values.put(USER_NAME,name);
        values.put(USER_EMAIL,email);
        values.put(USER_ADD,address);
        values.put(USER_PASS,password);
        //values.put(USER_IMAGE,image);

        long result = database.insert(TABLE_NAME1,null,values);

        if (result == -1) {
            return false;
        }
        return true;
    }

    public boolean checkUser(String email, String password){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME1+" WHERE " + USER_EMAIL + " =? AND " + USER_PASS + " =?", new String[]{email,password});

        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

}
