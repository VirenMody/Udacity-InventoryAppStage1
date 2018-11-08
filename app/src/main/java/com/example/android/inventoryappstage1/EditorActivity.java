package com.example.android.inventoryappstage1;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.android.inventoryappstage1.data.InventoryDbHelper;
import com.example.android.inventoryappstage1.data.InventoryContract.InventoryEntry;

public class EditorActivity extends AppCompatActivity {

    InventoryDbHelper mDbHelper;

    private EditText mTitleEditText;
    private EditText mAuthorEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private EditText mSupplierNameEditText;
    private EditText mSupplierPhoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mDbHelper = new InventoryDbHelper(this);

        mTitleEditText = (EditText) findViewById(R.id.edit_book_title);
        mAuthorEditText = (EditText) findViewById(R.id.edit_author);
        mPriceEditText = (EditText) findViewById(R.id.edit_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_quantity);
        mSupplierNameEditText = (EditText) findViewById(R.id.edit_supplier_name);
        mSupplierPhoneEditText = (EditText) findViewById(R.id.edit_supplier_phone);
    }

    private void insertBook() {

        String title = mTitleEditText.getText().toString().trim();
        String author = mAuthorEditText.getText().toString().trim();
        double price = Double.parseDouble(mPriceEditText.getText().toString().trim());
        int quantity = Integer.parseInt(mQuantityEditText.getText().toString());
        String supplierName = mSupplierNameEditText.getText().toString().trim();
        String supplierPhone = mSupplierPhoneEditText.getText().toString().trim();

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_BOOK_TITLE, title);
        values.put(InventoryEntry.COLUMN_BOOK_AUTHOR, author);
        values.put(InventoryEntry.COLUMN_BOOK_PRICE, price);
        values.put(InventoryEntry.COLUMN_BOOK_QUANTITY, quantity);
        values.put(InventoryEntry.COLUMN_BOOK_SUPPLIER_NAME, supplierName);
        values.put(InventoryEntry.COLUMN_BOOK_SUPPLIER_PHONE, supplierPhone);

        db.insert(InventoryEntry.TABLE_NAME, null, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    //  Taken from Udacity: ud845-Pets and modified for my app
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                insertBook();
                finish();
                return true;
            case R.id.action_delete:
                //TODO
                return true;
            case android.R.id.home:
                // Navigate back to parent activity (MainActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
