package com.example.android.inventoryappstage1;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventoryappstage1.data.InventoryContract.InventoryEntry;

public class InventoryCursorAdapter extends CursorAdapter {

    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView titleAuthorTextView = (TextView) view.findViewById(R.id.title_author);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView supplierTextView = (TextView) view.findViewById(R.id.supplier_name);
        TextView supplierPhoneTextView = (TextView) view.findViewById(R.id.supplier_phone);

        int titleColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_TITLE);
        int authorColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_AUTHOR);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_PRICE);
        int supplierColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_SUPPLIER_NAME);
        int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryEntry
                .COLUMN_BOOK_SUPPLIER_PHONE);

        String title_author = cursor.getString(titleColumnIndex) + " by " + cursor.getString(authorColumnIndex);
        String quantity = context.getResources().getString(R.string.item_quantity, cursor.getString(quantityColumnIndex));
        String price = context.getResources().getString(R.string.item_price, cursor.getString(priceColumnIndex));
        String supplier = context.getResources().getString(R.string.item_supplier, cursor.getString(supplierColumnIndex));
        String supplierPhone = context.getResources().getString(R.string.item_supplier_phone, cursor.getString(supplierPhoneColumnIndex));

        titleAuthorTextView.setText(title_author);
        quantityTextView.setText(quantity);
        priceTextView.setText(price);
        supplierTextView.setText(supplier);
        supplierPhoneTextView.setText(supplierPhone);
    }
}