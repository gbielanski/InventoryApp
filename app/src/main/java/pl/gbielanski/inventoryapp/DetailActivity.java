package pl.gbielanski.inventoryapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_NAME;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_PICTURE;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_PRICE;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_QUANTITY;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_SUPPLIER;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.CONTENT_URI;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry._ID;

public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CAMERA_REQUEST = 1;
    private static final int LOADER_ID = 123;
    private static final int REQUEST_CALL_PHONE = 222;
    private static final String IMAGE_KEY = "Image";
    private final String LOG_TAG = DetailActivity.class.getSimpleName();

    private Uri mCurrentUri;
    private EditText mEditTextName;
    private EditText mEditTextPrice;
    private EditText mEditTextQuantity;
    private EditText mEditTextSupplier;
    private ImageView mItemImage;

    private final View.OnClickListener mSaveButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            insertInventoryItem();
        }
    };

    private final View.OnClickListener mOrderButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            orderFromSupplier();
        }
    };

    private final View.OnClickListener mIncreaseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String quantityString = mEditTextQuantity.getText().toString();
            Integer quantity = 0;
            try {
                if (!TextUtils.isEmpty(quantityString))
                    quantity = Integer.parseInt(quantityString);
            } catch (NumberFormatException e) {
                Toast.makeText(DetailActivity.this, R.string.quantity_must_be_a_number, Toast.LENGTH_LONG).show();
                return;
            }

            quantity++;
            mEditTextQuantity.setText(quantity.toString());

            if (mCurrentUri != null)
                updateItemDetails(quantity);
        }
    };

    private final View.OnClickListener mDecreaseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String quantityString = mEditTextQuantity.getText().toString();
            Integer quantity = 0;
            try {
                if (!TextUtils.isEmpty(quantityString))
                    quantity = Integer.parseInt(quantityString);
            } catch (NumberFormatException e) {
                Toast.makeText(DetailActivity.this, R.string.quantity_must_be_a_number, Toast.LENGTH_LONG).show();
                return;
            }

            if (quantity > 0)
                quantity--;
            mEditTextQuantity.setText(quantity.toString());

            if (mCurrentUri != null)
                updateItemDetails(quantity);
        }
    };

    private final View.OnClickListener mImageOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    };

    private void updateItemDetails(Integer quantity) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ITEM_QUANTITY, quantity);
        getContentResolver().update(mCurrentUri, cv, null, null);
    }

    private void orderFromSupplier() {

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + mEditTextSupplier.getText().toString()));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {

                Toast.makeText(this, R.string.permission_required, Toast.LENGTH_LONG).show();
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        REQUEST_CALL_PHONE);
            }
            return;
        }
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL_PHONE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + mEditTextSupplier.getText().toString()));
                    try {
                        startActivity(intent);
                    } catch (SecurityException e) {
                        Log.v(LOG_TAG, "SecurityException");
                    }
                } else {
                    Toast.makeText(this, R.string.cannot_granted, Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mEditTextName = (EditText) findViewById(R.id.detail_name);
        mEditTextPrice = (EditText) findViewById(R.id.detail_price);
        mEditTextQuantity = (EditText) findViewById(R.id.detail_quantity);
        mEditTextSupplier = (EditText) findViewById(R.id.supplier_number);
        Button mSaveButton = (Button) findViewById(R.id.save_button);
        Button mOrderButton = (Button) findViewById(R.id.order_button);
        ImageView mIncreaseButton = (ImageView) findViewById(R.id.increase);
        ImageView mDecreaseButton = (ImageView) findViewById(R.id.decrease);

        mIncreaseButton.setOnClickListener(mIncreaseListener);
        mDecreaseButton.setOnClickListener(mDecreaseListener);

        mItemImage = (ImageView) findViewById(R.id.item_image);
        if (savedInstanceState != null) {
            Bitmap bitmap = savedInstanceState.getParcelable(IMAGE_KEY);
            mItemImage.setImageBitmap(bitmap);
        }

        Intent intent = getIntent();
        Uri uri = intent.getData();

        if (uri == null) {
            setTitle(getString(R.string.title_add));
            invalidateOptionsMenu();
            mEditTextName.setEnabled(true);
            mEditTextPrice.setEnabled(true);
            mEditTextQuantity.setEnabled(true);
            mEditTextSupplier.setEnabled(true);
            mSaveButton.setVisibility(View.VISIBLE);
            mSaveButton.setOnClickListener(mSaveButtonListener);
            mOrderButton.setVisibility(View.GONE);
            mItemImage.setOnClickListener(mImageOnClickListener);

        } else {
            setTitle(getString(R.string.title_details));

            mCurrentUri = uri;

            mEditTextName.setEnabled(false);
            mEditTextPrice.setEnabled(false);
            mEditTextQuantity.setEnabled(false);
            mEditTextSupplier.setEnabled(false);
            mOrderButton.setVisibility(View.VISIBLE);
            mOrderButton.setOnClickListener(mOrderButtonListener);
            mSaveButton.setVisibility(View.GONE);

            getLoaderManager().initLoader(LOADER_ID, null, this);
        }
    }

    private static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mItemImage.setImageBitmap(photo);
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
        String supplier = mEditTextSupplier.getText().toString();
        if (TextUtils.isEmpty(supplier)) {
            Toast.makeText(this, R.string.supplier_cannot_be_empty, Toast.LENGTH_LONG).show();
            return;
        }

        cv.put(COLUMN_ITEM_NAME, name);
        cv.put(COLUMN_ITEM_QUANTITY, quantity);
        cv.put(COLUMN_ITEM_PRICE, price);
        Bitmap bitmap = ((BitmapDrawable) mItemImage.getDrawable()).getBitmap();
        cv.put(COLUMN_ITEM_PICTURE, getBitmapAsByteArray(bitmap));
        cv.put(COLUMN_ITEM_SUPPLIER, supplier);
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
                COLUMN_ITEM_PICTURE,
                COLUMN_ITEM_SUPPLIER
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

        if (cursor.moveToFirst()) {
            int columnNameIndex = cursor.getColumnIndex(COLUMN_ITEM_NAME);
            int columnQuantityIndex = cursor.getColumnIndex(COLUMN_ITEM_QUANTITY);
            int columnPriceIndex = cursor.getColumnIndex(COLUMN_ITEM_PRICE);
            int columnPictureIndex = cursor.getColumnIndex(COLUMN_ITEM_PICTURE);
            int columnSupplierIndex = cursor.getColumnIndex(COLUMN_ITEM_SUPPLIER);

            String name = cursor.getString(columnNameIndex);
            Integer quantity = cursor.getInt(columnQuantityIndex);
            Integer price = cursor.getInt(columnPriceIndex);
            byte[] blob = cursor.getBlob(columnPictureIndex);
            String supplier = cursor.getString(columnSupplierIndex);

            ByteArrayInputStream imageStream = new ByteArrayInputStream(blob);
            Bitmap picture = BitmapFactory.decodeStream(imageStream);

            mEditTextName.setText(name);
            mEditTextQuantity.setText(quantity.toString());
            mEditTextPrice.setText(price.toString());
            mItemImage.setImageBitmap(picture);
            mEditTextSupplier.setText(supplier);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!hasItemChanged()) {
                    NavUtils.navigateUpFromSameTask(DetailActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(DetailActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (!hasItemChanged()) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private boolean hasItemChanged() {
        if (mCurrentUri != null)
            return false;

        String price = mEditTextPrice.getText().toString();
        String qunatity = mEditTextQuantity.getText().toString();
        String name = mEditTextName.getText().toString();
        String supplier = mEditTextSupplier.getText().toString();
        return !TextUtils.isEmpty(price) ||
                !TextUtils.isEmpty(qunatity) ||
                !TextUtils.isEmpty(name) ||
                !TextUtils.isEmpty(supplier);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteInventoryItem();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteInventoryItem() {
        if (mCurrentUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_item_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Bitmap bitmap = ((BitmapDrawable) mItemImage.getDrawable()).getBitmap();
        outState.putParcelable(IMAGE_KEY, bitmap);
        super.onSaveInstanceState(outState);
    }
}
