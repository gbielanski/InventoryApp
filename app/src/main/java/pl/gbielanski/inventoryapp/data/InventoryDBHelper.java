package pl.gbielanski.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry;

class InventoryDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 2;

    public InventoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + InventoryItemEntry.TABLE_NAME + " ("
                + InventoryItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryItemEntry.COLUMN_ITEM_NAME + " TEXT NOT NULL, "
                + InventoryItemEntry.COLUMN_ITEM_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + InventoryItemEntry.COLUMN_ITEM_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + InventoryItemEntry.COLUMN_ITEM_SUPPLIER + " TEXT NOT NULL, "
                + InventoryItemEntry.COLUMN_ITEM_PICTURE + " BLOB NOT NULL);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + InventoryItemEntry.TABLE_NAME);
        onCreate(db);
    }
}
