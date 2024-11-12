package com.example.app_datn_haven_inn.ui.room.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.app_datn_haven_inn.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;


import com.bumptech.glide.Glide;
import com.example.app_datn_haven_inn.R;
import com.example.app_datn_haven_inn.ui.room.model.Photo;


import java.util.List;

public class PhotoAdapter extends PagerAdapter {

    private Context mcontext;
    private List<Photo> mListPhoto;
    public PhotoAdapter(Context mcontext, List<Photo> mListPhoto) {
        this.mcontext = mcontext;
        this.mListPhoto = mListPhoto;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_room_slideshow,container,false);
        ImageView imgPhoto = view.findViewById(R.id.img_photo);

        Photo photo = mListPhoto.get(position);
        if (photo != null){
            Glide.with(mcontext).load(photo.getResourceId()).into(imgPhoto);

        }

        // Add view to viewgroup
        container.addView(view);

        return view;
    }
    @Override
    public int getCount() {
        if (mListPhoto != null){
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
        // Remove view
        container.removeView((View) object);
    }
}
