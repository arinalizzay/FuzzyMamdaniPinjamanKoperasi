package com.example.pinjamankoperasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView logoSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoSplash =findViewById(R.id.logoSplash);

        Animation myanim    = AnimationUtils.loadAnimation(this, R.anim.transition);
        logoSplash.startAnimation(myanim);
        final Intent a = new Intent (this, HomeActivity.class);
        Thread timer = new Thread() {
            public void run(){
                try{
                    sleep(3000);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }finally {
                    startActivity(a);
                    finish();
                }
            }
        };
        timer.start();
    }
}
