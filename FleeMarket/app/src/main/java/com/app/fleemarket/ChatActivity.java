package com.app.fleemarket;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.fleemarket.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private String chatRoomId = null;
    private ChatAdapter adapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference roomRef = database.getReference("/chatroom");
    FirebaseUser me = firebaseAuth.getCurrentUser();

    private DatabaseReference chatsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatRoomId = getIntent().getStringExtra("roomId");


        adapter = new ChatAdapter();
        binding.chatList.setLayoutManager(new LinearLayoutManager(this));
        binding.chatList.setAdapter(adapter);

        roomRef.child(chatRoomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatRoomItem room = snapshot.getValue(ChatRoomItem.class);
                updateChat(room);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.sendBtn.setOnClickListener(v -> {
            String msg = binding.message.getText().toString();
            if (msg.isEmpty()) {
                return;
            }

            if (chatsRef != null) {
                ChatItem chat = new ChatItem();
                chat.setSender(me.getUid());
                chat.setMessage(msg);
                chat.setTime(System.currentTimeMillis());
                chatsRef.push().setValue(chat);
                binding.message.setText("");
                Log.d(">>>", "new ChatREf: "+chat.toString());
            }

        });

    }

    private void updateChat(ChatRoomItem item) {
        chatsRef = database.getReference("/chatroom/" + item.getId() + "/chats");
        binding.title.setText(item.getTitle());
        chatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<ChatItem> chatMessages = new ArrayList<>();
                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    ChatItem chatMessage = messageSnapshot.getValue(ChatItem.class);
                    chatMessages.add(chatMessage);
                }
                adapter.setData(chatMessages);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}