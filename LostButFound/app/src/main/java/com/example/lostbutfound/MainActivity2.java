package com.example.lostbutfound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.lostbutfound.BottomNavigation.AddStatementFragment;
import com.example.lostbutfound.BottomNavigation.CategoryFragment;
import com.example.lostbutfound.BottomNavigation.MainFragment;
import com.example.lostbutfound.BottomNavigation.SavedFragment;
import com.example.lostbutfound.BottomNavigation.StatisticsFragment;
import com.example.lostbutfound.BottomNavigation.UserPageFragment;
import com.example.lostbutfound.TabLayout.HumanMain;
import com.example.lostbutfound.User.UserPage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity2 extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    MainFragment mainFragment = new MainFragment();
    CategoryFragment categoryFragment = new CategoryFragment();
    AddStatementFragment addStatementFragment = new AddStatementFragment();
    StatisticsFragment statisticsFragment = new StatisticsFragment();
//    UserPageFragment profileFragment = new UserPageFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setNewFragment(mainFragment);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeItem:
                        setNewFragment(mainFragment);
                        return true;
                    case R.id.categoryItem:
                        setNewFragment(categoryFragment);
                        return true;
                    case R.id.addItem:
                        setNewFragment(addStatementFragment);
                        return true;
                    case R.id.statisticsItem:
                        setNewFragment(statisticsFragment);
                        return true;
                    case R.id.profileItem:
//                        setNewFragment(profileFragment);
                        startActivity(new Intent(MainActivity2.this, UserPage.class));
                        return true;
                }

                return false;
            }
        });

    }

    private void setNewFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.addToBackStack(null); // TODO !!! для того, чтобы память не засорялась
        ft.commit();
    }

    public void startUserPage(View view){
        Intent intent = new Intent(this, UserPage.class);
        startActivity(intent);
    }

    public void startSearchPage(View view){
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
    }

    public void startFirstActivity(View view){
        startActivity(new Intent(this, FirstActivity.class));
    }

    public void startYandexActivity(View view){
        startActivity(new Intent(this, Yandex.class));
    }
}