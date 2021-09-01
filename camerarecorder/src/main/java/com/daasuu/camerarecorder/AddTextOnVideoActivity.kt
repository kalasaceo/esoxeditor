package com.daasuu.camerarecorder
import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.kalasa.library.R
import android.widget.Toast
import com.jaiselrahman.filepicker.model.MediaFile
import com.simform.videooperations.*
import com.simform.videooperations.Common.getFileFromAssets
import com.timqi.sectorprogressview.SectorProgressView
import kotlinx.android.synthetic.main.activity_add_text_on_video.*
import java.util.concurrent.CompletableFuture.runAsync
class AddTextOnVideoActivity : BaseActivity(R.layout.activity_add_text_on_video, R.string.add_text_on_video) {
    private var isInputVideoSelected = false
    private var VideoOutputPath="none"
    private var p_comp: TextView? = null
    private var stickertopass="none"
    private var edtXPosstr="20"
    private var edtYPosstr="30"
    private var tvInputPathVideostr="none"
    private var edtTextstr="none"
    private var spv: SectorProgressView? = null
    private var thistime="1";
    private var thiscolor="#000000";
    override fun initialization() {
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        stickertopass = "/storage/emulated/0/Movies/image.png"
        spv = findViewById<View>(R.id.spv) as SectorProgressView
        p_comp = findViewById<View>(R.id.progress_comp) as TextView
        val intent: Intent = intent
        var thisdata = intent.getStringExtra("LocalVideoPath")
        //??//thistime = intent.getStringExtra("LocalVideoTime")
        var thistext = intent.getStringExtra("LocalVideoText")
        thiscolor = intent.getStringExtra("LocalTextColor").toString()
        if (thisdata != null) {
            isInputVideoSelected = true
            edtXPosstr="20"
            edtYPosstr="30"
            edtTextstr= thistext.toString()
            tvInputPathVideostr= thisdata
            var xx=tvInputPathVideostr.toString()
            processStart()
            addTextProcess()
            if (mediaFiles != null) {
                runAsync {
                    retriever = MediaMetadataRetriever()
                    retriever?.setDataSource(tvInputPathVideostr.toString())
                    val bit = retriever?.frameAtTime
                    width = bit?.width
                    height = bit?.height
                }
            }
        }
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            /*R.id.btnVideoPath -> {
                Common.selectFile(this, maxSelection = 1, isImageSelection = false, isAudioSelection = false)
            }
            R.id.btnAdd -> {
                when {
                    !isInputVideoSelected -> {
                        Toast.makeText(this, getString(R.string.input_video_validate_message), Toast.LENGTH_SHORT).show()
                    }
                    TextUtils.isEmpty(edtTextstr.toString()) -> {
                        Toast.makeText(this, getString(R.string.please_add_text_validation), Toast.LENGTH_SHORT).show()
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
                        addTextProcess()
                    }
                }
            }*/
        }
    }
    private fun addTextProcess() {
        val outputPath = Common.getFilePath(this, Common.VIDEO)
        val xPos = ((edtXPosstr.toString().toFloat().times(1)).div(100))*1000
        val yPos = ((edtYPosstr.toString().toFloat().times(1)).div(100))*1000
        val fontPath = getFileFromAssets(this, "FingerPaint-Regular.ttf").absolutePath
        //val query = ffmpegQueryExtension.addTextOnVideo(tvInputPathVideostr.toString(),edtTextstr.toString(), xPos, yPos, fontPath = fontPath, isTextBackgroundDisplay = false, fontSize = 79, fontcolor = "black", output = outputPath)
        val query=addTextOnVideoFun(tvInputPathVideostr.toString(),edtTextstr.toString(), xPos, yPos, fontPath = fontPath, isTextBackgroundDisplay = true, fontSize = 79, fontcolor = thiscolor, output = outputPath);
            CallBackOfQuery().callQuery(this, query, object : FFmpegCallBack {
            override fun process(logMessage: LogMessage) {
            }
            override fun statisticsProcess(statistics: Statistics) {
                super.statisticsProcess(statistics)
                var comp=((((statistics.time.toFloat()/1000)/(thistime.toFloat()/1000))*100).toInt()/1000)
                spv?.setPercent((comp).toFloat())
                p_comp?.text=((((statistics.time.toFloat()/1000)/(thistime.toFloat()/1000))*100).toInt()/1000).toString()+"%"
            }
            override fun success() {
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
    private fun processStop() {
        runOnUiThread {
            mProgressView.visibility = View.GONE
        }
    }
    private fun processStart() {
            mProgressView.visibility = View.VISIBLE
    }
    override fun finish() {
        val data = Intent()
        data.putExtra("returnKey1", VideoOutputPath.toString())
        setResult(RESULT_OK, data)
        super.finish()
    }
    fun addTextOnVideoFun(inputVideo: String, textInput: String, posX: Float?, posY: Float?, fontPath: String, isTextBackgroundDisplay: Boolean, fontSize: Int, fontcolor: String, output: String): Array<String> {
        val inputs: ArrayList<String> = ArrayList()
        var borderQuery = ""
        if (isTextBackgroundDisplay) {
            borderQuery = ":box=1:boxcolor=white@0.9:boxborderw=19:enable='between(t,2,10)"
        }
        inputs.apply {
            add("-i")
            add(inputVideo)
            add("-qscale:v")
            add("1")
            add("-r")
            add("24")
            add("-vf")
            add("drawtext=text='$textInput':fontfile=$fontPath:x=$posX:y=$posY:fontsize=$fontSize:line_spacing=7:shadowcolor=black@0.5:shadowx=2:shadowy=2:fontcolor=$fontcolor${borderQuery.trim()}")
            add("-c:a")
            add("copy")
            add("-preset")
            add("veryfast")
            add(output)
        }
        /*inputs.apply {
            add("-i")
            add(inputVideo)
            add("-s")
            add("1088x1920")
            add("-b:v")
            add("14000k")
            add("-vf")
            add("drawtext=text='$textInput':fontfile=$fontPath:x=$posX:y=$posY:fontsize=$fontSize:fontcolor=$fontcolor${borderQuery.trim()}")
            add("-c:a")
            add("copy")
            add("-preset")
            add("ultrafast")
            add(output)
        }*/
        return inputs.toArray(arrayOfNulls<String>(inputs.size))
    }
    @SuppressLint("NewApi")
    override fun selectedFiles(mediaFiles: List<MediaFile>?, requestCode: Int) {
        when (requestCode) {
            Common.VIDEO_FILE_REQUEST_CODE -> {
                if (mediaFiles != null && mediaFiles.isNotEmpty()) {
                    tvInputPathVideostr = mediaFiles[0].path
                    var xx=tvInputPathVideostr.toString()
                    isInputVideoSelected = true
                    runAsync {
                        retriever = MediaMetadataRetriever()
                        retriever?.setDataSource(tvInputPathVideostr.toString())
                        val bit = retriever?.frameAtTime
                        width = bit?.width
                        height = bit?.height
                    }
                } else {
                    Toast.makeText(this, getString(R.string.video_not_selected_toast_message), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}