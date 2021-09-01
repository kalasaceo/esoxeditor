package com.daasuu.sample;
import android.content.Intent;

import com.daasuu.camerarecorder.BaseCameraActivity;
import com.daasuu.camerarecorder.CollageActivity;
import com.daasuu.camerarecorder.MainFrameActivity;
import com.daasuu.camerarecorder.MessageActivity;
import com.daasuu.camerarecorder.SearchGiffActivity;
import com.daasuu.camerarecorder.StatusOptionsActivity;
import com.daasuu.camerarecorder.StoryVideoActivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 88888;
    private static final int ADD_MAIN_REQUEST_CODE = 4444;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra("Chats", "how are you-*-0%%%%i am fine how are you bro so this will be much bigger than you expected it to woth all the worth i will put into it to beat afacebook and twitter-*-1%%%%justdoing fine thnak sofr asking-*-1%%%%thanks for asking man-*-0");
        intent.putExtra("Name", "Rowan Apte");
        Intent intent = new Intent(this, VideoRecyclerActivity.class);
        intent.putExtra("Collarge", "54QQ77AQQES<-->079974AF490");
        intent.putExtra("Password", "@#aaaa#@");
        intent.putExtra("CoUserName", "@#aaaa#@");
        startActivityForResult(intent, ADD_MAIN_REQUEST_CODE);*/
        Intent intent = new Intent(this, BaseCameraActivity.class);
        startActivityForResult(intent, ADD_MAIN_REQUEST_CODE);
    }
    @Override
    public void onBackPressed() {
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_MAIN_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data.hasExtra("framepath")) {
                    Toast.makeText(this, "framepath"+data.getExtras().getString("framepath"), Toast.LENGTH_SHORT).show();
            }
        }
    }
}