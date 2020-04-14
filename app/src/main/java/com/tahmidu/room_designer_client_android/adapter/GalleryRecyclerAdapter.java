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
import com.tahmidu.room_designer_client_android.model.GalleryItem;
import java.util.List;

/**
 * Adapter used for the recycler view that shows model installed within the device in the AR session.
 */
public class GalleryRecyclerAdapter extends RecyclerView.Adapter<GalleryRecyclerAdapter.MyViewHolder>
{
    private List<GalleryItem> items;
    private Context context;
    private MainRecyclerAdapter.OnClickListener clickListener;

    public GalleryRecyclerAdapter(List<GalleryItem> items, Context context, MainRecyclerAdapter.OnClickListener clickListener) {
        this.items = items;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public GalleryRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_item_layout, parent,
                false);
        return new GalleryRecyclerAdapter.MyViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryRecyclerAdapter.MyViewHolder holder, int position)
    {
        String URL = "https://192.168.0.8:8443/image/fetch-thumbnail?itemId="
                + items.get(position).getItem().getItemId();

        Picasso.get()
                .load(URL)
                .placeholder(R.color.colorTextPrimary)
                .into(holder.itemImage);

        holder.itemTitle.setText(items.get(position).getItem().getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView itemImage;
        private TextView itemTitle;

        private MainRecyclerAdapter.OnClickListener clickListener;

        public MyViewHolder(@NonNull View itemView, MainRecyclerAdapter.OnClickListener clickListener)
        {
            super(itemView);

            itemImage = itemView.findViewById(R.id.gallery_item_image);
            itemTitle = itemView.findViewById(R.id.gallery_item_title);

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
