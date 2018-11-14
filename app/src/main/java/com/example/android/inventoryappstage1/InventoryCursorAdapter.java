package com.example.android.inventoryappstage1;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryappstage1.data.InventoryContract.InventoryEntry;

public class InventoryCursorAdapter extends CursorAdapter {

    private Button mSaleBtn;

    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView titleAuthorTextView = (TextView) view.findViewById(R.id.title_author);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);

        int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);
        int titleColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_TITLE);
        int authorColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_AUTHOR);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_PRICE);

        final long id = cursor.getInt(idColumnIndex);
        String strQuantity = cursor.getString(quantityColumnIndex);
        final int quantity = Integer.parseInt(strQuantity);

        String title_author_display = cursor.getString(titleColumnIndex) + " by " + cursor.getString(authorColumnIndex);
        String quantity_display = context.getResources().getString(R.string.item_quantity, strQuantity);
        String price_display = context.getResources().getString(R.string.item_price, cursor.getString(priceColumnIndex));

        titleAuthorTextView.setText(title_author_display);
        quantityTextView.setText(quantity_display);
        priceTextView.setText(price_display);

        mSaleBtn = (Button) view.findViewById(R.id.sale);
        mSaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues contentValues = new ContentValues();
                Uri currentUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);
                if(quantity == 0){
                    Toast.makeText(context, context.getResources().getString(R.string.editor_min_books),
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    contentValues.put(InventoryEntry.COLUMN_BOOK_QUANTITY, quantity-1);
                    context.getContentResolver().update(currentUri, contentValues, null, null);
                }
            }

        });
    }
}