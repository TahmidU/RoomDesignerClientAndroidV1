package com.tahmidu.room_designer_client_android.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.tahmidu.room_designer_client_android.R;
import com.tahmidu.room_designer_client_android.model.Item;
import com.tahmidu.room_designer_client_android.preferences.PreferenceProvider;
import com.tahmidu.room_designer_client_android.util.BasicAuthInterceptor;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
        String URL = "http://192.168.0.8:8080/image/fetch-thumbnail?itemId="
                + items.get(position).getItemId();

        Picasso.get()
                .load(URL)
                .placeholder(R.color.colorTextPrimary)
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
        private ImageView itemFavourite;
        private TextView itemTitle;

        private OnClickListener clickListener;

        public MyViewHolder(@NonNull View itemView, OnClickListener clickListener)
        {
            super(itemView);

            itemImage = itemView.findViewById(R.id.item_image);
            itemFavourite = itemView.findViewById(R.id.item_favourite);
            itemTitle = itemView.findViewById(R.id.item_title);

            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition());
        }
    }

    public interface OnClickListener
    {
        void onClick(View view, int position);
    }
}
