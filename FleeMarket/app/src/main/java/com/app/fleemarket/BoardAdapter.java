package com.app.fleemarket;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {
    public interface OnClickItemListener<GoodsItem> {
        void onClickItem(View view, GoodsItem item);
    }

    private List<GoodsItem> mData = new ArrayList<>();
    private List<GoodsItem> filterData = new ArrayList<>();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
    private static final DecimalFormat decFormat = new DecimalFormat("###,###");
    private int filterMax = Integer.MAX_VALUE;
    private int filterMin = 0;
    private boolean filterSoldout = true;
    private OnClickItemListener listener;
    private boolean showOnlyMine;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    public void setData(List<GoodsItem> mData) {
        this.mData = mData;
        updateFilter();
    }

    public void setOnItemClickListener(OnClickItemListener<GoodsItem> listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GoodsItem item = filterData.get(position);
        holder.title.setText(item.getTitle());
        holder.price.setText(decFormat.format(item.getPrice()) + "원");
        if (item.isSoldout()) {
            holder.price.setTextColor(Color.rgb(170, 170, 170));
        } else {
            holder.price.setTextColor(Color.rgb(50, 50, 50));
        }
        holder.author.setText(item.getAuthor());
        holder.soldout.setVisibility(item.isSoldout() ? View.VISIBLE : View.GONE);
        if (listener != null) {
            holder.itemView.setOnClickListener(view -> {
                listener.onClickItem(view, item);
            });
        }
    }

    @Override
    public int getItemCount() {
        return filterData.size();
    }

    public void setSoldoutFilter(boolean b) {
        this.filterSoldout = b;
        updateFilter();
    }

    public void setPriceFilter(float filterMin, float filterMax) {
        this.filterMin = (int) (filterMin);
        this.filterMax = (int) (filterMax);
        updateFilter();
    }

    private void updateFilter() {
        filterData = mData.stream().filter(goodsItem -> {
            return this.filterMin <= goodsItem.getPrice() && goodsItem.getPrice() <= this.filterMax
                    && (!filterSoldout || !goodsItem.isSoldout())
                    && (!showOnlyMine || firebaseAuth.getCurrentUser().getUid().equals(goodsItem.getAuthorId()));
        }).collect(Collectors.toList());
        this.notifyDataSetChanged();
    }

    public void setShowOnlyMine(boolean checked) {
        this.showOnlyMine = checked;
        this.notifyDataSetChanged();
        updateFilter();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView price;
        TextView soldout;
        TextView author;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            soldout = itemView.findViewById(R.id.soldout);
            author = itemView.findViewById(R.id.author);
        }
    }
}
