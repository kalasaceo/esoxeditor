package com.daasuu.camerarecorder;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;


import com.kalasa.library.R;

import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daasuu.camerarecorder.ui.CommentAdapter;
import com.factor.bouncy.BouncyRecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
public class CommentActivity extends AppCompatActivity {
    private static final int ADD_COMMENT_REQUEST_CODE =1450 ;
    CommentAdapter commentAdapter;
    List<Chat> cchat;
    public DatabaseReference mDatabase;
    String AES = "AES";
    ArrayList<String> replier_arr;
    String allChats="empty";
    String str_password="justdo";
    ProgressBar c_progress;
    String str_to_encrypt="yes sure you will";
    BouncyRecyclerView recyclerView;
    String myUserName="empty";
    byte[] initializationVector = null;
    String same_comments="empty";
    byte[] encrypted = null;
    RecyclerView rc;
    String key;
    String c_id;
    String key_value="empty";
    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent data=this.getIntent();
        c_id=data.getStringExtra("CId");
        myUserName = data.getStringExtra("CUserName");
        String passwordKey =data.getStringExtra("CPassword");
        c_progress=findViewById(R.id.c_progress);
        key=passwordKey;
        overridePendingTransition(R.anim.activity_slider_in_right, R.anim.activity_slider_out_left);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        replier_arr = new ArrayList<>();
        setContentView(R.layout.activity_comment);
        recyclerView = findViewById(R.id.c_recycler_view);
        ImageButton add_new_c=findViewById(R.id.add_comment);
        add_new_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAddCommentBox();
            }
        });
        ImageButton c_new=findViewById(R.id.first_c);
        c_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.first_c).setVisibility(View.GONE);
                findViewById(R.id.first_info).setVisibility(View.GONE);
                OpenAddCommentBox();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setFlingAnimationSize(0.3f);
        recyclerView.setOverscrollAnimationSize(0.4f);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        ReadDBMS(c_id);
    }
   void  OpenPopupOptions(){
       Intent intent = new Intent(this,PopupMenuAcitivty.class);
       startActivity(intent);
   }
    void AddPreviousMessages() {
        recyclerView=findViewById(R.id.c_recycler_view);
        commentAdapter = new CommentAdapter(CommentActivity.this, cchat, "xx", CommentActivity.this);
        //commentAdapter.addEventListener(CommentActivity.this);
        recyclerView.setAdapter(commentAdapter);
        c_progress=findViewById(R.id.c_progress);
        c_progress.setVisibility(View.GONE);
    }
    void  OpenAddCommentBox()
    {
        Intent intent = new Intent(this, AddCommentActivity.class);
        startActivityForResult(intent, ADD_COMMENT_REQUEST_CODE);
    }
    public void WriteDBMS(String comment,String output)
    {
        Chat chat = new Chat();
        chat.setIsseen("MSG_TYPE_LEFT");
        chat.setUserName("You");
        chat.setMessage(comment);
        chat.setDate("now");
        cchat.add(chat);
        AddPreviousMessages();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("chtposts").child(c_id).child(myUserName).setValue(output);
    }
    public void onBackPressed(){
        overridePendingTransition(R.anim.activity_slider_in_right, R.anim.activity_slider_out_left);
        finish();
        overridePendingTransition(R.anim.activity_slider_in_right, R.anim.activity_slider_out_left);
    }
    public String ReadDBMS(String HashTagName)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ReadInfo(HashTagName);
        return "empty";
    }
    public void ReadInfo(String childValue)
    {
        mDatabase.child("chtposts").child(childValue).get().addOnCompleteListener(new OnCompleteListener <DataSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {
            if (!task.isSuccessful()) {
                findViewById(R.id.first_c).setVisibility(View.VISIBLE);
            }
            else {
                if(String.valueOf(task.getResult().getValue())=="null")
                {
                    findViewById(R.id.first_c).setVisibility(View.VISIBLE);
                    findViewById(R.id.first_info).setVisibility(View.VISIBLE);
                }
                else
                {
                    findViewById(R.id.add_comment).setVisibility(View.VISIBLE);
                }
                same_comments=String.valueOf(task.getResult().getValue());
                DecryptProcess(String.valueOf(task.getResult().getValue()));
            }
        }
    });
    }
    @Override
    public void finish() {
        String replier="empty";
        Intent data = new Intent();
        if(replier_arr.size()>0) {
            replier = replier_arr.toString();
        }
        data.putExtra("ReplyArray",replier);
        this.setResult(RESULT_OK, data);
        super.finish();
    }
    void DecryptProcess(String indata)
    {
        indata=indata.substring(1,indata.length()-1);
        String output="";
        String[] y = indata.split(",");
        int num=indata.split(",").length;
        cchat = new ArrayList<>();
        String[] x;
        for(int j=0;j<num;j++) {
            String[] arr = y[j].split("=");
            String a1 = arr[0];
            String a2 = "";
            for (int i = 1; i < arr.length; i++) {
                a2 = a2 + arr[i];
            }
            try {
                output = decryption(a2, str_password.toString());
                Chat chat = new Chat();
                chat.setUserName(a1);
                x=output.split("--");
                chat.setMessage(x[0]);
                chat.setDate(x[1]);
                cchat.add(chat);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        AddPreviousMessages();
    }
    private String encryption(String input, String password) throws Exception {
        SecretKeySpec keySpec = generateKeySpec(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptValue = cipher.doFinal(input.getBytes());
        String encryptedValue = Base64.encodeToString(encryptValue, Base64.DEFAULT);
        return encryptedValue;
    }

    private String decryption(String output, String password) throws Exception{
        SecretKeySpec keySpec = generateKeySpec(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decodedValue = Base64.decode(output, Base64.DEFAULT);
        byte[] decriptValue = cipher.doFinal(decodedValue);
        String decriptedValue = new String(decriptValue);
        return decriptedValue;
    }

    private SecretKeySpec generateKeySpec(String password) throws Exception{
        final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        messageDigest.update(bytes, 0, bytes.length);
        byte[] keySpec = messageDigest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keySpec, "AES");
        return secretKeySpec;
    }
    void AddAfter(String comment)
    {
            String output = "";
            String this_date = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
            try {
                output = encryption(comment + "--" + this_date, str_password.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            WriteDBMS(comment, output);
        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_COMMENT_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data.hasExtra("TextChat")) {
                if (!data.getExtras().getString("TextChat").equals("none")) {
                    AddAfter(data.getExtras().getString("TextChat"));
                }
            }
        }
    }
}