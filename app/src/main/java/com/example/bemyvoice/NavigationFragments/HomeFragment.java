package com.example.bemyvoice.NavigationFragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.bemyvoice.HomePage;
import com.example.bemyvoice.R;
import com.example.bemyvoice.SignToText;
import com.example.bemyvoice.TextToSign;

public class HomeFragment extends Fragment {

    Button signToText, textToSign;
    ImageView backBtn;
    Context context;

    public HomeFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context = getContext();
        signToText = view.findViewById(R.id.signToText);
        textToSign = view.findViewById(R.id.textToSign);
        backBtn = view.findViewById(R.id.backBtn);
        backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v->{
            closeApp();
        });

        textToSign.setOnClickListener(v->
        {
            Intent intent = new Intent(context, TextToSign.class);
            startActivity(intent);
        });
        signToText.setOnClickListener(v->
        {
            Intent intent = new Intent(context, SignToText.class);
            startActivity(intent);
        });
        return view;
    }
    private void closeApp()
    {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_exit_app, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setCancelable(false);
        Button accept = view.findViewById(R.id.acceptDialogBtn);
        Button reject = view.findViewById(R.id.rejectDialogBtn);
        AlertDialog dialog = builder.create();
        accept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                getActivity().finish();
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