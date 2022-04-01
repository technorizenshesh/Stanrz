package com.technorizen.stanrz.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ablanco.zoomy.Zoomy;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.activites.HomeActivity;
import com.technorizen.stanrz.activites.LoginActivity;
import com.technorizen.stanrz.activites.PlayVideoActivity;
import com.technorizen.stanrz.activites.SplashActivity;
import com.technorizen.stanrz.models.Result;
import com.technorizen.stanrz.models.SuccessResGetUploads;
import com.technorizen.stanrz.models.UploadedVideos;
import com.technorizen.stanrz.models.UserPost;
import com.technorizen.stanrz.retrofit.Constant;
import com.technorizen.stanrz.utility.AddLike;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.technorizen.stanrz.retrofit.Constant.USER_ID;

/**
 * Created by Ravindra Birla on 26,August,2021
 */
public class ImagesAndVideosPagerAdapter extends PagerAdapter implements Serializable {

    private AddLike addFollow;

    private static final String TAG = "ImagesAndVideosPager";

    private  List<UserPost> userPosts;

    private Context context;

    private View itemView;

    private final LayoutInflater mLayoutInflater;

    private Result model;

    private Boolean isAttached;

    public ImageView ivMute,ivExpand;

    private int pos;

    private String status;

    private String nsfwStatus;

    private String superlikes;

    private ProgressBar progressBar;

    private AppCompatImageView playPause;

    private boolean isPlaying = true;

    /**
     * The click event listener which will propagate click events to the parent or any other listener set
     */
    /**
     * Constructor for gallery adapter which will create and screen slide of images.
     *
     * @param context
     *         The context which will be used to inflate the layout for each page.
     * @param userPosts
     *         The list of items which need to be displayed as screen slide.
     */

    public ImagesAndVideosPagerAdapter(Context context,
                          @NonNull List<UserPost> userPosts,Result model,String status,String nsfwStatus,String superlikes,AddLike addFollow) {
        super();
        // Inflater which will be used for creating all the necessary pages
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // The items which will be displayed.
        this.userPosts = userPosts;
        this.context = context;
        this.model = model;
        this.nsfwStatus = nsfwStatus;
        this.status = status;
        this.superlikes = superlikes;
        this.addFollow = addFollow;
        setMyScroller();
    }

