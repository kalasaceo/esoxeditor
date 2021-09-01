package com.daasuu.camerarecorder;

import android.content.Intent;
import android.os.Bundle;
import com.kalasa.library.R;
import androidx.appcompat.app.AppCompatActivity;

public class PopupMenuAcitivty extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_info_pop_up);
    }
    public void onBackPressed(){
        overridePendingTransition(R.anim.activity_slider_in_right, R.anim.activity_slider_out_left);
        finish();
        overridePendingTransition(R.anim.activity_slider_in_right, R.anim.activity_slider_out_left);
    }
    @Override
    public void finish() {
        String option="none";
        Intent data = new Intent();
        data.putExtra("OptionChoosed", option);
        this.setResult(RESULT_OK, data);
        super.finish();
    }
}