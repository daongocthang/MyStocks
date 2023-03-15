package com.standalone.mystocks.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class AdapterFilterable<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements Filterable {
    protected List<T> itemList;


    public View instantiateItemView(@LayoutRes int resId, @NotNull ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(resId, parent, false);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<T> filteredList;
                String keywords = charSequence.toString();
                if (TextUtils.isEmpty(keywords)) {
                    filteredList = itemList;
                } else {
                    filteredList = new ArrayList<>();
                    for (T t : itemList) {
                        if (applyFilterCriteria(charSequence, t)) {
                            filteredList.add(t);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                @SuppressWarnings("unchecked")
                List<T> filteredList = (List<T>) filterResults.values;
                setItemList(filteredList);
            }
        };
    }

    public abstract void setItemList(List<T> itemList);

    protected abstract boolean applyFilterCriteria(CharSequence constraint, T t);
}
