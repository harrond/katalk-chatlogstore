package com.example.hoyoung.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Hoyoung on 2017-02-01.
 */

public class SplashActivity extends Activity {
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_loading);
        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(),2000);

    }
    private class splashhandler implements Runnable{
        public void run(){
           // startActivity(new Intent(getApplication(),MainActivity.class));
            SplashActivity.this.finish();
        }
    }
}
