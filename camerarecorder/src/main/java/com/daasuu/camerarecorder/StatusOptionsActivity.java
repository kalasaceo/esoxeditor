package com.daasuu.camerarecorder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidadvance.topsnackbar.TSnackbar;
import com.kalasa.library.R;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StatusOptionsActivity extends AppCompatActivity {
    private static final int ADD_PPIC_CROP_REQUEST_CODE = 2316;
    boolean isLocked = false;
    private String result1 = "empty";
    private String result2 = "empty";
    private String result5 = "0";
    private String opt_choosedtext2 = "empty";
    private String opt_choosedtext3 = "empty";
    private String opt_choosedtext4 = "empty";
    private String opt_choosedstatus = "Your Status for Today";
    private static final String QUOTE = "proud to be indian";
    private Animation animation;
    private Paint gPaint;
    private Paint cPaint;
    private Path glowCircle;
    private Path circle;
    private Paint tPaint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_statusoption);
        //Intent data=this.getIntent();
        //String isFirst=data.getStringExtra("FirstTime");
        //String choosedtext=data.getStringExtra("Opt");
        //String[] aarr = choosedtext.split("--");
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        CustomSnackbar();
        SetProfileImage();
        Intent data = this.getIntent();
        String choosedtext = data.getStringExtra("Opt");
        String[] aarr = choosedtext.split("--");
        opt_choosedtext2 = aarr[0];
        opt_choosedtext3 = aarr[1];
        opt_choosedtext4 = aarr[2];
        opt_choosedstatus = aarr[3];
        EditText stext = findViewById(R.id.mystatus);
        stext.setText(opt_choosedstatus);
        SetFields(opt_choosedtext2, opt_choosedtext3, opt_choosedtext4);
        TextView status_text = findViewById(R.id.status2);
        ImageView img_lock = findViewById(R.id.img_status2);
        findViewById(R.id.btn_status1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartCropping();
            }
        });
        findViewById(R.id.btn_verifyacc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result5 = "1";
                finish();
            }
        });
        findViewById(R.id.btn_status2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLocked == false) {
                    result2 = "true";
                    status_text.setText("Make Profile Visible to all");
                    Toast.makeText(StatusOptionsActivity.this, "Your profile is now Private", Toast.LENGTH_SHORT).show();
                    img_lock.setImageResource(R.drawable.newlockon);
                    isLocked = true;
                } else {
                    result2 = "false";
                    status_text.setText("Make Profile Private");
                    Toast.makeText(StatusOptionsActivity.this, "Your profile is now Public", Toast.LENGTH_SHORT).show();
                    img_lock.setImageResource(R.drawable.newlockoff);
                    isLocked = false;
                }
            }
        });
    }

    void StartCropping() {
        Intent intent = new Intent(this, ProfileCropper.class);
        startActivityForResult(intent, ADD_PPIC_CROP_REQUEST_CODE);
    }
    void SetProfileImage() {
        ImageView ppic = findViewById(R.id.sppic_texmenu);
        String path = "/storage/emulated/0/Movies/.required/ppic.png";
        if (new File(path).exists()) {
            ppic.setImageBitmap(getRoundBitmapWithText(BitmapFactory.decodeFile(path),66f));
        }
    }
    void UpdateProfileImage() {
        ImageView ppic = findViewById(R.id.sppic_texmenu);
        String path = "/storage/emulated/0/Movies/.required/testcropped_ppic.png";
        if (new File(path).exists()) {
            ppic.setImageBitmap(getRoundBitmapWithText(BitmapFactory.decodeFile(path),122f));
        }
    }
    public Bitmap getRoundBitmap(Bitmap bitmap) {
        int min = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap bitmapRounded = Bitmap.createBitmap(min, min, bitmap.getConfig());
        Canvas canvas = new Canvas(bitmapRounded);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Path path = new Path();
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0.0f, 0.0f, min, min)), min / 2, min / 2, paint);
        paint.setColor(Color.GREEN);
        path.addArc((new RectF(0.0f, 0.0f, min, min)), 94.28f, -64.28f);
        canvas.drawTextOnPath("FERTILE WINDOW", path, 0f, 0f, paint);
        return bitmapRounded;
    }
    public Bitmap getRoundBitmapWithText(Bitmap bitmap,float FontSize)
    {
        int min = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap bitmapRounded = Bitmap.createBitmap(min, min, bitmap.getConfig());
        Canvas canvas = new Canvas(bitmapRounded);
        gPaint = new Paint();
        gPaint.setAlpha(255);
        gPaint.setShadowLayer(40, 0, 0, Color.argb(200, 255, 0, 0));
        cPaint = new Paint();
        cPaint.setAntiAlias(true);
        cPaint.setDither(true);
        BlurMaskFilter filter = new BlurMaskFilter(10f, BlurMaskFilter.Blur.INNER);
        cPaint.setMaskFilter(filter);
        int x = min/2-20;
        int y = min/2-20;
        int r = min/2-20;
        glowCircle = new Path();
        glowCircle.addCircle(x, y, r, Path.Direction.CW);
        int color1 = Color.parseColor("#FF9933");
        int color2 = Color.parseColor("#138808");
        LinearGradient gradient = new LinearGradient(0, 0, 0, y*2, color2, color1, Shader.TileMode.REPEAT);
        cPaint.setShader(gradient);
        //cPaint.setColor(Color.WHITE);
        circle = new Path();
        circle.addCircle(x, y, r, Path.Direction.CW);
        tPaint = new Paint();
        tPaint.setTextSize(min/8);
        tPaint.setTypeface(Typeface.DEFAULT);
        tPaint.setColor(Color.GREEN);
        tPaint.setAntiAlias(true);
        //canvas.drawPath(glowCircle, gPaint);
        canvas.drawPath(circle, cPaint);
        tPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(60.0f, 60.0f, min-90, min-90)), min / 2, min / 2, tPaint);
        canvas.drawTextOnPath(QUOTE, circle, 30, 30, tPaint);
        return bitmapRounded;
    }
    void SetFields(String strs1,String strs2,String strs3)
    {
        TextView status_text=findViewById(R.id.status2);
        ImageView img_lock=findViewById(R.id.img_status2);
        Switch swt1=findViewById(R.id.switch1);
        Switch swt2=findViewById(R.id.switch2);
        switch (strs1) {
            case "true":
                result2="true";
                status_text.setText("Make Profile Visible to all");
                img_lock.setImageResource(R.drawable.newlockon);
                isLocked=true;
                break;
            case "false":
                result2="false";
                status_text.setText("Make Profile Private");
                img_lock.setImageResource(R.drawable.newlockoff);
                isLocked=false;
                break;
        }
        switch (strs2) {
            case "true":
                swt1.setChecked(true);
                result1="true";
                break;
            case "false":
                swt1.setChecked(false);
                result1="false";
                break;
        }
        switch (strs3) {
            case "true":
                swt2.setChecked(true);
                result2="true";
                break;
            case "false":
                swt2.setChecked(false);
                result2="false";
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_slider_in_right, R.anim.activity_slider_out_left);
        finish();
    }
    @Override
    public void finish() {
        Switch sw1=findViewById(R.id.switch1);
        String result3=String.valueOf(sw1.isChecked());
        Switch sw2=findViewById(R.id.switch2);
        String result4=String.valueOf(sw2.isChecked());
        Intent data = new Intent();
        data.putExtra("Choosed", result1+"--"+result2+"--"+result3+"--"+result4+"--"+result5);
        this.setResult(RESULT_OK, data);
        super.finish();
    }
    void CustomSnackbar() {
        TSnackbar snackbar = TSnackbar
                .make(getWindow().getDecorView().getRootView(), "Kindly Add your Status for today to be seen in Messenger", TSnackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setIconLeft(R.drawable.item_message_check_bg, 37);
        snackbar.setIconPadding(10);
        snackbar.setDuration(TSnackbar.LENGTH_LONG);
        snackbar.setAction("set now", new MyUndoListener());
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.BLACK);
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public class MyUndoListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PPIC_CROP_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data.hasExtra("Status")) {
                    result1=data.getExtras().getString("Status");
                    UpdateProfileImage();
            }
        }
    }
}