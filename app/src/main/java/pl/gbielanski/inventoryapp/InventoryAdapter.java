package pl.gbielanski.inventoryapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryHolder>{
    public InventoryAdapter(Context context) {
    }

    @Override
    public InventoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
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
