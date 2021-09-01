package com.daasuu.camerarecorder;
import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.GLException;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.kalasa.library.R;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.daasuu.camerarecorder.ui.activity.VideoRecyclerActivity;
import com.daasuu.camerarecorder.widget.Filters;
import com.daasuu.camerarecorder.widget.SampleGLView;
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.github.dhaval2404.colorpicker.model.ColorSwatch;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;
public class BaseCameraActivity extends AppCompatActivity {
    private static final int ADD_VIDEO_WITH_GIF_REQUEST_CODE =2614 ;
    private SampleGLView sampleGLView;
    protected CameraRecorder cameraRecorder;
    public static String filepath="none";
    protected LensFacing lensFacing = LensFacing.BACK;
    protected int cameraWidth = 1080;
    private int max_width=200;
    private int max_height=200;
    private String SingleStickerPath="empty";
    protected int counter_i = 0;
    private boolean bgif=false;
    private MyImageView thisImage=null;
    protected int cameraHeight = 1920;
    protected int videoWidth = 1080;
    public float x_spos = 0;
    private String gifH="empty";
    private String gifW="empty";
    public float y_spos = 0;
    TextView counter=null;
    protected int videoHeight = 1920;
    private String musicpath = "none";
    private String GifPath="empty";
    private String thiscolorHEX = "#000000";
    private boolean ismusicAdded = false;
    private boolean bisText = false;
    private ImageButton trecordBtn=null;
    private TextView tt=null;
    private static final int ADD_STICKER_REQUEST_CODE = 5459;
    private EditText mEdit=null;
    private static final int ADD_OPTION_REQUEST_CODE = 7061;
    private static final int ADD_VOICE_RECORD_REQUEST_CODE = 2035;
    private static final int ADD_TEXT_WM_REQUEST_CODE = 2777;
    private static final int ADD_ATV_ARECORDING_REQUEST_CODE = 1204;
    private static final int ADD_SPEECH_REQUEST_CODE = 2712;
    private static final int ADD_WM_REQUEST_CODE = 3092;
    private static final int ADD_GIFF_BOX_CODE = 2781;
    private RelativeLayout frameLayout;
    public static String vidtextFileName="empty";
    public boolean bUpdate;
    int i=0;
    int j=0;
    int z=0;
    public static final int WRITE_REQUEST_CODE = 101;
    public Canvas tcanvas;
    private AlertDialog filterDialog;
    private boolean toggleClick = false;
    public ProgressBar progressBar;
    public int progressStatus = 0;
    private static String timetopass="10";
    public Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portrate);
        trecordBtn = (ImageButton)findViewById(R.id.btn_trecord);
        findViewById(R.id.btn_flash).setOnClickListener(v -> {
            if (cameraRecorder != null && cameraRecorder.isFlashSupport()) {
                cameraRecorder.switchFlashMode();
                cameraRecorder.changeAutoFocus();
           }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mEdit=findViewById(R.id.text_on_video);
        tt=findViewById(R.id.text_on_video);
        counter=findViewById(R.id.counter);
        trecordBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(i==0) {
                    StartCountdown();
                }
                else
                {
                    StopCountdown();
                }
            }
        });
        findViewById(R.id.btn_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPicker();
            }
        });
        findViewById(R.id.opt3).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //sampleGLView.onPause();
                //cameraRecorder.stop();
                OpenGiffBox();
                /*if(j%2==0) {
                    findViewById(R.id.ssss).setVisibility(View.GONE);
                    j++;
                }
                else {
                    findViewById(R.id.ssss).setVisibility(View.VISIBLE);
                    j++;
                }*/
            }
        });
        EditText dummy =findViewById(R.id.text_on_video);
        dummy.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                if (hasFocus){
                    findViewById(R.id.btn_color).setVisibility(View.VISIBLE);
                }
                else
                {
                    findViewById(R.id.btn_color).setVisibility(View.GONE);
                }
            }
        });
        findViewById(R.id.tovoiceopen).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenSpeechManipulator();
            }
        });
        findViewById(R.id.opt1).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ShowAudios();
            }
        });
        findViewById(R.id.uploadvideo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(R.id.video_proceed).setVisibility(View.GONE);
                exportMp4ToGallery(getApplicationContext(), filepath);
                bUpdate=false;
                finish();
            }
        });
        findViewById(R.id.btn_switch_camera).setOnClickListener(v -> {
            releaseCamera();
            if (lensFacing == LensFacing.BACK) {
                lensFacing = LensFacing.FRONT;
            } else {
                lensFacing = LensFacing.BACK;
            }
            toggleClick = true;
        });
        findViewById(R.id.opt2).setOnClickListener(v -> {
                if(z%2==0) {
                    findViewById(R.id.text_on_video).setVisibility(View.GONE);
                    bisText=false;
                    z++;
                }
                else
                {
                    findViewById(R.id.text_on_video).setVisibility(View.VISIBLE);
                    bisText=true;
                    z++;
                }
        });
        findViewById(R.id.opt4).setOnClickListener(v -> {
            findViewById(R.id.horiscro).setVisibility(View.VISIBLE);
            ObjectAnimator animation = ObjectAnimator.ofFloat(findViewById(R.id.horiscro), "translationY", -10f);
            animation.setDuration(250);
            animation.start();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ObjectAnimator animation = ObjectAnimator.ofFloat(findViewById(R.id.horiscro), "translationY", 700f);
                    animation.setDuration(250);
                    animation.start();
                }
            }, 5000);
        });
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox OptionChoosen_1=(CheckBox) findViewById(R.id.check_option1);
                CheckBox OptionChoosen_2=(CheckBox) findViewById(R.id.check_option2);
                CheckBox OptionChoosen_3=(CheckBox) findViewById(R.id.check_option3);
                if(OptionChoosen_1.isChecked() && OptionChoosen_2.isChecked() && OptionChoosen_3.isChecked())
                {
                    findViewById(R.id.video_proceed).setVisibility(View.GONE);
                    counter_i=0;
                    counter.setText(String.valueOf("00:00"));
                    bUpdate=true;

                }
                if(OptionChoosen_1.isChecked())
                {
                    findViewById(R.id.video_proceed).setVisibility(View.GONE);
                    progressStatus=0;
                    bUpdate=false;
                }
                if(OptionChoosen_3.isChecked())
                {
                    findViewById(R.id.video_proceed).setVisibility(View.GONE);
                    exportMp4ToGallery(getApplicationContext(), filepath);
                    bUpdate=false;
                    startCustomisingVideo();
                }
                if(OptionChoosen_2.isChecked())
                {
                    findViewById(R.id.video_proceed).setVisibility(View.GONE);
                    exportMp4ToGallery(getApplicationContext(), filepath);
                    bUpdate=false;
                    String text_on_video=mEdit.getText().toString();
                    if(ismusicAdded==true)
                    {
                        AddVoicetoThisVideo();
                    }
                    else
                    {
                        CompleteProcess();
                    }
                }
            }
        });
        findViewById(R.id.bFilter1).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeFilter(Filters.INVERT);
            }
        });
        findViewById(R.id.bFilter2).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeFilter(Filters.BULGE_DISTORTION);
            }
        });
        findViewById(R.id.bFilter3).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeFilter(Filters.INVERT);
            }
        });
        findViewById(R.id.bFilter4).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeFilter(Filters.BILATERAL);
            }
        });
        findViewById(R.id.bFilter5).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeFilter(Filters.BOX_BLUR);
            }
        });
        findViewById(R.id.bFilter6).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeFilter(Filters.FILTER_GROUP);
            }
        });
        findViewById(R.id.bFilter7).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeFilter(Filters.GAUSSIAN_BLUR);
            }
        }); findViewById(R.id.bFilter8).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeFilter(Filters.GLAY_SCALE);
            }
        });
        findViewById(R.id.bFilter9).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeFilter(Filters.MONOCHROME);
            }
        });
        findViewById(R.id.bFilter10).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeFilter(Filters.OVERLAY);
            }
        });
        findViewById(R.id.bFilter11).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeFilter(Filters.SEPIA);
            }
        }); findViewById(R.id.bFilter12).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeFilter(Filters.SHARPEN);
            }
        });
        findViewById(R.id.bFilter13).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeFilter(Filters.SPHERE_REFRACTION);
            }
        }); findViewById(R.id.bFilter14).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeFilter(Filters.TONE);
            }
        });
        findViewById(R.id.bFilter15).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeFilter(Filters.TONE_CURVE);
            }
        });
        findViewById(R.id.bFilter16).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeFilter(Filters.WEAKPIXELINCLUSION);
            }
        });
        findViewById(R.id.sticker1).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddStickers(R.drawable.emoti1);
                SingleStickerPath="/storage/emulated/0/Movies/.required/emoti1.png";
            }
        });
        findViewById(R.id.sticker2).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddStickers(R.drawable.emoti2);
                SingleStickerPath="/storage/emulated/0/Movies/.required/emoti2.png";
            }
        });
        findViewById(R.id.sticker3).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddStickers(R.drawable.emoti3);
                SingleStickerPath="/storage/emulated/0/Movies/.required/emoti3.png";
            }
        });
        findViewById(R.id.sticker4).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddStickers(R.drawable.emoti4);
                SingleStickerPath="/storage/emulated/0/Movies/.required/emoti4.png";
            }
        });
        findViewById(R.id.sticker5).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddStickers(R.drawable.emoti5);
                SingleStickerPath="/storage/emulated/0/Movies/.required/emoti5.png";
            }
        });
        findViewById(R.id.sticker6).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddStickers(R.drawable.emoti6);
                SingleStickerPath="/storage/emulated/0/Movies/.required/emoti6.png";
            }
        });
        findViewById(R.id.sticker7).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddStickers(R.drawable.emoti7);
                SingleStickerPath="/storage/emulated/0/Movies/.required/emoti7.png";
            }
        });
        findViewById(R.id.sticker8).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddStickers(R.drawable.emoti8);
                SingleStickerPath="/storage/emulated/0/Movies/.required/emoti8.png";
            }
        });
        findViewById(R.id.sticker9).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddStickers(R.drawable.emoti9);
                SingleStickerPath="/storage/emulated/0/Movies/.required/emoti9.png";
            }
        });
        findViewById(R.id.sticker10).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddStickers(R.drawable.emoti10);
                SingleStickerPath="/storage/emulated/0/Movies/.required/emoti10png";
            }
        });
        findViewById(R.id.sticker11).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddStickers(R.drawable.emoti11);
                SingleStickerPath="/storage/emulated/0/Movies/.required/emoti11.png";
            }
        });
        findViewById(R.id.sticker12).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddStickers(R.drawable.emoti12);
                SingleStickerPath="/storage/emulated/0/Movies/.required/emoti12.png";
            }
        }); findViewById(R.id.sticker13).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddStickers(R.drawable.emoti13);
                SingleStickerPath="/storage/emulated/0/Movies/.required/emoti13.png";
            }
        }); findViewById(R.id.sticker14).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddStickers(R.drawable.emoti14);
                SingleStickerPath="/storage/emulated/0/Movies/.required/emoti14.png";
            }
        });
        findViewById(R.id.sticker15).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddStickers(R.drawable.emoti15);
                SingleStickerPath="/storage/emulated/0/Movies/.required/emoti15.png";
            }
        });
        findViewById(R.id.btn_cut).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                frameLayout.removeView(thisImage);
                findViewById(R.id.btn_cut).setVisibility(View.GONE);
            }
        });
        findViewById(R.id.sticker16).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddStickers(R.drawable.emoti16);
            }
        }); findViewById(R.id.sticker17).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddStickers(R.drawable.emoti17);
            }
        }); findViewById(R.id.sticker18).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddStickers(R.drawable.emoti18);
            }
        }); findViewById(R.id.sticker19).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddStickers(R.drawable.emoti19);
            }
        }); findViewById(R.id.sticker20).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddStickers(R.drawable.emoti20);
            }
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    @Override
    protected void onResume() {
        super.onResume();
        setUpCamera();
    }
    @Override
    protected void onStop() {
        super.onStop();
        releaseCamera();
    }
    private void StartCountdown()
    {
        TextView start_counter=findViewById(R.id.StartCounter);
        start_counter.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StartCountdownA(start_counter);
            }
        }, 1000);
    }
    private void StopCountdown()
    {
        tt.clearFocus();
        trecordBtn.setImageResource(R.drawable.play_v);
        cameraRecorder.stop();
        i=0;
        bUpdate=false;
    }
    private void StartCountdownA(TextView start_counter)
    {
        start_counter.setText("2");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StartCountdownB(start_counter);
            }
        }, 1000);
    }
    private void StartCountdownB(TextView start_counter)
    {
        start_counter.setText("1");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AfterStartFuns();
                start_counter.setVisibility(View.GONE);
            }
        }, 1000);
    }
    private void AfterStartFuns()
    {
        tt.clearFocus();
        bUpdate=true;
        trecordBtn.setImageResource(R.drawable.play_vstart);
        filepath = getVideoFilePath();
        cameraRecorder.start(filepath);
        i++;
        UpdateProgress();
        StartCounter();
    }
    private void ShowAudios()
    {
        Intent intent = new Intent(this, VoiceRecorderActivity.class);
        startActivityForResult(intent, ADD_VOICE_RECORD_REQUEST_CODE);
    }
    private void StartCounter()
    {
        new CountDownTimer(60000, 1000){
            public void onTick(long millisUntilFinished){
                if(bUpdate==true) {
                    counter_i++;
                    if(counter_i<59) {
                        if (counter_i < 10) {
                            counter.setText("00:0" + String.valueOf(counter_i));
                        } else {
                            counter.setText("00:" + String.valueOf(counter_i));
                        }
                    }
                    else
                    {

                        StopCountdown();
                    }
                }
            }
            public  void onFinish(){
            }
        }.start();
    }
    private void releaseCamera() {
        if (sampleGLView != null) {
            sampleGLView.onPause();
        }
        if (cameraRecorder != null) {
            cameraRecorder.stop();
            cameraRecorder.release();
            cameraRecorder = null;
        }
        if (sampleGLView != null) {
            ((RelativeLayout) findViewById(R.id.relative_view)).removeView(sampleGLView);

            sampleGLView = null;
        }
    }
    public void FinishRecording()
    {
        cameraRecorder.stop();
        i=0;
        bUpdate=false;
    }
    private void ColorPicker()
    {
        // Java Code
        new MaterialColorPickerDialog
                .Builder(this)
                .setTitle("Pick Theme")
                .setColorShape(ColorShape.CIRCLE)
                .setColorSwatch(ColorSwatch._300)
                .setColorSwatch(ColorSwatch._300)
                .setDefaultColor("#002884")
                .setColorListener(new ColorListener() {
                    @Override
                    public void onColorSelected(int color, @NotNull String colorHex) {
                        thiscolorHEX=colorHex;
                        mEdit.setTextColor((Color.parseColor(colorHex)));
                    }
                })
                .show();
    }
    /*private void writeInFile(@NonNull String text) {
        OutputStream outputStream;
        String textfile="/storage/emulated/0/Android/com.epep.notes/" + vidtextFileName + ".txt";
        try {
            outputStream = getContentResolver().openOutputStream(Uri.fromFile(new File(textfile)));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            bw.write(text);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    private void setUpCameraView() {
        runOnUiThread(() -> {
            frameLayout = findViewById(R.id.relative_view);
            sampleGLView = null;
            sampleGLView = new SampleGLView(getApplicationContext());
            sampleGLView.setTouchListener((event, width, height) -> {
                if (cameraRecorder == null) return;
                cameraRecorder.changeManualFocusPoint(event.getX(), event.getY(), width, height);
            });
            frameLayout.addView(sampleGLView);
        });
    }
    private void ScreenDensity()
    {
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outmetrics = new DisplayMetrics();
        Display display = wm.getDefaultDisplay();
        display.getMetrics(outmetrics);
        max_width = outmetrics.widthPixels;
        max_height = outmetrics.heightPixels;
    }
    private void AddStickers(int sticker)
    {
       /*ScreenDensity();
        thisImage=new MyImageView(this,sticker);
        frameLayout.addView(thisImage);
        findViewById(R.id.ssss).setVisibility(View.GONE);
        findViewById(R.id.btn_cut).setVisibility(View.VISIBLE);*/
    }
    private void AddStickersFromDevice(String url)
    {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SetNewGifView(url);
            }
        }, 1000);
    }
    void SetNewGifView(String url_path)
    {
        GifPath=url_path;
        ScreenDensity();
        thisImage=new MyImageView(this,url_path);
        frameLayout.addView(thisImage);
        findViewById(R.id.ssss).setVisibility(View.GONE);
        findViewById(R.id.btn_cut).setVisibility(View.VISIBLE);
    }
    private void setUpCamera() {
        setUpCameraView();
        cameraRecorder = new CameraRecorderBuilder(this, sampleGLView)
                //.recordNoFilter(true)
                .cameraRecordListener(new CameraRecordListener() {
                    @Override
                    public void onGetFlashSupport(boolean flashSupport) {
                        runOnUiThread(() -> {
                            findViewById(R.id.btn_flash).setEnabled(flashSupport);
                        });
                    }
                    @Override
                    public void onRecordComplete() {
                        findViewById(R.id.video_proceed).setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onRecordStart() {

                    }

                    @Override
                    public void onError(Exception exception) {
                        Log.e("CameraRecorder", exception.toString());
                    }

                    @Override
                    public void onCameraThreadFinish() {
                        if (toggleClick) {
                            runOnUiThread(() -> {
                                setUpCamera();
                            });
                        }
                        toggleClick = false;
                    }
                })
                .videoSize(videoWidth, videoHeight)
                .cameraSize(cameraWidth, cameraHeight)
                .lensFacing(lensFacing)
                .build();
    }
    private void changeFilter(Filters filters) {
        cameraRecorder.setFilter(Filters.getFilterInstance(filters, getApplicationContext()));
    }
    public void UpdateProgress()
    {
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                        progressStatus += 1;
                    handler.post(new Runnable() {
                        public void run() {
                            if(bUpdate==true) {
                                progressBar.setProgress(progressStatus);
                            }
                        }
                    });
                    try {
                        Thread.sleep(1800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    private interface BitmapReadyCallbacks {
        void onBitmapReady(Bitmap bitmap);
    }

    private void captureBitmap(final BitmapReadyCallbacks bitmapReadyCallbacks) {
        sampleGLView.queueEvent(() -> {
            EGL10 egl = (EGL10) EGLContext.getEGL();
            GL10 gl = (GL10) egl.eglGetCurrentContext().getGL();
            Bitmap snapshotBitmap = createBitmapFromGLSurface(sampleGLView.getMeasuredWidth(), sampleGLView.getMeasuredHeight(), gl);

            runOnUiThread(() -> {
                bitmapReadyCallbacks.onBitmapReady(snapshotBitmap);
            });
        });
    }
    public void FFMPEGcallbackForTEXTWM(String inputVideo)
    {
        Intent intent = new Intent(this, AddTextOnVideoActivity.class);
        String vText=mEdit.getText().toString();
        timetopass=String.valueOf(counter_i);
        intent.putExtra("LocalVideoTime", timetopass);
        intent.putExtra("LocalVideoPath", inputVideo);
        intent.putExtra("LocalVideoText", vText);
        intent.putExtra("LocalTextColor", thiscolorHEX);
        startActivityForResult(intent, ADD_TEXT_WM_REQUEST_CODE);
    }
    public void Call_Single_WMActivity(String inputVideo)
    {
        Intent intent = new Intent(this, AddWaterMarkOnVideoActivity.class);
        String stickerpath="/storage/emulated/0/Movies/.required/image.png";
        timetopass=String.valueOf(counter_i);
        intent.putExtra("LocalVideoTime", timetopass);
        intent.putExtra("LocalVideoPath", inputVideo);
        intent.putExtra("StickerPath", stickerpath);
        intent.putExtra("OtherStickerPosX", "0");
        intent.putExtra("OtherStickerPosY", "0");
        intent.putExtra("OtherStickerPath", "empty");
        startActivityForResult(intent, ADD_WM_REQUEST_CODE);
    }
    public void Call_GIF_Add_Activity(String inputVideo)
    {
        Intent intent = new Intent(this, AddGiffOnVideoActivity.class);
        timetopass=String.valueOf(counter_i);
        intent.putExtra("LocalVideoTime", timetopass);
        intent.putExtra("LocalVideoPath", inputVideo);
        intent.putExtra("StickerPath", GifPath);
        intent.putExtra("StickerH", gifH);
        intent.putExtra("StickerW", gifW);
        startActivityForResult(intent, ADD_VIDEO_WITH_GIF_REQUEST_CODE);
    }
    public void Call_Multi_WMActivity(String inputVideo)
    {
        Intent intent = new Intent(this, AddWaterMarkOnVideoActivity.class);
        String stickerpath=SingleStickerPath;
        timetopass=String.valueOf(counter_i);
        if(x_spos>max_width)
        {
            x_spos=max_width-300;
        }
        if(y_spos>max_height)
        {
            y_spos=max_height-300;
        }
        intent.putExtra("LocalVideoTime", timetopass);
        intent.putExtra("LocalVideoPath", inputVideo);
        intent.putExtra("StickerPath", stickerpath);
        intent.putExtra("OtherStickerPosX", String.valueOf(x_spos));
        intent.putExtra("OtherStickerPosY", String.valueOf(y_spos));
        intent.putExtra("OtherStickerPath",stickerpath);
        startActivityForResult(intent, ADD_WM_REQUEST_CODE);
    }
    public void FFMPEGcallbackForWM(String inputVideo)
    {
        if(frameLayout.getChildCount()>1)
        {
            Call_Multi_WMActivity(inputVideo);
        }
        else{
            Call_Single_WMActivity(inputVideo);
        }
    }
    /*String inputimage="/storage/emulated/0/Movies/image.png";
 String output="/storage/emulated/0/Movies/newvideo2.mp4";
 FFmpegQueryExtension sss = new FFmpegQueryExtension();
 String xx="90";
 float xpos= (float) ((Float.parseFloat(xx)*0.5)/100);
 String yy="90";
 float ypos= (float) ((Float.parseFloat(xx)*0.5)/100);
 String[] query = sss.addVideoWaterMark(inputVideo, inputimage, 80.0f, 80.0f, output);
 AppCompatActivity xy=new AppCompatActivity();
 new CallBackOfQuery().callQuery(xy, query, (FFmpegCallBack)(new FFmpegCallBack()
 {
     @Override
     public void process(LogMessage logMessage) {

     }
     @Override
     public void failed() {

     }
     @Override
     public void cancel() {

     }
     @Override
     public void success() {

     }
     @Override
     public void statisticsProcess(Statistics statistics) {

     }
 }));*/
    private Bitmap createBitmapFromGLSurface(int w, int h, GL10 gl) {
        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);
        try {
            gl.glReadPixels(0, 0, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2, texturePixel, blue, red, pixel;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    texturePixel = bitmapBuffer[offset1 + j];
                    blue = (texturePixel >> 16) & 0xff;
                    red = (texturePixel << 16) & 0x00ff0000;
                    pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            Log.e("CreateBitmap", "createBitmapFromGLSurface: " + e.getMessage(), e);
            return null;
        }
        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
    }
    public void saveAsPngImage(Bitmap bitmap, String filePath) {
        try {
            File file = new File(filePath);
            FileOutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void exportMp4ToGallery(Context context, String filePath) {
        final ContentValues values = new ContentValues(2);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, filePath);
        context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                values);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + filePath)));
    }
    public static String getVideoFilePath() {
        vidtextFileName=new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date());
        return getAndroidMoviesFolder().getAbsolutePath() + "/" +vidtextFileName+ "camerarecorder.mp4";
    }
    public static File getAndroidMoviesFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
    }
    private static void exportPngToGallery(Context context, String filePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(filePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
    public static String getImageFilePath() {
        return getAndroidImageFolder().getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "cameraRecorder.png";
    }
    public static File getAndroidImageFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }
    public void startCustomisingVideo()
    {
        Intent intent = new Intent(this, TrimmerActivity.class);
        String pathtopass= filepath.toString();
        timetopass=String.valueOf(counter_i);
        intent.putExtra("PassedVideoPath", pathtopass);
        intent.putExtra("PassedVideoTime", timetopass);
        startActivityForResult(intent, ADD_OPTION_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_VOICE_RECORD_REQUEST_CODE && resultCode == RESULT_OK ) {
            if (data.hasExtra("returnvoicepath")) {
                if(data.getExtras().getString("returnvoicepath")!="none") {
                    ismusicAdded=true;
                    musicpath= data.getExtras().getString("returnvoicepath");
                    Toast.makeText(this, "Audio added to Background of your Video", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if(requestCode == ADD_TEXT_WM_REQUEST_CODE && resultCode == RESULT_OK ) {
            if (data.hasExtra("returnKey1")) {
                if (data.getExtras().getString("returnKey1") != "none") {
                    filepath=data.getExtras().getString("returnKey1");
                    FFMPEGcallbackForWM(filepath.toString());
                }
            }
        }
        else if(requestCode == ADD_WM_REQUEST_CODE && resultCode == RESULT_OK ) {
                if (data.hasExtra("returnKey2")) {
                    if (data.getExtras().getString("returnKey2") != "none") {
                        filepath=data.getExtras().getString("returnKey2");
                        finish();
                    }
                }
        }
        else if(requestCode == ADD_SPEECH_REQUEST_CODE && resultCode == RESULT_OK ) {
           if (data.hasExtra("SpeechPath")) {
                if (data.getExtras().getString("SpeechPath") != "empty") {
                    ismusicAdded=true;
                    musicpath=data.getExtras().getString("SpeechPath");
                }
            }
        }
        else  if(requestCode == ADD_ATV_ARECORDING_REQUEST_CODE && resultCode == RESULT_OK ) {
            if (data.hasExtra("returnKey2")) {
                if (data.getExtras().getString("returnKey2") != "empty") {
                   filepath=data.getExtras().getString("returnKey2");
                    CompleteProcess();
                }
            }
        }
        else  if(requestCode == ADD_GIFF_BOX_CODE && resultCode == RESULT_OK ) {
            if (data.hasExtra("newgiffpath")) {
                if (!Objects.equals(data.getExtras().getString("newgiffpath"), "empty")) {
                    String newgiff=data.getExtras().getString("newgiffpath");
                    gifH=data.getExtras().getString("gifheight");
                    gifW=data.getExtras().getString("gifwidth");
                    bgif=true;
                    AddStickersFromDevice(newgiff);
                }
            }
        }
        else  if(requestCode == ADD_VIDEO_WITH_GIF_REQUEST_CODE && resultCode == RESULT_OK ) {
            if (data.hasExtra("videowithgif")) {
                if (!Objects.equals(data.getExtras().getString("videowithgif"), "empty")) {
                    String newpath=data.getExtras().getString("videowithgif");
                    Call_Single_WMActivity(newpath);
                }
            }
        }
    }
   @Override
    public void finish() {
        String storypath="none";
        Intent data = new Intent();
        if(filepath.toString().length()>7)
        {
            storypath=filepath.toString();
        }
        data.putExtra("storyvideopath", storypath);
        this.setResult(RESULT_OK, data);
        super.finish();
    }
    public void AddVoicetoThisVideo()
    {
        Intent intent = new Intent(this, MergeAudioVideoActivity.class);
        String pathtopass= filepath.toString();
        timetopass=String.valueOf(counter_i);
        intent.putExtra("PassedVideoPath", pathtopass);
        intent.putExtra("PassedMusicTime", musicpath);
        intent.putExtra("PassedVideoTime", timetopass);
        startActivityForResult(intent, ADD_ATV_ARECORDING_REQUEST_CODE);
    }
    public void OpenSpeechManipulator()
    {
        Intent intent = new Intent(this, SpeechManipulatorActivity.class);
        startActivityForResult(intent, ADD_SPEECH_REQUEST_CODE);
    }
    public void OpenGiffBox()
    {
        Intent intent = new Intent(this, SearchGiffActivity.class);
        startActivityForResult(intent, ADD_GIFF_BOX_CODE);
    }
    void CompleteProcess()
    {
        if(bgif)
        {
            Call_GIF_Add_Activity(filepath.toString());
        }
        else if(mEdit.getText().toString().length()>2)
        {
            FFMPEGcallbackForTEXTWM(filepath.toString());
        }
        else {
            FFMPEGcallbackForWM(filepath.toString());
        }
    }
}