package com.example.bemyvoice;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

public class SignToText extends AppCompatActivity{

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int VIDEO_RECORD_CODE = 101;
    private static final String TAG = "moja";
    Button startRecording, stopRecording;
    Uri videoPath;
    String paths;
    TextView responseText, urduResponseText;
    String result, english, urdu;
    ArrayList<String> response = new ArrayList<>();
    ArrayList<String> urduResponse = new ArrayList<>();
    int clickedCount = 0;
    TextToSpeech urduText, englishText;
    ImageView englishVoice, urduVoice, backBtn;
    ConstraintLayout layout;
    LinearLayout animLayoutLL;
    private Observer observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_to_text);
        startRecording = findViewById(R.id.startRecording);
        responseText = findViewById(R.id.responseText);
        urduResponseText = findViewById(R.id.urduResponseText);
        stopRecording = findViewById(R.id.stopRecording);
        englishVoice = findViewById(R.id.englishVoice);
        urduVoice = findViewById(R.id.urduVoice);
        layout = findViewById(R.id.hiddenLayout);
        animLayoutLL = findViewById(R.id.animLayoutLL);
        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v->{
            onBackPressed();
        });


        urduText = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR)
                {
                    urduText.setLanguage(new Locale("ur"));
                }
            }
        });
        englishText = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR)
                {
                    englishText.setLanguage(Locale.ENGLISH);
                }
            }
        });


        permissions();
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        if(isCameraAvailableInPhone())
        {
            getCameraPermissions();
        }
        startRecording.setOnClickListener(v->{
                captureVideo();
                clickedCount++;
        });
        observer = new Observer() {
            @Override
            public void playAnim() {
                animLayoutLL.setVisibility(View.VISIBLE);
            }
            public void showResults() {
                startRecording.setText("Start Recording");
                String concatString = " ";
                for (int i = 0; i < response.size(); i++) {
                    concatString+=response.get(i) + " ";

                }
                responseText.setText(concatString);

                String concatUrduString = " ";
                for (int i = 0; i < urduResponse.size(); i++) {
                    concatUrduString+=urduResponse.get(i) + " ";

                }
                urduResponseText.setText(concatUrduString);
                layout.setVisibility(View.VISIBLE);
                //stopRecording.setVisibility(View.GONE);
            }
        };
        stopRecording.setOnClickListener(v->
        {
            if(clickedCount==response.size()) {
                startRecording.setText("Start Recording");
                String concatString = " ";
                for (int i = 0; i < response.size(); i++) {
                    concatString+=response.get(i) + " ";

                }
                responseText.setText(concatString);

                String concatUrduString = " ";
                for (int i = 0; i < urduResponse.size(); i++) {
                    concatUrduString+=urduResponse.get(i) + " ";

                }
                urduResponseText.setText(concatUrduString);
                layout.setVisibility(View.VISIBLE);
                //stopRecording.setVisibility(View.GONE);
            }
        });

        urduVoice.setOnClickListener(v->{
            urduText.speak(urdu,TextToSpeech.QUEUE_FLUSH,null);
        });
        englishVoice.setOnClickListener(v->{
            englishText.speak(english,TextToSpeech.QUEUE_FLUSH,null);
        });



    }
    private boolean isCameraAvailableInPhone()
    {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }
    private void getCameraPermissions()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},CAMERA_PERMISSION_CODE);
        }
    }
    private void captureVideo()
    {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,10);
        startActivityForResult(intent,VIDEO_RECORD_CODE);

    }
public void permissions()
{
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_DENIED) {
        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
    }
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == VIDEO_RECORD_CODE) {
            //stopRecording.setVisibility(View.VISIBLE);
            startRecording.setText("Record More");
            observer.playAnim();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (resultCode == RESULT_OK) {

                        assert data != null;
                        videoPath = data.getData();


                        paths = String.valueOf(data.getData());
                        Log.d("moja", "Video Path : " + videoPath);

                        Python py = Python.getInstance();
                        PyObject pyObject = py.getModule("SignToText");
                        PyObject object = pyObject.callAttr("SignToText", VideoBase64());
                        String err = object.toString();
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "run: "+err);
                                String [] splits = err.split(",");
                                english = splits[0];
                                english = english.substring(2,english.length()-1);
                                urdu = splits[1];
                                urdu = urdu.substring(2,urdu.length()-2);
                                Log.d(TAG, "Eng "+ english +" Urdu : "+ urdu);
                                response.add(english);
                                urduResponse.add(urdu);
                                if(clickedCount == response.size())
                                {
                                    animLayoutLL.setVisibility(View.GONE);
                                    observer.showResults();
                                }

                            }
                        });
                    }
                    else if(resultCode == RESULT_CANCELED)
                    {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SignToText.this, "Recording Cancelled ", Toast.LENGTH_SHORT).show();
                                //stopRecording.setVisibility(View.GONE);
                            }
                        });
                    }
                    else
                    {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //stopRecording.setVisibility(View.GONE);
                                Toast.makeText(SignToText.this, "Error Capturing Video! Please try Again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        }

    }
    private String VideoBase64() {
        ByteArrayOutputStream outputStream;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            try (InputStream inputStream = getContentResolver().openInputStream(videoPath)) {
                outputStream = getByteArrayOutputStream(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

             return result = EncodeVideo(outputStream);
        }
        return null;
    }
    public ByteArrayOutputStream getByteArrayOutputStream(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while (true) {
            try {
                if ((length = inputStream.read(buffer)) == -1) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream.write(buffer, 0, length);
        }
        return outputStream;
    }

    public static String EncodeVideo( ByteArrayOutputStream outputStream)
    {
        byte[] data = outputStream.toByteArray();
        // encode the file into base64
        String encoded = Base64.encodeToString(data, 0);
        return "data:video/mp4;base64," + encoded;
    }

    private interface Observer {
        void playAnim();
        void showResults();
    }
}