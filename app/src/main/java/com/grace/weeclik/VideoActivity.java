package com.grace.weeclik;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    private VideoView myVideoView;
    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;
    private String CommerceID,pathVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        pathVideo = getIntent().getExtras().getString("objectID");



        if (mediaControls == null) {
            mediaControls = new MediaController(VideoActivity.this);
        }

        myVideoView = (VideoView) findViewById(R.id.videoView);

        progressDialog = new ProgressDialog(VideoActivity.this);
        progressDialog.setTitle("Veillez patienter la vidéo charge");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        myVideoView.setMediaController(mediaControls);
        myVideoView.setVideoPath(pathVideo);
        //myVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video1));
        myVideoView.start();
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                //first starting the video, when loaded
                progressDialog.dismiss();
                myVideoView.start();
                //then waiting for 1 millisecond
                try {
                    Thread.sleep(2);
                }
                catch (InterruptedException e) {
                    //     TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //then pausing the video. i guess it's the first frame
                myVideoView.pause();
                //showing the control buttons here
                mediaControls.show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            //Toast.makeText(this,"back button pressed",Toast.LENGTH_SHORT).show();
            Log.d(this.getClass().getName(), "back button pressed");
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());
        myVideoView.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt("Position");
        myVideoView.seekTo(position);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }
}
