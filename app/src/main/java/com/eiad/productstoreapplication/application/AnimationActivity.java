package com.eiad.productstoreapplication.application;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.eiad.productstoreapplication.R;
import com.squareup.picasso.Picasso;

public class AnimationActivity extends AppCompatActivity implements Animation.AnimationListener {

    ImageView img;
    Animation animSequential;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        img = (ImageView) findViewById(R.id.imgItem);

        Intent intent = getIntent();
        String imgUrl = intent.getSerializableExtra("img").toString();

        Picasso.with(getApplicationContext()).load(Uri.parse(imgUrl)).into(img);

        // load the animation
        animSequential = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.sequential);

        // set animation listener
        animSequential.setAnimationListener(this);

        img.startAnimation(animSequential);
        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                finish();
                return false;
            }
        });
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
