package com.daasuu.camerarecorder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kalasa.library.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
public class ZoomActivity extends AppCompatActivity {
    String photo_link="empty";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_zoom);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent data=this.getIntent();
        photo_link=data.getStringExtra("link");
        String photo_likes=data.getStringExtra("likes");
        String photo_views=data.getStringExtra("views");
        String photo_height=data.getStringExtra("height");
        ImageView photo=findViewById(R.id.img_photo);
        Picasso picasso = Picasso.with(this);
        photo.getLayoutParams().height= (int) Float.parseFloat(photo_height);
        photo.requestLayout();
        RequestCreator requestCreator = picasso.load(photo_link);
        requestCreator.into(photo);
        photo.setScaleType(ImageView.ScaleType.FIT_XY);
        photo.getLayoutParams().height= (int) Float.parseFloat(photo_height);
        photo.requestLayout();
        TextView t1=findViewById(R.id.imgb2_t);
        TextView t2=findViewById(R.id.imgb3_t);
        t1.setText(photo_likes);
        t2.setText(photo_views);
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