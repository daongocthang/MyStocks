package com.standalone.mystocks.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.standalone.mystocks.handlers.dbase.DatabaseManager;
import com.standalone.mystocks.handlers.HistoryTableHandler;
import com.standalone.mystocks.models.Stock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HistoryFragment extends MonoFragment implements Filterable {
    private final MainActivity activity;

    private HistoryTableHandler db;
    private HistoryAdapter adapter;

    private final Comparator<Stock> comparator;

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

        db = new HistoryTableHandler(DatabaseManager.getDatabase(activity));

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
    public void update() {
        List<Stock> itemList = db.fetchAll();
        Collections.sort(itemList, comparator);

        // Display default row from db
        adapter.setItemList(itemList);
    }

    @Override
    public void filter(CharSequence constraint) {
        getFilter().filter(constraint);
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Stock> filteredList;
                List<Stock> itemList = db.fetchAll();
                String keywords = constraint.toString();
                if (TextUtils.isEmpty(keywords)) {
                    filteredList = itemList;
                } else {
                    filteredList = new ArrayList<>();
                    for (Stock s : itemList) {
                        if (s.getSymbol().toLowerCase().contains(keywords.toLowerCase())) {
                            filteredList.add(s);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                @SuppressWarnings("unchecked")
                List<Stock> filteredList = (List<Stock>) results.values;
                adapter.setItemList(filteredList);
            }
        };
    }
}