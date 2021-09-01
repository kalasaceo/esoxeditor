package com.daasuu.camerarecorder;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.giphy.sdk.core.models.Image;
import com.kalasa.library.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;

public class MyImageView extends View {
    private static final int INVALID_POINTER_ID = -1;
    private Drawable mImage;
    private float mPosX;
    private BaseCameraActivity m_activity;
    private float mPosY;
    public static float mLastTouchX;
    public static float mLastTouchY;
    private boolean can_scale=false;
    private int mActivePointerId = INVALID_POINTER_ID;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    public MyImageView(Context context,int Sticker) {
        this(context, null, 0);
        m_activity= (BaseCameraActivity) context;
        mScaleFactor = 1.f;
        can_scale=false;
        mImage = getResources().getDrawable(Sticker);
        mImage.setBounds(0, 0, mImage.getIntrinsicWidth(), mImage.getIntrinsicHeight());
    }
    public MyImageView(Context context,String url) {
        this(context, null, 0);
        mScaleFactor = 2.8f;
        can_scale=true;
        m_activity= (BaseCameraActivity) context;
        mImage = new BitmapDrawable(getResources(), BitmapFactory.decodeFile(url));
        mImage.setAutoMirrored(true);
        mImage.setBounds(0, 0, mImage.getIntrinsicWidth(), mImage.getIntrinsicHeight());
    }
    public MyImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mScaleDetector.onTouchEvent(ev);
        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();
                mLastTouchX = x;
                mLastTouchY = y;
                m_activity.x_spos=mPosX;
                m_activity.y_spos=mPosY;
                mActivePointerId = ev.getPointerId(0);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float x = ev.getX(pointerIndex);
                final float y = ev.getY(pointerIndex);
                m_activity.x_spos=mPosX;
                m_activity.y_spos=mPosY;
                if (!mScaleDetector.isInProgress()) {
                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;
                    mPosX += dx;
                    mPosY += dy;
                    invalidate();
                }
                mLastTouchX = x;
                mLastTouchY = y;
                break;
            }
            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                break;
            }
        }
        return true;
    }
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float pivotX, pivotY;
        pivotX = mImage.getIntrinsicWidth()/2;
        pivotY = mImage.getIntrinsicHeight()/2;
        canvas.save();
        canvas.translate(mPosX, mPosY);
        canvas.scale(mScaleFactor, mScaleFactor, pivotX, pivotY);
        mImage.draw(canvas);
        canvas.restore();
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if(can_scale==false) {
                mScaleFactor *= detector.getScaleFactor();
                mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 2.0f));
            }
            invalidate();
            return true;
        }
    }

}