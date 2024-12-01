package com.example.app_datn_haven_inn.ui.room.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.app_datn_haven_inn.R;

import java.util.List;

public class PhotoAdapter extends PagerAdapter {

    private Context mcontext;
    private List<String> mListPhoto;

    public PhotoAdapter(Context mcontext, List<String> mListPhoto) {
        this.mcontext = mcontext;
        this.mListPhoto = mListPhoto;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_room_slideshow, container, false);
        ImageView imgPhoto = view.findViewById(R.id.img_photo);

        String photo = mListPhoto.get(position);

        Glide.with(mcontext).load(photo).error(R.drawable.img_2).into(imgPhoto);

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        if (mListPhoto != null) {
            return mListPhoto.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
