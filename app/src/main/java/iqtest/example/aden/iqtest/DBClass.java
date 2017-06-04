package iqtest.example.aden.iqtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;


public class DBClass extends SQLiteOpenHelper {
    private static String DB_PATH= "/data/data/com.example.aden.iqtest/databases/iqtest.db";

    //Database Details
    public static final String DATABASE_NAME = "iqtest";
    public static final int DATABASE_VERSION = 1;

    //Database Tables
    public static final String TABLE_NAME = "usermarks";

    //Database Columns
    public static final String COLUMN1 = "date";
    public static final String COLUMN2 = "marks";

    public DBClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        /*try {
            copyDB(context);
        }catch (IOException e) {
            Toast toast = Toast.makeText(context, "Database fail to write", Toast.LENGTH_SHORT );
            toast.show();
        }*/
    }

    public void copyDB(Context context) throws IOException {
        try {
            InputStream ip =  context.getAssets().open(DATABASE_NAME);
            Log.i("Input Stream....", ip + "");
            String op = DB_PATH;
            OutputStream output = new FileOutputStream(op);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = ip.read(buffer))>0){
                output.write(buffer, 0, length);
            }
            output.flush();
            output.close();
            ip.close();
        }
        catch (IOException e) {
            Toast toast = Toast.makeText(context, "Database fail to write", Toast.LENGTH_SHORT );
            toast.show();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" + COLUMN1 + " DATETIME, " + COLUMN2 + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertMarks(int marks){
        ContentValues values = new ContentValues();
        //Current date and time
        values.put("date", DateFormat.getDateTimeInstance().format(new Date()));
        values.put("marks", marks);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert("usermarks", null, values);
        //db.close();
    }

    public Cursor getAllMarks() {
        String query = "Select * FROM " + TABLE_NAME ;

        //SQLiteDatabase.openDatabase(DB_PATH, null,SQLiteDatabase.OPEN_READWRITE);

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        //db.close();

        return cursor;
    }

    public Cursor getAllQuestions(String difficulty) {
        String query = "Select * FROM " + TABLE_NAME + " where difficulty = '" + difficulty + "'" ;

        //SQLiteDatabase.openDatabase(DB_PATH, null,SQLiteDatabase.OPEN_READWRITE);

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        //db.close();

        return cursor;
    }
}
