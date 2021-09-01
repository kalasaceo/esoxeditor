package com.daasuu.camerarecorder;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.giphy.sdk.core.models.Image;
import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.ui.GPHContentType;
import com.giphy.sdk.ui.GPHSettings;
import com.giphy.sdk.ui.Giphy;
import com.kalasa.library.R;
import com.giphy.sdk.ui.GiphyFrescoHandler;
import com.giphy.sdk.ui.pagination.GPHContent;
import com.giphy.sdk.ui.views.GiphyDialogFragment;
import com.giphy.sdk.ui.views.GiphyGridView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.os.Parcelable;
import android.widget.ImageView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Calendar;
public class SearchGiffActivity extends AppCompatActivity implements GiphyDialogFragment.GifSelectionListener {
    private String gifpath="empty";
    private  String gif_h="empty";
    private String gif_w="empty";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        StartGiff();
    }
    void StartGiff() {
        Giphy.INSTANCE.configure(this, "LSdiuyzhPU04cgIoos9q77GM6YOBYqyu");
        GiphyDialogFragment.Companion.newInstance().show(this.getSupportFragmentManager(),"giffo");
    }
    @Override
    public void didSearchTerm(String s) {
    }
    @Override
    public void onDismissed(GPHContentType gphContentType) {
    }
    @Override
    public void onGifSelected(Media media,String s,GPHContentType gphContentType) {
        final Image image = media
                .getImages()
                .getFixedWidth();
        final String url = image.getGifUrl();
        gif_h=String.valueOf(image.getHeight());
        gif_w=String.valueOf(image.getWidth());
        DownloadGiff(image.getGifUrl());
    }
    void DownloadGiff(String url)
    {
        Glide.with(SearchGiffActivity.this).asFile()
                .load(url)
                .apply(new RequestOptions()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL))
                .into(new Target<File>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onDestroy() {

                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {

                    }

                    @Override
                    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                        storeImage(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void getSize(@NonNull SizeReadyCallback cb) {
                    }

                    @Override
                    public void removeCallback(@NonNull SizeReadyCallback cb) {

                    }
                    @Override
                    public void setRequest(@Nullable Request request) {

                    }
                    @Nullable
                    @Override
                    public Request getRequest() {
                        return null;
                    }
                });
    }
    private void storeImage(File image) {
        gifpath="storage/emulated/0/Movies/.required/"+Calendar.getInstance().getTimeInMillis()+".gif";
        File pictureFile = new File(gifpath);
        if (pictureFile == null) {
            return;
        }
        try {
            FileOutputStream output = new FileOutputStream(pictureFile);
            FileInputStream input = new FileInputStream(image);
            FileChannel inputChannel = input.getChannel();
            FileChannel outputChannel = output.getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            output.close();
            input.close();
            overridePendingTransition(0,0);
            finish();
            overridePendingTransition(0,0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("newgiffpath", gifpath);
        data.putExtra("gifheight", gif_h);
        data.putExtra("gifwidth", gif_w);
        this.setResult(RESULT_OK, data);
        super.finish();
    }
}