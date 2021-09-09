package com.technorizen.stanrz.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bolaware.viewstimerstory.Momentz;
import com.bolaware.viewstimerstory.MomentzCallback;
import com.bolaware.viewstimerstory.MomentzView;
import com.technorizen.stanrz.R;
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

        ArrayList<SuccessResGetStories.UserStory> transactionList = (ArrayList<SuccessResGetStories.UserStory>)extras.getSerializable("key");

        userImage = extras.getString("UserImage");
        userName = extras.getString("UserName");

      if(stories!=null)
      {

          for (SuccessResGetStories.UserStory story:transactionList)
          {
              if(story.getImage().endsWith("jpeg") || story.getImage().endsWith("png") || story.getImage().endsWith("gif") || story.getImage().endsWith("webp"))
              {

                  ImageView internetLoadedImageView = new ImageView(this);
                  storyView.add(new MomentzView(internetLoadedImageView,10));
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

        momentz = new Momentz(this,storyView,container,this,0);
        momentz.start();

    }


    @Override
    public void done() {

    }

    @Override
    public void onNextCalled(@NotNull View view, @NotNull Momentz momentz, int i) {

    }

}