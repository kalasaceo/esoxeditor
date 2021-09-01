package com.daasuu.camerarecorder
import android.view.View
import com.daasuu.camerarecorder.BaseActivity
import com.kalasa.library.R
import com.daasuu.camerarecorder.*
import kotlinx.android.synthetic.main.activity_video_process.*
/**
 * Created by Ashvin Vavaliya on 29,December,2020
 * Simform Solutions Pvt Ltd.
 */
class VideoProcessActivity : BaseActivity(R.layout.activity_video_process, R.string.video_operations) {
    override fun initialization() {
        supportActionBar?.title = getString(R.string.video_operations)
    }
    override fun onClick(v: View?) {
    }
}