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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.androidadvance.topsnackbar.TSnackbar;
import com.google.android.material.snackbar.Snackbar;
import com.kalasa.library.R;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class ExtraOptionsActivity extends AppCompatActivity {
    private static final int ADD_UPDATE_MENU_REQUEST_CODE = 2691;
    private static final int ADD_DATE_REQUEST_CODE = 2147;
    private String opt_choosed = "0";
    private String opt_choosedtext1 = "empty";
    private String opt_choosedtext2 = "empty";
    private String opt_choosedtext3 = "empty";
    private String opt_choosedtext4 = "empty";
    private String opt_choosedtext5 = "empty";
    private TextView opt1;
    private TextView opt2;
    private TextView opt3;
    private TextView opt4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_options);
        Intent data = this.getIntent();
        String isFirst = data.getStringExtra("FirstTime");
        String choosedtext = data.getStringExtra("Opt");
        String[] aarr = choosedtext.split("--");
        opt_choosedtext1 = aarr[0];
        opt_choosedtext2 = aarr[1];
        opt_choosedtext3 = aarr[2];
        opt_choosedtext4 = aarr[3];
        opt_choosedtext5 = aarr[4];
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        CustomSnackbar();
        CustomStatus();
        opt1 = findViewById(R.id.t_opt1);
        opt1.setText(formatDate(opt_choosedtext1));
        opt2 = findViewById(R.id.t_opt2);
        opt2.setText(opt_choosedtext2);
        opt3 = findViewById(R.id.t_opt3);
        opt3.setText(opt_choosedtext3);
        opt4 = findViewById(R.id.t_opt4);
        opt4.setText(opt_choosedtext4);
        SetProfileImage();
        findViewById(R.id.btn_logoutmenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opt_choosed = "1";
                finish();
            }
        });
        findViewById(R.id.btn_deleteaccmenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opt_choosed = "2";
                finish();
            }
        });
        findViewById(R.id.btn_ratemenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opt_choosed = "3";
                finish();
            }
        });
        findViewById(R.id.btn_comp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opt_choosed = "4";
                finish();
            }
        });
        findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opt_choosed = "5";
                finish();
            }
        });
        findViewById(R.id.btn_qrcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opt_choosed = "6";
                finish();
            }
        });
        findViewById(R.id.btn_resetpwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opt_choosed = "7";
                finish();
            }
        });
        findViewById(R.id.btn_chnagelan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opt_choosed = "8";
                finish();
            }
        });
        findViewById(R.id.btn_d1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetDate();
            }
        });
        findViewById(R.id.btn_d2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile("2");
            }
        });
        findViewById(R.id.btn_d3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile("3");
            }
        });
        findViewById(R.id.btn_d4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile("4");
            }
        });
    }

    void UpdateProfile(String update_option) {
        Intent intent = new Intent(this, UpdateProfileActivity.class);
        intent.putExtra("UpdateOption", update_option);
        startActivityForResult(intent, ADD_UPDATE_MENU_REQUEST_CODE);
    }

    void SetProfileImage() {
        ImageView ppic = findViewById(R.id.ppic_texmenu);
        String path = "/storage/emulated/0/Movies/.required/ppic.png";
        if (new File(path).exists()) {
            ppic.setImageBitmap(getRoundBitmap(BitmapFactory.decodeFile(path)));
        }
        /*InputStream imageStream = getContentResolver().openInputStream(Uri.parse(path));
        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        ppic.setImageBitmap(selectedImage);
            SetProfileImage();
            Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "untrue", Toast.LENGTH_SHORT).show();
        }*/
    }

    void CustomStatus() {
        ImageView img_st = findViewById(R.id.img_status);
        switch (opt_choosedtext5) {
            case "Single":
                img_st.setImageResource(R.drawable.st1);
                break;
            case "Married":
                img_st.setImageResource(R.drawable.st2);
                break;
            case "Divorced":
                img_st.setImageResource(R.drawable.st3);
                break;
            case "Engaged":
                img_st.setImageResource(R.drawable.st4);
                break;
        }
    }

    public static Bitmap getRoundBitmap(Bitmap bitmap) {
        int min = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap bitmapRounded = Bitmap.createBitmap(min, min, bitmap.getConfig());
        Canvas canvas = new Canvas(bitmapRounded);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0.0f, 0.0f, min, min)), min / 2, min / 2, paint);
        return bitmapRounded;
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
        data.putExtra("Choosed", opt_choosed);
        data.putExtra("ChoosedText", opt_choosedtext1 + "*" + opt_choosedtext2 + "*" + opt_choosedtext3 + "*" + opt_choosedtext4 + "*" + opt_choosedtext5);
        this.setResult(RESULT_OK, data);
        super.finish();
    }

    void CustomSnackbar() {
        TSnackbar snackbar = TSnackbar
                .make(getWindow().getDecorView().getRootView(), "Your Profile is incomplete.Kindly update your Profile", TSnackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setIconLeft(R.drawable.user, 37);
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

    void SetDate() {
        Intent intent = new Intent(this, CustomDateActivity.class);
        startActivityForResult(intent, ADD_DATE_REQUEST_CODE);
    }

    void SendFinish(String strs, String strstext) {
        switch (Integer.valueOf(strs)) {
            case 1:
                opt_choosedtext1 = strstext;
                opt1.setText(formatDate(opt_choosedtext1));
                break;
            case 2:
                opt_choosedtext2 = strstext;
                opt2.setText(opt_choosedtext2);
                break;
            case 3:
                opt_choosedtext3 = strstext;
                opt3.setText(opt_choosedtext3);
                break;
            case 4:
                opt_choosedtext4 = strstext;
                opt4.setText(opt_choosedtext4);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_UPDATE_MENU_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data.hasExtra("UpdateChoosed")) {
                if (data.getExtras().getString("UpdateChoosed") != "empty") {
                    SendFinish(data.getExtras().getString("UpdateNum"), data.getExtras().getString("UpdateChoosed"));
                }
            }
        } else if (requestCode == ADD_DATE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data.hasExtra("DatePicked")) {
                opt_choosedtext1 = data.getExtras().getString("DatePicked");
                opt1.setText(formatDate(opt_choosedtext1));
            }
        }
    }

    public String formatDate(String date) {
        if(date.contains("-"))
        {
        String[] x = date.split("-");
        String month = "";
        String day=x[1];
        if(Integer.parseInt(day)<10)
        {
            day="0"+day;
        }
        String year=x[2];
        int a1= Integer.parseInt(x[1])+1;
        String a = String.valueOf(a1);
        switch (a) {
            case "1":
                month = "January";
                break;
            case "2":
                month = "February";
                break;
            case "3":
                month = "March";
                break;
            case "4":
                month = "April";
                break;
            case "5":
                month = "May";
                break;
            case "6":
                month = "June";
                break;
            case "7":
                month = "July";
                break;
            case "8":
                month = "August";
                break;
            case "9":
                month = "September";
                break;
            case "10":
                month = "October";
                break;
            case "11":
                month = "November";
                break;
            case "12":
                month = "Decemeber";
                break;
        }
        return day+", "+month+" "+year;
    }
        else
        {
            return "Kindly fill out your Date of Birth";
        }
    }
}