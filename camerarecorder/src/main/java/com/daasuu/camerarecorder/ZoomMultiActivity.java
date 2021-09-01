package com.daasuu.camerarecorder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kalasa.library.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class ZoomMultiActivity extends AppCompatActivity {
    String in_link="empty";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_multizoom);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent data = this.getIntent();
        in_link = data.getStringExtra("link");
        String photo_likes = data.getStringExtra("likes");
        String photo_views = data.getStringExtra("views");
        String photo_height = data.getStringExtra("height");
        String photo_width = data.getStringExtra("height");
        LinearLayout out1 = findViewById(R.id.out1);
        TextView t1=findViewById(R.id.imgb2_t);
        TextView t2=findViewById(R.id.imgb3_t);
        t1.setText(photo_likes);
        t2.setText(photo_views);
        String[] s1 = in_link.split("~~");
        String t = s1[1];
        String[] s2 = t.split("!!");
        for (int j = 0; j < s2.length; j++) {
            String[] s3 = s2[j].split("=");
            ImageView photo = new ImageView(this);

            out1.addView(photo);
            Picasso picasso = Picasso.with(this);
            float x1 = Float.parseFloat(s3[1]);
            float y1 = x1 * Float.valueOf(photo_height);
            String h = String.valueOf(y1);
            photo.getLayoutParams().height = (int) Float.parseFloat(h);
            photo.getLayoutParams().width = Integer.valueOf(photo_width);
            photo.requestLayout();
            String this_link="https://s3.ap-south-1.amazonaws.com/toasterco.com/Posts/"+s3[0]+".png";
            RequestCreator requestCreator = picasso.load(this_link);
            requestCreator.into(photo);
            photo.getLayoutParams().height = (int) Float.parseFloat(h);
            photo.getLayoutParams().width = Integer.valueOf(photo_width);
            photo.setScaleType(ImageView.ScaleType.FIT_XY);
            photo.requestLayout();
        }
        findViewById(R.id.opt_b1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikeFunc();
            }
        });
        findViewById(R.id.opt_b2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentFunc();
            }
        });
        findViewById(R.id.opt_b3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareFunc();
            }
        });
    }
    void LikeFunc()
    {
        Intent intent = new Intent(this, LikeActivity.class);
        startActivity(intent);
    }
    void CommentFunc()
    {

    }
    void ShareFunc()
    {

    }
}