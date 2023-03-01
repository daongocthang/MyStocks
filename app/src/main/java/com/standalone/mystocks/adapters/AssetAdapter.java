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
import com.standalone.mystocks.constant.Config;
import com.standalone.mystocks.fragments.TradeDialogFragment;
import com.standalone.mystocks.handlers.AssetTableHandler;
import com.standalone.mystocks.models.Stock;

import java.util.List;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.ViewHolder> {

    private List<Stock> itemList;
    private final MainActivity activity;
    private final AssetTableHandler db;

    public AssetAdapter(MainActivity activity, AssetTableHandler db) {
        this.activity = activity;
        this.db = db;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_asset, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Stock s = itemList.get(position);
        double stopLoss = s.getPrice() * (1 - Config.STOP_LOSS_RATE);

        holder.tvSymbol.setText(s.getSymbol());
        holder.tvPrice.setText(String.valueOf(s.getPrice()));
        holder.tvShares.setText(String.valueOf(s.getShares()));
        holder.tvStopLoss.setText(String.valueOf(stopLoss));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public Context getContext() {
        return activity;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setItemList(List<Stock> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public void removeItem(int pos) {
        Stock s = itemList.get(pos);
        db.remove(s.getId());
        itemList.remove(pos);
        notifyItemRemoved(pos);
    }

    public void updateItem(int pos) {
        Stock s = itemList.get(pos);
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
        TextView tvStopLoss;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSymbol = itemView.findViewById(R.id.elAssetSymbol);
            tvPrice = itemView.findViewById(R.id.elAssetPrice);
            tvShares = itemView.findViewById(R.id.elAssetShares);
            tvStopLoss = itemView.findViewById(R.id.elAssetStopLoss);

        }
    }
}
