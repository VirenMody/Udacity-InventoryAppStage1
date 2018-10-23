package com.example.android.inventoryappstage1;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.inventoryappstage1.data.InventoryDbHelper;
import com.example.android.inventoryappstage1.data.InventoryContract.InventoryEntry;

public class MainActivity extends AppCompatActivity {

    private InventoryDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new InventoryDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
//        SQLiteDatabase db = mDbHelper.getReadabaleDatabase();


    }

    private void insertData() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, "Outliers by Malcolm Gladwell");
        values.put(InventoryEntry.COLUMN_PRODUCT_PRICE, 19.99);
        values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, 3);
        values.put(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME, "Little, Brown and Company");
        values.put(InventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE, "(312) 555-5555");
    }
}
