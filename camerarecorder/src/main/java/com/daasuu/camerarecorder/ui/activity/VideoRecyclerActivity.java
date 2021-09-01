package com.daasuu.camerarecorder.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.daasuu.camerarecorder.AutoPlayVideoRecyclerView;
import com.daasuu.camerarecorder.Chat;
import com.daasuu.camerarecorder.LikeActivity;
import com.daasuu.camerarecorder.MessageAdapter;
import com.daasuu.camerarecorder.PopupMenuAcitivty;
import com.kalasa.library.R;
import com.daasuu.camerarecorder.mode.Feed;
import com.daasuu.camerarecorder.mode.Photo;
import com.daasuu.camerarecorder.mode.Video;
import com.daasuu.camerarecorder.ui.adapter.BaseAdapter;
import com.daasuu.camerarecorder.ui.adapter.FeedAdapter;
import com.daasuu.camerarecorder.ui.view.CenterLayoutManager;

public class VideoRecyclerActivity extends AppCompatActivity {
    public AutoPlayVideoRecyclerView listFeed;
    private FeedAdapter adapter;
    public static String cids = "fj";
    String allCollarge="empty";
    public static String cusernames = "fj";
    public static String cpasswords = "fj";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listFeed=findViewById(R.id.listFeed);
        setContentView(R.layout.activity_videorecycler);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent data=this.getIntent();
        allCollarge=data.getStringExtra("Collarge");
        cids=data.getStringExtra("Collarge");
        cpasswords=data.getStringExtra("Password");
        cusernames=data.getStringExtra("CoUserName");
        initView();
    }

    private void initView() {
        AutoPlayVideoRecyclerView listFeed=findViewById(R.id.listFeed);
        adapter = new FeedAdapter(this);
        listFeed.setLayoutManager(new CenterLayoutManager(this));
        listFeed.setAdapter(adapter);
        initData();
    }

    private void initData() {
        String[] allchats_single = allCollarge.split("<-->");
        for (int i = 0; i < allchats_single.length; i++) {
            adapter.add(new Feed(new Video("https://s3.ap-south-1.amazonaws.com/toasterco.com/Posts/"+allchats_single[i]+"-00001.png",
                    "https://s3.ap-south-1.amazonaws.com/toasterco.com/Posts/"+allchats_single[i]+".mp4",
                    0), Feed.Model.M2));
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        listFeed=findViewById(R.id.listFeed);
        if (listFeed.getHandingVideoHolder() != null) listFeed.getHandingVideoHolder().playVideo();
    }
    @Override
    protected void onPause() {
        super.onPause();
        listFeed=findViewById(R.id.listFeed);
        if (listFeed.getHandingVideoHolder() != null) listFeed.getHandingVideoHolder().stopVideo();
    }
    @Override
    public void finish() {
        String replier="empty";
        Intent data = new Intent();
        data.putExtra("ReplyArray",replier);
        this.setResult(RESULT_OK, data);
        super.finish();
    }
}