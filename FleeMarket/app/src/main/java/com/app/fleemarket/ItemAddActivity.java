package com.app.fleemarket;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.fleemarket.databinding.ActivityItemAddBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class ItemAddActivity extends AppCompatActivity {

    private ActivityItemAddBinding binding;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 mm월 dd일 HH시 MM분");
    private static final DecimalFormat decFormat = new DecimalFormat("###,###");
    private String boardId;
    private boolean isWriteMode = false;


    FirebaseDatabase database = FirebaseDatabase.getInstance("https://flee-market-6f509-default-rtdb.firebaseio.com/");
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference itemsRef = database.getReference("/goods");
    private GoodsItem goodsItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.goodsItem = getIntent().getSerializableExtra("item", GoodsItem.class);
            if (this.goodsItem != null) {
                setItem(this.goodsItem);
            }
        } else {
            String id = getIntent().getStringExtra("itemId");
            if (id != null) {

                itemsRef.child(id).get().addOnSuccessListener(dataSnapshot -> {
                    this.goodsItem = dataSnapshot.getValue(GoodsItem.class);
                    setItem(this.goodsItem);
                });
            }
        }


        binding.writeButton.setOnClickListener(view -> {
            if (binding.title.getText().toString().isEmpty()
                    || binding.content.getText().toString().isEmpty()
                    || binding.content.getText().toString().isEmpty()) {
                Toast.makeText(this, "항목을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            GoodsItem newGoodsItem = this.goodsItem;
            if (newGoodsItem == null) {
                newGoodsItem = new GoodsItem();
            }
            newGoodsItem.setCreated(System.currentTimeMillis());
            newGoodsItem.setTitle(binding.title.getText().toString());
            newGoodsItem.setContent(binding.content.getText().toString());
            newGoodsItem.setPrice(Integer.parseInt(binding.price.getText().toString()));
            newGoodsItem.setAuthorId(firebaseAuth.getCurrentUser().getUid());
            newGoodsItem.setAuthor(firebaseAuth.getCurrentUser().getDisplayName());

            addItem(newGoodsItem);
        });

    }

    void setItem(GoodsItem item) {
        goodsItem = item;
        binding.price.setText(String.valueOf(goodsItem.getPrice()));
        binding.title.setText(goodsItem.getTitle());
        binding.content.setText(goodsItem.getContent());
        binding.soldoutCheck.setVisibility(goodsItem.getId() == null ? View.GONE : View.VISIBLE);
        binding.soldoutCheck.setChecked(goodsItem.isSoldout());
        binding.soldoutCheck.setOnCheckedChangeListener((compoundButton, b) -> {
            item.setSoldout(b);
        });

    }

    void addItem(GoodsItem goodsItem) {
        if (goodsItem.getId() == null) {
            String itemId = itemsRef.push().getKey();
            goodsItem.setId(itemId);
        }
        Log.d(">>>", "addItem: " + goodsItem.toString());
        itemsRef.child(goodsItem.getId()).setValue(goodsItem)
                .addOnSuccessListener(v -> {
                    Toast.makeText(this, "게시물 작성 성공", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, ItemDetailActivity.class);
                    intent.putExtra("id", goodsItem.getId());
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(v -> {
                    Toast.makeText(this, "게시물 작성 실패", Toast.LENGTH_SHORT).show();
                })
                .addOnCompleteListener(v -> {
                    Log.d(">>>", "addItem: " + v);
                });
    }
}