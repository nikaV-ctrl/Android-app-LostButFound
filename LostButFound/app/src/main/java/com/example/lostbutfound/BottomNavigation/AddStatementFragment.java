package com.example.lostbutfound.BottomNavigation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lostbutfound.Add.AddAnimalsStatements;
import com.example.lostbutfound.AddThings;
import com.example.lostbutfound.R;
import com.example.lostbutfound.Add.AddPeopleStatements;

public class AddStatementFragment extends Fragment {

    private CardView peopleCardView, animalsCardView, thingsCardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_statement, container, false);
    }

        @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

//            Button addHumanBtn = view.findViewById(R.id.addHumanBtn);
//            addHumanBtn.setOnClickListener((v) ->
//                    startActivity(new Intent(getActivity(), AddPeopleStatements.class)));
//
//            Button addAnimalBtn = view.findViewById(R.id.addAnimalBtn);
//            addAnimalBtn.setOnClickListener((v) ->
//                    startActivity(new Intent(getActivity(), AddAnimalsStatements.class)));
//
//            Button addRelativeBtn = view.findViewById(R.id.addRelativeBtn);
//            addRelativeBtn.setOnClickListener((v) ->
//                    startActivity(new Intent(getActivity(), AddRelativesStatement.class)));

            peopleCardView = view.findViewById(R.id.peopleCardView);
            peopleCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), AddPeopleStatements.class));
                }
            });

            animalsCardView = view.findViewById(R.id.animalsCardView);
            animalsCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), AddAnimalsStatements.class));
                }
            });

            thingsCardView = view.findViewById(R.id.thingsCardView);
            thingsCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), AddThings.class));
                }
            });
    }
}