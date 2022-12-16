package com.example.lostbutfound.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class TabAdapter
        extends FragmentPagerAdapter {

//    public TabAdapter(@NonNull FragmentManager fm) {
//        super(fm);
//    }
//
//    @NonNull
//    @Override
//    public Fragment getItem(int position) {
//        Fragment fragment = null;
//        if (position == 0){
//            fragment = new HumanMain();
//        } else if (position == 1){
//            fragment = new AnimalsMain();
//        }
//        return fragment;
//    }
//
//    @Override
//    public int getCount() {
//        return 2;
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position)
//    {
//        String title = null;
//        if (position == 0)
//            title = "Розыск людей";
//        else if (position == 1)
//            title = "Розыск животных";
//        return title;
//    }


// ---------------------------------------------------------------------------------------




    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();


    public TabAdapter(@NonNull FragmentManager fm, int behavior) {

        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {

        return fragmentArrayList.size();
    }

    public void addFragment(Fragment fragment, String title){

        fragmentArrayList.add(fragment);
        fragmentTitle.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return fragmentTitle.get(position);
    }
}
