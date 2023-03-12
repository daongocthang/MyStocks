package com.standalone.mystocks.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.standalone.mystocks.R;
import com.standalone.mystocks.activities.MainActivity;
import com.standalone.mystocks.adapters.AssetAdapter;
import com.standalone.mystocks.handlers.dbase.DatabaseManager;
import com.standalone.mystocks.handlers.AssetTableHandler;
import com.standalone.mystocks.models.Stock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssetFragment extends MonoFragment implements Filterable {
    private AssetTableHandler db;
    private AssetAdapter adapter;
    private final MainActivity activity;

    public AssetFragment(MainActivity activity) {
        super(R.layout.fragment_asset);
        this.activity = activity;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_asset, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new AssetTableHandler(DatabaseManager.getDatabase(activity));

        RecyclerView assetRecyclerView = view.findViewById(R.id.assetRecyclerView);
        assetRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new AssetAdapter(activity, db);
        assetRecyclerView.setAdapter(adapter);

//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(adapter));
//        itemTouchHelper.attachToRecyclerView(assetRecyclerView);

        List<Stock> stocks = db.fetchAll();
        Collections.reverse(stocks);
        adapter.setItemList(stocks);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void update() {
        List<Stock> stocks = db.fetchAll();
        Collections.reverse(stocks);

        // Display default row from db
        adapter.setItemList(stocks);
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
