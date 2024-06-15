package com.s22010170.heathtrakerapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.s22010170.heathtrakerapp.ArticalFragment;
import com.s22010170.heathtrakerapp.VideosFragment;

public class EDUAdapter extends FragmentStateAdapter {
    public EDUAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new VideosFragment();
        }
        return new ArticalFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
