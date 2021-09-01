package com.daasuu.camerarecorder
import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.jaiselrahman.filepicker.model.MediaFile
import com.daasuu.camerarecorder.BaseActivity
import com.kalasa.library.R
import com.simform.videooperations.*
import com.simform.videooperations.Common.VIDEO
import com.simform.videooperations.Common.getFilePath
import com.simform.videooperations.Common.selectFile
import com.timqi.sectorprogressview.SectorProgressView
import kotlinx.android.synthetic.main.activity_add_text_on_video.*
import java.util.concurrent.CompletableFuture.runAsync
import kotlinx.android.synthetic.main.activity_add_water_mark_on_video.btnAdd
import kotlinx.android.synthetic.main.activity_add_water_mark_on_video.btnImagePath
import kotlinx.android.synthetic.main.activity_add_water_mark_on_video.btnVideoPath
import kotlinx.android.synthetic.main.activity_add_water_mark_on_video.edtXPos
import kotlinx.android.synthetic.main.activity_add_water_mark_on_video.edtYPos
import kotlinx.android.synthetic.main.activity_add_water_mark_on_video.mProgressView
import kotlinx.android.synthetic.main.activity_add_water_mark_on_video.tvInputPathImage
import kotlinx.android.synthetic.main.activity_add_water_mark_on_video.tvInputPathVideo
import kotlinx.android.synthetic.main.activity_add_water_mark_on_video.tvOutputPath
class AddWaterMarkOnVideoActivity : BaseActivity(R.layout.activity_add_water_mark_on_video, R.string.add_water_mark_on_video) {
    private var isInputVideoSelected = false
    private var isWaterMarkImageSelected = false
    private var VideoOutputPath="none"
    private var AddedStickerPath="none"
    private var p_comp: TextView? = null
    private var posX2="0"
    private var posY2="0"
    private var edtXPosstr="75"
    private var edtYPosstr="180"
    private var thistime="1";
    private var spv: SectorProgressView? = null
    override fun initialization() {
        btnVideoPath.setOnClickListener(this)
        btnImagePath.setOnClickListener(this)
        btnAdd.setOnClickListener(this)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        spv = findViewById<View>(R.id.spv) as SectorProgressView
        p_comp = findViewById<View>(R.id.progress_comp) as TextView
        val intent: Intent = intent
        var thisdata = intent.getStringExtra("LocalVideoPath")
        var stickerthisdata = intent.getStringExtra("StickerPath")
        thistime = intent.getStringExtra("LocalVideoTime").toString()
        AddedStickerPath= intent.getStringExtra("OtherStickerPath").toString()
        posX2= intent.getStringExtra("OtherStickerPosX").toString()
        posY2= intent.getStringExtra("OtherStickerPosY").toString()
        if (thisdata != null) {
            isInputVideoSelected = true
            isWaterMarkImageSelected =true
            edtXPos.setText("20");
            edtYPos.setText("20");
            tvInputPathVideo.text = thisdata
            tvInputPathImage.text = stickerthisdata
            var xx=tvInputPathVideo.text.toString()
            //findViewById<View>(R.id.btnVideoPath).visibility = View.GONE
            processStart()
            addWaterMarkProcess()
            if (mediaFiles != null) {
                runAsync {
                    retriever = MediaMetadataRetriever()
                    retriever?.setDataSource(tvInputPathVideo.text.toString())
                    val bit = retriever?.frameAtTime
                    width = bit?.width
                    height = bit?.height
                }
            }
        }
    }
    override fun onClick(v: View?) {
        when (v?.id) {
           /* R.id.btnVideoPath -> {
                selectFile(this, maxSelection = 1, isImageSelection = false, isAudioSelection = false)
            }
            R.id.btnImagePath -> {
                selectFile(this, maxSelection = 1, isImageSelection = true, isAudioSelection = false)
            }
            R.id.btnAdd -> {
                when {
                    !isInputVideoSelected -> {
                        Toast.makeText(this, getString(R.string.input_video_validate_message), Toast.LENGTH_SHORT).show()
                    }
                    !isWaterMarkImageSelected -> {
                        Toast.makeText(this, getString(R.string.input_image_validate_message), Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(edtXPos.text.toString()) -> {
                        Toast.makeText(this, getString(R.string.x_position_validation), Toast.LENGTH_SHORT).show()
                    }
                    edtXPos.text.toString().toFloat() > 100 || edtXPos.text.toString().toFloat() <= 0 -> {
                        Toast.makeText(this, getString(R.string.x_validation_invalid), Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(edtYPos.text.toString()) -> {
                        Toast.makeText(this, getString(R.string.y_position_validation), Toast.LENGTH_SHORT).show()
                    }
                    edtYPos.text.toString().toFloat() > 100 || edtYPos.text.toString().toFloat() <= 0 -> {
                        Toast.makeText(this, getString(R.string.y_validation_invalid), Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        processStart()
                        addWaterMarkProcess()
                    }
                }
            }*/
        }
    }
    @SuppressLint("NewApi")
    override fun selectedFiles(mediaFiles: List<MediaFile>?, fileRequestCode: Int) {
        when (fileRequestCode) {
            Common.VIDEO_FILE_REQUEST_CODE -> {
                if (mediaFiles != null && mediaFiles.isNotEmpty()) {
                    tvInputPathVideo.text = mediaFiles[0].path
                    isInputVideoSelected = true
                    runAsync {
                        retriever = MediaMetadataRetriever()
                        retriever?.setDataSource(tvInputPathVideo.text.toString())
                        val bit = retriever?.frameAtTime
                        width = bit?.width
                        height = bit?.height
                    }
                } else {
                    Toast.makeText(this, getString(R.string.video_not_selected_toast_message), Toast.LENGTH_SHORT).show()
                }
            }
            Common.IMAGE_FILE_REQUEST_CODE -> {
                if (mediaFiles != null && mediaFiles.isNotEmpty()) {
                    tvInputPathImage.text = mediaFiles[0].path
                    isWaterMarkImageSelected = true
                } else {
                    Toast.makeText(this, getString(R.string.image_not_selected_toast_message), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun addWaterMarkProcess() {
        val outputPath = getFilePath(this, VIDEO)
        val xPos = ((edtXPosstr.toString().toFloat().times(1)).div(100))*1000
        val yPos = ((edtYPosstr.toString().toFloat().times(1)).div(100))*1000
        val query = addVideoWaterMark(tvInputPathVideo.text.toString(), tvInputPathImage.text.toString(), xPos, yPos, outputPath)
        CallBackOfQuery().callQuery(this, query, object : FFmpegCallBack {
            override fun statisticsProcess(statistics: Statistics) {
                super.statisticsProcess(statistics)
                var comp=((((statistics.time.toFloat()/1000)/(thistime.toFloat()/1000))*100).toInt()/1000)
                spv?.setPercent((comp).toFloat())
                p_comp?.text=((((statistics.time.toFloat()/1000)/(thistime.toFloat()/1000))*100).toInt()/1000).toString()+"%"
            }
            override fun process(logMessage: LogMessage) {
                tvOutputPath.text = logMessage.text
            }
            override fun success() {
                tvOutputPath.text = String.format(getString(R.string.output_path), outputPath)
                processStop()
                VideoOutputPath=outputPath;
                finish()
            }
            override fun cancel() {
                processStop()
            }
            override fun failed() {
                processStop()
            }
        })
    }
    fun addVideoWaterMark(inputVideo: String, imageInput: String, posX: Float?, posY: Float?, output: String): Array<String> {
        if(AddedStickerPath!="empty") {
            return addVideoWaterMark_MULTI(
                tvInputPathVideo.text.toString(),
                tvInputPathImage.text.toString(),
                posX,
                posY,
                output,
                posX2.toFloat(),
                posY2.toFloat(),
                AddedStickerPath
            )
        }
        else
        {
            return addVideoWaterMark_SINGLE(
                tvInputPathVideo.text.toString(),
                tvInputPathImage.text.toString(),
                posX,
                posY,
                output
            )
        }
        }
    fun addVideoWaterMark_SINGLE(inputVideo: String, imageInput: String, posX: Float?, posY: Float?, output: String): Array<String> {
        val inputs: ArrayList<String> = ArrayList()
        inputs.apply {
            add("-i")
            add(inputVideo)
            add("-i")
            add(imageInput)
            add("-r")
            add("24")
            add("-qscale:v")
            add("1")
            add("-filter_complex")
            add("overlay=$posX:$posY")
            add("-preset")
            add("veryfast")
            add(output)
        }
        return inputs.toArray(arrayOfNulls<String>(inputs.size))
    }
    fun addVideoWaterMark_MULTI(inputVideo: String, imageInput: String, posX: Float?, posY: Float?, output: String,posX2: Float?, posY2: Float?,NewStickerPath: String): Array<String> {
        val inputs: ArrayList<String> = ArrayList()
        inputs.apply {
            add("-i")
            add(inputVideo)
            add("-i")
            add(imageInput)
            add("-i")
            add(NewStickerPath)
            add("-qscale:v")
            add("-r")
            add("24")
            add("1")
            add("-filter_complex")
            add("[1:v]scale=270:-1[img1];[2:v]scale=270:-1[img2];[0:v][img1]overlay=$posX:$posY[bkg];[bkg][img2]overlay=$posX2:$posY2")
            add("-preset")
            add("veryfast")
            add(output)
        }
        return inputs.toArray(arrayOfNulls<String>(inputs.size))
    }
    private fun processStop() {
        runOnUiThread {
            btnVideoPath.isEnabled = true
            btnImagePath.isEnabled = true
            btnAdd.isEnabled = true
            mProgressView.visibility = View.GONE
        }
    }
    override fun finish() {
        val data = Intent()
        data.putExtra("returnKey2", VideoOutputPath.toString())
        setResult(RESULT_OK, data)
        super.finish()
    }
    private fun processStart() {
        btnVideoPath.isEnabled = false
        btnImagePath.isEnabled = false
        btnAdd.isEnabled = false
        mProgressView.visibility = View.VISIBLE
    }
}