package com.daasuu.camerarecorder;

import android.app.DatePickerDialog;
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
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidadvance.topsnackbar.TSnackbar;
import com.kalasa.library.R;

import java.io.File;
import java.util.Calendar;

public class CustomDateActivity extends AppCompatActivity {
    private static final int ADD_UPDATE_MENU_REQUEST_CODE = 2691;
    String enddate="empty";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        overridePendingTransition(R.anim.activity_slider_in_right, R.anim.activity_slider_out_left);
        DatePicker dp = findViewById(R.id.datePicker1);
        findViewById(R.id.btn_setdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enddate=String.valueOf(dp.getDayOfMonth())+"-"+String.valueOf(dp.getMonth())+"-"+String.valueOf(dp.getYear());
                finish();
                overridePendingTransition(R.anim.activity_slider_in_right, R.anim.activity_slider_out_left);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_slider_in_right, R.anim.activity_slider_out_left);
        finish();
    }
    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("DatePicked", enddate);
        this.setResult(RESULT_OK, data);
        super.finish();
    }
}