package com.howaboutthis.satyaraj.locotestapp;

import android.animation.Animator;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity{

    VideoView videoView;
    int i = 0;
    long time = 0;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.video_view);

        //specify the location of media file
        Uri uri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(getPackageName())
                .path(Integer.toString(R.raw.big_buck_bunny))
                .build();

       //  starting the videoView
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();

        autoResize();

    }

    private void autoResize() {



        final long DELAY_MS = 5000;
        final long PERIOD_MS = 5000;

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {

                time = time + PERIOD_MS;

                ConstraintLayout constraintLayout = findViewById(R.id.con);

                if (i==0) {

                    shrink(videoView);
                    videoView.setLayoutParams(new ConstraintLayout.LayoutParams(1000, 1000));
                    videoView.setBackgroundResource(R.drawable.rounded_background);

                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);

                    constraintSet.connect(R.id.video_view, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
                    constraintSet.connect(R.id.video_view, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
                    constraintSet.connect(R.id.video_view, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
                    constraintSet.connect(R.id.video_view, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);

                    constraintSet.applyTo(constraintLayout);
                    i=1;
                }else
                {
                    expand(videoView);
                    videoView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.MATCH_CONSTRAINT));
                    videoView.setBackgroundResource(0);

                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);

                    constraintSet.connect(R.id.video_view, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
                    constraintSet.connect(R.id.video_view, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
                    constraintSet.connect(R.id.video_view, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
                    constraintSet.connect(R.id.video_view, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);

                    constraintSet.applyTo(constraintLayout);
                    i=0;
                }

                if (time > 20000) {
                    timer.cancel();
                    TextView firstText = findViewById(R.id.first_text);
                    TextView secondText = findViewById(R.id.second_text);
                    videoView.setVisibility(View.GONE);
                    firstText.setVisibility(View.VISIBLE);
                    secondText.setVisibility(View.VISIBLE);
                }




            }
        };

         timer = new Timer(); // This will create a new Thread
        timer.scheduleAtFixedRate(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);

            }
        }, DELAY_MS, PERIOD_MS);


    }

    private void shrink(VideoView videoView) {
        try {
            int cx = (videoView.getLeft() + videoView.getRight() / 2);
            int cy = (videoView.getTop() + videoView.getBottom() / 2);

            int initialRadius = videoView.getWidth();

            Animator anim = ViewAnimationUtils.createCircularReveal(videoView, cx, cy, initialRadius, 0);
            anim.setDuration(300);
            anim.start();
        }catch (Exception e){

            Log.e("Main","Unable to start animation");
        }
    }

    private void expand(VideoView videoView) {
        try{
        int cx = (videoView.getLeft() + videoView.getRight()/2);
        int cy = (videoView.getTop() + videoView.getBottom()/2);

        int finalRadius = Math.max(videoView.getWidth(),videoView.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(videoView,cx,cy,0,finalRadius);
        anim.setDuration(200);
        anim.start();

        }catch (Exception e){
            Log.e("Main","Unable to start animation");
        }
    }


}
