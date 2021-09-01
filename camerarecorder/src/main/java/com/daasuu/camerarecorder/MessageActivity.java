package com.daasuu.camerarecorder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kalasa.library.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.factor.bouncy.BouncyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity implements MessageAdapter.EventListener {
    boolean reply_ = false;
    EditText text_send;
    public TextView Iusername;
    MessageAdapter messageAdapter;
    boolean notify = false;
    List<Chat> mchat;
    ArrayList<String> replier_arr;
    String allChats="empty";
    BouncyRecyclerView recyclerView;
    RecyclerView rc;
    ImageButton btnSend;
    Button menu_open;
    String Iname="empty";
    String Ilink="empty";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_slider_in_right, R.anim.activity_slider_out_left);
        setContentView(R.layout.activity_message);
        Intent data=this.getIntent();
        allChats=data.getStringExtra("Chats");
        Iname = data.getStringExtra("Name");
        Ilink = data.getStringExtra("link");
        CircleImageView p_img=findViewById(R.id.profile_image);
        Glide.with(this).load(Ilink).into(p_img);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        replier_arr = new ArrayList<>();
        text_send = findViewById(R.id.text_send);
        Iusername=findViewById(R.id.name_user);
        btnSend = findViewById(R.id.btn_send);
        menu_open = findViewById(R.id.profiler);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setFlingAnimationSize(0.3f);
        recyclerView.setOverscrollAnimationSize(0.4f);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        AddPreviousMessages();
        btnSend.setOnClickListener(v -> {
            AddNewMessages();
        });
       menu_open.setOnClickListener(v -> {
            OpenPopupOptions();
        });
    }
   void  OpenPopupOptions(){
       Intent intent = new Intent(this,PopupMenuAcitivty.class);
       startActivity(intent);
   }
    void AddPreviousMessages() {
        Iusername=findViewById(R.id.name_user);
        Iusername.setText(Iname.toString());
        mchat = new ArrayList<>();
        String[] ch;
        if(allChats.contains("%%%%")) {
            String[] allchats_single = allChats.split("%%%%");
            for (int i = 0; i < allchats_single.length; i++) {
                Chat chat = new Chat();
                ch = allchats_single[i].split("-*-");
                chat.setMessage(ch[0]);
                if (ch[1] == "0") {
                    chat.setIsseen("MSG_TYPE_RIGHT");
                } else {
                    chat.setIsseen("MSG_TYPE_LEFT");
                }
                mchat.add(chat);
            }
            recyclerView = findViewById(R.id.recycler_view);
            messageAdapter = new MessageAdapter(MessageActivity.this, mchat, "xx", MessageActivity.this);
            messageAdapter.addEventListener(MessageActivity.this);
            recyclerView.setAdapter(messageAdapter);
        }
        else
        {
        }
    }
    void AddNewMessages() {
        EditText textsend = findViewById(R.id.text_send);
        if(textsend.length()>0) {
            Chat chat = new Chat();
            chat.setMessage(textsend.getText().toString());
            replier_arr.add(textsend.getText().toString()+"**");
            chat.setIsseen("MSG_TYPE_LEFT");
            mchat.add(chat);
            recyclerView = findViewById(R.id.recycler_view);
            messageAdapter = new MessageAdapter(MessageActivity.this, mchat, "xx", MessageActivity.this);
            messageAdapter.addEventListener(MessageActivity.this);
            recyclerView.setAdapter(messageAdapter);
        }
        textsend.setText("");
        textsend.clearFocus();
    }
    @Override
    public void openImage(String uri, String timestamp, String senderid, String extraid, ImageView chatimageview) {

    }
    @Override
    public void ShowOptions(String index) {
        OpenPopupOptions();
    }
    public void onBackPressed(){
        overridePendingTransition(R.anim.activity_slider_in_right, R.anim.activity_slider_out_left);
        finish();
        overridePendingTransition(R.anim.activity_slider_in_right, R.anim.activity_slider_out_left);
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
}