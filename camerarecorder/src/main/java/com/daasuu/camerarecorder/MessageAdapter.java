package com.daasuu.camerarecorder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import com.kalasa.library.R;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Random;
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private final Context mContext;
    private final List<Chat> mChat;
    private int index=-1;
    private final String imageUrl;
    private final Activity activity;
    EventListener eventListener;

    public interface EventListener {
        void openImage(String uri, String timmessageAdapterestamp, String senderid, String extraid, ImageView chatimageview);
        void ShowOptions(String index);
    }
    public void addEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void removeEventListener() {
        eventListener = null;
    }

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    public static final int REPLY_TYPE_LEFT = 2;
    public static final int REPLY_TYPE_RIGHT = 3;
    public static final int IMAGE_TYPE_LEFT = 4;
    public static final int IMAGE_TYPE_RIGHT = 5;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageUrl, Activity activity) {
        this.mContext = mContext;
        this.mChat = mChat;
        this.imageUrl = imageUrl;
        this.activity = activity;
    }


    @Override
    public int getItemViewType(int position) {
        if(index+1< mChat.size() && index+1>-1) {
            if (mChat.get(position).getIsseen().equals("MSG_TYPE_RIGHT")) {
                return MSG_TYPE_RIGHT;
            } else {
                return MSG_TYPE_LEFT;
            }
        }
        else
        {
            return -1;
        }
            /*if(mChat.get(position).getReply().equals("true")) {
                return REPLY_TYPE_RIGHT;
            }

            if(mChat.get(position).getReply().equals("image")){
                return REPLY_TYPE_RIGHT;
            }
            else{
                return REPLY_TYPE_RIGHT;
            }*/

        }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(viewGroup);
        }
        if (viewType == MSG_TYPE_LEFT) {
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(viewGroup);
        }
        if (viewType == REPLY_TYPE_RIGHT) {
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(viewGroup);
        }
        if(viewType == REPLY_TYPE_LEFT) {
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(viewGroup);
        }
        if(viewType == IMAGE_TYPE_LEFT){
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(viewGroup);
        }
        else {
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(viewGroup);
        }
    }
    static void Checking()
    {

    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        index=index+1;
        //position=position+index;
        //Chat chat = mChat.get(position);
        //holder.username.setVisibility(View.GONE);
        //String decryptedmessage = String.valueOf(rd.nextFloat()) ;
        //if(index+1<= mChat.size() && index+1>-1) {
        String decryptedmessage = mChat.get(position).getMessage();
        holder.show_message.setText(decryptedmessage);
        /*holder.surface.setOnTouchListener(new LongTouchIntervalListener(7000) {
            @Override
            public void onTouchInterval() {
                ShowOptions("1");
            }
        });*/
        /*holder.surface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowOptions("1");
            }
        });*/
        //}
        //holder.reply_txt_us.setVisibility(View.GONE);
        //holder.reply_txt_them.setVisibility(View.VISIBLE);
        //holder.reply_username.setText("ROWANAPTE");

        //holder.reply_txt_them.setText(decryptedmessages);
        /*if(imageUrl.equals("default"))
        {
            //holder.profile.setImageResource(R.drawable.user);
        }else {
            //Glide.with(mContext).load(imageUrl).into(holder.profile);
        }
        if(position == mChat.size()-1) {

        }else {
//            holder.txt_seen.setVisibility(View.GONE);
        }*/
    }
    private void openImage(String uri, String timestamp, String senderid,String extraid, ImageView chatimageview) {
        eventListener.openImage(uri, timestamp, senderid, extraid, chatimageview);
    }
    private void ShowOptions(String index) {
        eventListener.ShowOptions(index);
    }
    @Override
    public int getItemCount() {
        return mChat.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        //public TextView username;
        public LinearLayout linearLayout;
        //public Button surface;
        public ViewHolder(View view) {
            super(view);
            show_message = itemView.findViewById(R.id.show_message);
            //username = itemView.findViewById(R.id.username);
            //surface= itemView.findViewById(R.id.surfaced);
        }
    }
}