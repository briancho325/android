package com.app.fleemarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.fleemarket.databinding.FragmentChatroomBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatRoomFragment extends Fragment {

    private FragmentChatroomBinding binding;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference roomsRef = database.getReference("/chatroom");
    private FirebaseUser me;

    ArrayList<ChatRoomItem> rooms = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentChatroomBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        me = firebaseAuth.getCurrentUser();

        ChatRoomListAdapter adapter = new ChatRoomListAdapter();
        adapter.setData(rooms);

        adapter.setOnClickListener((view, item) -> {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra("roomId", item.getId());
            startActivity(intent);
        });
        binding.chatroomList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.chatroomList.setAdapter(adapter);


        roomsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getChildren().forEach(dataSnapshot -> {
                    ChatRoomItem room = dataSnapshot.getValue(ChatRoomItem.class);
                    if (room.getSender().equals(me.getUid()) || room.getReceiver().equals(me.getUid())) {
                        rooms.add(room);
                    }
                });
                adapter.setData(rooms);
                adapter.notifyDataSetChanged();
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
}