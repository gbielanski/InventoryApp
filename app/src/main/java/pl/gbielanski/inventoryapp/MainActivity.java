package pl.gbielanski.inventoryapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry;

import static android.view.View.GONE;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_QUANTITY;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        InventoryAdapter.OnButtonSalesListener, InventoryAdapter.OnInventoryItemListener {

    private static final int LOADER_ID = 1234;
    private InventoryAdapter mAdapter;

    @OnClick(R.id.fab)
    public void startAddActivity(){
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        startActivity(intent);
    }

    @BindView(R.id.empty_list_textview) TextView mEmptyListTextView;
    @BindView(R.id.rc_inventory_list) RecyclerView mInventoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mInventoryList.setLayoutManager(layoutManager);
        mAdapter = new InventoryAdapter(this, this);
        mEmptyListTextView.setVisibility(View.VISIBLE);
        mInventoryList.setAdapter(mAdapter);
        mInventoryList.setVisibility(GONE);
        getLoaderManager().initLoader(LOADER_ID, null, this);
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor.getCount() > 0) {
            mEmptyListTextView.setVisibility(GONE);
            mInventoryList.setVisibility(View.VISIBLE);
            mAdapter.setData(cursor);
            mAdapter.notifyDataSetChanged();
        } else {
            mEmptyListTextView.setVisibility(View.VISIBLE);
            mInventoryList.setVisibility(GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void salesButtonOnClick(int position) {
        Cursor cursor = mAdapter.getData();
        cursor.moveToPosition(position);
        Integer quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_ITEM_QUANTITY));
        if (quantity == 0)
            Toast.makeText(this, R.string.cannot_sold, Toast.LENGTH_LONG).show();
        else {
            quantity--;
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_ITEM_QUANTITY, quantity);
            int id = cursor.getInt(cursor.getColumnIndex(InventoryItemEntry._ID));
            Uri uri = InventoryItemEntry.getContentUriForId(id);
            getContentResolver().update(uri, cv, null, null);
        }
    }

    @Override
    public void inventoryItemOnClick(int position) {
        Cursor cursor = mAdapter.getData();
        cursor.moveToPosition(position);
        int id = cursor.getInt(cursor.getColumnIndex(InventoryItemEntry._ID));
        Uri uri = InventoryItemEntry.getContentUriForId(id);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.setData(uri);
        startActivity(intent);
    }

}
