package pl.gbielanski.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry;

import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_QUANTITY;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_NAME;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_PRICE;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final int LOADER_ID = 1234;
    private InventoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rcInventoryList = (RecyclerView)findViewById(R.id.rc_inventory_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcInventoryList.setLayoutManager(layoutManager);
        mAdapter = new InventoryAdapter(this);
        rcInventoryList.setAdapter(mAdapter);
        insertDummyData();
        getLoaderManager().initLoader(LOADER_ID, null, this);

    }

    private void insertDummyData() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ITEM_NAME, "Pencils");
        contentValues.put(COLUMN_ITEM_QUANTITY, 1);
        contentValues.put(COLUMN_ITEM_PRICE, 2);
        Uri uri = getContentResolver().insert(InventoryItemEntry.CONTENT_URI, contentValues);

        if(uri == null)
            Log.v(LOG_TAG, "Inserting dummy data error");
        else
            Log.v(LOG_TAG, "Data inserted " + uri.toString());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                InventoryItemEntry._ID,
                InventoryItemEntry.COLUMN_ITEM_NAME,
                InventoryItemEntry.COLUMN_ITEM_PRICE,
                InventoryItemEntry.COLUMN_ITEM_QUANTITY
        };

        return new CursorLoader(this,
                InventoryItemEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
