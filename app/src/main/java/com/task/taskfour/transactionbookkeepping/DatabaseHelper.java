package com.task.taskfour.transactionbookkeepping;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Personal on 2/1/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "bookkeeping.db";

    public static final String TABLE_NAME_EXPENSES = "expenses";

    public static final String TABLE_NAME_INCOME = "income";

    public static final String COL_EXPENSES_ID = "ID";

    public static final String COL_EXPENSES_DESC = "Description";

    public static final String COL_EXPENSES_AMOUNT = "Amount";

    public static final String COL_INCOME_ID = "ID";

    public static final String COL_INCOME_DESC = "Description";

    public static final String COL_INCOME_AMOUNT = "Amount";

    public static final String TABLE_CREATE_EXPENSES = "CREATE TABLE " + TABLE_NAME_EXPENSES + " ( " +

            COL_EXPENSES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

            COL_EXPENSES_DESC + " TEXT, " +

            COL_EXPENSES_AMOUNT + " TEXT );";

    public static final String TABLE_CREATE_INCOME = "CREATE TABLE " + TABLE_NAME_INCOME + " ( " +

            COL_INCOME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

            COL_INCOME_DESC + " TEXT, " +

            COL_INCOME_AMOUNT + " TEXT );";


    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }



    @Override

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TABLE_CREATE_EXPENSES);
        db.execSQL(TABLE_CREATE_INCOME);

    }



    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_INCOME);

        onCreate(db);

    }



    public boolean save_expense(String desc, String amount) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content_values = new ContentValues();

        content_values.put(COL_EXPENSES_DESC, desc);

        content_values.put(COL_EXPENSES_AMOUNT, amount);

        long result = db.insert(TABLE_NAME_EXPENSES, null, content_values);

        return result != -1;

    }

    public boolean save_income(String desc, String amount) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content_values = new ContentValues();

        content_values.put(COL_INCOME_DESC, desc);

        content_values.put(COL_INCOME_AMOUNT, amount);

        long result = db.insert(TABLE_NAME_INCOME, null, content_values);

        return result != -1;

    }



    public Cursor list_expenses() {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor expense = db.rawQuery("SELECT "+COL_EXPENSES_ID+" as '_id', "+COL_EXPENSES_DESC+", "
                +COL_EXPENSES_AMOUNT+" FROM " + TABLE_NAME_EXPENSES, null);
        return expense;


    }

    public Cursor list_income() {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor income = db.rawQuery("SELECT "+COL_INCOME_ID+" as '_id', "+COL_INCOME_DESC+", "
                +COL_INCOME_AMOUNT+" FROM " + TABLE_NAME_INCOME, null);

        return income;

    }



    public boolean update_expense(String id, String desc, String amount) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content_values = new ContentValues();

        content_values.put(COL_EXPENSES_ID, id);

        content_values.put(COL_EXPENSES_DESC, desc);

        content_values.put(COL_EXPENSES_AMOUNT, amount);

        db.update(TABLE_NAME_EXPENSES, content_values, "ID = ? ", new String[]{id});

        return true;

    }

    public boolean update_income(String id, String desc, String amount) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content_values = new ContentValues();

        content_values.put(COL_INCOME_ID, id);

        content_values.put(COL_INCOME_DESC, desc);

        content_values.put(COL_INCOME_AMOUNT, amount);

        db.update(TABLE_NAME_INCOME, content_values, "ID = ? ", new String[]{id});

        return true;

    }



    public Integer delete_expense(String id) {

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_NAME_EXPENSES, "ID = ?", new String[] {id});

    }

    public Integer delete_Income(String id) {

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_NAME_INCOME, "ID = ?", new String[] {id});

    }

}
