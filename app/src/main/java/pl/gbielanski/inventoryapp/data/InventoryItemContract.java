package pl.gbielanski.inventoryapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class InventoryItemContract {
    private InventoryItemContract(){

    }

    public static final String CONTENT_AUTHORITY = "pl.gbielanski.inventoryapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_INVENTORY_ITEMS = "items";

    public static final class InventoryItemEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY_ITEMS);

        public final static String TABLE_NAME = "inventory_items";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_ITEM_NAME = "name";
        public final static String COLUMN_ITEM_COUNT = "count";
        public final static String COLUMN_ITEM_PRICE = "price";
        public final static String COLUMN_ITEM_PICTURE = "picture";
    }
}
