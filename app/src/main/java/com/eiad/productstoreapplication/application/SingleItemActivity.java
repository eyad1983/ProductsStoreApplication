package com.eiad.productstoreapplication.application;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eiad.productstoreapplication.R;
import com.eiad.productstoreapplication.util.ListItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

public class SingleItemActivity extends AppCompatActivity implements View.OnClickListener {

    ListItem product;
    ImageView img;
    TextView title;
    TextView desc;
    Button buyBtn;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item);

        Intent intent = getIntent();

        product = (ListItem) intent.getSerializableExtra("product");

        img = (ImageView) findViewById(R.id.pic);
        title = (TextView) findViewById(R.id.name);
        desc = (TextView) findViewById(R.id.description);
        buyBtn = (Button) findViewById(R.id.buyBtn);

        initFields();

        buyBtn.setOnClickListener(this);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                Intent nextPageIntent = new Intent(getApplicationContext(), AnimationActivity.class);
                nextPageIntent.putExtra("img", product.getImg());
                startActivity(nextPageIntent);
                return false;
            }
        });
    }

    private void initFields() {
        Picasso.with(getApplicationContext()).load(Uri.parse(product.getImg())).into(img);
        title.setText(product.getName());
        desc.setText(product.getDescription());
    }

    @Override
    public void onClick(View v) {

        Intent nextPageIntent = new Intent(getApplicationContext(), ItemViewOnWebActivity.class);
        nextPageIntent.putExtra("url", product.getProductUrl());
        startActivity(nextPageIntent);

    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
