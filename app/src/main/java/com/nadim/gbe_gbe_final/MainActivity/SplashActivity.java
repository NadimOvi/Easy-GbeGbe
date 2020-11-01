package com.nadim.gbe_gbe_final.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.nadim.gbe_gbe_final.R;

import static maes.tech.intentanim.CustomIntent.customType;

public class SplashActivity extends AppCompatActivity {
    ImageView imageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageview = (ImageView) findViewById(R.id.image);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        imageview.setAnimation(animation);
        Thread timer = new Thread()
        {

            @Override
            public void run() {
                try {
                    sleep(4000);
                    super.run();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    customType(SplashActivity.this,"left-to-right");
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } ;
        timer.start();
    }
}
