package com.example.bemyvoice;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.Locale;

public class TextToSign extends AppCompatActivity {

    Button getVideo;
    EditText text;
    VideoView video;
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_sign);
        text = findViewById(R.id.editText);
        video = findViewById(R.id.videoView);
        animationView = findViewById(R.id.loading);



        getVideo = findViewById(R.id.button);
        if(!Python.isStarted())
        {
            Python.start(new AndroidPlatform(this));
        }
        getVideo.setOnClickListener(v->
        {
            animationView.setVisibility(View.VISIBLE);
            if(video.getVisibility()==View.VISIBLE)
            {
                video.setVisibility(View.GONE);
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String path = String.valueOf(getApplicationContext().getFileStreamPath("video.mp4"));
                    Python py = Python.getInstance();
                    PyObject pyObject = py.getModule("myscript");
                    PyObject object = pyObject.callAttr("TextToSign",path,text.getText().toString());
                    String err = object.toString();
                    if(err.contains("done")) {

                        Uri uri = Uri.parse(path);
                        Handler threadHandler = new Handler(Looper.getMainLooper());
                        threadHandler.post(new Runnable() {
                            @Override
                            public void run() {
//                            video.setVideoURI(uri);
//                            video.start();
                                video.setVisibility(View.VISIBLE);
                                animationView.setVisibility(View.GONE);
                                MediaController mediaController = new MediaController(TextToSign.this);
                                mediaController.setAnchorView(video);
                                video.setMediaController(mediaController);
                                video.setVideoURI(Uri.parse(path));
                                video.start();
                            }
                        });
                    }
                    else
                    {
                        Handler threadHandler = new Handler(Looper.getMainLooper());
                        threadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TextToSign.this,"Wrong Input",Toast.LENGTH_SHORT).show();
                            }
                        });
                            }
                    }
            }).start();

        });
    }
}