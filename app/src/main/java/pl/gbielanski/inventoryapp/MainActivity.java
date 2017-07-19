package pl.gbielanski.inventoryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rcInventoryList = (RecyclerView)findViewById(R.id.rc_inventory_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcInventoryList.setLayoutManager(layoutManager);
        InventoryAdapter mAdapter = new InventoryAdapter(this);
        rcInventoryList.setAdapter(mAdapter);

    }
}
