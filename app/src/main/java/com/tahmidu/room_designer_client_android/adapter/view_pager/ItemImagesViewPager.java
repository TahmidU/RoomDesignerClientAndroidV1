package com.tahmidu.room_designer_client_android.adapter.view_pager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;
import com.tahmidu.room_designer_client_android.R;

import java.util.List;

/**
 * Adapter used for the view pager that show image previews of an item in the item view.
 */
public class ItemImagesViewPager extends PagerAdapter
{
    private Context context;
    private List<Long> images;

    public ItemImagesViewPager(Context context, List<Long> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position)
    {
        ImageView imageView = new ImageView(context);
        String URL = "http://192.168.0.8:8080/image/fetch-image?imageId="
                + images.get(position);

        Picasso.get()
                .load(URL)
                .fit()
                .centerCrop()
                .placeholder(android.R.color.transparent)
                .error(R.color.colorTextPrimary)
                .into(imageView);
        container.addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
