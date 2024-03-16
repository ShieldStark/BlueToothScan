package com.example.bluetoothscan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_DURATION = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final ImageView liquid = findViewById(R.id.bucket);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.filling_animation);
        liquid.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close the splash activity
            }
        }, SPLASH_DURATION);
    }
}