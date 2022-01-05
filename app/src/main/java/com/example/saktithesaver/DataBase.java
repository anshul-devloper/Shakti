package com.example.saktithesaver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "mylist.db";
        public static final String TABLE_NAME = "mylist_data";
        public static final String Col1 = "ID";
        public static final String Col2 = "ITEM1";

        public DataBase(Context context){super(context,DATABASE_NAME,null,1);}
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String createTable="CREATE TABLE "+ TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+"ITEM1 TEXT)";
            sqLiteDatabase.execSQL(createTable);
        }
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            String a= "DROP TABLE IF EXISTS " +TABLE_NAME ;
            sqLiteDatabase.execSQL(a);
            onCreate(sqLiteDatabase);
        }
        public boolean addData(String item1){
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(Col2,item1);
            long result=db.insert(TABLE_NAME,null,contentValues);
            if(result==-1){
                return false;
            }else{
                return  true;
            }
        }
        public Cursor getListContent(){
            SQLiteDatabase db=this.getWritableDatabase();
            Cursor data=db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
            return  data;
        }
    }
