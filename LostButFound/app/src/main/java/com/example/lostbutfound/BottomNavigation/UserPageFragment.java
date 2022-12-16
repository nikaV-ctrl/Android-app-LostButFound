package com.example.lostbutfound.BottomNavigation;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lostbutfound.Adapter.MyAdapter;
import com.example.lostbutfound.MainActivity2;
import com.example.lostbutfound.Model.People;
import com.example.lostbutfound.R;
import com.example.lostbutfound.User.LoginActivity;
import com.example.lostbutfound.User.UserPage;
import com.example.lostbutfound.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserPageFragment extends Fragment {

    private TextView emailTextView;
    private Button logoutBtn;
    private RecyclerView humanList;
    private MyAdapter myAdapter;
    private ArrayList<People> list;
    private DatabaseReference database =
            FirebaseDatabase.getInstance().getReference("People");
    private Query query;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    LinearLayoutManager HorizontalLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            init(view);
            String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            emailTextView.setText(userEmail);

            query = database.orderByChild("user").equalTo(userEmail);
            takeDataFromDbByQuery(query);

            logoutBtn.setOnClickListener(v-> logout());
        } else{
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    private void logout() {
        Utility.showToast(getActivity(), "Вы вышли из аккаунта");
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), MainActivity2.class));
    }

    private void init(View view) {
        emailTextView = view.findViewById(R.id.emailEditText);
        logoutBtn = view.findViewById(R.id.logoutBtn);
        humanList = view.findViewById(R.id.humanList);

        RecyclerViewLayoutManager
                = new LinearLayoutManager(
                getActivity().getApplicationContext());
        humanList.setLayoutManager(
                RecyclerViewLayoutManager);
        HorizontalLayout
                = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false);
        humanList.setHasFixedSize(true);
        humanList.setLayoutManager(HorizontalLayout);
        list = new ArrayList<>();
    }

    private void takeDataFromDbByQuery(Query query) {

        list.clear();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    People people = dataSnapshot.getValue(People.class);
                    list.add(people);
                }
                myAdapter = new MyAdapter(getActivity(), list);
                humanList.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}