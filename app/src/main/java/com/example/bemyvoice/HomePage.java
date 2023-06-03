package com.example.bemyvoice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {

    Button signToText, textToSign;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        signToText = findViewById(R.id.signToText);
        textToSign = findViewById(R.id.textToSign);
        backBtn = findViewById(R.id.backBtn);
        getReadPermission();
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v->{
            closeApp();
        });

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
    private void closeApp()
    {
        View view = LayoutInflater.from(HomePage.this).inflate(R.layout.dialog_exit_app, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
        builder.setView(view);
        builder.setCancelable(false);
        Button accept = view.findViewById(R.id.acceptDialogBtn);
        Button reject = view.findViewById(R.id.rejectDialogBtn);
        AlertDialog dialog = builder.create();
        accept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                finishAffinity();
                System.exit(0);
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}