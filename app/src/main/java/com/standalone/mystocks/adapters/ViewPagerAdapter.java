package com.standalone.mystocks.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.standalone.mystocks.activities.MainActivity;
import com.standalone.mystocks.fragments.AssetFragment;
import com.standalone.mystocks.fragments.HistoryFragment;
import com.standalone.mystocks.fragments.ReportFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private final MainActivity activity;
    private List<Fragment> itemList;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.activity = (MainActivity) fragmentActivity;
        itemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new HistoryFragment(activity);
            case 2:
                return new ReportFragment(activity);
            default:
                return new AssetFragment(activity);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
