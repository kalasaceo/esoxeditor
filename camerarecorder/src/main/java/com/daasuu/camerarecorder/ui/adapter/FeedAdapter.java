package com.daasuu.camerarecorder.ui.adapter;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.daasuu.camerarecorder.CommentActivity;
import com.daasuu.camerarecorder.LikeActivity;
import com.daasuu.camerarecorder.LikeActivity2;
import com.daasuu.camerarecorder.MessageAdapter;
import com.daasuu.camerarecorder.PopupMenuAcitivty;
import com.daasuu.camerarecorder.VideoHolder;
import com.kalasa.library.R;
import com.daasuu.camerarecorder.mode.Feed;
import com.daasuu.camerarecorder.mode.Photo;
import com.daasuu.camerarecorder.mode.Video;
import com.daasuu.camerarecorder.ui.activity.VideoRecyclerActivity;
import com.daasuu.camerarecorder.ui.view.CameraAnimation;
import com.daasuu.camerarecorder.ui.view.VideoView;
import com.squareup.picasso.Picasso;
/**
 * Created by HoangAnhTuan on 1/21/2018.
 */
public class FeedAdapter extends BaseAdapter<Feed> {
    private static final int PHOTO_M1 = 0;
    private static final int PHOTO_M2 = 1;
    private static final int VIDEO_M1 = 2;
    private static final int VIDEO_M2 = 3;
    private static int screenWight = 0;
    public FeedAdapter(Activity activity) {
        super(activity);
        screenWight = getScreenWight();
    }
    @Override
    public int getItemViewType(int position) {
        Feed feed = list.get(position);
        if (feed.getInfo() instanceof Photo) {
            if (feed.getModel() == Feed.Model.M1) return PHOTO_M1;
            return PHOTO_M2;
        } else {
            if (feed.getModel() == Feed.Model.M1) return VIDEO_M1;
            return VIDEO_M2;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case PHOTO_M1:
                view = activity.getLayoutInflater().inflate(R.layout.item_photo, parent, false);
                return new Photo11Holder(view);
            case PHOTO_M2:
                view = activity.getLayoutInflater().inflate(R.layout.item_photo, parent, false);
                return new Photo169Holder(view);
            case VIDEO_M1:
                view = activity.getLayoutInflater().inflate(R.layout.item_video, parent, false);
                return new Video11Holder(view);
            default:
                view = activity.getLayoutInflater().inflate(R.layout.item_video, parent, false);
                return new Video169Holder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Feed feed = list.get(position);
        if (holder instanceof Video11Holder) {
            onBindVideo11Holder((Video11Holder) holder, feed);
        } else if (holder instanceof Video169Holder) {
            onBindVideo169Holder((Video169Holder) holder, feed);
        } else if (holder instanceof Photo11Holder) {
            onBindPhoto11Holder((Photo11Holder) holder, feed);
        } else if (holder instanceof Photo169Holder) {
            onBindPhoto169Holder((Photo169Holder) holder, feed);
        }
    }
    private void onBindPhoto11Holder(Photo11Holder holder, Feed feed) {
        Picasso.with(activity)
                .load(feed.getInfo().getUrlPhoto())
                .resize(screenWight, screenWight)
                .centerCrop()
                .into(holder.ivInfo);
    }

    private void onBindPhoto169Holder(Photo169Holder holder, Feed feed) {
        Picasso.with(activity)
                .load(feed.getInfo().getUrlPhoto())
                .resize(screenWight, screenWight * 9 / 16)
                .centerCrop()
                .into(holder.ivInfo);
    }

    private void onBindVideo11Holder(final DemoVideoHolder holder, Feed feed) {
        holder.vvInfo.setVideo((Video) feed.getInfo());
        Picasso.with(activity)
                .load(feed.getInfo().getUrlPhoto())
                .resize(screenWight, screenWight)
                .centerCrop()
                .into(holder.ivInfo);
    }

    private void onBindVideo169Holder(final DemoVideoHolder holder, Feed feed) {
        holder.vvInfo.setVideo((Video) feed.getInfo());
        Picasso.with(activity)
                .load(feed.getInfo().getUrlPhoto())
                .resize(screenWight, screenWight * 9 / 16)
                .centerCrop()
                .into(holder.ivInfo);
    }

    private int getScreenWight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static class PhotoHolder extends RecyclerView.ViewHolder {
        ImageView ivInfo;
        public PhotoHolder(View itemView) {
            super(itemView);
            ivInfo=itemView.findViewById(R.id.ivInfo);
        }

    }
    public static class Photo11Holder extends PhotoHolder {
        public Photo11Holder(View itemView) {
            super(itemView);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ivInfo.getLayoutParams();
            layoutParams.width = screenWight;
            layoutParams.height = screenWight;
            ivInfo.setLayoutParams(layoutParams);
        }
    }

    public static class Photo169Holder extends PhotoHolder {


        public Photo169Holder(View itemView) {
            super(itemView);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ivInfo.getLayoutParams();
            layoutParams.width = screenWight;
            layoutParams.height = screenWight * 9 / 16;
            ivInfo.setLayoutParams(layoutParams);
        }
    }
    public static class DemoVideoHolder extends VideoHolder {
        ImageButton btn_playing;
        VideoView vvInfo;
        ImageView ivInfo;
        Button ivliked;
        Button ivcomment;
        Button ivvolume;
        TextView comment_id;
        ImageView icon_playing;
        ImageView icon_volume;
        CameraAnimation ivCameraAnimation;
        boolean cond=true;
        boolean volume_status=true;
        public DemoVideoHolder(View itemView) {
            super(itemView);
            btn_playing=itemView.findViewById(R.id.btn_playing);
            icon_playing=itemView.findViewById(R.id.icon_playing);
            vvInfo=itemView.findViewById(R.id.vvInfo);
            ivInfo=itemView.findViewById(R.id.ivInfo);
            icon_volume=itemView.findViewById(R.id.ghjf1);
            ivCameraAnimation=itemView.findViewById(R.id.ivCameraAnimation);
            ivliked=itemView.findViewById(R.id.btn_liked);
            comment_id=itemView.findViewById(R.id.comment_id);
            ivcomment=itemView.findViewById(R.id.btn_comment);
            ivvolume=itemView.findViewById(R.id.btn_volume);
            ivvolume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(volume_status==true)
                    {
                        vvInfo.SetVolumeOFF();
                        icon_volume.setImageResource(R.drawable.volumeofff);
                        volume_status=false;
                    }
                    else
                    {
                        vvInfo.SetVolumeON();
                        icon_volume.setImageResource(R.drawable.volumeon);
                        volume_status=true;
                    }
                }
            });
            ivliked.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   Intent openProgramActivity = new Intent(v.getContext(), LikeActivity2.class);
                                                   v.getContext().startActivity(openProgramActivity);
                                               }
                                           });
            ivcomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] allchats_single = VideoRecyclerActivity.cids.split("<-->");
                    Intent openProgramActivity = new Intent(v.getContext(), CommentActivity.class);
                    openProgramActivity.putExtra("CId",allchats_single[getAdapterPosition()]);
                    openProgramActivity.putExtra("CUserName",VideoRecyclerActivity.cusernames);
                    openProgramActivity.putExtra("CPassword",VideoRecyclerActivity.cpasswords);
                    v.getContext().startActivity(openProgramActivity);
                }
            });
            btn_playing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cond==true) {
                        vvInfo.stop();
                        icon_playing.setImageResource(R.drawable.pause);
                        //icon_playing.setImageResource(R.drawable.avd_play_to_pause);
                        cond=false;
                    }
                    else
                    {
                        playVideo();
                        icon_playing.setImageResource(R.drawable.play);
                        //icon_playing.setImageResource(R.drawable.avd_pause_to_play);
                        cond=true;
                    }
                }
            });
        }
        @Override
        public View getVideoLayout() {
            return vvInfo;
        }
        public void openProgramActivity(View view, int position) {
            Intent openProgramActivity = new Intent(view.getContext(), LikeActivity.class);
            openProgramActivity.putExtra("index",position);
            view.getContext().startActivity(openProgramActivity);
        }
        @Override
        public void playVideo() {
            ivInfo.setVisibility(View.VISIBLE);
            icon_playing.setVisibility(View.GONE);
            ivCameraAnimation.start();
            vvInfo.play(new VideoView.OnPreparedListener() {
                @Override
                public void onPrepared() {
                    ivInfo.setVisibility(View.GONE);
                    ivCameraAnimation.stop();
                }
            });
        }

        @Override
        public void stopVideo() {
            ivInfo.setVisibility(View.VISIBLE);
            icon_playing.setVisibility(View.VISIBLE);
            ivCameraAnimation.stop();
            vvInfo.stop();
        }
    }
    public static class Video11Holder extends DemoVideoHolder {
        public Video11Holder(View itemView) {
            super(itemView);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) vvInfo.getLayoutParams();
            layoutParams.width = screenWight;
            layoutParams.height = screenWight;
            vvInfo.setLayoutParams(layoutParams);
        }
    }
    public static class Video169Holder extends DemoVideoHolder {

        public Video169Holder(View itemView) {
            super(itemView);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) vvInfo.getLayoutParams();
            layoutParams.width = screenWight;
            layoutParams.height = screenWight * 9 / 16;
            vvInfo.setLayoutParams(layoutParams);
        }
    }
}
