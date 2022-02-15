package com.ncodelab.adminapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ncodelab.adminapp.R;
import com.ncodelab.adminapp.adapters.TotalUserAdapter;
import com.ncodelab.adminapp.model.Users;

import java.util.ArrayList;

public class UserFragment extends Fragment {


    public UserFragment() {
        // Required empty public constructor
    }

    Context fragmentContext;
    RecyclerView usersRecyclerview;
    TotalUserAdapter totalUserAdapter;
    ArrayList<Users> usersList;
    FirebaseFirestore database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);


        database = FirebaseFirestore.getInstance();

        usersRecyclerview = view.findViewById(R.id.users_recyclerview);


        setUsersRecyclerview();
        DataChangeListener();

        return view;
    }
    public void setUsersRecyclerview(){
        usersRecyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        usersRecyclerview.setLayoutManager(layoutManager);
        usersList = new ArrayList<Users>();

        totalUserAdapter = new TotalUserAdapter(getContextNullSafety(),usersList);

        usersRecyclerview.setAdapter(totalUserAdapter);
    }

    public void DataChangeListener(){

        database.collection("Users").orderBy("lastLogin", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null){

                            Log.d("Firestore_error",error.getMessage());
                            return;
                        }

                        for (DocumentChange dc: value.getDocumentChanges()){

                            if (dc.getType() == DocumentChange.Type.ADDED){

                                usersList.add(dc.getDocument().toObject(Users.class));

                            }
                            totalUserAdapter.notifyDataSetChanged();

                        }


                    }
                });


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentContext = context;
    }

    /**CALL THIS IF YOU NEED CONTEXT*/
    public Context getContextNullSafety() {
        if (getContext() != null) return getContext();
        if (getActivity() != null) return getActivity();
        if (fragmentContext != null) return fragmentContext;
        if (getView() != null && getView().getContext() != null) return getView().getContext();
        if (requireContext() != null) return requireContext();
        if (requireActivity() != null) return requireActivity();
        if (requireView() != null && requireView().getContext() != null)
            return requireView().getContext();

        return null;

    }
}