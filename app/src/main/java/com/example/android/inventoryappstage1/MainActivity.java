package com.example.android.inventoryappstage1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryappstage1.data.InventoryContract.InventoryEntry;

public class MainActivity extends AppCompatActivity {

    TextView displayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayView = (TextView) findViewById(R.id.text_view_inventory);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Cursor queryData() {

        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_BOOK_TITLE,
                InventoryEntry.COLUMN_BOOK_AUTHOR,
                InventoryEntry.COLUMN_BOOK_PRICE,
                InventoryEntry.COLUMN_BOOK_QUANTITY,
                InventoryEntry.COLUMN_BOOK_SUPPLIER_NAME,
                InventoryEntry.COLUMN_BOOK_SUPPLIER_PHONE };

        Cursor cursor = getContentResolver().query(
                InventoryEntry.CONTENT_URI,
                projection,
                null,
                null);

        return cursor;
    }

    private void insertBook() {

        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_BOOK_TITLE, "Outliers");
        values.put(InventoryEntry.COLUMN_BOOK_AUTHOR, "Malcolm Gladwell");
        values.put(InventoryEntry.COLUMN_BOOK_PRICE, 19.99);
        values.put(InventoryEntry.COLUMN_BOOK_QUANTITY, 3);
        values.put(InventoryEntry.COLUMN_BOOK_SUPPLIER_NAME, "Little, Brown and Company");
        values.put(InventoryEntry.COLUMN_BOOK_SUPPLIER_PHONE, "(312) 555-5555");

        Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);
        if(null == newUri) {
            Toast.makeText(this, getString(R.string.editor_insert_book_failed),
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, getString(R.string.editor_insert_book_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertBook();
                displayDatabaseInfo();
                return true;
            case R.id.action_delete_all_entries:
                //TODO
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
