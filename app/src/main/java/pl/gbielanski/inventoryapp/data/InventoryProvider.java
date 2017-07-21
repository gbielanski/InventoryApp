package pl.gbielanski.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry;

import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_QUANTITY;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_NAME;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_PRICE;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.TABLE_NAME;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.PATH_INVENTORY_ITEMS;

public class InventoryProvider extends ContentProvider {
    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();

    private static final int CODE_ITEMS = 100;
    private static final int CODE_ITEM_ID = 101;

    public static final UriMatcher sUriMatcher = buildUriMatcher();
    private InventoryDBHelper mDbHelper;


    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(InventoryItemContract.CONTENT_AUTHORITY, PATH_INVENTORY_ITEMS, CODE_ITEMS);
        uriMatcher.addURI(InventoryItemContract.CONTENT_AUTHORITY, PATH_INVENTORY_ITEMS + "/#", CODE_ITEM_ID);
        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        mDbHelper = new InventoryDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case CODE_ITEMS:
                cursor = database.query(TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case CODE_ITEM_ID:
                selection = InventoryItemEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CODE_ITEMS:
                return insertItem(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertItem(Uri uri, ContentValues contentValues) {
        String name = contentValues.getAsString(COLUMN_ITEM_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Item requires a name");
        }

        Integer count = contentValues.getAsInteger(COLUMN_ITEM_QUANTITY);
        if (count != null && count < 0) {
            throw new IllegalArgumentException("Item requires valid count");
        }

        Integer price = contentValues.getAsInteger(COLUMN_ITEM_PRICE);
        if (price != null && price < 0) {
            throw new IllegalArgumentException("Item requires valid price");
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(TABLE_NAME, null, contentValues);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CODE_ITEMS:
                rowsDeleted = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case CODE_ITEM_ID:
                selection = InventoryItemEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Update is not supported operation");
    }
}
