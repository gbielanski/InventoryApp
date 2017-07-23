package pl.gbielanski.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

public class InventoryItemContract {
    private InventoryItemContract() {

    }

    public static final String CONTENT_AUTHORITY = "pl.gbielanski.inventoryapp";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_INVENTORY_ITEMS = "items";

    public static final class InventoryItemEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY_ITEMS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY_ITEMS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY_ITEMS;

        public final static String TABLE_NAME = "inventory_items";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_ITEM_NAME = "name";
        public final static String COLUMN_ITEM_QUANTITY = "quantity";
        public final static String COLUMN_ITEM_PRICE = "price";
        public final static String COLUMN_ITEM_PICTURE = "picture";
        public final static String COLUMN_ITEM_SUPPLIER = "supplier";

        public static Uri getContentUriForId(int id) {
            return Uri.withAppendedPath(CONTENT_URI, String.valueOf(id));
        }
    }
}
