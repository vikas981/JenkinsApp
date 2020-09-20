package com.viksingh.jenkins;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

public class SplashPage extends AppCompatActivity {
    int SPLASH_TIME = 3000;
    private ProgressBar splashProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(1024, 1024);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);

        this.splashProgress = (ProgressBar)findViewById(R.id.splashProgress);
        playProgress();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(SplashPage.this, LoginPage.class);
                SplashPage.this.startActivity(intent);
                SplashPage.this.finish();
            }
        },SPLASH_TIME);
    }
    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", new int[] { 100 }).setDuration(this.SPLASH_TIME).start();
    }
}