    private void setMyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyScroller extends Scroller {
        public MyScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }
        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 1000 /*1 secs*/);
        }
    }

    public void isAttachedView(Boolean isAttached,int pos){
        this.isAttached = isAttached;
        this.pos = pos ;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return null == userPosts ? 0 : userPosts.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        itemView = mLayoutInflater.inflate(R.layout.view_pager_item, container, false);

        RelativeLayout rlLock = itemView.findViewById(R.id.rlLock);

        TextView tvUnlockCoins = itemView.findViewById(R.id.tvUnlockCoins);

        TextView tvNsfw = itemView.findViewById(R.id.tvNsfw);

        AppCompatImageView imageView = itemView.findViewById(R.id.ivImage);

        ImageView ivLock1 = itemView.findViewById(R.id.ivLock1);

        ImageView ivMyLock = itemView.findViewById(R.id.ivMyLock);

        ImageView ivLock2 = itemView.findViewById(R.id.ivLock2);

        ivMute = itemView.findViewById(R.id.ivMuteUnMute);

        ivExpand = itemView.findViewById(R.id.ivExpand);

        playPause = itemView.findViewById(R.id.ivPlayPause1);

        VideoView videoView = itemView.findViewById(R.id.videoView);

        progressBar = itemView.findViewById(R.id.progressBar);

        FrameLayout frameLayout = itemView.findViewById(R.id.flExoPlayer);

        String post = userPosts.get(position).getPostData();

        String userId = SharedPreferenceUtility.getInstance(context).getString(USER_ID);

        if(userId.equalsIgnoreCase(model.getUserId()))
        {
            if(model.getUploadedAs().equalsIgnoreCase("Locked"))
            {
                ivMyLock.setVisibility(View.VISIBLE);
            }
            else
            {
                ivMyLock.setVisibility(View.GONE);
            }
        }
        else
        {
            ivMyLock.setVisibility(View.GONE);
        }

        ivLock1.setOnClickListener(view ->
                {

                    new AlertDialog.Builder(context)
                            .setTitle("Add Superlike")
                            .setMessage(context.getString(R.string.are_you_sure_you_want_to_gift)+" "+model.getUnlockWith()+" "+context.getString(R.string.superlike_to_unlock_post))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                    String superlikes = model.getUnlockWith();

                                    int totlaRemainingSuperlikes = Integer.parseInt(superlikes);

                                    addFollow.addSuperLikes(view,model.getUserPost().get(0).getPostId(),"",model.getUnlockWith());

                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
                );

        ivLock2.setOnClickListener(view ->
                {

                    new AlertDialog.Builder(context)
                            .setTitle(R.string.nsfw)
                            .setMessage("This post may contain material that may be disturbing to some users. Proceed to view?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation

                                    addFollow.unlockNsfw(view,model.getUserPost().get(0).getPostId(),"",model.getNsfw());

                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
        );

        if (status.equalsIgnoreCase("Locked"))
        {
            frameLayout.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            rlLock.setVisibility(View.VISIBLE);
            ivLock1.setVisibility(View.VISIBLE);
            ivLock2.setVisibility(View.GONE);
            tvUnlockCoins.setVisibility(View.VISIBLE);
            tvNsfw.setVisibility(View.GONE);
            tvUnlockCoins.setText(context.getResources().getString(R.string.uplock_with_n)+superlikes+" "+context.getResources().getString(R.string.superlies));
        } else if(nsfwStatus.equalsIgnoreCase("Yes"))
        {
            frameLayout.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            rlLock.setVisibility(View.VISIBLE);
            ivLock1.setVisibility(View.GONE);
            ivLock2.setVisibility(View.VISIBLE);
            tvUnlockCoins.setVisibility(View.GONE);
            tvNsfw.setVisibility(View.VISIBLE);
            tvNsfw.setText(R.string.nsfw);
        }
        else
        {
            if(userPosts.get(position).getPostData().endsWith("jpg") || userPosts.get(position).getPostData().endsWith("jpeg") || userPosts.get(position).getPostData().endsWith("png") || userPosts.get(position).getPostData().endsWith("/r/n"))
            {
                frameLayout.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                displayGalleryItem(imageView,userPosts.get(position).getPostData());
                frameLayout.setVisibility(View.GONE);
                rlLock.setVisibility(View.GONE);
            }
            else if(userPosts.get(position).getPostData().endsWith("mp4"))
            {
                frameLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                initExpo(videoView,position,ivMute,progressBar,playPause,ivExpand);
                rlLock.setVisibility(View.GONE);
            }
        }

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

          try {
            container.removeView((View) object);
            unbindDrawables((View) object);
            object = null;
        } catch (Exception e) {
            Log.w(TAG, "destroyItem: failed to destroy item and clear its used resrces", e);
        }
    }

    protected void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    /**
     * Set an listener which will notify of any click events that are detected on the pages of the view pager.
     *
     * @param onItemClickListener
     *         The listener. If {@code null} it will disable any events from being sent.
     */

    /**
     * Display the gallery image into the image view provided.
     *
     * @param galleryView
     *         The view which will display the image.
     * @param galleryItem
     *         The item from which to get the image.
     */

    private void displayGalleryItem(AppCompatImageView galleryView, String galleryItem) {
        if (null != galleryItem) {

            Zoomy.Builder builder = new Zoomy.Builder((Activity) context).target(galleryView);
            builder.register();

            Glide.with(galleryView.getContext()) // Bind it with the context of the actual view used
                    .load(galleryItem) // Load the image
                    .format(DecodeFormat.PREFER_RGB_565) // the decode format - this will not use alpha at all
                    .centerInside() // scale type
                    .thumbnail(0.2f) // make use of the thumbnail which can display a down-sized version of the image
                    .into(galleryView); // Voilla - the target view
        }
    }

    private void initExpo(VideoView videoView, int position,ImageView ivmute,ProgressBar progress,AppCompatImageView playPauseIcon,ImageView ivFullScreen) {

        isPlaying = true;

        Uri uri = Uri.parse(userPosts.get(position).getPostData());

        model.setPlayer(videoView);

        userPosts.get(position).setPlayer(videoView);

        model.setMediaPlayer(new MediaPlayer());

        videoView.setVideoURI(uri);

        playPauseIcon.setVisibility(View.GONE);

        playPauseIcon.setOnClickListener(view ->
                {
                    if(isPlaying)
                    {
                        videoView.pause();
                        playPauseIcon.setImageResource(R.drawable.playicon);
                    }
                    else
                    {
                        videoView.seekTo(videoView.getCurrentPosition());
                        videoView.start();
                        playPauseIcon.setImageResource(R.drawable.pauseicon);
                    }
                    isPlaying = !isPlaying;
                }
                );

        videoView.seekTo(1);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });

        Log.d(TAG, "initExpo: "+SharedPreferenceUtility.getInstance(context).getBoolean(Constant.MUTE_UNMUTE));

        if(SharedPreferenceUtility.getInstance(context).getBoolean(Constant.MUTE_UNMUTE))
        {
            model.getMediaPlayer().setVolume(15f,15f);
            ivmute.setImageResource(R.drawable.unmuteicon);
        }
        else
        {
            model.getMediaPlayer().setVolume(0f,0f);
            ivmute.setImageResource(R.drawable.muteicon);
        }

        MediaController mediaController = new MediaController(context);

        // sets the anchor view
        // anchor view for the videoView
        mediaController.setAnchorView(videoView);

        // sets the media player to the videoView
        mediaController.setMediaPlayer(videoView);

        // sets the media controller to the videoView
        videoView.setMediaController(mediaController);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
  //                    videoView.start();
                progress.setVisibility(View.GONE);
                playPauseIcon.setVisibility(View.VISIBLE);
                model.setMediaPlayer(mp);

                ivFullScreen.setOnClickListener(view ->
                        {
                            Intent intent = new Intent(context, PlayVideoActivity.class);
                            intent.putExtra("uri",userPosts.get(position).getPostData());
                            context.startActivity(intent);
                        }
                );

                videoView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        playPauseIcon.setVisibility(View.VISIBLE);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playPauseIcon.setVisibility(View.GONE);
                            }
                        },2000);

                        return false;
                    }
                });

                if(SharedPreferenceUtility.getInstance(context).getBoolean(Constant.MUTE_UNMUTE))
                {
                    mp.setVolume(1F, 1F);
                    ivmute.setImageResource(R.drawable.unmuteicon);
                }
                else
                {
                    mp.setVolume(0f,0f);
                    ivmute.setImageResource(R.drawable.muteicon);
                }

                ivmute.setOnClickListener(v ->
                        {

                            if(SharedPreferenceUtility.getInstance(context).getBoolean(Constant.MUTE_UNMUTE))
                            {
                                ivmute.setImageResource(R.drawable.muteicon);
                                mp.setVolume(0f,0f);
                                SharedPreferenceUtility.getInstance(context).putBoolean(Constant.MUTE_UNMUTE, false);
                            }else
                            {
                                ivmute.setImageResource(R.drawable.unmuteicon);
                                mp.setVolume(1F, 1F);
                                SharedPreferenceUtility.getInstance(context).putBoolean(Constant.MUTE_UNMUTE, true);
                            }
                        }
                );
            }
        });

        videoView.setMediaController(mediaController);

        mediaController.setVisibility(View.GONE);

        // creating object of
        // media controller class

       /* // sets the anchor view
        // anchor view for the videoView
        mediaController.setAnchorView(videoView);
*/
        // sets the media player to the videoView
       // mediaController.setMediaPlayer(videoView);

        if(model.getUserPost().size()>1)
        {
            if(position==0)
            {
                model.getUserPost().get(0).getPlayer().start();
            }
            else
            {
                model.getUserPost().get(position).getPlayer().pause();
            }
        }
    }

    public void playVideo(int pos, Result model, int viewPagerPosition)
    {

        if(itemView !=null && model.getPlayer() != null && model.getMediaPlayer() != null && model.getUserPost().get(viewPagerPosition).getPlayer() != null)
        {

            //       model.getVideos().get(0).getPlayer().start();
            ivMute = itemView.findViewById(R.id.ivMuteUnMute);

            Log.d(TAG, "playVideo: "+model.getUserPost().get(viewPagerPosition).getPlayer());

            try {

                model.getUserPost().get(viewPagerPosition).getPlayer().start();

                if(SharedPreferenceUtility.getInstance(context).getBoolean(Constant.MUTE_UNMUTE))
                {
                    model.getMediaPlayer().setVolume(15f,15f);
                    ivMute.setImageResource(R.drawable.unmuteicon);
                }
                else
                {
                    model.getMediaPlayer().setVolume(0f,0f);
                    ivMute.setImageResource(R.drawable.muteicon);
                }

            } catch(IllegalStateException e) {
                // media player is not initialized
            }

        }

    }

    public void stopVideo(int pos,Result model,int viewPagerPosition)
    {

        if( model.getPlayer() != null)
        {
            model.getUserPost().get(viewPagerPosition).getPlayer().pause();
        }

    }

    public void playVideoFromViewPager(int pos,Result model,int viewPagerPosition)
    {

        if( model.getPlayer() != null)
        {
            //       model.getVideos().get(0).getPlayer().start();
//            model.getVideos().get(viewPagerPosition).getPlayer().start();

            for(int i=0;i<model.getUserPost().size();i++) {

                if (i == viewPagerPosition) {
                    model.getUserPost().get(i).getPlayer().start();

                } else
                {
            //        model.getUserPost().get(i).getPlayer().pause();

                    if(model.getUserPost().get(i).getPlayer() != null)
                    {
                        model.getUserPost().get(i).getPlayer().pause();

                    }

                }

            }

        }

    }

}