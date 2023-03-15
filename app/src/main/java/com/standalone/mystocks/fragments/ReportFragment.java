package com.standalone.mystocks.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.standalone.mystocks.R;
import com.standalone.mystocks.activities.MainActivity;
import com.standalone.mystocks.handlers.dbase.DatabaseManager;
import com.standalone.mystocks.handlers.HistoryTableHandler;
import com.standalone.mystocks.models.Stock;
import com.standalone.mystocks.utils.Humanize;

import java.util.ArrayList;
import java.util.List;

public class ReportFragment extends MonoFragment<Stock> {
    private final MainActivity activity;
    private TextView tvTrading;
    private TextView tvWinRate;
    private TextView tvGain;
    private TextView tvLoss;
    private TextView tvGrossProfit;

    private HistoryTableHandler db;

    public ReportFragment(MainActivity activity) {
        super(R.layout.fragment_report);
        this.activity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new HistoryTableHandler(DatabaseManager.getDatabase(activity));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTrading = view.findViewById(R.id.tvReportTrading);
        tvWinRate = view.findViewById(R.id.tvReportWinRate);
        tvGain = view.findViewById(R.id.tvReportGain);
        tvLoss = view.findViewById(R.id.tvReportLoss);
        tvGrossProfit = view.findViewById(R.id.tvReportGrossProfit);

        update();
    }

    @Override
    public void update() {
        itemList = db.fetchAll();
        int win = 0;
        int count = 0;
        double gain = 0;
        double loss = 0;
        for (Stock s : itemList) {
            if (s.getOrder().equals(Stock.OrderType.BUY))
                continue;
            count++;
            double profit = s.getProfit();
            if (profit > 0) {
                gain += profit;
                win++;
            } else {
                loss += profit;
            }

            tvTrading.setText(String.valueOf(itemList.size()));
            tvWinRate.setText(Humanize.percent((double) win / count));
            tvGain.setText(Humanize.doubleComma(gain));
            tvLoss.setText(Humanize.doubleComma(-1 * loss));
            tvGrossProfit.setText(Humanize.doubleComma(gain + loss));
        }
    }

    @Override
    public void filter(CharSequence constraint) {
    }

}
