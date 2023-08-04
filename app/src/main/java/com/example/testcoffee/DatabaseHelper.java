package com.example.testcoffee;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TRANSACTION_TABLE = "TRANSACTION_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_DATE_AND_TIME = "DATE_AND_TIME";
    public static final String COLUMN_ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
    public static final String COLUMN_CARD_TYPE = "CARD_TYPE";
    public static final String COLUMN_APPRVD_AMT = "APPRVD_AMT";
    public static final String COLUMN_ECR_REF_NUM = "ECR_REF_NUM";
    public static final String COLUMN_AUTH_CODE = "AUTH_CODE";
    public static final String COLUMN_HOST_CODE = "HOST_CODE";
    public static final String COLUMN_HOST_RESPONSE = "HOST_RESPONSE";
    public static final String COLUMN_HOST_MESSAGE = "HOST_MESSAGE";
    public static final String COLUMN_REF_NUM = "REF_NUM";
    public static final String COLUMN_NAME = "NAME";

    public DatabaseHelper(@Nullable Context context) { super(context, "transactionDB.db", null, 2); }

    //Use when the db is first being created
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE "+TRANSACTION_TABLE +" ("+ COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE_AND_TIME + " TEXT," +COLUMN_ACCOUNT_NUMBER + " TEXT, " + COLUMN_CARD_TYPE + " INT, " +
                COLUMN_APPRVD_AMT + " TEXT, " + COLUMN_ECR_REF_NUM + " TEXT, " + COLUMN_NAME + " TEXT, " + COLUMN_AUTH_CODE + " TEXT, " +
                COLUMN_HOST_CODE + " TEXT, " +COLUMN_HOST_RESPONSE + " TEXT, " + COLUMN_HOST_MESSAGE + " TEXT, " +
                COLUMN_REF_NUM + " TEXT )";
        db.execSQL(createTableStatement);
    }
    public boolean addOne(transactionModel transactionModel){
    //public void addOne (transactionModel transactionModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DATE_AND_TIME, transactionModel.getTransDateTime());
        cv.put(COLUMN_ACCOUNT_NUMBER, transactionModel.getAcctNm());
        cv.put(COLUMN_CARD_TYPE, transactionModel.getCardType());
        cv.put(COLUMN_APPRVD_AMT, transactionModel.getApprvdamt());
        cv.put(COLUMN_ECR_REF_NUM, transactionModel.getEcrRef());
        cv.put(COLUMN_NAME, transactionModel.getNameCard());
        cv.put(COLUMN_AUTH_CODE, transactionModel.getAuthCode());
        cv.put(COLUMN_HOST_CODE, transactionModel.getHostCode());
        cv.put(COLUMN_HOST_RESPONSE, transactionModel.getHostResponse());
        cv.put(COLUMN_HOST_MESSAGE, transactionModel.getHostMessage());
        cv.put(COLUMN_REF_NUM, transactionModel.getRefNum());
        //cv.put(COLUMN_ID, customerModel.getIntID()); Do not have to use this line bc auto increment otherwise use me
        db.insert(TRANSACTION_TABLE, null, cv);
        long insert = db.insert(TRANSACTION_TABLE,null,cv );

        if(insert == -1){
            return false;
        }
        else{
            return true;

        }

    }

    //When version of db changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

   // public ArrayList<transactionModel> getEverything(){
    public List<transactionModel> getEverything(){

        java.util.List<transactionModel> returnList = new ArrayList<>();
        //Get data from database
        String queryString = "SELECT * FROM " + TRANSACTION_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()){
            //Loop results and create new customer results and put into a return list
            do{

                String transdatetime = cursor.getString(0);
                String acctNm = cursor.getString(1);
                String cardType = cursor.getString(2);
                String transType = cursor.getString(3);
                String apprvdAmt = cursor.getString(4);
                String ecrRef = cursor.getString(5);
                String nameCard = cursor.getString(6);
                String authCode = cursor.getString(7);
                String hostCode = cursor.getString(8);
                String hostResponse = cursor.getString(9);
                String hostMessage = cursor.getString(10);
                String refNum = cursor.getString(11);
            }while(cursor.moveToNext());
        }
        else{
            //Failure
        }
        cursor.close();
        db.close();
        return returnList;

    }
    public String getSpecific(String ecr, String ref, String cell){
        String queryString = "SELECT "+cell+" FROM "+TRANSACTION_TABLE+" WHERE "+COLUMN_ECR_REF_NUM+
                " = '"+ecr+"' AND "+COLUMN_REF_NUM+" = '"+ref+"'";

        String results = "null";
        //String results;
        System.out.println("LOOKUP: "+queryString);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);


        if (cursor.moveToFirst()) {
            results = cursor.getString(cursor.getColumnIndex(cell));
            System.out.println("LOOKUP "+cell+" Results: "+results);;
       }
        else{
          //Failure
       }
        cursor.close();
        db.close();
        return results;

    }
    public void isEmpty(){
        java.util.List<transactionModel> returnList = new ArrayList<>();
        //Get data from database
        String queryString = "SELECT COUNT (*) FROM "+ TRANSACTION_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        int dataCount = cursor.getCount();
        if (dataCount >=1){
            System.out.println("There are "+dataCount);
        }
        else{

            System.out.println("There are less than 1 items in Cursor "+dataCount);
        }

        cursor.close();
        db.close();
    }
//    public List<transactionModel> getSomething(String ecr_ref, String dateTime, String refN){
//
//        java.util.List<transactionModel> returnList = new ArrayList<>();
//        //Get data from database based on ecrRefNUM
//        String queryString = "SELECT "+ecr_ref+" FROM " + TRANSACTION_TABLE;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(queryString, null);
//        if (cursor.moveToFirst()){
//            //Loop results and create new customer results and put into a return list
//            do{
//                int transactionID = cursor.getInt(0);
//                String transdatetime = cursor.getString(1);
//                String acctNm = cursor.getString(2);
//                String cardType = cursor.getString(3);
//                String transType = cursor.getString(4);
//                String apprvdAmt = cursor.getString(5);
//                String ecrRef = cursor.getString(6);
//                String nameCard = cursor.getString(7);
//                String authCode = cursor.getString(8);
//                String hostCode = cursor.getString(9);
//                String hostResponse = cursor.getString(10);
//                String hostMessage = cursor.getString(11);
//                String refNum = cursor.getString(12);
//            }while(cursor.moveToNext());
//        }
//        else{
//            //Failure
//        }
//        cursor.close();
//        db.close();
//        return returnList;
//
//    }



}
