package com.standalone.mystocks.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.standalone.mystocks.R;
import com.standalone.mystocks.activities.MainActivity;
import com.standalone.mystocks.fragments.TradeDialogFragment;
import com.standalone.mystocks.handlers.AssetHandler;
import com.standalone.mystocks.models.Stock;

import java.util.List;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.ViewHolder> {

    private List<Stock> stocks;
    private final MainActivity activity;
    private final AssetHandler db;

    public AssetAdapter(AssetHandler db, MainActivity activity) {
        this.activity = activity;
        this.db = db;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.asset_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db.openDb();
        final Stock item = stocks.get(position);

        holder.tvSymbol.setText(item.getSymbol());
        holder.tvPrice.setText(String.valueOf(item.getPrice()));
        holder.tvShares.setText(String.valueOf(item.getShares()));
        holder.tvAmount.setText(String.valueOf(item.getAmount()));
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }


    public Context getContext() {
        return activity;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAsset(List<Stock> stocks) {
        this.stocks = stocks;
        notifyDataSetChanged();
    }

    public void removeItem(int pos) {
        Stock s = stocks.get(pos);
        db.remove(s.getId());
        stocks.remove(pos);
        notifyItemRemoved(pos);
    }

    public void updateItem(int pos) {
        Stock s = stocks.get(pos);
        Bundle bundle = new Bundle();
        bundle.putSerializable("stock", s);
        // Show a dialog fragment
        TradeDialogFragment fragment = new TradeDialogFragment();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), TradeDialogFragment.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSymbol;
        TextView tvPrice;
        TextView tvShares;
        TextView tvAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSymbol = itemView.findViewById(R.id.assetItemSymbol);
            tvPrice = itemView.findViewById(R.id.assetItemPrice);
            tvShares = itemView.findViewById(R.id.assetItemShares);
            tvAmount = itemView.findViewById(R.id.assetItemAmount);

        }
    }
}
