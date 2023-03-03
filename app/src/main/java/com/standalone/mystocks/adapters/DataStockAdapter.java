package com.standalone.mystocks.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.standalone.mystocks.R;
import com.standalone.mystocks.models.DataStock;

import java.util.ArrayList;
import java.util.List;

public class DataStockAdapter extends ArrayAdapter<String> {
    private final List<DataStock> itemList;
    private final Context context;
    private final int layoutResource;

    public DataStockAdapter(@NonNull Context context, int resource, @NonNull List<DataStock> itemList) {
        super(context, resource);
        List<String> suggestion = new ArrayList<>();
        for (DataStock item : itemList) {
            suggestion.add(item.getSymbol());
        }
        super.addAll(suggestion);
        this.itemList = itemList;
        this.context = context;
        this.layoutResource = resource;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String symbol = getItem(position);

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(layoutResource, parent, false);

        TextView tvSymbol = convertView.findViewById(R.id.acItemSymbol);
        TextView tvShortName = convertView.findViewById(R.id.acShortName);

        tvSymbol.setText(symbol);
        String shortName = "";
        for (DataStock item : itemList) {
            if (item.getSymbol().equals(symbol)) {
                shortName = item.getShortName();
                break;
            }
        }
        tvShortName.setText(shortName);

        return convertView;
    }
}
