package votive.com.appuaet10.Activities;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.AppData;
import votive.com.appuaet10.Utilities.UIUtils;

public class VideoViewActivity extends AppCompatActivity {

    private static String TAG = VideoViewActivity.class.getSimpleName();
    private Context mContext;
    private VideoView mVideoView;
    private int position = 0;
    private String mVideoStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoview_main);
        mContext = this;
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        init();
    }

    public void init() {
        mVideoView = (VideoView)findViewById(R.id.vv_video_play);
        mVideoStr = AppData.getInstance().getVideoStr(this);
        Uri uri = Uri.parse(mVideoStr);

        try{
            if (uri != null){
                mVideoView.setVideoURI(uri);
            }
        } catch (Exception e){
            UIUtils.showToast(mContext,"Unable to load video");
            finish();
        }

        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                mVideoView.seekTo(position);
                if (position == 0) {
                    mVideoView.start();
                } else {
                    //if we come from a resumed activity, video playback will be paused
                    mVideoView.pause();
                }
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                UIUtils.showToast(mContext,"Thank you for watching video!");
                finish();
            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                finish();
                return false;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //we use onSaveInstanceState in order to store the video playback position for orientation change
        savedInstanceState.putInt("Position", mVideoView.getCurrentPosition());
        mVideoView.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //we use onRestoreInstanceState in order to play the video playback from the stored position
        position = savedInstanceState.getInt("Position");
        mVideoView.seekTo(position);
    }

    @Override
    protected void onDestroy() {
        if(mVideoView != null && mVideoView.isPlaying()){
            mVideoView.stopPlayback();
            mVideoView = null;
        }
        super.onDestroy();
    }
}


