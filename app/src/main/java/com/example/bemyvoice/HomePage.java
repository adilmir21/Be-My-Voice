package com.example.bemyvoice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.bemyvoice.databinding.ActivityHomePageBinding;
import com.example.bemyvoice.databinding.BottomNavLayoutBinding;

public class HomePage extends AppCompatActivity {

    private ActivityHomePageBinding binding;
    public static int currentSelection = 1;
    private BottomNavLayoutBinding bottomNavBar;
    private NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_page);

        navController = Navigation.findNavController(this, R.id.homeActivityNavContainer);
        bottomNavBar = binding.bottomNavBar;
        bottomNavBar.bottomItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelection != 1) {
                    navController.navigate(R.id.homeFragment);
                    setBottomNavItems(1);
                }
            }
        });
        bottomNavBar.bottomItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelection != 2) {
                    setBottomNavItems(2);
                    navController.navigate(R.id.aboutProject);

                }
            }
        });
        bottomNavBar.bottomItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelection != 3) {
                    setBottomNavItems(3);
                    navController.navigate(R.id.PSL);

                }
            }
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
    private void setBottomNavItems(int selection) {
        if (selection == currentSelection || selection < 1 || selection > 3) {
            return;
        }
        unselectAllViews();
        if (selection == 1)
        {
            selectCurrentItem(bottomNavBar.bottomItem1, bottomNavBar.title1);
        }
        if (selection == 2)
        {
            selectCurrentItem(bottomNavBar.bottomItem2, bottomNavBar.title2);
        }
        if (selection == 3)
        {
            selectCurrentItem(bottomNavBar.bottomItem3, bottomNavBar.title3);
        }
        currentSelection = selection;
    }

    private void selectCurrentItem(LinearLayout selectedItem, TextView title) {
        selectedItem.setBackground(ContextCompat.getDrawable(this, R.drawable.bottom_bar_selected_item_bk));
        title.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.ZoomIn).duration(300).playOn(selectedItem);
        YoYo.with(Techniques.FadeIn).duration(300).playOn(title);
    }

    private void unselectAllViews() {
        //BottomNavLayoutBinding bottomNavBar = binding.bottomNavBar;
        setTransparentBackground((View) bottomNavBar.bottomItem1);
        setTransparentBackground((View) bottomNavBar.bottomItem2);
        setTransparentBackground((View) bottomNavBar.bottomItem3);
        bottomNavBar.title1.setVisibility(View.GONE);
        bottomNavBar.title2.setVisibility(View.GONE);
        bottomNavBar.title3.setVisibility(View.GONE);
    }

    private void setTransparentBackground(View view) {
        view.setBackground(ContextCompat.getDrawable(this, R.drawable.transparent_background));
    }
}