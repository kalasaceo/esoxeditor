package com.daasuu.camerarecorder;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.danikula.videocache.HttpProxyCacheServer;
import com.kalasa.library.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class ZoomVideoActivity extends AppCompatActivity {
    String video_link="empty";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_videozoom);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent data=this.getIntent();
        video_link=data.getStringExtra("link");
        String photo_likes=data.getStringExtra("likes");
        String photo_views=data.getStringExtra("views");
        String video_height=data.getStringExtra("height");
        VideoView zoom_video=findViewById(R.id.zoom_video);
        zoom_video.getLayoutParams().height=(int) Float.parseFloat(video_height);
        MediaPlayer mediaPlayer;
        try {
            //mediaPlayer = new MediaPlayer();
            HttpProxyCacheServer proxy = App.getProxy(this);
            String proxyUrl = proxy.getProxyUrl(video_link);
            //mediaPlayer.setDataSource(proxyUrl);
            zoom_video.setVideoPath(video_link);
            //mediaPlayer.prepareAsync();
            zoom_video.start();
            //mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView t1=findViewById(R.id.imgb2_t);
        TextView t2=findViewById(R.id.imgb3_t);
        t1.setText(photo_likes);
        t2.setText(photo_views);
        findViewById(R.id.v_opt_b1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikeFunc();
            }
        });
        findViewById(R.id.v_opt_b2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentFunc();
            }
        });
        findViewById(R.id.v_opt_b3).setOnClickListener(new View.OnClickListener() {
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