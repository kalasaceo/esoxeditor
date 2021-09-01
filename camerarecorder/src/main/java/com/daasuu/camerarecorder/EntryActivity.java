package com.daasuu.camerarecorder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.kalasa.library.R;
public class EntryActivity extends AppCompatActivity {
    private boolean isSeen=false;
    private String result1="false";
    private String result2="false";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        TextView enter_text=findViewById(R.id.op_enterpwd);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        ImageButton showpwd= findViewById(R.id.btn_showpwd);
        showpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSeen==false) {
                    showpwd.setImageResource(R.drawable.pwd1);
                    enter_text.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isSeen = true;
                }
                else {
                    showpwd.setImageResource(R.drawable.pwd0);
                    enter_text.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isSeen=false;
                }
            }
        });
        findViewById(R.id.btn_forgotpwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result2="true";
                finish();
            }
        });
        findViewById(R.id.btn_enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result1="true";
                finish();
            }
        });
        findViewById(R.id.btn_createbus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
            }
        });
    }
    @Override
    public void finish() {
        Intent data = new Intent();
        TextView thisname=findViewById(R.id.op_entername);
        TextView thispwd=findViewById(R.id.op_enterpwd);
        data.putExtra("status", result1+"--"+result2+"--"+thisname.getText().toString()+"--"+thispwd.getText().toString());
        this.setResult(RESULT_OK, data);
        super.finish();
    }
}
