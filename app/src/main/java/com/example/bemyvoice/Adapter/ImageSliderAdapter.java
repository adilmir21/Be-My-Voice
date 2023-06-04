package com.example.bemyvoice.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.bemyvoice.R;

import java.util.ArrayList;

public class ImageSliderAdapter extends PagerAdapter {
    private Context mContext;
    private int[] mImageIds;
    private ArrayList<String> imageText;
    private TextView imageTextTV;

    public ImageSliderAdapter(Context context, int[] imageIds, ArrayList<String> imageText, TextView imageTextTV) {
        mContext = context;
        mImageIds = imageIds;
        this. imageText = imageText;
        this.imageTextTV = imageTextTV;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.slider_item, container, false);

        ImageView imageView = view.findViewById(R.id.imageViewIV);
        imageView.setImageResource(mImageIds[position]);

        TextView textView = view.findViewById(R.id.imageTextTV);
        textView.setText(imageText.get(position)); // Set your desired text here
        //imageTextTV.setText(imageText.get(position));
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mImageIds.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}

