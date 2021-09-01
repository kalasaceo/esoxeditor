package com.daasuu.camerarecorder.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daasuu.camerarecorder.LikeActivity;
import com.daasuu.camerarecorder.MessageAdapter;
import com.kalasa.library.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected List<T> list;
    protected Activity activity;
    protected LayoutInflater inflater;
    public BaseAdapter(Activity activity) {
        this.activity = activity;
        this.list = new ArrayList<>();
        this.inflater = activity.getLayoutInflater();
    }
    public interface EventListener {
        void LikedFunction(String i);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public synchronized void add(T t) {
        list.add(t);
        sort();
        int position = list.indexOf(t);
        notifyItemInserted(position);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        }
    public synchronized void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public synchronized void addPosition(T t, int position) {
        list.add(position, t);
        sort();
        position = list.indexOf(t);
        notifyItemInserted(position);
    }

    public synchronized void removerPosition(int position) {
        T t = list.get(position);
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, list.size());
    }

    public synchronized void updatePosition(T t, int position) {
        list.set(position, t);
        notifyItemChanged(position);
        notifyItemRangeChanged(position, list.size());
    }

    public ArrayList<T> getData() {
        return (ArrayList<T>) list;
    }

    synchronized void sort() {
    }
}
