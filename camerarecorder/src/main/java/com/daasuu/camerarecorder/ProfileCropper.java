package com.daasuu.camerarecorder;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;
import com.kalasa.library.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.fenchtose.nocropper.BitmapResult;
import com.fenchtose.nocropper.CropInfo;
import com.fenchtose.nocropper.CropMatrix;
import com.fenchtose.nocropper.CropResult;
import com.fenchtose.nocropper.CropState;
import com.fenchtose.nocropper.CropperCallback;
import com.fenchtose.nocropper.CropperView;
import com.fenchtose.nocropper.ScaledCropper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class ProfileCropper extends AppCompatActivity {
    private Paint gPaint;
    private Paint cPaint;
    private Path glowCircle;
    private Path circle;
    private Paint tPaint;
    private static final int REQUEST_CODE_READ_PERMISSION = 22;
    private static final int REQUEST_GALLERY = 21;
    private static final String TAG = "MainActivity";
    private String status="empty";
    CropperView mImageView;
    CheckBox originalImageCheckbox;
    private static final String QUOTE = "proud to be indian";
    private Bitmap originalBitmap;
    private Bitmap mBitmap;
    private boolean isSnappedToCenter = false;
    private String PathChoosen="empty";
    private int rotationCount = 0;

    private HashMap<String, CropMatrix> matrixMap = new HashMap<>();
    private String currentFilePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropper);
        mImageView = findViewById(R.id.imageview);
        originalImageCheckbox = findViewById(R.id.original_checkbox);
        findViewById(R.id.image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGalleryIntent();
            }
        });
        findViewById(R.id.crop_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageCropClicked();
            }
        });

        findViewById(R.id.rotate_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateImage();

            }
        });

        findViewById(R.id.snap_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snapImage();
            }
        });
        mImageView.setDebug(true);
        mImageView.setGridCallback(new CropperView.GridCallback() {
            @Override
            public boolean onGestureStarted() {
                return true;
            }

            @Override
            public boolean onGestureCompleted() {
                return false;
            }
        });
        startGalleryIntent();
    }
    private void SetProfileImage() {
        String path = "/storage/emulated/0/Movies/.required/cropped_ppic.png";
        if(new File(path).exists()){
            Bitmap xx=getRoundBitmap(BitmapFactory.decodeFile(path));
            String rpath_src="/storage/emulated/0/Movies/.required/testrounded_ppic.png";
            String rpath_des="/storage/emulated/0/Movies/.required/rounded_ppic.png";
            if (xx != null) {
                try {
                    BitmapUtils.writeBitmapToFile(xx, new File(rpath_src), 100);
                    compressppic2(rpath_src,rpath_des);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                //Toast.makeText(ProfileCropper.this, "bitmap", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public Bitmap getRoundBitmap(Bitmap bitmap) {
        int min = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap bitmapRounded = Bitmap.createBitmap(min, min, bitmap.getConfig());
        Canvas canvas = new Canvas(bitmapRounded);
        gPaint = new Paint();
        gPaint.setAlpha(255);
        gPaint.setShadowLayer(40, 0, 0, Color.argb(200, 255, 0, 0));
        cPaint = new Paint();
        cPaint.setAntiAlias(true);
        cPaint.setDither(true);
        BlurMaskFilter filter = new BlurMaskFilter(10f, BlurMaskFilter.Blur.INNER);
        cPaint.setMaskFilter(filter);
        int x = min/2-20;
        int y = min/2-20;
        int r = min/2-20;
        glowCircle = new Path();
        glowCircle.addCircle(x, y, r, Path.Direction.CW);
        int color1 = Color.parseColor("#FF9933");
        int color2 = Color.parseColor("#138808");
        LinearGradient gradient = new LinearGradient(0, 0, 0, y*2, color2, color1, Shader.TileMode.REPEAT);
        cPaint.setShader(gradient);
        //cPaint.setColor(Color.WHITE);
        circle = new Path();
        circle.addCircle(x, y, r, Path.Direction.CW);
        tPaint = new Paint();
        tPaint.setTextSize(min/8);
        tPaint.setTypeface(Typeface.DEFAULT);
        tPaint.setColor(Color.GREEN);
        tPaint.setAntiAlias(true);
        //canvas.drawPath(glowCircle, gPaint);
        canvas.drawPath(circle, cPaint);
        tPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(60.0f, 60.0f, min-90, min-90)), min / 2, min / 2, tPaint);
        canvas.drawTextOnPath(QUOTE, circle, 30, 30, tPaint);
        return bitmapRounded;
    }
    public void onImageButtonClicked() {
        startGalleryIntent();
    }
    public void onImageCropClicked() {
            cropImageAsync();
    }
    private void loadNewImage(String filePath) {
        this.currentFilePath = filePath;
        rotationCount = 0;
        Log.i(TAG, "load image: " + filePath);
        mBitmap = BitmapFactory.decodeFile(filePath);
        originalBitmap = mBitmap;
        Log.i(TAG, "bitmap: " + mBitmap.getWidth() + " " + mBitmap.getHeight());

        int maxP = Math.max(mBitmap.getWidth(), mBitmap.getHeight());
        float scale1280 = (float)maxP / 1280;
        Log.i(TAG, "scaled: " + scale1280 + " - " + (1/scale1280));

        if (mImageView.getWidth() != 0) {
            mImageView.setMaxZoom(mImageView.getWidth() * 2 / 1280f);
        } else {

            ViewTreeObserver vto = mImageView.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                    mImageView.setMaxZoom(mImageView.getWidth() * 2 / 1280f);
                    return true;
                }
            });

        }

        mBitmap = Bitmap.createScaledBitmap(mBitmap, (int)(mBitmap.getWidth()/scale1280),
                (int)(mBitmap.getHeight()/scale1280), true);

        mImageView.setImageBitmap(mBitmap);
        final CropMatrix matrix = matrixMap.get(filePath);
        if (matrix != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mImageView.setCropMatrix(matrix, true);
                }
            }, 30);
        }
    }
    private void startGalleryIntent() {
        if (currentFilePath != null) {
            matrixMap.put(currentFilePath, mImageView.getCropMatrix());
        }
        if (!hasGalleryPermission()) {
            askForGalleryPermission();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }
    private boolean hasGalleryPermission() {
        return ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }
    private void askForGalleryPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_READ_PERMISSION);
    }
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent resultIntent) {
        super.onActivityResult(requestCode, responseCode, resultIntent);
        if (responseCode == RESULT_OK) {
            String absPath = BitmapUtils.getFilePathFromUri(this, resultIntent.getData());
            PathChoosen=absPath;
            loadNewImage(absPath);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_READ_PERMISSION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startGalleryIntent();
                return;
            }
        }

        Toast.makeText(this, "Gallery permission not granted", Toast.LENGTH_SHORT).show();
    }

    private void cropImageAsync() {
        CropState state = mImageView.getCroppedBitmapAsync(new CropperCallback() {
            @Override
            public void onCropped(Bitmap bitmap) {
                String path_src="/storage/emulated/0/Movies/.required/testcropped_ppic.png";
                String path_des="/storage/emulated/0/Movies/.required/cropped_ppic.png";
                if (bitmap != null) {
                    try {
                        BitmapUtils.writeBitmapToFile(bitmap, new File(path_src), 100);
                        compressppic1(path_src,path_des);
                        status="completed"+"=="+currentFilePath;
                        finish();
                        overridePendingTransition(R.anim.activity_slider_in_right, R.anim.activity_slider_out_left);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onOutOfMemoryError() {

            }
        });

        if (state == CropState.FAILURE_GESTURE_IN_PROCESS) {
            Toast.makeText(this, "unable to crop. Gesture in progress", Toast.LENGTH_SHORT).show();
        }

        if (originalImageCheckbox.isChecked()) {
            cropOriginalImageAsync();
        }
    }
    void compressppic1(String oldin_path,String in_path)
    {
        File oldFile= new File(oldin_path);
        Luban.with(this)
                .load(oldFile)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }
                    @Override
                    public void onSuccess(File newFile) {
                        File newf= new File(in_path);
                        try {
                            copy(newFile,newf);
                            SetProfileImage();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ProfileCropper.this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).launch();
    }
    void compressppic2(String oldin_path,String in_path)
    {
        File oldFile= new File(oldin_path);
        Luban.with(this)
                .load(oldFile)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }
                    @Override
                    public void onSuccess(File newFile) {
                        File newf= new File(in_path);
                        try {
                            copy(newFile,newf);
                            newppic();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ProfileCropper.this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).launch();
    }
    void newppic()
    {
        String cpath_des="/storage/emulated/0/Movies/.required/ppic.png";
        compressppic3(PathChoosen,cpath_des);
    }
    void compressppic3(String oldin_path,String in_path)
    {
        File oldFile= new File(oldin_path);
        Luban.with(this)
                .load(oldFile)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }
                    @Override
                    public void onSuccess(File newFile) {
                        File newf= new File(in_path);
                        try {
                            copy(newFile,newf);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ProfileCropper.this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).launch();
    }
    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    private void cropImage() {

        BitmapResult result = mImageView.getCroppedBitmap();

        if (result.getState() == CropState.FAILURE_GESTURE_IN_PROCESS) {
            Toast.makeText(this, "unable to crop. Gesture in progress", Toast.LENGTH_SHORT).show();
            return;
        }

        Bitmap bitmap = result.getBitmap();

        if (bitmap != null) {
            Log.d("Cropper", "crop1 bitmap: " + bitmap.getWidth() + ", " + bitmap.getHeight());
            try {
                BitmapUtils.writeBitmapToFile(bitmap, new File(Environment.getExternalStorageDirectory() + "/crop_test.jpg"), 90);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (originalImageCheckbox.isChecked()) {
            cropOriginalImage();
        }

    }

    private ScaledCropper prepareCropForOriginalImage() {
        CropResult result = mImageView.getCropInfo();
        if (result.getCropInfo() == null) {
            return null;
        }

        float scale;
        if (rotationCount % 2 == 0) {
            // same width and height
            scale = (float) originalBitmap.getWidth()/mBitmap.getWidth();
        } else {
            // width and height are interchanged
            scale = (float) originalBitmap.getWidth()/mBitmap.getHeight();
        }

        CropInfo cropInfo = result.getCropInfo().rotate90XTimes(mBitmap.getWidth(), mBitmap.getHeight(), rotationCount);
        return new ScaledCropper(cropInfo, originalBitmap, scale);
    }

    private void cropOriginalImage() {
        if (originalBitmap != null) {
            ScaledCropper cropper = prepareCropForOriginalImage();
            if (cropper == null) {
                return;
            }

            Bitmap bitmap = cropper.cropBitmap();
            if (bitmap != null) {
                try {
                    BitmapUtils.writeBitmapToFile(bitmap, new File(Environment.getExternalStorageDirectory() + "/crop_test_info_orig.jpg"), 90);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void cropOriginalImageAsync() {
        if (originalBitmap != null) {
            ScaledCropper cropper = prepareCropForOriginalImage();
            if (cropper == null) {
                return;
            }

            cropper.crop(new CropperCallback() {
                @Override
                public void onCropped(Bitmap bitmap) {
                    if (bitmap != null) {
                        try {
                            BitmapUtils.writeBitmapToFile(bitmap, new File(Environment.getExternalStorageDirectory() + "/crop_test_info_orig.jpg"), 90);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private void rotateImage() {
        if (mBitmap == null) {
            Log.e(TAG, "bitmap is not loaded yet");
            return;
        }

        mBitmap = BitmapUtils.rotateBitmap(mBitmap, 90);
        mImageView.setImageBitmap(mBitmap);
        rotationCount++;
    }

    private void snapImage() {
        if (isSnappedToCenter) {
            mImageView.cropToCenter();
        } else {
            mImageView.fitToCenter();
        }
        isSnappedToCenter = !isSnappedToCenter;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_slider_in_right, R.anim.activity_slider_out_left);
        finish();
    }
    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("Status", status);
        this.setResult(RESULT_OK, data);
        super.finish();
    }
}
