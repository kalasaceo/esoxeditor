package com.daasuu.camerarecorder
import android.Manifest
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaRecorder
import com.kalasa.library.R
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.arthenica.mobileffmpeg.FFmpeg
import com.timqi.sectorprogressview.SectorProgressView
import kotlinx.android.synthetic.main.activity_effects.*
import kotlinx.android.synthetic.main.activity_voice_recorder.*
import java.io.IOException
private const val LOG_TAG = "AudioRecordTest"
class VoiceRecorderActivity : AppCompatActivity() {
    private val REQUEST_RECORD_AUDIO_PERMISSION = 200
    private var fileName: String = ""
    private var fileNameNew: String = ""
    private var fileNameMerge: String = ""
    private var sendVar: String = "none"
    private var spv: SectorProgressView? = null
    private var isEffectAddedOnce = false
    private var audio_processed = false
    private var v_counter=0.1
    private var playerBackground: MediaPlayer? = null
    private var playerEffects: MediaPlayer? = null
    private var player: MediaPlayer? = null
    private var recorder: MediaRecorder? = null
    private lateinit var countDownTimer: CountDownTimer
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_recorder)
        fileName = "${Environment.getExternalStorageDirectory()?.absolutePath}/audioRecord.mp3"
        fileNameNew =
            "${Environment.getExternalStorageDirectory()?.absolutePath}/audioRecordNew.mp3"
        fileNameMerge =
            "${Environment.getExternalStorageDirectory()?.absolutePath}/audioRecordMerge.mp3"
        // Record to the external cache directory for visibility
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        spv = findViewById<View>(R.id.spv2) as SectorProgressView
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
    }
    fun onClick(view: View) {
        when (view.id) {
            R.id.voiceoption1 -> {
                findViewById<View>(R.id.voice_modulator).visibility = View.GONE
                finish()
            }
            R.id.voiceoption2 -> {
                findViewById<View>(R.id.voice_modulator).visibility = View.GONE
                sendVar=fileNameNew
                finish()
            }
            R.id.tovoicespeech -> {
                if (tovoicespeech.text == "start")
                {
                startRecording();
                }
                else
                {
                    tovoicespeech.text = getString(R.string.record)
                    countDownTimer.cancel()
                    audio_processed=true
                    stopRecording()
                }
                /*else {
                    btRecord.text = getString(R.string.record)
                    countDownTimer.cancel()
                    audio_processed=true
                    stopRecording()
                }*/
            }
            R.id.ivNext -> {
                //startActivity(Intent(this, EffectsActivity::class.java))
                findViewById<View>(R.id.voice_modulator).visibility = View.VISIBLE
            }
            R.id.voice_changer1 -> {
                playSpeeddown80Percent(fileName, fileNameNew);
                findViewById<View>(R.id.voiceoption2).visibility = View.VISIBLE
            }
            R.id.voice_changer2 -> {
                playChipmunk(fileName, fileNameNew)
                findViewById<View>(R.id.voiceoption2).visibility = View.VISIBLE
            }

            R.id.voice_changer3 -> {
                playRobot(fileName, fileNameNew)
                findViewById<View>(R.id.voiceoption2).visibility = View.VISIBLE
            }

            R.id.voice_changer4 -> {
                playWhisper(fileName, fileNameNew)
                findViewById<View>(R.id.voiceoption2).visibility = View.VISIBLE
            }

            R.id.voice_changer5 -> {
                playCave(fileName, fileNameNew)
                findViewById<View>(R.id.voiceoption2).visibility = View.VISIBLE
            }
            R.id.voice_changer6-> {
                playChorusDelay1(fileName, fileNameNew)
                findViewById<View>(R.id.voiceoption2).visibility = View.VISIBLE
            }
        }
    }
    override fun onStop() {
        super.onStop()
        if(audio_processed==false) {
            recorder?.release()
            recorder = null
            playerBackground?.release()
            playerEffects?.release()
            if (::countDownTimer.isInitialized) {
                countDownTimer.cancel()
            }
            playerBackground = null
            playerEffects = null
        } else{
            recorder?.release()
            recorder = null
            player?.release()
            player = null
        }
    }

    /**
     * Function used to start the recordings
     */
    private fun startRecording() {
        if (ContextCompat.checkSelfPermission(
                this@VoiceRecorderActivity,
                Manifest.permission.RECORD_AUDIO
            ) == -1 ||
            ContextCompat.checkSelfPermission(
                this@VoiceRecorderActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == -1 ||
            ContextCompat.checkSelfPermission(
                this@VoiceRecorderActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == -1
        ) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
            return
        }
        ivNext.visibility = View.GONE
        tovoicespeech.text = getString(R.string.stop)
        tvTimer.text = getString(R.string._30)
        countDownTimer = object : CountDownTimer(31000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                v_counter=v_counter+2.7;
                spv?.setPercent(v_counter.toFloat())
                tvTimer.text = (millisUntilFinished / 1000).toString()
                if (tvTimer.text == "1") {
                    Handler().postDelayed({
                        if (!isFinishing) {
                            v_counter=v_counter+2.7;
                            spv?.setPercent(v_counter.toFloat())
                            tovoicespeech.performClick()
                            tvTimer.text = getString(R.string._30)
                        }
                    }, 1000)
                }
            }
            override fun onFinish() {
            }
        }
        countDownTimer.start()
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
            try {
                prepare()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }
            start()
        }
    }
    private fun stopRecording() {
        playerEffects?.stop()
        playerBackground?.stop()
        findViewById<View>(R.id.voiceoption1).visibility = View.VISIBLE
        //findViewById<View>(R.id.hh1).visibility = View.VISIBLE
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        findViewById<View>(R.id.voice_modulator).visibility = View.VISIBLE
    }
    private fun start() {
        player = MediaPlayer().apply {
            try {
                setDataSource(fileNameNew)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }
        }
    }
    private fun exceuteFFMPEG(cmd: Array<String>) {
        FFmpeg.execute(cmd)
        hideProgress()
        isEffectAddedOnce = true
        start()
    }
   private fun playRadio(fileName1: String, fileName2: String) {
        showProgress()
        player?.stop()
        val cmd = arrayOf(
            "-y",
            "-i",
            fileName1,
            "-af",
            "atempo=1",
            fileName2
        )//Radio

        exceuteFFMPEG(cmd)

    }

    /**
     * Function used to play the audio like a Chipmunk
     */
    private fun playChipmunk(fileName1: String, fileName2: String) {
        showProgress()
        player?.stop()
        val cmd = arrayOf(
            "-y",
            "-i",
            fileName1,
            "-af",
            "asetrate=22100,atempo=1/2",
            fileName2
        )//Chipmunk
        exceuteFFMPEG(cmd)
    }

    /**
     * Function used to play the audio like a Robot
     */
    private fun playRobot(fileName1: String, fileName2: String) {
        showProgress()
        player?.stop()
        val cmd = arrayOf(
            "-y",
            "-i",
            fileName1,
            "-af",
            "asetrate=11100,atempo=4/3,atempo=1/2,atempo=3/4",
            fileName2
        )//Robot
        exceuteFFMPEG(cmd)
    }

    /**
     * Function used to play the audio like a Cave
     */
    private fun playCave(fileName1: String, fileName2: String) {
        showProgress()
        player?.stop()
        val cmd = arrayOf(
            "-y",
            "-i",
            fileName1,
            "-af",
            "aecho=0.8:0.9:1000:0.3",
            fileName2
        )//Cave
        exceuteFFMPEG(cmd)
    }
    private fun playChorusDelay1(fileName1: String, fileName2: String) {
        showProgress()
        player?.stop()
        val cmd = arrayOf(
            "-y",
            "-i",
            fileName1,
            "-af",
            "chorus=0.7:0.9:55:0.4:0.25:2",
            fileName2
        )//Cave
        exceuteFFMPEG(cmd)
    }
    private fun playOpenAir(fileName1: String, fileName2: String) {
        showProgress()
        player?.stop()
        val cmd = arrayOf(
            "-y",
            "-i",
            fileName1,
            "-af",
            "aecho=0.8:0.9:1000:0.3",
            fileName2
        )//Cave
        exceuteFFMPEG(cmd)
    }
    private fun playFirst10SecondsFade(fileName1: String, fileName2: String) {
        showProgress()
        player?.stop()
        val cmd = arrayOf(
            "-y",
            "-i",
            fileName1,
            "-af",
            "afade=t=in:ss=0:d=15",
            fileName2
        )//Cave
        exceuteFFMPEG(cmd)
    }
    private fun playWhisper(fileName1: String, fileName2: String) {
        showProgress()
        player?.stop()
        val cmd = arrayOf(
            "-y",
            "-i",
            fileName1,
            "-af",
            "afftfilt=\"real='hypot(re,im)*cos((random(0)*2-1)*2*3.14)':imag='hypot(re,im)*sin((random(1)*2-1)*2*3.14)':win_size=128:overlap=0.8\"",
            fileName2
        )//Cave
        exceuteFFMPEG(cmd)
    }
    private fun playChorusDelay2(fileName1: String, fileName2: String) {
        showProgress()
        player?.stop()
        val cmd = arrayOf(
            "-y",
            "-i",
            fileName1,
            "-af",
            "chorus=0.6:0.9:50|60:0.4|0.32:0.25|0.4:2|1.3",
            fileName2
        )//Cave
        exceuteFFMPEG(cmd)
    }
    private fun playLowFilter(fileName1: String, fileName2: String) {
        showProgress()
        player?.stop()
        val cmd = arrayOf(
            "-y",
            "-i",
            fileName1,
            "-af",
            "aiir=z=1.3057 0 0 0:p=1.3057 2.3892 2.1860 1:f=sf:r=d",
            fileName2
        )//Cave
        exceuteFFMPEG(cmd)
    }
    private fun playStreeoAudio(fileName1: String, fileName2: String) {
        showProgress()
        player?.stop()
        val cmd = arrayOf(
            "-y",
            "-i",
            fileName1,
            "-af",
            "asplit[a][b],[a]adelay=32S|32S[a],[b][a]anlms=order=128:leakage=0.0005:mu=.5:out_mode=o",
            fileName2
        )//Cave
        exceuteFFMPEG(cmd)
    }
    private fun playReverseAndTrim(fileName1: String, fileName2: String) {
        showProgress()
        player?.stop()
        val cmd = arrayOf(
            "-y",
            "-i",
            fileName1,
            "-af",
            "atrim=end=5,areverse",
            fileName2
        )//Cave
        exceuteFFMPEG(cmd)
    }
    private fun playSpeedup300Percent(fileName1: String, fileName2: String) {
        showProgress()
        player?.stop()
        val cmd = arrayOf(
            "-y",
            "-i",
            fileName1,
            "-af",
            "atempo=3",
            fileName2
        )//Cave
        exceuteFFMPEG(cmd)
    }
    private fun playSpeeddown80Percent(fileName1: String, fileName2: String) {
        showProgress()
        player?.stop()
        val cmd = arrayOf(
            "-y",
            "-i",
            fileName1,
            "-af",
            "atempo=0.8",
            fileName2
        )//Cave
        exceuteFFMPEG(cmd)
    }
    private fun playEqualiser(fileName1: String, fileName2: String) {
        showProgress()
        player?.stop()
        val cmd = arrayOf(
            "-y",
            "-i",
            fileName1,
            "-af",
            "equalizer=f=1000:t=h:width=200:g=-10",
            fileName2
        )//Cave
        exceuteFFMPEG(cmd)
    }
    private fun playcustom1(fileName1: String, fileName2: String) {
        showProgress()
        player?.stop()
        val cmd = arrayOf(
            "-y",
            "-i",
            fileName1,
            "-af",
            "sofalizer=sofa=/path/to/ClubFritz6.sofa:type=freq:radius=2:speakers=FL 45|FR 315|BL 135|BR 225:gain=28",
            fileName2
        )//Cave
        exceuteFFMPEG(cmd)
    }
    private fun playcustom2(fileName1: String, fileName2: String) {
        showProgress()
        player?.stop()
        val cmd = arrayOf(
            "-y",
            "-i",
            fileName1,
            "-af",
            "silencedetect=noise=0.0001",
            fileName2
        )//Cave
        exceuteFFMPEG(cmd)
    }
    private fun playcustom3(fileName1: String, fileName2: String) {
        showProgress()
        player?.stop()
        val cmd = arrayOf(
            "-y",
            "-i",
            fileName1,
            "-af",
            "channelsplit,axcorrelate=size=1024:algo=fast",
            fileName2
        )//Cave
        exceuteFFMPEG(cmd)
    }
    private fun playcustom4(fileName1: String, fileName2: String) {
        showProgress()
        player?.stop()
        val cmd = arrayOf(
            "-y",
            "-i",
            fileName1,
            "-af",
            "channelsplit,axcorrelate=size=1024:algo=slow",
            fileName2
        )//Cave
        exceuteFFMPEG(cmd)
    }
    private fun playcustom5(fileName1: String, fileName2: String) {
        showProgress()
        player?.stop()
        val cmd = arrayOf(
            "-y",
            "-i",
            fileName1,
            "-af",
            "ladspa=file=tap_reverb:tap_reverb",
            fileName2
        )//Cave
        exceuteFFMPEG(cmd)
    }
    private fun showProgress() {
        progress_circular.visibility = View.VISIBLE
    }
    private fun hideProgress() {
        progress_circular.visibility = View.GONE
    }
    override fun finish() {
        val data = Intent()
        data.putExtra("returnvoicepath", sendVar)
        setResult(RESULT_OK, data)
        super.finish()
    }
}