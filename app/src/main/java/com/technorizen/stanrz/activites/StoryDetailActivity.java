package com.technorizen.stanrz.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bolaware.viewstimerstory.Momentz;
import com.bolaware.viewstimerstory.MomentzCallback;
import com.bolaware.viewstimerstory.MomentzView;
import com.bumptech.glide.Glide;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.fragments.HomeFragment;
import com.technorizen.stanrz.models.SuccessResGetStories;
import com.technorizen.stanrz.models.SuccessResGetUploads;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StoryDetailActivity extends AppCompatActivity implements MomentzCallback {

    ArrayList<MomentzView> storyView = new ArrayList<>();

    private ArrayList<SuccessResGetStories.UserStory> stories= new ArrayList<>();

    private String userName = "";

    private String userImage = "";

    private Momentz momentz ;

    private ConstraintLayout container ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        container = findViewById(R.id.container);
        initializeComponents();
    }

    private void initializeComponents() {

        Bundle extras = getIntent().getExtras();

        SuccessResGetStories.Result storyObject = HomeFragment.story;

        userImage = extras.getString("UserImage");

        userName = extras.getString("UserName");

        stories.addAll(storyObject.getUserStory());

      if(stories!=null)
      {

          for (SuccessResGetStories.UserStory story:stories)
          {

              if(story.getStoryType().equalsIgnoreCase("image"))
              {
                  ImageView internetLoadedImageView = new ImageView(this);
                  storyView.add(new MomentzView(internetLoadedImageView,10));
              }
              else
              {
                  VideoView videoView = new VideoView(this);
                  storyView.add(new MomentzView(videoView,60));
              }
          }
      }

/*
        if(!stories.isNullOrEmpty()){
            for (i in 0 until stories.size){
                if(stories[i].image_video_type.equals("video",true)){
                    val internetLoadedVideo = VideoView(this)
                    storyView.add(MomentzView(internetLoadedVideo, 60, stories[i].caption!!,userImage,userName, stories[i].created_date!!))
                }else{
                    val internetLoadedImageView = ImageView(this)
                    storyView.add(MomentzView(internetLoadedImageView, 10, stories[i].caption!!,userImage,userName,stories[i].created_date!!))
                }
            }
        }*/

        momentz = new Momentz(this,storyView,container,this,R.drawable.green_lightgrey_drawable);
        momentz.start();
    }

    @Override
    public void done() {
        finish();
    }

    @Override
    public void onNextCalled(@NotNull View view, @NotNull Momentz momentz, int i) {

        if(view instanceof VideoView)
        {
            momentz.pause(true);
            playVideo((VideoView) view,i,momentz);
        }
        else
        {
            momentz.pause(true);
            if(!isFinishing()){
                Glide.with(this).load(stories.get(i).getStoryData()).into((ImageView) view);
                momentz.resume();
            }
        }

    }

    public void playVideo(VideoView videoView,int index,Momentz momentz)
    {

        String str = stories.get(index).getStoryData();
        Uri uri = Uri.parse(str);

        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();

        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {

                if(what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
                {
                    momentz.editDurationAndResume(index, (videoView.getDuration()) / 1000);
                    return true;
                }

                return false;
            }
        });


    }

}