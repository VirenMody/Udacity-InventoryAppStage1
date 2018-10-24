package com.example.android.inventoryappstage1.data;

import android.provider.BaseColumns;

public final class InventoryContract {

    private InventoryContract() {}

    /**
     * Each database table needs its own inner class - this one is for store inventory
     */
    public static final class InventoryEntry implements BaseColumns {

        public final static String TABLE_NAME = "inventory";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_BOOK_TITLE = "title";
        public final static String COLUMN_BOOK_AUTHOR = "author";
        public final static String COLUMN_BOOK_PRICE = "price";
        public final static String COLUMN_BOOK_QUANTITY = "quantity";
        public final static String COLUMN_BOOK_SUPPLIER_NAME = "supplier_name";
        public final static String COLUMN_BOOK_SUPPLIER_PHONE = "supplier_phone";
    }
}
