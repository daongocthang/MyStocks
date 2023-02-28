package com.standalone.mystocks.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.standalone.mystocks.R;
import com.standalone.mystocks.adapters.AssetAdapter;
import com.standalone.mystocks.adapters.helpers.RecyclerItemTouchHelper;
import com.standalone.mystocks.constant.Config;
import com.standalone.mystocks.fragments.TradeDialogFragment;
import com.standalone.mystocks.handlers.AssetTableHandler;
import com.standalone.mystocks.handlers.generic.OpenDB;
import com.standalone.mystocks.interfaces.DialogCloseListener;
import com.standalone.mystocks.models.Stock;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {
    private AssetAdapter adapter;
    private AssetTableHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OpenDB openDB = new OpenDB(this, Config.DATABASE_NAME, Config.VERSION);
        db = new AssetTableHandler(openDB);
        openDB.init();

        RecyclerView assetRecyclerView = findViewById(R.id.assetRecyclerView);
        assetRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AssetAdapter(db, this);
        assetRecyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(assetRecyclerView);

        List<Stock> stocks = db.fetchAll();
        Collections.reverse(stocks);
        adapter.setAsset(stocks);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TradeDialogFragment().show(getSupportFragmentManager(), TradeDialogFragment.TAG);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        reloadAdapter();
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        reloadAdapter();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void reloadAdapter() {
        List<Stock> stocks = db.fetchAll();
        Collections.reverse(stocks);

        // Display default row from db
        adapter.setAsset(stocks);
        adapter.notifyDataSetChanged();
    }
}