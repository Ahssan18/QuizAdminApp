package com.ncodelab.adminapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ncodelab.adminapp.R;
import com.ncodelab.adminapp.adapters.TotalUserAdapter;
import com.ncodelab.adminapp.adapters.WithdrawRequestAdapter;
import com.ncodelab.adminapp.model.Users;
import com.ncodelab.adminapp.model.WithdrawRequests;

import java.util.ArrayList;


public class WithdrawRequestFragment extends Fragment {

    public WithdrawRequestFragment() {
        // Required empty public constructor
    }

    WithdrawRequestAdapter withdrawRequestAdapter;
    ArrayList<WithdrawRequests> withdrawRequestsArrayList;
    RecyclerView withdrawRequestsRecyclerview;
    FirebaseFirestore database;
    Context fragmentContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_withdraw_request, container, false);

        database = FirebaseFirestore.getInstance();

        withdrawRequestsRecyclerview = view.findViewById(R.id.withdraw_requests_recyclerview);

        setWithdrawRequestsRecyclerview();
        DataChangeListener();

        return view;
    }
    public void setWithdrawRequestsRecyclerview(){
        withdrawRequestsRecyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        withdrawRequestsRecyclerview.setLayoutManager(layoutManager);
        withdrawRequestsArrayList = new ArrayList<>();

        withdrawRequestAdapter = new WithdrawRequestAdapter(getContextNullSafety(),withdrawRequestsArrayList);

        withdrawRequestsRecyclerview.setAdapter(withdrawRequestAdapter);



    }

    public void DataChangeListener(){

        database.collection("WithdrawRequests").orderBy("requestedDate", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null){

                            Log.d("Firestore_error",error.getMessage());
                            return;
                        }

                        for (DocumentChange dc: value.getDocumentChanges()){

                            if (dc.getType() == DocumentChange.Type.ADDED){

                                withdrawRequestsArrayList.add(dc.getDocument().toObject(WithdrawRequests.class));

                            }
                            withdrawRequestAdapter.notifyDataSetChanged();

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