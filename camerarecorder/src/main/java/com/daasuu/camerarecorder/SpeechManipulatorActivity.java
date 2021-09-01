package com.daasuu.camerarecorder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import com.kalasa.library.R;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
public class SpeechManipulatorActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 2039;
    private EditText etext;
    private Button btn;
    private TextToSpeech textToSpeech;
    private  String filepath="empty";
    private Boolean isAudioSelected=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_manipulator);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        etext = (EditText) findViewById(R.id.tovoicetext);
        btn = (Button) findViewById(R.id.tovoicespeech);
        btn.setOnClickListener(this);
        findViewById(R.id.tovoicesave).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isAudioSelected=true;
                finish();
            }
        });
        textToSpeech = new TextToSpeech(getBaseContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    //Voice voiceobj2 = new Voice("it-it-x-kda#male_2-local", Locale.getDefault(), 1, 1, false, null);
                    Set<String> a=new HashSet<>();
                    a.add("male");
                    Voice voiceobj2=new Voice("en-us-x-sfg#male_2-local",new Locale("en","US"),400,200,true,a);
                    textToSpeech.setVoice(voiceobj2);
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        HashMap<String, String> params = new HashMap<String,String>();
        if(checkPermission()==true) {
            //Voice voiceobj2 = new Voice("it-it-x-kda#male_2-local", Locale.getDefault(), 1, 1, false, null);
            //Set<String> a=new HashSet<>();
            //a.add("male");
            //Voice voiceobj2=new Voice("en-us-x-sfg#male_2-local",new Locale("en","US"),400,200,true,a);
            //textToSpeech.setVoice(voiceobj2);
            String text = etext.getText().toString();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, text);
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            filepath = "/storage/emulated/0/Movies/.required/" + new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + ".mp3";
            textToSpeech.synthesizeToFile(text, params, filepath);
        }
        else
        {
            Toast.makeText(SpeechManipulatorActivity.this, "Audio permission granted not successfully", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void finish() {
        Intent data = new Intent();
        if(isAudioSelected!=true)
        {
            filepath="empty";
        }
        data.putExtra("SpeechPath",filepath);
        this.setResult(RESULT_OK, data);
        super.finish();
    }
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE);
            return false;
        }

        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(SpeechManipulatorActivity.this, "Audio permission granted not successfully", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

