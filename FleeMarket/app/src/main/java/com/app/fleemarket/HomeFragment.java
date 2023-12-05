package com.app.fleemarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fleemarket.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private static final DecimalFormat decFormat = new DecimalFormat("###,###");


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference itemsRef = database.getReference("/goods");

    private float filterMin = 0;
    private float filterMax = 10000;
    private BoardAdapter adapter;
    private RecyclerView itemList;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // item list

        itemList = binding.recyclerView;
        itemList.setLayoutManager(new LinearLayoutManager(getContext()));


        adapter = new BoardAdapter();
        adapter.setPriceFilter(filterMin, filterMax);
        adapter.setSoldoutFilter(binding.filterSoldoutCheckbox.isChecked());
        adapter.setOnItemClickListener((view, item) -> {
            Intent intent = new Intent(getContext(), ItemDetailActivity.class);
            intent.putExtra("id", item.getId());
            startActivity(intent);
        });


        // filters
        String text = decFormat.format(filterMin) + " ~ " + decFormat.format(filterMax) + "원";
        binding.priceRangeText.setText(text);


        binding.applyFilterBtn.setOnClickListener(view -> {
            filterMin = Integer.parseInt(binding.minPriceText.getText().toString());
            filterMax = Integer.parseInt(binding.maxPriceText.getText().toString());
            adapter.setPriceFilter(filterMin, filterMax);
            adapter.setSoldoutFilter(binding.filterSoldoutCheckbox.isChecked());
            binding.filterLayout.setVisibility(View.GONE);
        });

        binding.fabFilter.setOnClickListener(view -> {
            binding.filterLayout.setVisibility(View.VISIBLE);
        });
        binding.fabAddPost.setVisibility(View.GONE);
        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<GoodsItem> items = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    GoodsItem item = postSnapshot.getValue(GoodsItem.class);
                    items.add(item);
                }
                adapter.setData(items);
                binding.recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


                if (items.size() > 0) {
                    filterMin = items.stream().min(Comparator.comparingInt(GoodsItem::getPrice)).get().getPrice();
                    filterMax = items.stream().max(Comparator.comparingInt(GoodsItem::getPrice)).get().getPrice();
                }
                adapter.setPriceFilter(filterMin, filterMax);
                adapter.setSoldoutFilter(binding.filterSoldoutCheckbox.isChecked());

                binding.fabAddPost.setVisibility(View.VISIBLE);

                binding.fabAddPost.setOnClickListener(v -> {
                    startActivity(new Intent(getContext(), ItemAddActivity.class));
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}