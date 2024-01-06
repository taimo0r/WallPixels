package com.taimoor.wallpixels.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.taimoor.wallpixels.ApiService.RequestManager;
import com.taimoor.wallpixels.R;

public class SplashScreen extends AppCompatActivity {


    ImageView backgroundImage;
    TextView appName, poweredBy;
    Animation sideAnim, bottomAnim ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        RequestManager.initApiKey();

        //Hooks
        backgroundImage = findViewById(R.id.app_icon);
        appName = findViewById(R.id.app_name_text);
        poweredBy = findViewById(R.id.poweredby_text);


        //Animations

        sideAnim = AnimationUtils.loadAnimation(this, R.anim.side_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        //setAnimations
        backgroundImage.setAnimation(sideAnim);
        appName.setAnimation(bottomAnim);
        poweredBy.setAnimation(bottomAnim);

        int DISPLAY_LENGTH = 5000;
        new Handler().postDelayed(() -> {

            Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
            SplashScreen.this.startActivity(mainIntent);
            SplashScreen.this.finish();
        }, DISPLAY_LENGTH);
    }
}