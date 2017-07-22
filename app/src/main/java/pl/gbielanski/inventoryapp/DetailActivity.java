package pl.gbielanski.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_NAME;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_PICTURE;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_PRICE;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_QUANTITY;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.CONTENT_URI;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry._ID;

public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 123;
    Uri mCurrentUri;
    private EditText mEditTextName;
    private EditText mEditTextPrice;
    private EditText mEditTextQuantity;
    private View.OnClickListener mSaveButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            insertInventoryItem();
        }
    };

    private View.OnClickListener mOrderButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            orderFromSupplier();
        }
    };

    private View.OnClickListener mIncreaseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String quantityString = mEditTextQuantity.getText().toString();
            Integer quantity = 0;
            try {
                if(!TextUtils.isEmpty(quantityString))
                    quantity = Integer.parseInt(quantityString);
            }catch (NumberFormatException e){
                Toast.makeText(DetailActivity.this, R.string.quantity_must_be_a_number, Toast.LENGTH_LONG).show();
                return;
            }

            quantity++;
            mEditTextQuantity.setText(quantity.toString());

            if(mCurrentUri != null)
                updateItemDetails(quantity);
        }
    };

    private View.OnClickListener mDecreaseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String quantityString = mEditTextQuantity.getText().toString();
            Integer quantity = 0;
            try {
                if(!TextUtils.isEmpty(quantityString))
                    quantity = Integer.parseInt(quantityString);
            }catch (NumberFormatException e){
                Toast.makeText(DetailActivity.this, R.string.quantity_must_be_a_number, Toast.LENGTH_LONG).show();
                return;
            }

            if(quantity > 0)
                quantity--;
            mEditTextQuantity.setText(quantity.toString());

            if(mCurrentUri != null)
                updateItemDetails(quantity);
        }
    };

    private void updateItemDetails(Integer quantity) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ITEM_QUANTITY, quantity);
        getContentResolver().update(mCurrentUri, cv, null, null);
    }

    private Cursor mCursor;

    private void orderFromSupplier() {
        Toast.makeText(DetailActivity.this, R.string.item_ordered, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mEditTextName = (EditText) findViewById(R.id.detail_name);
        mEditTextPrice = (EditText) findViewById(R.id.detail_price);
        mEditTextQuantity = (EditText) findViewById(R.id.detail_quantity);
        Button mSaveButton = (Button) findViewById(R.id.save_button);
        Button mOrderButton = (Button) findViewById(R.id.order_button);
        ImageView mIncreaseButton = (ImageView) findViewById(R.id.increase);
        ImageView mDecreaseButton = (ImageView) findViewById(R.id.decrease);

        mIncreaseButton.setOnClickListener(mIncreaseListener);
        mDecreaseButton.setOnClickListener(mDecreaseListener);

        Intent intent = getIntent();
        Uri uri = intent.getData();

        if (uri == null) {
            setTitle(getString(R.string.title_add));
            mEditTextName.setEnabled(true);
            mEditTextPrice.setEnabled(true);
            mEditTextQuantity.setEnabled(true);
            mSaveButton.setVisibility(View.VISIBLE);
            mSaveButton.setOnClickListener(mSaveButtonListener);
            mOrderButton.setVisibility(View.GONE);
        } else {
            setTitle(getString(R.string.title_details));
            mCurrentUri = uri;

            mEditTextName.setEnabled(false);
            mEditTextPrice.setEnabled(false);
            mEditTextQuantity.setEnabled(false);
            mOrderButton.setVisibility(View.VISIBLE);
            mOrderButton.setOnClickListener(mOrderButtonListener);
            mSaveButton.setVisibility(View.GONE);

            getLoaderManager().initLoader(LOADER_ID, null, this);
        }
    }

    private void insertInventoryItem() {
        ContentValues cv = new ContentValues();

        String name = mEditTextName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, R.string.name_cannot_be_empty, Toast.LENGTH_LONG).show();
            return;
        }

        String quantity = mEditTextQuantity.getText().toString();
        if (TextUtils.isEmpty(quantity)) {
            Toast.makeText(this, R.string.quantity_cannot_be_empty, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.quantity_must_be_a_number, Toast.LENGTH_LONG).show();
            return;
        }

        String price = mEditTextPrice.getText().toString();
        if (TextUtils.isEmpty(price)) {
            Toast.makeText(this, R.string.price_cannot_be_empty, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Integer.parseInt(price);
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.price_must_be_a_number, Toast.LENGTH_LONG).show();
            return;
        }

        cv.put(COLUMN_ITEM_NAME, name);
        cv.put(COLUMN_ITEM_QUANTITY, quantity);
        cv.put(COLUMN_ITEM_PRICE, price);

        getContentResolver().insert(CONTENT_URI, cv);
        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                _ID,
                COLUMN_ITEM_NAME,
                COLUMN_ITEM_PRICE,
                COLUMN_ITEM_QUANTITY,
                COLUMN_ITEM_PICTURE
        };

        return new CursorLoader(this,
                mCurrentUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        mCursor = cursor;
        if (cursor.moveToFirst()) {
            int columnNameIndex = cursor.getColumnIndex(COLUMN_ITEM_NAME);
            int columnQuantityIndex = cursor.getColumnIndex(COLUMN_ITEM_QUANTITY);
            int columnPriceIndex = cursor.getColumnIndex(COLUMN_ITEM_PRICE);
            int columnPictureIndex = cursor.getColumnIndex(COLUMN_ITEM_PICTURE);

            String name = cursor.getString(columnNameIndex);
            Integer quantity = cursor.getInt(columnQuantityIndex);
            Integer price = cursor.getInt(columnPriceIndex);
            String picture = cursor.getString(columnPictureIndex);

            mEditTextName.setText(name);
            mEditTextQuantity.setText(quantity.toString());
            mEditTextPrice.setText(price.toString());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
