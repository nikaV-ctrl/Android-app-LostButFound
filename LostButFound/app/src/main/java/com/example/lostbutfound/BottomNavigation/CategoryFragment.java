package com.example.lostbutfound.BottomNavigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lostbutfound.TabLayout.AnimalsMain;
import com.example.lostbutfound.TabLayout.HumanMain;
import com.example.lostbutfound.R;
import com.example.lostbutfound.TabLayout.RelativesMain;
import com.example.lostbutfound.TabLayout.TabAdapter;
import com.example.lostbutfound.TabLayout.ThingsMain;
import com.google.android.material.tabs.TabLayout;

public class CategoryFragment extends Fragment{

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);


//        TabAdapter tabAdapter = new TabAdapter(getFragmentManager());
//        viewPager.setAdapter(tabAdapter);
//        tabLayout.setupWithViewPager(viewPager);

// ---------------------------------------------
        tabLayout.setupWithViewPager(viewPager);
        // FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        TabAdapter tabAdapter = new TabAdapter(getChildFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        tabAdapter.addFragment(new HumanMain(), "Розыск людей");
        tabAdapter.addFragment(new AnimalsMain(), "Поиск животных");
        tabAdapter.addFragment(new ThingsMain(), "Поиск вещей");
        viewPager.setAdapter(tabAdapter);
    }



}
