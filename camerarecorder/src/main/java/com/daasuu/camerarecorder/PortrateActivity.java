package com.daasuu.camerarecorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.kalasa.library.R;
public class PortrateActivity extends BaseCameraActivity {
    private static final int ADD_BASE_REQUEST_CODE = 3789;
    public void startActivity(Activity activity) {
        Intent intent = new Intent(activity, BaseCameraActivity.class);
        startActivityForResult(intent, ADD_BASE_REQUEST_CODE);
        //activity.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portrate);
        //onCreateActivity();
        videoWidth = 1080;
        videoHeight = 1920;
        cameraWidth = 1080;
        cameraHeight = 1920;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
