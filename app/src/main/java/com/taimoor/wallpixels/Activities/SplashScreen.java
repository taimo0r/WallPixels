package com.taimoor.wallpixels.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.taimoor.wallpixels.ApiService.RequestManager;
import com.taimoor.wallpixels.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        RequestManager.initApiKey();

        int DISPLAY_LENGTH = 5000;
        new Handler().postDelayed(() -> {

            Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
            SplashScreen.this.startActivity(mainIntent);
            SplashScreen.this.finish();
        }, DISPLAY_LENGTH);
    }
}