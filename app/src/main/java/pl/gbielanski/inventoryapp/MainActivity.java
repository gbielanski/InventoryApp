package pl.gbielanski.inventoryapp;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import pl.gbielanski.inventoryapp.data.InventoryItemContract;
import pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry;
import pl.gbielanski.inventoryapp.data.InventoryProvider;

import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_COUNT;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_NAME;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_PRICE;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rcInventoryList = (RecyclerView)findViewById(R.id.rc_inventory_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcInventoryList.setLayoutManager(layoutManager);
        InventoryAdapter mAdapter = new InventoryAdapter(this);
        rcInventoryList.setAdapter(mAdapter);

        insertDummyData();

    }

    private void insertDummyData() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ITEM_NAME, "Pencils");
        contentValues.put(COLUMN_ITEM_COUNT, 1);
        contentValues.put(COLUMN_ITEM_PRICE, 2);
        Uri uri = getContentResolver().insert(InventoryItemEntry.CONTENT_URI, contentValues);

        if(uri == null)
            Log.v(LOG_TAG, "Inserting dummy data error");
        else
            Log.v(LOG_TAG, "Data inserted " + uri.toString());
    }
}
