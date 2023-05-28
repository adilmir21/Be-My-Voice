package com.example.bemyvoice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {

    Button signToText, textToSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        signToText = findViewById(R.id.signToText);
        textToSign = findViewById(R.id.textToSign);
        getReadPermission();
        textToSign.setOnClickListener(v->
        {
            Intent intent = new Intent(getApplicationContext(),TextToSign.class);
            startActivity(intent);
        });
        signToText.setOnClickListener(v->
        {
            Intent intent = new Intent(getApplicationContext(),SignToText.class);
            startActivity(intent);
            //Toast.makeText(this, "This feature is not Available for now!", Toast.LENGTH_SHORT).show();
        });
    }
    private void getReadPermission(){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE},111);
        }
    }

}