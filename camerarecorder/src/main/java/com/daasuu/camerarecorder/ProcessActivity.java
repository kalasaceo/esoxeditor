package com.daasuu.camerarecorder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.xiaopo.flying.poiphoto.Define;
import com.xiaopo.flying.poiphoto.PhotoPicker;
import com.xiaopo.flying.puzzle.PuzzleLayout;
import com.xiaopo.flying.puzzle.PuzzlePiece;
import com.xiaopo.flying.puzzle.PuzzleView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.kalasa.library.R;
/**
 * @author wupanjie
 */
public class ProcessActivity extends AppCompatActivity implements View.OnClickListener {
  private static final int FLAG_CONTROL_LINE_SIZE = 1;
  private static final int FLAG_CONTROL_CORNER = 1 << 1;
  String f_path="empty";
  private PuzzleLayout puzzleLayout;
  private List<String> bitmapPaint;
  private PuzzleView puzzleView;
  private DegreeSeekBar degreeSeekBar;

  private List<Target> targets = new ArrayList<>();
  private int deviceWidth = 0;

  private int controlFlag;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_process);
    if (getSupportActionBar() != null) {
      getSupportActionBar().hide();
    }
    deviceWidth = getResources().getDisplayMetrics().widthPixels;

    int type = getIntent().getIntExtra("type", 0);
    int pieceSize = getIntent().getIntExtra("piece_size", 0);
    int themeId = getIntent().getIntExtra("theme_id", 0);
    bitmapPaint = getIntent().getStringArrayListExtra("photo_path");
    puzzleLayout = PuzzleUtils.getPuzzleLayout(type, pieceSize, themeId);

    initView();

    puzzleView.post(new Runnable() {
      @Override public void run() {
        loadPhoto();
      }
    });
  }

  @Override protected void onResume() {
    super.onResume();
  }
  @Override
  public void finish() {
    String storypath="none";
    Intent data = new Intent();
    data.putExtra("fpath", f_path);
    this.setResult(RESULT_OK, data);
    super.finish();
  }
  private void loadPhoto() {
    if (bitmapPaint == null) {
      loadPhotoFromRes();
      return;
    }

    final List<Bitmap> pieces = new ArrayList<>();

    final int count = bitmapPaint.size() > puzzleLayout.getAreaCount() ? puzzleLayout.getAreaCount()
        : bitmapPaint.size();

    for (int i = 0; i < count; i++) {
      final Target target = new Target() {
        @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
          pieces.add(bitmap);
          if (pieces.size() == count) {
            if (bitmapPaint.size() < puzzleLayout.getAreaCount()) {
              for (int i = 0; i < puzzleLayout.getAreaCount(); i++) {
                puzzleView.addPiece(pieces.get(i % count));
              }
            } else {
              puzzleView.addPieces(pieces);
            }
          }
          targets.remove(this);
        }

        @Override public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
      };

      Picasso.with(this)
          .load("file:///" + bitmapPaint.get(i))
          .resize(deviceWidth, deviceWidth)
          .centerInside()
          .config(Bitmap.Config.RGB_565)
          .into(target);

      targets.add(target);
    }
  }

  private void loadPhotoFromRes() {
    final List<Bitmap> pieces = new ArrayList<>();

    final int[] resIds = new int[] {
        R.drawable.demo1, R.drawable.demo2, R.drawable.demo3, R.drawable.demo4, R.drawable.demo5,
        R.drawable.demo6, R.drawable.demo7, R.drawable.demo8, R.drawable.demo9,
    };

    final int count =
        resIds.length > puzzleLayout.getAreaCount() ? puzzleLayout.getAreaCount() : resIds.length;

    for (int i = 0; i < count; i++) {
      final Target target = new Target() {
        @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
          pieces.add(bitmap);
          if (pieces.size() == count) {
            if (resIds.length < puzzleLayout.getAreaCount()) {
              for (int i = 0; i < puzzleLayout.getAreaCount(); i++) {
                puzzleView.addPiece(pieces.get(i % count));
              }
            } else {
              puzzleView.addPieces(pieces);
            }
          }
          targets.remove(this);
        }

        @Override public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
      };

      Picasso.with(this).load(resIds[i]).config(Bitmap.Config.RGB_565).into(target);

      targets.add(target);
    }
  }

  private void initView() {
    ImageView btnBack = (ImageView) findViewById(R.id.btn_cancel2);
    btnBack.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        onBackPressed();
      }
    });
    puzzleView = (PuzzleView) findViewById(R.id.puzzle_view);
    degreeSeekBar = (DegreeSeekBar) findViewById(R.id.degree_seek_bar);
    //TODO the method we can use to change the puzzle view's properties
    puzzleView.setPuzzleLayout(puzzleLayout);
    puzzleView.setTouchEnable(true);
    puzzleView.setNeedDrawLine(false);
    puzzleView.setNeedDrawOuterLine(false);
    puzzleView.setLineSize(4);
    puzzleView.setLineColor(Color.BLACK);
    puzzleView.setSelectedLineColor(Color.BLACK);
    puzzleView.setHandleBarColor(Color.BLACK);
    puzzleView.setAnimateDuration(300);
    puzzleView.setOnPieceSelectedListener(new PuzzleView.OnPieceSelectedListener() {
      @Override public void onPieceSelected(PuzzlePiece piece, int position) {
        Snackbar.make(puzzleView, "Piece " + position + " selected", Snackbar.LENGTH_SHORT).show();
      }
    });

    // currently the SlantPuzzleLayout do not support padding
    puzzleView.setPiecePadding(10);
    ImageView btnBorder = (ImageView) findViewById(R.id.btn_border);
    ImageView btnCorner = (ImageView) findViewById(R.id.btn_corner);


    btnBorder.setOnClickListener(this);
    btnCorner.setOnClickListener(this);

    Button btnSave = findViewById(R.id.btn_fsave);
    btnSave.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(final View view) {
        File file = FileUtils.getNewFile(ProcessActivity.this, "Puzzle");
        FileUtils.savePuzzle(puzzleView, file, 100, new Callback() {
          @Override public void onSuccess() {
            f_path=file.getAbsolutePath().toString();
            Snackbar.make(view,"saved", Snackbar.LENGTH_SHORT).show();
            finish();
          }
          @Override public void onFailed() {
            Snackbar.make(view,"failed", Snackbar.LENGTH_SHORT).show();
            finish();
          }
        });
      }
    });

    degreeSeekBar.setCurrentDegrees(puzzleView.getLineSize());
    degreeSeekBar.setDegreeRange(0, 30);
    degreeSeekBar.setScrollingListener(new DegreeSeekBar.ScrollingListener() {
      @Override public void onScrollStart() {

      }
      @Override public void onScroll(int currentDegrees) {
        switch (controlFlag) {
          case FLAG_CONTROL_LINE_SIZE:
            puzzleView.setLineSize(currentDegrees);
            break;
          case FLAG_CONTROL_CORNER:
            puzzleView.setPieceRadian(currentDegrees);
            break;
        }
      }

      @Override public void onScrollEnd() {

      }
    });
  }
  @Override public void onClick(View view) {
    int id = view.getId();
    if (id == R.id.btn_border) {
      controlFlag = FLAG_CONTROL_LINE_SIZE;
      puzzleView.setNeedDrawLine(!puzzleView.isNeedDrawLine());
      if (puzzleView.isNeedDrawLine()) {
        degreeSeekBar.setVisibility(View.VISIBLE);
        degreeSeekBar.setCurrentDegrees(puzzleView.getLineSize());
        degreeSeekBar.setDegreeRange(0, 30);
      } else {
        degreeSeekBar.setVisibility(View.INVISIBLE);
      }
    } else if (id == R.id.btn_corner) {
      if (controlFlag == FLAG_CONTROL_CORNER && degreeSeekBar.getVisibility() == View.VISIBLE) {
        degreeSeekBar.setVisibility(View.INVISIBLE);
        return;
      }
      degreeSeekBar.setCurrentDegrees((int) puzzleView.getPieceRadian());
      controlFlag = FLAG_CONTROL_CORNER;
      degreeSeekBar.setVisibility(View.VISIBLE);
      degreeSeekBar.setDegreeRange(0, 100);
    }
  }

  private void showSelectedPhotoDialog() {
    PhotoPicker.newInstance().setMaxCount(1).pick(this);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == Define.DEFAULT_REQUEST_CODE && resultCode == RESULT_OK) {
      List<String> paths = data.getStringArrayListExtra(Define.PATHS);
      String path = paths.get(0);

      final Target target = new Target() {
        @Override public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
          puzzleView.replace(bitmap, "");
        }

        @Override public void onBitmapFailed(Drawable errorDrawable) {
          Snackbar.make(puzzleView, "Replace Failed!", Snackbar.LENGTH_SHORT).show();
        }

        @Override public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
      };

      //noinspection SuspiciousNameCombination
      Picasso.with(this)
          .load("file:///" + path)
          .resize(deviceWidth, deviceWidth)
          .centerInside()
          .config(Bitmap.Config.RGB_565)
          .into(target);
    }
  }
}
