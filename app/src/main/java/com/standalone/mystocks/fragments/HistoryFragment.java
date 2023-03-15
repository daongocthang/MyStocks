package com.standalone.mystocks.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.standalone.mystocks.R;
import com.standalone.mystocks.activities.MainActivity;
import com.standalone.mystocks.adapters.HistoryAdapter;
import com.standalone.mystocks.handlers.HistoryTableHandler;
import com.standalone.mystocks.handlers.dbase.DatabaseManager;
import com.standalone.mystocks.models.Stock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryFragment extends MonoFragment<Stock> {
    private final MainActivity activity;
    private HistoryTableHandler db;
    private HistoryAdapter adapter;

    public HistoryFragment(MainActivity activity) {
        super(R.layout.fragment_history);
        this.activity = activity;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new HistoryTableHandler(DatabaseManager.getDatabase(activity));

        RecyclerView historyRecyclerView = view.findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new HistoryAdapter(activity);
        historyRecyclerView.setAdapter(adapter);

        List<Stock> itemList = db.fetchAll();
        adapter.setItemList(itemList);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void update() {
        List<Stock> itemList = db.fetchAll();
        // Display default row from db
        adapter.setItemList(itemList);
    }

    @Override
    public void filter(CharSequence constraint) {
        adapter.getFilter().filter(constraint);
    }
}