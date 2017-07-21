package pl.gbielanski.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_QUANTITY;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_NAME;
import static pl.gbielanski.inventoryapp.data.InventoryItemContract.InventoryItemEntry.COLUMN_ITEM_PRICE;

class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryHolder>{
    private Cursor mCursor;
    private OnButtonSalesListener mSalesButtonListener;
    private OnInventoryItemListener mInventoryItemListener;

    public InventoryAdapter(Context context, OnButtonSalesListener salesListener, OnInventoryItemListener itemListener) {
        mSalesButtonListener = salesListener;
        mInventoryItemListener = itemListener;

    }

    public interface OnButtonSalesListener{
        void salesButtonOnClick(int position);
    }

    public interface OnInventoryItemListener{
        void inventoryItemOnClick(int position);
    }

    public void setData(Cursor c){
        mCursor = c;
    }

    public Cursor getData(){
        return mCursor;
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

    public class InventoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvItemName;
        TextView tvItemQuantity;
        TextView tvItemPrice;
        ImageView buttonSale;

        public InventoryHolder(View itemView) {
            super(itemView);
            tvItemName = (TextView)itemView.findViewById(R.id.name);
            tvItemQuantity = (TextView)itemView.findViewById(R.id.quantity);
            tvItemPrice = (TextView)itemView.findViewById(R.id.price);
            buttonSale = (ImageView)itemView.findViewById(R.id.sale);
            buttonSale.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.sale)
                mSalesButtonListener.salesButtonOnClick(getAdapterPosition());
            else
                mInventoryItemListener.inventoryItemOnClick(getAdapterPosition());
        }
    }
}
