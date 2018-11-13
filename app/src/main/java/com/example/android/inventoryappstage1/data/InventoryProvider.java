package com.example.android.inventoryappstage1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.inventoryappstage1.data.InventoryContract.InventoryEntry;

public class InventoryProvider extends ContentProvider {

    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();

    private InventoryDbHelper mDbHelper;
    private static final int INVENTORY = 100;
    private static final int INVENTORY_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORY,
                INVENTORY);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract
                .PATH_INVENTORY_SINGLEROW, INVENTORY_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new InventoryDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor = null;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                cursor = database.query(InventoryEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case INVENTORY_ID:
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(InventoryEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    //  Taken from Udacity: ud845-Pets and modified for my app
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return InventoryEntry.CONTENT_LIST_TYPE;
            case INVENTORY_ID:
                return InventoryEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    //  Taken from Udacity: ud845-Pets and modified for my app
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return insertInventory(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertInventory(Uri uri, ContentValues contentValues) {
        String title = contentValues.getAsString(InventoryEntry.COLUMN_BOOK_TITLE);
        if(null == title) {
            throw new IllegalArgumentException("Book requires a title");
        }

        String author = contentValues.getAsString(InventoryEntry.COLUMN_BOOK_AUTHOR);
        if(null == author) {
            throw new IllegalArgumentException("Book requires an author");
        }

        Double price = contentValues.getAsDouble(InventoryEntry.COLUMN_BOOK_PRICE);
        if(null == price) {
            throw new IllegalArgumentException("Book requires a price");
        }

        Integer quantity = contentValues.getAsInteger(InventoryEntry.COLUMN_BOOK_QUANTITY);
        if(null == quantity || quantity < 1) {
            throw new IllegalArgumentException("Book requires a valid quantity");
        }

        String supplier = contentValues.getAsString(InventoryEntry.COLUMN_BOOK_SUPPLIER_NAME);
        if(null == supplier) {
            throw new IllegalArgumentException("Book requires a supplier name");
        }

        String supplier_phone = contentValues.getAsString(InventoryEntry
                .COLUMN_BOOK_SUPPLIER_PHONE);
        if(null == supplier_phone) {
            throw new IllegalArgumentException("Book requires a supplier phone number");
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(InventoryEntry.TABLE_NAME, null, contentValues);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    //  Taken from Udacity: ud845-Pets and modified for my app
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                rowsDeleted = database.delete(InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case INVENTORY_ID:
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if(rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    //  Taken from Udacity: ud845-Pets and modified for my app
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return updateInventory(uri, contentValues, selection, selectionArgs);
            case INVENTORY_ID:
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateInventory(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }    }

    private int updateInventory(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        if(contentValues.size() == 0) {
            return 0;
        }

        if(contentValues.containsKey(InventoryEntry.COLUMN_BOOK_TITLE)) {
            String title = contentValues.getAsString(InventoryEntry.COLUMN_BOOK_TITLE);
            if (null == title) {
                throw new IllegalArgumentException("Book requires a title");
            }
        }

        if (contentValues.containsKey(InventoryEntry.COLUMN_BOOK_AUTHOR)) {
            String author = contentValues.getAsString(InventoryEntry.COLUMN_BOOK_AUTHOR);
            if(null == author) {
                throw new IllegalArgumentException("Book requires an author");
            }
        }

        if (contentValues.containsKey(InventoryEntry.COLUMN_BOOK_PRICE)) {
            Double price = contentValues.getAsDouble(InventoryEntry.COLUMN_BOOK_PRICE);
            if(null == price) {
                throw new IllegalArgumentException("Book requires a price");
            }
        }


        if (contentValues.containsKey(InventoryEntry.COLUMN_BOOK_QUANTITY)) {
            Integer quantity = contentValues.getAsInteger(InventoryEntry.COLUMN_BOOK_QUANTITY);
            if(null == quantity || quantity < 0) {
                throw new IllegalArgumentException("Book requires a valid quantity");
            }
        }

        if (contentValues.containsKey(InventoryEntry.COLUMN_BOOK_SUPPLIER_NAME)) {
            String supplier = contentValues.getAsString(InventoryEntry.COLUMN_BOOK_SUPPLIER_NAME);
            if(null == supplier) {
                throw new IllegalArgumentException("Book requires a supplier name");
            }
        }

        if (contentValues.containsKey(InventoryEntry.COLUMN_BOOK_SUPPLIER_PHONE)) {
            String supplier_phone = contentValues.getAsString(InventoryEntry
                    .COLUMN_BOOK_SUPPLIER_PHONE);
            if(null == supplier_phone) {
                throw new IllegalArgumentException("Book requires a supplier phone number");
            }
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsUpdated =  db.update(InventoryEntry.TABLE_NAME, contentValues, selection,
                selectionArgs);

        if(rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
