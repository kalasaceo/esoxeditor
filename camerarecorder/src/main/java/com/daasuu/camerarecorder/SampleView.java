package com.daasuu.camerarecorder;
import android.content.Context;
import android.graphics.*;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
public class SampleView extends View {
    private static final String QUOTE = "Now is the time for all good men to come to the aid of their country.";
    private Animation animation;
    private Paint gPaint;
    private Paint cPaint;
    private Path glowCircle;
    private Path circle;
    private Paint tPaint;
    public SampleView(Context context) {
        super(context);
        init();
    }
    private void init() {
        gPaint = new Paint();
        gPaint.setAlpha(255);
        gPaint.setShadowLayer(40, 0, 0, Color.argb(200, 255, 0, 0));

        cPaint = new Paint();
        cPaint.setAntiAlias(true);
        cPaint.setDither(true);

        BlurMaskFilter filter = new BlurMaskFilter(10f, BlurMaskFilter.Blur.INNER);
        cPaint.setMaskFilter(filter);


        int x = 150;
        int y = 150;
        int r = 100;

        glowCircle = new Path();
        glowCircle.addCircle(x, y, r, Path.Direction.CW);

        int color1 = Color.rgb(40, 40, 40);
        int color2 = Color.rgb(220, 220, 220);
        LinearGradient gradient = new LinearGradient(0, 0, 0, y*2, color2, color1, Shader.TileMode.REPEAT);
        cPaint.setShader(gradient);

        circle = new Path();
        circle.addCircle(x, y, r, Path.Direction.CW);

        tPaint = new Paint();
        tPaint.setTextSize(18f);
        tPaint.setTypeface(Typeface.DEFAULT_BOLD);
        tPaint.setColor(Color.BLACK);
        tPaint.setAntiAlias(true);
    }

    private void initAnimation() {
        animation = new RotateAnimation(0, 360, 150, 150);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.RESTART);
        animation.setDuration(7500L);
        animation.setInterpolator(new LinearInterpolator());
        startAnimation(animation);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (animation == null)
            initAnimation();

        canvas.drawPath(glowCircle, gPaint);
        canvas.drawPath(circle, cPaint);
        canvas.drawTextOnPath(QUOTE, circle, 0, 20, tPaint);
    }
}