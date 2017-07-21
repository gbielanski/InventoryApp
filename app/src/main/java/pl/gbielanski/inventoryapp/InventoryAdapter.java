package pl.gbielanski.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_QUANTITY;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_NAME;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_PRICE;

class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryHolder>{
    private Cursor mCursor;
    public InventoryAdapter(Context context) {
    }

    public void setData(Cursor c){
        mCursor = c;
    }

    @Override
    public InventoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int inventoryItemResourceId = R.layout.inventory_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(inventoryItemResourceId, parent, false);
        return new InventoryHolder(view);
    }

    @Override
    public void onBindViewHolder(InventoryHolder holder, int position) {
        mCursor.moveToPosition(position);
        String itemName = mCursor.getString(mCursor.getColumnIndex(COLUMN_ITEM_NAME));
        holder.tvItemName.setText(itemName);

        Integer itemPrice = mCursor.getInt(mCursor.getColumnIndex(COLUMN_ITEM_PRICE));
        holder.tvItemPrice.setText(itemPrice.toString());

        Integer itemQuantity = mCursor.getInt(mCursor.getColumnIndex(COLUMN_ITEM_QUANTITY));
        holder.tvItemQuantity.setText(itemQuantity.toString());
    }

    @Override
    public int getItemCount() {
        if(mCursor == null)
            return 0;

        return mCursor.getCount();
    }

    public class InventoryHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        TextView tvItemQuantity;
        TextView tvItemPrice;

        public InventoryHolder(View itemView) {
            super(itemView);
            tvItemName = (TextView)itemView.findViewById(R.id.name);
            tvItemQuantity = (TextView)itemView.findViewById(R.id.quantity);
            tvItemPrice = (TextView)itemView.findViewById(R.id.price);

        }
    }
}
