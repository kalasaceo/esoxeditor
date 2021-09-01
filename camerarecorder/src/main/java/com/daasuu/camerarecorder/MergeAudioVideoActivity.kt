package com.daasuu.camerarecorder
import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.jaiselrahman.filepicker.model.MediaFile
import com.daasuu.camerarecorder.BaseActivity
import com.kalasa.library.R
import com.simform.videooperations.*
import com.timqi.sectorprogressview.SectorProgressView
import kotlinx.android.synthetic.main.activity_add_text_on_video.*
import kotlinx.android.synthetic.main.activity_merge_audio_video.*
import kotlinx.android.synthetic.main.activity_merge_audio_video.mProgressView
class MergeAudioVideoActivity : BaseActivity(R.layout.activity_merge_audio_video, R.string.merge_video_and_audio) {
    private var isInputVideoSelected: Boolean = false
    private var isInputAudioSelected: Boolean = false
    private var spv: SectorProgressView? = null
    private var thistime="1"
    private var p_comp: TextView? = null
    private var VideoOutputPath="none"
    private var video_path="empty"
    private var music_path="empty"
    override fun initialization() {
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        val intent: Intent = intent
        video_path = intent.getStringExtra("PassedVideoPath").toString()
        music_path = intent.getStringExtra("PassedMusicTime").toString()
        thistime = intent.getStringExtra("PassedVideoTime").toString()
        if (video_path != null) {
            isInputVideoSelected = true
            isInputAudioSelected = true
            processStart()
            mergeProcess()
        }
        spv = findViewById<View>(R.id.spv) as SectorProgressView
        p_comp = findViewById<View>(R.id.progress_comp) as TextView
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            /*R.id.btnVideoPath -> {
                Common.selectFile(this, maxSelection = 1, isImageSelection = false, isAudioSelection = false)
            }
            R.id.btnMp3Path -> {
                Common.selectFile(this, maxSelection = 1, isImageSelection = false, isAudioSelection = true)
            }
            R.id.btnMerge -> {
                when {
                    !isInputVideoSelected -> {
                        Toast.makeText(this, getString(R.string.input_video_validate_message), Toast.LENGTH_SHORT).show()
                    }
                    !isInputAudioSelected -> {
                        Toast.makeText(this, getString(R.string.please_select_input_audio), Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        processStart()
                        mergeProcess()
                    }
                }
            }*/
        }
    }
    private fun mergeProcess() {
        val outputPath = Common.getFilePath(this, Common.VIDEO)
        val query = mergeAudioVideo(video_path, music_path, outputPath)
        CallBackOfQuery().callQuery(this, query, object : FFmpegCallBack {
            override fun process(logMessage: LogMessage) {
                tvOutputPath2.text = logMessage.text
            }
            override fun statisticsProcess(statistics: Statistics) {
                tvOutputPath3.text = statistics.toString()
                super.statisticsProcess(statistics)
            var comp=((((statistics.time.toFloat()/1000)/(thistime.toFloat()/1000))*100).toInt()/1000)
            spv?.setPercent((comp).toFloat())
            p_comp?.text=((((statistics.time.toFloat()/1000)/(thistime.toFloat()/1000))*100).toInt()/1000).toString()+"%"
        }
            override fun success() {
                VideoOutputPath=outputPath
                processStop()
                finish()
            }
            override fun cancel() {
                processStop()
                finish()
            }
            override fun failed() {
                processStop()
                finish()
            }
        })
    }
    /*override fun selectedFiles(mediaFiles: List<MediaFile>?, requestCode: Int) {
        when (requestCode) {
            Common.VIDEO_FILE_REQUEST_CODE -> {
                if (mediaFiles != null && mediaFiles.isNotEmpty()) {
                    isInputVideoSelected = true
                } else {
                    Toast.makeText(this, getString(R.string.video_not_selected_toast_message), Toast.LENGTH_SHORT).show()
                }
            }
            Common.AUDIO_FILE_REQUEST_CODE -> {
                if (mediaFiles != null && mediaFiles.isNotEmpty()) {
                    isInputAudioSelected = true
                } else {
                    Toast.makeText(this, getString(R.string.video_not_selected_toast_message), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }*/
    private fun processStop() {
        runOnUiThread {
            mProgressView.visibility = View.GONE
        }
    }
    private fun processStart() {
        mProgressView.visibility = View.VISIBLE
    }
    fun mergeAudioVideo(inputVideo: String, inputAudio: String, output: String): Array<String> {
        val inputs: ArrayList<String> = ArrayList()
        inputs.apply {
            add("-i")
            add(inputVideo)
            add("-i")
            add("-r")
            add("24")
            add(inputAudio)
            add("-filter_complex")
            add("[1:a]volume=0.15,apad[A];[0:a][A]amerge[out]")
            add("-c:v")
            add("copy")
            add("-map")
            add("0:v")
            add("-map")
            add("[out]")
            add("-y")
            add("-shortest")
            add("-preset")
            add("ultrafast")
            add(output)
        }
        return inputs.toArray(arrayOfNulls<String>(inputs.size))
    }
    override fun finish() {
        val data = Intent()
        data.putExtra("returnKey2", VideoOutputPath)
        setResult(RESULT_OK, data)
        super.finish()
    }
}