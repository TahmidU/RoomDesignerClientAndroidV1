package com.tahmidu.room_designer_client_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tahmidu.room_designer_client_android.R;
import com.tahmidu.room_designer_client_android.model.Item;

import java.util.List;

/**
 * Adapter used for the recycler view in Main and User Library.
 */
public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MyViewHolder>
{
    private List<Item> items;
    private Context context;
    private OnClickListener clickListener;

    public MainRecyclerAdapter(List<Item> items, Context context, OnClickListener clickListener) {
        this.items = items;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent,
                false);
        return new MyViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        String URL = "https://192.168.0.8:8443/api/image/fetch-thumbnail?itemId="
                + items.get(position).getItemId();

        Picasso.get()
                .load(URL)
                .placeholder(R.color.colorTextPrimary)
                .error(R.color.colorTextPrimary)
                .into(holder.itemImage);

        holder.itemTitle.setText(items.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView itemImage;
        private TextView itemTitle;

        private OnClickListener clickListener;

        public MyViewHolder(@NonNull View itemView, OnClickListener clickListener)
        {
            super(itemView);

            itemImage = itemView.findViewById(R.id.item_image);
            itemTitle = itemView.findViewById(R.id.item_title);

            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition());
        }
    }

    public void updateList(List<Item> items)
    {
        this.items = items;
        notifyDataSetChanged();
    }

    public void resetList()
    {
        items.clear();
        notifyDataSetChanged();
    }

    public interface OnClickListener
    {
        void onClick(View view, int position);
    }
}
