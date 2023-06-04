package com.example.bemyvoice.NavigationFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bemyvoice.Adapter.ImageSliderAdapter;
import com.example.bemyvoice.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AboutProject extends Fragment {

    private ViewPager mViewPager;
    private ImageSliderAdapter mAdapter;
    private Timer mTimer;
    private TextView imageTextTV;
    private int pos = 0;

    private int[] mImageIds = {
            R.drawable.pic,
            R.drawable.sign_to_text,
            R.drawable.text_to_sign,
    };
    private ArrayList<String> imageText;
    public AboutProject() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about_project, container, false);
        mViewPager = view.findViewById(R.id.viewPager);
        imageTextTV = view.findViewById(R.id.imageTextTV);
        addImageText();
        imageTextTV.setText(imageText.get(0));
        mAdapter = new ImageSliderAdapter(getContext(), mImageIds, imageText, imageTextTV) ;
        mViewPager.setAdapter(mAdapter);
        // Auto slide images every 3 seconds
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new AutoSlideTask(), 4000, 4000);
        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }
    private void addImageText()
    {
        imageText = new ArrayList<>();
        imageText.add("Be My Voice. Pakistan Sign Language Interpreter App");
        imageText.add("Convert Sign to Text with ease.");
        imageText.add("Get Signs in Response to Text easily!");

    }
    private class AutoSlideTask extends TimerTask {
        @Override
        public void run() {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    pos++;
                    imageTextTV.setText(imageText.get(pos%imageText.size()));
                    int currentPosition = mViewPager.getCurrentItem();
                    int newPosition = (currentPosition + 1) % mImageIds.length;
                    mViewPager.setCurrentItem(newPosition);
                }
            });
        }
    }
}