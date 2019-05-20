package com.apps.er.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity
{
    private Boolean pressed = false;
    //private static final int SPLASH_DURATION = 4500;
    private static final int SPLASH_DURATION = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                finish();
                if (!pressed)
                {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                }
            }
        }, SPLASH_DURATION);
    }

    @Override
    public void onBackPressed()
    {
        pressed = true;
        super.onBackPressed();
    }
}
