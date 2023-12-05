package com.app.fleemarket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.fleemarket.databinding.ActivityItemDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class ItemDetailActivity extends AppCompatActivity {

    private ActivityItemDetailBinding binding;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 mm월 dd일 HH시 MM분");
    private static final DecimalFormat decFormat = new DecimalFormat("###,###");
    private String boardId;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference itemsRef = database.getReference("/goods");
    DatabaseReference roomRef = database.getReference("/chatroom");
    FirebaseUser me = firebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        boardId = getIntent().getStringExtra("id");
        Log.d(">>>", "onCreate: ID=" + boardId);

        binding = ActivityItemDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        itemsRef.child(boardId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GoodsItem item = snapshot.getValue(GoodsItem.class);
                updateUI(item);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        updateUI(new BoardItem(boardId, "제목" + boardId, "내용", new Date(), "글쓴이", 125000, false));
    }

    void updateUI(GoodsItem item) {
        Log.d(">>>", "updateUI: " + item);
        binding.title.setText(item.getTitle());
        binding.author.setText(item.getAuthor());
        binding.time.setText(sdf.format(item.getCreated()));
        binding.price.setText(decFormat.format(item.getPrice()));
        binding.soldout.setVisibility(item.isSoldout() ? View.VISIBLE : View.GONE);
        binding.content.setText(item.getContent());
        binding.contactBtn.setEnabled(!item.isSoldout());
        binding.contactBtn.setText(item.isSoldout() ? "판매 완료" : "연락 하기");
        if (firebaseAuth.getCurrentUser().getUid().equals(item.getAuthorId())) {
            binding.contactBtn.setVisibility(View.GONE);
            binding.editBtn.setVisibility(View.VISIBLE);
        } else {
            binding.contactBtn.setVisibility(View.VISIBLE);
            binding.editBtn.setVisibility(View.GONE);
        }
        if (item.isSoldout()) {
            binding.contactBtn.setEnabled(false);
        }


        binding.editBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, ItemAddActivity.class);
            intent.putExtra("item", item);
            intent.putExtra("itemId", item.getId());
            startActivity(intent);
            finish();
        });

        String roomId = boardId + "+" + item.getAuthorId() + "+" + me.getUid();

        binding.contactBtn.setOnClickListener(view -> {
            openRoom(item, me.getUid());
        });

    }

    void openRoom(GoodsItem goodsItem, String uid) {
        String roomId = boardId + "+" + goodsItem.getAuthorId() + "+" + uid;
        roomRef.child(roomId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(">>>", "snapshot: " + snapshot);
                ChatRoomItem chatroom = snapshot.getValue(ChatRoomItem.class);
                Log.d(">>>", "initRoom: " + chatroom);
                if (chatroom == null) {
                    ChatRoomItem newRoom = new ChatRoomItem(roomId, boardId, goodsItem.getTitle(), "", me.getUid(), 0);
                    newRoom.setReceiver(goodsItem.getAuthorId());
                    roomRef.child(roomId).setValue(newRoom).onSuccessTask(succ -> {
                        Intent intent = new Intent(ItemDetailActivity.this, ChatActivity.class);
                        intent.putExtra("roomId", roomId);
                        startActivity(intent);
                        return null;
                    });
                } else {
                    Intent intent = new Intent(ItemDetailActivity.this, ChatActivity.class);
                    intent.putExtra("roomId", roomId);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}