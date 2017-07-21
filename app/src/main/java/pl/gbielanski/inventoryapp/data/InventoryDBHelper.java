package pl.gbielanski.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry;

public class InventoryDBHelper extends SQLiteOpenHelper {
    public InventoryDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ITEMS_TABLE =  "CREATE TABLE " + InventoryItemEntry.TABLE_NAME + " ("
                + InventoryItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryItemEntry.COLUMN_ITEM_NAME + " TEXT NOT NULL, "
                + InventoryItemEntry.COLUMN_ITEM_COUNT + " INTEGER NOT NULL DEFAULT 0, "
                + InventoryItemEntry.COLUMN_ITEM_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + InventoryItemEntry.COLUMN_ITEM_PICTURE + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + InventoryItemEntry.TABLE_NAME);
        onCreate(db);
    }
}
