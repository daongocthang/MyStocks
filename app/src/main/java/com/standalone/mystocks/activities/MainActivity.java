package com.standalone.mystocks.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.standalone.mystocks.R;
import com.standalone.mystocks.adapters.ViewPagerAdapter;
import com.standalone.mystocks.handlers.dbase.DatabaseManager;
import com.standalone.mystocks.fragments.TradeDialogFragment;
import com.standalone.mystocks.handlers.CompanyTableHandler;
import com.standalone.mystocks.interfaces.AdapterUpdateListener;
import com.standalone.mystocks.interfaces.DialogCloseListener;
import com.standalone.mystocks.models.DataStock;
import com.standalone.mystocks.utils.ApiStock;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    public ViewPagerAdapter adapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_main);

        loadCompanyTable();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TradeDialogFragment().show(getSupportFragmentManager(), TradeDialogFragment.TAG);
            }
        });

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                TabLayout.Tab tab = tabLayout.getTabAt(position);
                assert tab != null;
                tab.select();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        viewPager2.setCurrentItem(0);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment f : fragmentList) {
            if (f instanceof AdapterUpdateListener) {
                ((AdapterUpdateListener) f).onUpdate();
            }
        }
    }

    private void loadCompanyTable() {
        CompanyTableHandler db = new CompanyTableHandler(DatabaseManager.getDatabase(this));

        if (db.getCount() > 0) return;

        new ApiStock(this).requestAllStocks(new ApiStock.OnResponseListener<List<DataStock>>() {
            @Override
            public void onResponse(List<DataStock> dataStocks) {
                for (DataStock d : dataStocks) {
                    db.insert(d);
                }
            }

            @Override
            public void onError() {

            }
        });
    }
}