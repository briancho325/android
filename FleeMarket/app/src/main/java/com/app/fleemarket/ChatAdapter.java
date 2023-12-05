package com.app.fleemarket;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<ChatItem> mData = new ArrayList<>();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 mm월 dd일 HH시 MM분");
    private static final DecimalFormat decFormat = new DecimalFormat("###,###");
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void setData(List<ChatItem> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FirebaseUser me = firebaseAuth.getCurrentUser();
        ChatItem prevItem = null;
        ChatItem nextItem = null;
        ChatItem item = mData.get(position);

        if (position > 0) {
            prevItem = mData.get(position - 1);
        }
        if (position < mData.size() - 1) {
            nextItem = mData.get(position + 1);
        }

        boolean samePrevSender = prevItem != null && (item.getSender().equals(prevItem.getSender()));
        boolean sameNextSender = nextItem != null && (item.getSender().equals(nextItem.getSender()));
        boolean sameTime = nextItem != null && sdf.format(item.getTime()).equals(sdf.format(nextItem.getTime()));

        holder.message.setText(item.getMessage());
        holder.time.setText(sdf.format(item.getTime()));

        if (samePrevSender) {
            holder.sender.setVisibility(View.GONE);
        } else {
            holder.sender.setVisibility(View.VISIBLE);
        }

        if (sameNextSender && sameTime) {
            holder.time.setVisibility(View.GONE);
        } else {
            holder.time.setVisibility(View.VISIBLE);
        }

        if (item.getSender().equals(me.getUid())) {
            holder.view.setGravity(Gravity.END);
            holder.sender.setText("나");
        } else {
            holder.view.setGravity(Gravity.START);
            holder.sender.setText("상대");
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView sender;
        TextView time;
        LinearLayout view;

        ViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            sender = itemView.findViewById(R.id.sender);
            view = itemView.findViewById(R.id.chat_item_view);
        }
    }
}
