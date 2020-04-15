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
 * Adapter used for the recycler view that shows other available items in the AR session.
 */
public class SubLibraryRecyclerAdapter extends RecyclerView.Adapter<SubLibraryRecyclerAdapter.MyViewHolder>
{
    private List<Item> items;
    private Context context;
    private SubLibraryRecyclerAdapter.OnClickListener clickListener;

    public SubLibraryRecyclerAdapter(List<Item> items, Context context,
                                     SubLibraryRecyclerAdapter.OnClickListener clickListener) {
        this.items = items;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public SubLibraryRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_item_layout, parent,
                false);
        return new SubLibraryRecyclerAdapter.MyViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SubLibraryRecyclerAdapter.MyViewHolder holder, int position)
    {
        String URL = "https://192.168.0.8:8443/api/image/fetch-thumbnail?itemId="
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
        private TextView itemTitle;

        private SubLibraryRecyclerAdapter.OnClickListener clickListener;

        public MyViewHolder(@NonNull View itemView, SubLibraryRecyclerAdapter.OnClickListener clickListener)
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
