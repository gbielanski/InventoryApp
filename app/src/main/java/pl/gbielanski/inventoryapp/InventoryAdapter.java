package pl.gbielanski.inventoryapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryHolder>{
    public InventoryAdapter(Context context) {
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

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class InventoryHolder extends RecyclerView.ViewHolder {
        public InventoryHolder(View itemView) {
            super(itemView);
        }
    }
}
