package com.example.android.inventoryappstage1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.inventoryappstage1.data.InventoryContract.InventoryEntry;


public class InventoryDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = InventoryDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_INVENTORY_TABLE =
                "CREATE TABLE " + InventoryEntry.TABLE_NAME
                +  " ("
                + InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryEntry.COLUMN_BOOK_TITLE + " TEXT NOT NULL, "
                + InventoryEntry.COLUMN_BOOK_AUTHOR + " TEXT NOT NULL, "
                + InventoryEntry.COLUMN_BOOK_PRICE + " REAL NOT NULL, "
                + InventoryEntry.COLUMN_BOOK_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + InventoryEntry.COLUMN_BOOK_SUPPLIER_NAME + " TEXT NOT NULL, "
                + InventoryEntry.COLUMN_BOOK_SUPPLIER_PHONE + " TEXT NOT NULL"
                + ");";

        Log.d(LOG_TAG, SQL_CREATE_INVENTORY_TABLE);
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

