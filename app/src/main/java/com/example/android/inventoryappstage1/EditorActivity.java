package com.example.android.inventoryappstage1;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventoryappstage1.data.InventoryContract.InventoryEntry;

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int EXISTING_BOOK_LOADER = 0;

    private Uri mCurrentBookUri;
    private EditText mTitleEditText;
    private EditText mAuthorEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private EditText mSupplierNameEditText;
    private EditText mSupplierPhoneEditText;
    private Button mIncrementQuantityBtn;
    private Button mDecrementQuantityBtn;

    private boolean mBookHasChanged = false;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mBookHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        Log.d(LOG_TAG, intent.toString());
        mCurrentBookUri = intent.getData();
        Log.d(LOG_TAG, mCurrentBookUri + "");

        if(null == mCurrentBookUri) {
            setTitle(getString(R.string.editor_activity_title_new_book));
            invalidateOptionsMenu();
        }
        else {
            setTitle(getString(R.string.editor_activity_title_edit_book));
            getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        }

        mTitleEditText = (EditText) findViewById(R.id.edit_book_title);
        mAuthorEditText = (EditText) findViewById(R.id.edit_author);
        mPriceEditText = (EditText) findViewById(R.id.edit_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_quantity);
        mSupplierNameEditText = (EditText) findViewById(R.id.edit_supplier_name);
        mSupplierPhoneEditText = (EditText) findViewById(R.id.edit_supplier_phone);
        mIncrementQuantityBtn = (Button) findViewById(R.id.quantity_increment);
        mIncrementQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strQuantity = mQuantityEditText.getText().toString().trim();
                if(!TextUtils.isEmpty(strQuantity)) {
                    int quantity = Integer.parseInt(strQuantity);
                    if(quantity < 100) {
                        Log.d(LOG_TAG, "Before increment, quantity = " + strQuantity);
                        strQuantity = String.valueOf(++quantity);
                        Log.d(LOG_TAG, "After increment, quantity = " + strQuantity);
                    }
                    else {
                        mQuantityEditText.setText(getString(R.string.editor_max_quantity));
                        Toast.makeText(EditorActivity.this, getString(R.string.editor_max_books),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    strQuantity = "1";
                }
                mQuantityEditText.setText(strQuantity);
            }
        });
        mDecrementQuantityBtn = (Button) findViewById(R.id.quantity_decrement);
        mDecrementQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strQuantity = mQuantityEditText.getText().toString().trim();
                if(!TextUtils.isEmpty(strQuantity)) {
                    int quantity = Integer.parseInt(strQuantity);
                    if(quantity > 0) {
                        Log.d(LOG_TAG, "Before decrement, quantity = " + strQuantity);
                        strQuantity = String.valueOf(--quantity);
                        Log.d(LOG_TAG, "After decrement, quantity = " + strQuantity);
                    }
                    else {
                        mQuantityEditText.setText(getString(R.string.editor_min_quantity));
                        Toast.makeText(EditorActivity.this, getString(R.string.editor_min_books),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    strQuantity = "0";
                }
                mQuantityEditText.setText(strQuantity);
            }
        });

        mTitleEditText.setOnTouchListener(mTouchListener);
        mAuthorEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mSupplierNameEditText.setOnTouchListener(mTouchListener);
        mSupplierPhoneEditText.setOnTouchListener(mTouchListener);
    }

    private void saveBook() {

        final String REQUIRED_FIELD = getString(R.string.editor_required_field);

        String title = mTitleEditText.getText().toString().trim();
        String author = mAuthorEditText.getText().toString().trim();
        String strPrice = mPriceEditText.getText().toString().trim();
        String strQuantity = mQuantityEditText.getText().toString().trim();
        String supplierName = mSupplierNameEditText.getText().toString().trim();
        String supplierPhone = mSupplierPhoneEditText.getText().toString().trim();

        if(null == mCurrentBookUri &&
                TextUtils.isEmpty(title) &&
                TextUtils.isEmpty(author) &&
                TextUtils.isEmpty(strPrice) &&
                TextUtils.isEmpty(strQuantity) &&
                TextUtils.isEmpty(supplierName) &&
                TextUtils.isEmpty(supplierPhone)) {

            Toast.makeText(this, getString(R.string.editor_all_fields_required),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if(!TextUtils.isEmpty(strPrice)) {
            if(Double.parseDouble(strPrice) <= 0) {
                mPriceEditText.setError(getString(R.string.editor_min_price_warning));
                mPriceEditText.setText(getString(R.string.editor_min_price));
                return;
            }
        }
        else {
            mPriceEditText.setError(REQUIRED_FIELD);
            return;
        }

        if(!TextUtils.isEmpty(strQuantity)) {
            int quantity = Integer.parseInt(strQuantity);
            if(quantity > 100) {
                mQuantityEditText.setError(getString(R.string.editor_quantity_range_warning));
                mQuantityEditText.setText(getString(R.string.editor_max_quantity));
                return;
            }
            if(quantity < 0) {
                mQuantityEditText.setError(getString(R.string.editor_quantity_range_warning));
                mQuantityEditText.setText(getString(R.string.editor_min_quantity));
                return;
            }
        }
        else {
            mQuantityEditText.setError(REQUIRED_FIELD);
            return;
        }

        if (TextUtils.isEmpty(title)) {
            mTitleEditText.setError(REQUIRED_FIELD);
            return;
        }

        if (TextUtils.isEmpty(author)) {
            mAuthorEditText.setError(REQUIRED_FIELD);
            return;
        }

        if (TextUtils.isEmpty(supplierName)) {
            mSupplierNameEditText.setError(REQUIRED_FIELD);
            return;
        }

        if (TextUtils.isEmpty(supplierPhone)) {
            mSupplierPhoneEditText.setError(REQUIRED_FIELD);
            return;
        }

        double price = Double.parseDouble(strPrice);
        int quantity = Integer.parseInt(strQuantity);

        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_BOOK_TITLE, title);
        values.put(InventoryEntry.COLUMN_BOOK_AUTHOR, author);
        values.put(InventoryEntry.COLUMN_BOOK_PRICE, price);
        values.put(InventoryEntry.COLUMN_BOOK_QUANTITY, quantity);
        values.put(InventoryEntry.COLUMN_BOOK_SUPPLIER_NAME, supplierName);
        values.put(InventoryEntry.COLUMN_BOOK_SUPPLIER_PHONE, supplierPhone);

        if(null == mCurrentBookUri) {
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
        else {
            int rowsAffected = getContentResolver().update(mCurrentBookUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_book_failed), Toast
                        .LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, getString(R.string.editor_update_book_successful), Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(null == mCurrentBookUri) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }

        return true;
    }

    //  Taken from Udacity: ud845-Pets and modified for my app
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveBook();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if(!mBookHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    }
                };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_BOOK_TITLE,
                InventoryEntry.COLUMN_BOOK_AUTHOR,
                InventoryEntry.COLUMN_BOOK_PRICE,
                InventoryEntry.COLUMN_BOOK_QUANTITY,
                InventoryEntry.COLUMN_BOOK_SUPPLIER_NAME,
                InventoryEntry.COLUMN_BOOK_SUPPLIER_PHONE };

        return new CursorLoader(this,
                mCurrentBookUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(LOG_TAG, "In onLoadFinished - cursor = " + cursor + " " + "  cursor count = " +
                cursor.getCount());
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int titleColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_TITLE);
            int authorColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_AUTHOR);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_PRICE);
            int supplierColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryEntry
                    .COLUMN_BOOK_SUPPLIER_PHONE);

            String title = cursor.getString(titleColumnIndex);
            String author = cursor.getString(authorColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            double price = cursor.getDouble(priceColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
            String supplierPhone = cursor.getString(supplierPhoneColumnIndex);

            Log.d(LOG_TAG, "In onLoadFinished - " + title + author + quantity + price);
            mTitleEditText.setText(title);
            mAuthorEditText.setText(author);
            mQuantityEditText.setText(Integer.toString(quantity));
            mPriceEditText.setText(Double.toString(price));
            mSupplierNameEditText.setText(supplier);
            mSupplierPhoneEditText.setText(supplierPhone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTitleEditText.setText("");
        mAuthorEditText.setText("");
        mQuantityEditText.setText("");
        mPriceEditText.setText("");
        mSupplierNameEditText.setText("");
        mSupplierPhoneEditText.setText("");
    }

    //  Taken from Udacity: ud845-Pets and modified for my app
    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.continue_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //  Taken from Udacity: ud845-Pets and modified for my app
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void deleteBook() {
        if(mCurrentBookUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentBookUri, null, null);

            if(rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_book_failed), Toast
                        .LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, getString(R.string.editor_delete_book_success), Toast
                        .LENGTH_SHORT).show();
            }
        }

        finish();
    }

    @Override
    public void onBackPressed() {
        if(!mBookHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }
}
