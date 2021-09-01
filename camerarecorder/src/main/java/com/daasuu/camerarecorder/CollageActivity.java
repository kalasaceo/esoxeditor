package com.daasuu.camerarecorder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.kalasa.library.R;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
public class CollageActivity extends AppCompatActivity {
    int this_height=320;
    int this_width=320;
    List<String> Adimens = new ArrayList<String>();
    List<String> Alikes = new ArrayList<String>();
    List<String> Aviews = new ArrayList<String>();
    String Adomain;
    private  String urls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_framemain);
        Intent data=this.getIntent();
        urls=data.getStringExtra("links");
        Adomain=data.getStringExtra("domain");
        SetImages(urls);
        ScreenDensity();
    }
void SetImages(String all_urls)
{
    String[] Aurls=all_urls.split("--");
    CollageView collageView = (CollageView) findViewById(R.id.collageView);
    List<String> urls = new ArrayList<String>();
    for(int i=0;i<Aurls.length;i++)
    {
        if(Aurls[i].contains("~~"))
        {
            String[] s = Aurls[i].split("=");
            int size = s[0].length();
            String t1 = s[0].substring(0, size - 3);
            String t2 = s[0].substring(size - 3, size);
            urls.add("https://s3.ap-south-1.amazonaws.com/toasterco.com/Posts/" + t1);
            Adimens.add(t2);
            Alikes.add(s[1]);
            String ace=s[2].split("~~")[0];
            Aviews.add(ace);
        }
        else {
            String[] s = Aurls[i].split("=");
            int size = s[0].length();
            String t1 = s[0].substring(0, size - 3);
            String t2 = s[0].substring(size - 3, size);
            urls.add("https://s3.ap-south-1.amazonaws.com/toasterco.com/Posts/" + t1);
            Adimens.add(t2);
            Alikes.add(s[1]);
            Aviews.add(s[2]);
        }
    }
    collageView
            .photoMargin(2)
            .photoPadding(2)
            .backgroundColor(Color.WHITE)
            .photoFrameColor(Color.WHITE)
            .useFirstAsHeader(true) // makes first photo fit device widtdh and use full line
            .defaultPhotosForLine(3) // sets default photos number for line of photos (can be changed by program at runtime)
            .useCards(false) // adds cardview backgrounds to all photos
            .maxWidth(960) // will resize images if their side is bigger than 100
            .placeHolder(R.drawable.wesa) //adds placeholder resource
            .headerForm(CollageView.ImageForm.IMAGE_FORM_SQUARE) // sets form of image for header (if useFirstAsHeader == true)
            .photosForm(CollageView.ImageForm.IMAGE_FORM_HALF_HEIGHT)
            .loadPhotos(urls);
    collageView.setOnPhotoClickListener(new CollageView.OnPhotoClickListener() {
        @Override
        public void onPhotoClick(int position) {
            if(urls.get(position).contains("-00001.png"))
            {
                ZoomVideo(urls.get(position),position);
            }
            else
            {
                Zoom(urls.get(position),position);
            }
        }
    });
}
    private void ScreenDensity()
    {
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outmetrics = new DisplayMetrics();
        Display display = wm.getDefaultDisplay();
        display.getMetrics(outmetrics);
        this_height = outmetrics.heightPixels/2;
        this_width = outmetrics.widthPixels;
    }
    void Zoom(String thislink,int position)
    {
        List<String> multi_urls = new ArrayList<String>();
        List<String> multi_cats = new ArrayList<String>();
        String[] Aurls=urls.split("--");
        String a=Aurls[position];
        if(a.contains("~~")) {
            String x_like = Alikes.get(position);
            String x_view = Aviews.get(position);
            Intent intent = new Intent(this, ZoomMultiActivity.class);
            intent.putExtra("link", a);
            intent.putExtra("height", String.valueOf(this_height));
            intent.putExtra("height", String.valueOf(this_width));
            intent.putExtra("likes", x_like);
            intent.putExtra("views", x_view);
            startActivity(intent);
        }
        else {
            float x = Float.parseFloat(Adimens.get(position));
            String x_like = Alikes.get(position);
            String x_view = Aviews.get(position);
            float y = x * this_height;
            String h = String.valueOf(y);
            Intent intent = new Intent(this, ZoomActivity.class);
            intent.putExtra("link", thislink);
            intent.putExtra("height", h);
            intent.putExtra("likes", x_like);
            intent.putExtra("views", x_view);
            startActivity(intent);
        }
    }
    void ZoomVideo(String thislink,int position)
    {
        String v_link=thislink.substring(0,thislink.length()-10)+".mp4";
        float x= Float.parseFloat(Adimens.get(position));
        String x_like= Alikes.get(position);
        String x_view= Aviews.get(position);
        float y=x*this_height;
        String h=String.valueOf(y);
        Intent intent = new Intent(this, ZoomVideoActivity.class);
        intent.putExtra("link", v_link);
        intent.putExtra("height",h);
        intent.putExtra("likes", x_like);
        intent.putExtra("views", x_view);
        startActivity(intent);
    }
    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("updatedata", "none");
        this.setResult(RESULT_OK, data);
        super.finish();
    }
}