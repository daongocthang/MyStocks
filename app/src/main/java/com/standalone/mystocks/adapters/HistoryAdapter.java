package com.standalone.mystocks.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.standalone.mystocks.R;
import com.standalone.mystocks.activities.MainActivity;
import com.standalone.mystocks.models.Stock;
import com.standalone.mystocks.utils.Humanize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<Stock> itemList;
    private final MainActivity activity;

    public HistoryAdapter(MainActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryAdapter.ViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Stock s = itemList.get(position);

        holder.tvSymbol.setText(s.getSymbol());
        holder.tvPrice.setText(Humanize.doubleComma(s.getPrice()));
        holder.tvShares.setText(Humanize.intComma(s.getShares()));
        holder.tvDate.setText(s.getDate());
        Stock.OrderType orderType = s.getOrder();

        holder.tvOrder.setText(orderType.toString());
        int colorPrimary = activity.getResources().getColor(R.color.primary);
        int colorDanger = activity.getResources().getColor(R.color.danger_dark);
        if (orderType.equals(Stock.OrderType.BUY)) {
            holder.tvOrder.setTextColor(colorPrimary);
            holder.tvProfit.setText("");
        } else {
            holder.tvOrder.setTextColor(colorDanger);
            holder.tvProfit.setText(Humanize.doubleComma(s.getProfit()));
            if (s.getProfit() > 0) {
                holder.tvProfit.setTextColor(colorPrimary);
            } else {
                holder.tvProfit.setTextColor(colorDanger);
            }
        }
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
        Collections.sort(itemList);
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSymbol;
        TextView tvPrice;
        TextView tvShares;
        TextView tvOrder;
        TextView tvProfit;
        TextView tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSymbol = itemView.findViewById(R.id.elHistorySymbol);
            tvPrice = itemView.findViewById(R.id.elHistoryPrice);
            tvShares = itemView.findViewById(R.id.elHistoryShares);
            tvOrder = itemView.findViewById(R.id.elHistoryOrder);
            tvProfit = itemView.findViewById(R.id.elHistoryProfit);
            tvDate = itemView.findViewById(R.id.elHistoryDate);
        }
    }
}
