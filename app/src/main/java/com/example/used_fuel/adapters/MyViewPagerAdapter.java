package com.example.used_fuel.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.used_fuel.fragments.FuelCalculatorFragment;
import com.example.used_fuel.fragments.HistoryFragment;

public class MyViewPagerAdapter  extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FuelCalculatorFragment();
            case 1:
                return new HistoryFragment();
            default:
                return new FuelCalculatorFragment();
        }
    }
    @Override
    public int getItemCount() {
        return 3;
    }
}