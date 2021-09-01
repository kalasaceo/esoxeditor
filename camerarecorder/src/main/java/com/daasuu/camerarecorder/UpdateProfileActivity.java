package com.daasuu.camerarecorder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidadvance.topsnackbar.TSnackbar;
import com.kalasa.library.R;

import java.io.File;
public class UpdateProfileActivity extends AppCompatActivity {
    private static final int ADD_UPDATE_MENU_REQUEST_CODE =2691 ;
    private String update_choosed="empty";
    private String update_num="empty";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editmenu0);
        overridePendingTransition(R.anim.activity_slider_in_right, R.anim.activity_slider_out_left);
        Intent data=this.getIntent();
        update_num=data.getStringExtra("UpdateOption");
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        findViewById(R.id.btn_selectedmenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.top_in, R.anim.top_out);
            }
        });
        TextView tx=findViewById(R.id.op_str);
        switch(Integer.valueOf(update_num)){
            case 1:
                tx.setHint("Date of Birth here");
                break;
            case 2:
                tx.setHint("HomeTown");
                break;
            case 3:
                tx.setHint("Hobby");
                break;
            case 4:
                tx.setHint("Works At");
                break;
            default:
                break;
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.activity_slider_in_right, R.anim.activity_slider_out_left);
    }
    @Override
    public void finish() {
        Intent data = new Intent();
        TextView xx=findViewById(R.id.op_str);
        if(xx.getText().length()>1)
        {
            update_choosed=xx.getText().toString();
        }
        data.putExtra("UpdateChoosed", update_choosed);
        data.putExtra("UpdateNum", update_num);
        this.setResult(RESULT_OK, data);
        super.finish();
    }
}