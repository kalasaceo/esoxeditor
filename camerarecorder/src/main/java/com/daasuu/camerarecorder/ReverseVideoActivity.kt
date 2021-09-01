package com.daasuu.camerarecorder
import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.kalasa.library.R
import com.jaiselrahman.filepicker.model.MediaFile
import com.simform.videooperations.*
import com.timqi.sectorprogressview.SectorProgressView
import kotlinx.android.synthetic.main.activity_reverse.isWithAudioSwitch
import kotlinx.android.synthetic.main.activity_reverse.mProgressView
class ReverseVideoActivity : BaseActivity(R.layout.activity_reverse, R.string.reverse_video) {
    private var isInputVideoSelected: Boolean = false
    private var p_comp: TextView? = null
    private var thistime="1";
    private var thisdata="1";
    private var VideoOutputPath="none"
    private var spv: SectorProgressView? = null
    override fun initialization() {
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        spv = findViewById<View>(R.id.spv) as SectorProgressView
        p_comp = findViewById<View>(R.id.progress_comp) as TextView
        val intent: Intent = intent
        thisdata = intent.getStringExtra("LocalVideoPath").toString()
        thistime = intent.getStringExtra("LocalVideoTime").toString()
        if (thisdata != null) {
            isInputVideoSelected = true
            processStart()
            reverseProcess()
        }
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            /*R.id.btnVideoPath -> {
                Common.selectFile(this, maxSelection = 1, isImageSelection = false, isAudioSelection = false)
            }
            R.id.btnMotion -> {
                when {
                    !isInputVideoSelected -> {
                        Toast.makeText(this, getString(R.string.input_video_validate_message), Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        processStart()
                        reverseProcess()
                    }
                }
            }*/
        }
    }
    private fun reverseProcess() {
        val outputPath = Common.getFilePath(this, Common.VIDEO)
        val query = ffmpegQueryExtension.videoReverse(thisdata, isWithAudioSwitch.isChecked, outputPath)
        CallBackOfQuery().callQuery(this, query, object : FFmpegCallBack {
            override fun process(logMessage: LogMessage) {
            }
            override fun statisticsProcess(statistics: Statistics) {
                super.statisticsProcess(statistics)
                var comp = ((((statistics.time.toFloat() / 1000) / (thistime.toFloat() / 1000)) * 100).toInt() / 1000)
                spv?.setPercent((comp).toFloat())
                p_comp?.text = ((((statistics.time.toFloat() / 1000) / (thistime.toFloat() / 1000)) * 100).toInt() / 1000).toString() + "%"
            }
            override fun success() {
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
    override fun finish() {
        val data = Intent()
        data.putExtra("returnKey17", VideoOutputPath.toString())
        setResult(RESULT_OK, data)
        super.finish()
    }
    override fun selectedFiles(mediaFiles: List<MediaFile>?, requestCode: Int) {
        when (requestCode) {
            Common.VIDEO_FILE_REQUEST_CODE -> {
                if (mediaFiles != null && mediaFiles.isNotEmpty()) {
                    //tvInputPathVideo.text = mediaFiles[0].path
                    isInputVideoSelected = true
                } else {
                    Toast.makeText(this, getString(R.string.video_not_selected_toast_message), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun processStop() {
        runOnUiThread {
            mProgressView.visibility = View.GONE
        }
    }

    private fun processStart() {
        mProgressView.visibility = View.VISIBLE
    }
}