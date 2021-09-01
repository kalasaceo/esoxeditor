package com.daasuu.camerarecorder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.kalasa.library.R;
public class PopBubbleActivity extends BaseCameraActivity {
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, PopBubbleActivity.class);
        activity.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_bubble);
        //onCreateActivity();
    }
}