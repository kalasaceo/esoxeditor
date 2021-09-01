package com.daasuu.camerarecorder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidadvance.topsnackbar.TSnackbar;
import com.kalasa.library.R;

import java.util.Objects;

public class FirstEntryActivity extends AppCompatActivity {
    private boolean isSeen1 = false;
    private boolean isSeen2 = false;
    private String result1 = "false";
    private String result2 = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstenter);
        ImageButton showpwd1 = findViewById(R.id.rbtn_showpwd1);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EditText enter_text1 = findViewById(R.id.rop_enterpwd1);
        EditText enter_text2 = findViewById(R.id.rop_enterpwd2);
        EditText enter_fname = findViewById(R.id.rop_entername1);
        EditText enter_lname = findViewById(R.id.rop_entername2);
        ImageButton showpwd2 = findViewById(R.id.rbtn_showpwd2);
        RadioButton r1 = findViewById(R.id.radioButton1);
        RadioButton r2 = findViewById(R.id.radioButton2);
        showpwd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSeen1 == false) {
                    showpwd1.setImageResource(R.drawable.pwd1);
                    enter_text1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isSeen1 = true;
                } else {
                    showpwd1.setImageResource(R.drawable.pwd0);
                    enter_text1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isSeen1 = false;
                }
            }
        });
        showpwd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSeen2 == false) {
                    showpwd2.setImageResource(R.drawable.pwd1);
                    enter_text2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isSeen2 = true;
                } else {
                    showpwd2.setImageResource(R.drawable.pwd0);
                    enter_text2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isSeen2 = false;
                }
            }
        });
        findViewById(R.id.rbtn_enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReferencerFunction(enter_text1, enter_text2, r1, r2, enter_fname, enter_lname);
            }
        });
        findViewById(R.id.rbtn_enter2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result2 = "true";
                finish();
            }
        });
        findViewById(R.id.rbtn_createbus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void ReferencerFunction(EditText enter_text1, EditText enter_text2, RadioButton r1, RadioButton r2, EditText enter_fname, EditText enter_lname) {
        if(Objects.equals(enter_text1.getText().toString(), enter_text2.getText().toString()) && enter_text1.getText().toString().length()>7) {
        if (r1.isChecked() || r2.isChecked()) {
            DatePicker dt = findViewById(R.id.datePicker1);
            if (2021 - dt.getYear() > 14) {
                String x = enter_fname.getText().toString() + enter_lname.getText().toString();
                if (x.length() > 9) {
                    result1 = "true";
                    finish();
                } else {
                    CustomSnackbar("Your Username is too small.Kindly set a Strong one.");
                }
            } else {
                CustomSnackbar("Your Age violates our Policy as you are below our setted Age.");
            }
        } else {
            CustomSnackbar("Kindly your select your Gender.");
        }
    }
    else
    {
     CustomSnackbar("your Password is too small.Kindly set it to more stronger one.");
    }
}
    void CustomSnackbar(String message) {
        TSnackbar snackbar = TSnackbar
                .make(getWindow().getDecorView().getRootView(), message, TSnackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setIconLeft(R.drawable.item_message_check_bg, 37);
        snackbar.setIconPadding(10);
        snackbar.setDuration(TSnackbar.LENGTH_LONG);
        snackbar.setAction("set now", new FirstEntryActivity.MyUndoListener());
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
    public void finish() {
        Intent data = new Intent();
        if(Objects.equals(result1, "true")) {
            EditText result3 = findViewById(R.id.rop_entername1);
            EditText result4 = findViewById(R.id.rop_entername2);
            EditText result5 = findViewById(R.id.rop_enterpwd1);
            data.putExtra("status", result1 + "--" + result2 + "--" + result3.getText().toString() + "--" + result4.getText().toString() + "--" + result5.getText().toString());
        }
        else
        {
            data.putExtra("status", "false"+"--"+"true"+"--"+"false"+"--"+"false"+"--"+"false");
        }
        this.setResult(RESULT_OK, data);
        super.finish();
    }
}
