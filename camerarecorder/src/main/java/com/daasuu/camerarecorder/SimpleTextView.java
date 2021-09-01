package com.daasuu.camerarecorder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import com.kalasa.library.R;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import android.text.TextPaint;
import android.view.View;
import com.daasuu.camerarecorder.utils.Utils;
import com.daasuu.camerarecorder.widget.SampleGLView;
public class SimpleTextView extends View {
    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private String text = "This is a simple text on canvas";
    private int leftMargin, topMargin;
    public SampleGLView thissampleGLView;
    public SimpleTextView(Context context,SampleGLView sampleGLView) {
        super(context);
        thissampleGLView=sampleGLView;
        init();
    }
    private void init() {
        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        textPaint.setTextSize(Utils.spToPx(getContext(), 36));
        leftMargin = Utils.dpToPx(getContext(), 36);
        topMargin  = Utils.dpToPx(getContext(), 200);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            setLayerType(LAYER_TYPE_HARDWARE, textPaint);
        canvas.drawText(text, leftMargin, topMargin, textPaint);
        //CameraFun(canvas);
        thissampleGLView.onDrawForeground(canvas);
    }
    void CameraFun( Canvas mCanvas)
    {
        Bitmap original = BitmapFactory.decodeResource(getResources(),R.drawable.abcde1);
        Bitmap mask = BitmapFactory.decodeResource(getResources(),R.drawable.play_vstart);
        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(original, 0, 0, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);
    }

}