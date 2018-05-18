package com.example.user.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;


public class LoadActivity extends Activity {
    private Intent intent;
    private ProgressBar progressBar;
    private Context context;
    private NetworkInfo mNetworkInfo;
    private AlphaAnimation animation;
    //生命週期 於被呼叫時優先執行之一
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);



        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_load);
        context = getApplicationContext();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ImageView loadingImage = (ImageView) findViewById(R.id.LoadImage);
        loadingImage.setImageResource(R.drawable.a10);

        animation = new AlphaAnimation(0.1f, 1.0f);
        animation.setDuration(2000);
        loadingImage.setAnimation(animation);
        intent = new Intent( context , Login.class);
        animation.setAnimationListener(new Animation.AnimationListener() {
            //設定讀取的浮現動畫的生命週期
            @Override
            public void onAnimationStart(Animation animation) {
                progressBar.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animation animationA) {
                progressBar.setVisibility(View.INVISIBLE);
                least();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }
    private void least() {
        intent.setClass(context , Login.class);
        startActivity(intent);
        finish();
    }
}