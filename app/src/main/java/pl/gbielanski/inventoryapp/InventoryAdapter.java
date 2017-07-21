package pl.gbielanski.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryHolder>{
    Cursor mCursor;
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

    }

    @Override
    public int getItemCount() {
        if(mCursor == null)
            return 0;

        return mCursor.getCount();
    }

    public class InventoryHolder extends RecyclerView.ViewHolder {
        public InventoryHolder(View itemView) {
            super(itemView);
        }
    }
}
