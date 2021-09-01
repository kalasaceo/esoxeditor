package com.daasuu.camerarecorder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.kalasa.library.R;
public class LandscapeActivity extends BaseCameraActivity {
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, LandscapeActivity.class);
        activity.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscape);
        //onCreateActivity();
        videoWidth = 720;
        videoHeight = 1280;
        cameraWidth = 720;
        cameraHeight = 1280;
    }
}