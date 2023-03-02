package com.standalone.mystocks.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.standalone.mystocks.R;
import com.standalone.mystocks.activities.MainActivity;
import com.standalone.mystocks.adapters.HistoryAdapter;
import com.standalone.mystocks.constant.Config;
import com.standalone.mystocks.handlers.HistoryTableHandler;
import com.standalone.mystocks.handlers.generic.OpenDB;
import com.standalone.mystocks.models.Stock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class HistoryFragment extends MonoFragment {
    private final MainActivity activity;

    private HistoryTableHandler db;
    private HistoryAdapter adapter;

    private Comparator<Stock> comparator;

    public HistoryFragment(MainActivity activity) {
        super(R.layout.fragment_history);
        this.activity = activity;

        comparator = new Comparator<Stock>() {
            @Override
            public int compare(Stock s1, Stock s2) {
                return s2.getDate().compareTo(s1.getDate());
            }
        };
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OpenDB openDB = new OpenDB(activity, Config.DATABASE_NAME, Config.VERSION);
        db = new HistoryTableHandler(openDB);

        RecyclerView historyRecyclerView = view.findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new HistoryAdapter(activity);
        historyRecyclerView.setAdapter(adapter);

        List<Stock> itemList = db.fetchAll();
        Collections.sort(itemList, comparator);

        adapter.setItemList(itemList);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onUpdate() {
        List<Stock> itemList = db.fetchAll();
        Collections.sort(itemList, comparator);

        // Display default row from db
        adapter.setItemList(itemList);
        adapter.notifyDataSetChanged();
    }
}