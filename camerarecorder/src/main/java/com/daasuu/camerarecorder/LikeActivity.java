package com.daasuu.camerarecorder;
import android.animation.ObjectAnimator;
import android.content.Intent;
import com.kalasa.library.R;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
public class LikeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likezero);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                overridePendingTransition(0,0);
                finish();
                overridePendingTransition(0,0);
            }
        }, 1400);

    }
}
