package com.app.fleemarket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChatRoomListAdapter extends RecyclerView.Adapter<ChatRoomListAdapter.ViewHolder> {
    public interface OnClickItemListener {
        void onClickItem(View view, ChatRoomItem item);
    }

    private List<ChatRoomItem> mData = new ArrayList<>();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 mm월 dd일 HH시 MM분 SS초");
    private static final DecimalFormat decFormat = new DecimalFormat("###,###");
    private OnClickItemListener listener;


    public void setData(List<ChatRoomItem> mData) {
        this.mData = mData.stream().sorted((t1, t2) -> {
            return (int) (t1.getLastMessageTime() - t2.getLastMessageTime());
        }).collect(Collectors.toList());
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickItemListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatRoomItem item = mData.get(position);

//        holder.message.setText(item.getLastMessage());
//        holder.time.setText(sdf.format(item.getLastMessageTime()));
//        holder.sender.setText(item.getSender());
        holder.title.setText(item.getTitle());
        if (this.listener != null) {
            holder.itemView.setOnClickListener(view -> {
                this.listener.onClickItem(view, item);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView message;
//        TextView sender;
//        TextView time;
        TextView title;

        ViewHolder(View itemView) {
            super(itemView);
//            message = itemView.findViewById(R.id.message);
//            time = itemView.findViewById(R.id.time);
//            sender = itemView.findViewById(R.id.sender);
            title = itemView.findViewById(R.id.title);
        }
    }
}
