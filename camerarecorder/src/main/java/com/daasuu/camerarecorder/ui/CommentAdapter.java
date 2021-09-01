package com.daasuu.camerarecorder.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daasuu.camerarecorder.Chat;
import com.kalasa.library.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private final Context mContext;
    private final List<Chat> mChat;
    private int index=-1;
    private final String imageUrl;
    private final Activity activity;
    public static final int MSG_TYPE_LEFT = 0;
    public CommentAdapter(Context mContext, List<Chat> mChat, String imageUrl, Activity activity) {
        this.mContext = mContext;
        this.mChat = mChat;
        this.imageUrl = imageUrl;
        this.activity = activity;
    }
    @Override
    public int getItemViewType(int position) {
        if(index+1< mChat.size() && index+1>-1) {
                return MSG_TYPE_LEFT;
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
        if (viewType == MSG_TYPE_LEFT) {
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);
            return new ViewHolder(viewGroup);
        }
        else {
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);
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
        String decryptedmessage =mChat.get(position).getMessage();
        String decryptedusername =mChat.get(position).getUserName();
        String decrypteddate =mChat.get(position).getDate();
        //String newString = decryptedmessage.replaceAll("empty", "<font color='red'>"+"this is good text"+"</font>");
        String newString = "<h6>"+decryptedmessage+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</h6>"+"<small><font color='grey'>"+decryptedusername+"</font></small>"+"<br></br>"+"<small><font color='grey'>"+decrypteddate+"</font></<small>";
        holder.show_message.setText(Html.fromHtml(newString));
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
    @Override
    public int getItemCount() {
        return mChat.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public LinearLayout linearLayout;
        //public Button surface;
        public ViewHolder(View view) {
            super(view);
            show_message = itemView.findViewById(R.id.show_message);
        }
    }
}