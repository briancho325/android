package com.app.fleemarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.fleemarket.LoginActivity;
import com.app.fleemarket.databinding.FragmentMyBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MyFragment extends Fragment {

    private FragmentMyBinding binding;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FirebaseUser me = firebaseAuth.getCurrentUser();
        binding.email.setText(me.getEmail());
        binding.name.setText(me.getDisplayName());
        binding.logoutBtn.setOnClickListener(view -> {
            firebaseAuth.signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return root;
    }
}