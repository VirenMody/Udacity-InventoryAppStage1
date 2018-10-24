package com.example.android.inventoryappstage1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.android.inventoryappstage1.data.InventoryDbHelper;
import com.example.android.inventoryappstage1.data.InventoryContract.InventoryEntry;

public class MainActivity extends AppCompatActivity {

    InventoryDbHelper mDbHelper;
    TextView displayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayView = (TextView) findViewById(R.id.text_view_inventory);
        mDbHelper = new InventoryDbHelper(this);
        insertData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {

        Cursor cursor = queryData();
        try {
            displayView.setText("The inventory table contains " +cursor.getCount()+ " books.\n\n");
            displayView.append(InventoryEntry._ID + " - " +
                    InventoryEntry.COLUMN_BOOK_TITLE + " - " +
                    InventoryEntry.COLUMN_BOOK_AUTHOR + " - " +
                    InventoryEntry.COLUMN_BOOK_PRICE + " - " +
                    InventoryEntry.COLUMN_BOOK_SUPPLIER_NAME + " - " +
                    InventoryEntry.COLUMN_BOOK_SUPPLIER_PHONE + "\n");

            int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_TITLE);
            int authorColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_AUTHOR);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_PRICE);
            int supplierNameColumnIndex = cursor.getColumnIndex(InventoryEntry
                    .COLUMN_BOOK_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryEntry
                    .COLUMN_BOOK_SUPPLIER_PHONE);

            while(cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentTitle = cursor.getString(titleColumnIndex);
                String currentAuthor = cursor.getString(authorColumnIndex);
                double currentPrice = cursor.getDouble(priceColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentSupplierPhone = cursor.getString(supplierPhoneColumnIndex);

                displayView.append("\n" + currentID + " - " +
                        currentTitle + " - " +
                        currentAuthor + " - " +
                        currentPrice + " - " +
                        currentSupplierName + " - " +
                        currentSupplierPhone);
            }
        } finally {
            cursor.close();
        }
    }

    private Cursor queryData() {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_BOOK_TITLE,
                InventoryEntry.COLUMN_BOOK_AUTHOR,
                InventoryEntry.COLUMN_BOOK_PRICE,
                InventoryEntry.COLUMN_BOOK_QUANTITY,
                InventoryEntry.COLUMN_BOOK_SUPPLIER_NAME,
                InventoryEntry.COLUMN_BOOK_SUPPLIER_PHONE };

        Cursor cursor = db.query(
                InventoryEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        return cursor;
    }

    private void insertData() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_BOOK_TITLE, "Outliers");
        values.put(InventoryEntry.COLUMN_BOOK_AUTHOR, "Malcolm Gladwell");
        values.put(InventoryEntry.COLUMN_BOOK_PRICE, 19.99);
        values.put(InventoryEntry.COLUMN_BOOK_QUANTITY, 3);
        values.put(InventoryEntry.COLUMN_BOOK_SUPPLIER_NAME, "Little, Brown and Company");
        values.put(InventoryEntry.COLUMN_BOOK_SUPPLIER_PHONE, "(312) 555-5555");
    }
}
