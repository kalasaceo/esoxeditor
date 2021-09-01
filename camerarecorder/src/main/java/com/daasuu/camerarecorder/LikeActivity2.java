package com.daasuu.camerarecorder;

import android.os.Bundle;
import android.os.Handler;
import com.kalasa.library.R;
import androidx.appcompat.app.AppCompatActivity;

public class LikeActivity2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likezero);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                overridePendingTransition(R.anim.activity_slider_in_right, R.anim.activity_slider_out_left);
                finish();
                overridePendingTransition(R.anim.activity_slider_in_right, R.anim.activity_slider_out_left);
            }
        }, 1400);

    }
}
