package com.daasuu.camerarecorder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import com.danikula.videocache.HttpProxyCacheServer;
import com.kalasa.library.R;
public class StoryVideoActivity extends AppCompatActivity {
    private String video_link="false";
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private String dura="0";
    private String output="empty";
    private int p_value=4;
    VideoView zoom_video=null;
    private Boolean isDuration=false;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_storyvideo);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        zoom_video=findViewById(R.id.story_video);
        Intent data=this.getIntent();
        video_link=data.getStringExtra("options");
        String[] ar=video_link.split("--");
        String photo_likes=ar[2];
        String photo_views=ar[3];
        String video_height=ar[1];
        String sv=ar[0];
        ScreenDensity(video_height,sv);
        zoom_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });
        TextView t=findViewById(R.id.story_infos);
        String v="Got "+photo_likes+" likes , "+photo_views+" views";
        t.setText(v);
        findViewById(R.id.vs_opt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikeFunc();
            }
        });
        findViewById(R.id.vs_opt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentFunc();
            }
        });
        findViewById(R.id.vs_opt3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareFunc();
            }
        });
        ImageButton img=findViewById(R.id.btn_createstory);
        Animation animRotateAclk = android.view.animation.AnimationUtils.loadAnimation(getApplicationContext(),R.anim.activity_rotate);
        img.startAnimation(animRotateAclk);
        findViewById(R.id.btn_createstory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                output="true";
                finish();
            }
        });
    }
    public void UpdateProgress()
    {
        progressBar = (ProgressBar) findViewById(R.id.story_interval);
        progressBar.setOutlineSpotShadowColor(Color.WHITE);
        progressBar.setOutlineAmbientShadowColor(Color.WHITE);
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus =progressStatus+ p_value;
                    handler.post(new Runnable() {
                        public void run() {
                                progressBar.setProgress(progressStatus);
                                String dura= String.valueOf(zoom_video.getDuration());
                            if(!isDuration) {
                                dura = String.valueOf(zoom_video.getDuration());
                                int i = Integer.parseInt(dura);
                                if (i > 0) {
                                    isDuration = true;
                                    p_value=100/(Integer.parseInt(dura)/1000);
                                }
                            }
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    private void ScreenDensity(String video_height,String this_link)
    {
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outmetrics = new DisplayMetrics();
        Display display = wm.getDefaultDisplay();
        display.getMetrics(outmetrics);
        int this_height = outmetrics.heightPixels/2;
        float x= Float.parseFloat(video_height);
        float h=x*this_height;
        zoom_video.getLayoutParams().height= (int) h;
        MediaPlayer mediaPlayer=null;
        try {
            HttpProxyCacheServer proxy = App.getProxy(this);
            String proxyUrl = proxy.getProxyUrl(this_link);
            zoom_video.setVideoPath(this_link);
            zoom_video.start();
            UpdateProgress();
            Toast.makeText(this, "started"+this_link, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("create", output);
        this.setResult(RESULT_OK, data);
        super.finish();
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