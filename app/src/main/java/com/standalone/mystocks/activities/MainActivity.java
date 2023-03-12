package com.standalone.mystocks.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.LongDef;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.standalone.mystocks.R;
import com.standalone.mystocks.adapters.ViewPagerAdapter;
import com.standalone.mystocks.handlers.dbase.DatabaseManager;
import com.standalone.mystocks.fragments.TradeDialogFragment;
import com.standalone.mystocks.handlers.CompanyTableHandler;
import com.standalone.mystocks.interfaces.AdapterController;
import com.standalone.mystocks.interfaces.DialogCloseListener;
import com.standalone.mystocks.models.DataStock;
import com.standalone.mystocks.utils.ApiStock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    public ViewPagerAdapter viewPagerAdapter;
    private ActionBar actionBar;
    private AdapterController currentAdapterController;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_main);
        actionBar = getSupportActionBar();

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
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setOffscreenPageLimit(2);
        viewPager2.setAdapter(viewPagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());

                if (tab.getPosition() < 2) {
                    actionBar.show();
                } else {
                    actionBar.hide();
                }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO: should use a thread
                for (AdapterController ac : getAdapterControllers()) {
                    ac.filter(newText);
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        for (AdapterController ac : getAdapterControllers()) {
            ac.update();
        }
    }

    private List<AdapterController> getAdapterControllers() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        List<AdapterController> results = new ArrayList<>();
        for (Fragment f : fragmentList) {
            if (f instanceof AdapterController) {
                results.add((AdapterController) f);
            }
        }

        return results;
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