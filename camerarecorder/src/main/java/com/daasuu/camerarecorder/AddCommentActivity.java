package com.daasuu.camerarecorder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.kalasa.library.R;
public class AddCommentActivity extends AppCompatActivity {
    private String c_comment="empty";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_ccomment);
        Button btn_canadd=findViewById(R.id.btn_canadd);
        EditText cstring=findViewById(R.id.cstring);
        btn_canadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c_comment=cstring.getText().toString();
                finish();
            }
        });
    }
    @Override
    public void finish() {
        String outputs="none";
        Intent data = new Intent();
        EditText cstring=findViewById(R.id.cstring);
        c_comment=cstring.getText().toString();
        if(c_comment.length()>1)
        {
            outputs=c_comment;
            data.putExtra("TextChat",c_comment);
            this.setResult(RESULT_OK, data);
        }
        super.finish();
    }
}