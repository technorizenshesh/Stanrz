package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.UploadsItemBinding;
import com.technorizen.stanrz.databinding.UploadsItemBinding;
import com.technorizen.stanrz.models.Result;
import com.technorizen.stanrz.models.SuccessResGetUploadedVideos;
import com.technorizen.stanrz.models.SuccessResGetUploads;
import com.technorizen.stanrz.models.UploadedVideos;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;

import java.util.ArrayList;

import static com.technorizen.stanrz.retrofit.Constant.USER_ID;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class UploadsAdapter extends RecyclerView.Adapter<UploadsAdapter.StoriesViewHolder> {

    private Context context;
    
    UploadsItemBinding binding;

    private ArrayList<Result> uploadedPostList;

    private String from;

    private SuccessResGetUploadedVideos successResGetUploadedVideos;

    public UploadsAdapter(Context context, ArrayList<Result> uploadedPostList, String from , SuccessResGetUploadedVideos successResGetUploadedVideos)
    {
      this.context = context;
      this.uploadedPostList=uploadedPostList;
      this.from = from;
      this.successResGetUploadedVideos = successResGetUploadedVideos;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= UploadsItemBinding.inflate(LayoutInflater.from(context));
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

         ImageView ivPhoto = holder.itemView.findViewById(R.id.ivPhoto);

         ImageView ivVideo = holder.itemView.findViewById(R.id.ivVideo);

         ImageView ivMyLock = holder.itemView.findViewById(R.id.ivMyLock);

         ImageView ivMultipleBadge = holder.itemView.findViewById(R.id.ivMultipleBadge);

         RelativeLayout rlLock,rlUnlock;

         rlUnlock = holder.itemView.findViewById(R.id.rlUnlock);

         rlLock = holder.itemView.findViewById(R.id.rlLock);

          RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.progress_animation)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .dontAnimate()
                .dontTransform();

        Glide
                .with(context)
                .load(uploadedPostList.get(position).getUserPost().get(0).getPostData())
                .centerCrop()
                .apply(options)
                .into(ivPhoto);

        if(uploadedPostList.get(position).getUserPost().get(0).getPostType().equalsIgnoreCase("Video"))
        {
            ivVideo.setVisibility(View.VISIBLE);
        }else
        {
            ivVideo.setVisibility(View.GONE);
        }

        if(uploadedPostList.get(position).getUserPost().size()>1)
        {
            ivMultipleBadge.setVisibility(View.VISIBLE);
        }
        else
        {
            ivMultipleBadge.setVisibility(View.GONE);
        }
        if(uploadedPostList.get(position).getPostIs().equalsIgnoreCase("Locked"))
        {
         rlUnlock.setVisibility(View.GONE);
         rlLock.setVisibility(View.VISIBLE);
        }  else if(uploadedPostList.get(position).getNsfw().equalsIgnoreCase("Yes"))
        {
            rlUnlock.setVisibility(View.GONE);
            rlLock.setVisibility(View.VISIBLE);
        }
        else
        {
            rlUnlock.setVisibility(View.VISIBLE);
            rlLock.setVisibility(View.GONE);
        }

        String userId = SharedPreferenceUtility.getInstance(context).getString(USER_ID);

        if(userId.equalsIgnoreCase(uploadedPostList.get(position).getUserId()))
        {
            if(uploadedPostList.get(position).getUploadedAs().equalsIgnoreCase("Locked"))
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

        ivPhoto.setOnClickListener(v ->
                {
                    if(from.equalsIgnoreCase("profile"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("key", uploadedPostList);
                        bundle.putString("pos",position+"");
                        bundle.putString("result",new Gson().toJson(successResGetUploadedVideos));
                        Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_postsFragment,bundle);
                    }
                    else if(from.equalsIgnoreCase("saved"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("key", uploadedPostList);
                        bundle.putString("pos",position+"");
                        bundle.putString("result",new Gson().toJson(successResGetUploadedVideos));
                        Navigation.findNavController(v).navigate(R.id.action_savedImagesAndVideoFragment_to_postsFragment,bundle);
                    }  else if(from.equalsIgnoreCase("other"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("key", uploadedPostList);
                        bundle.putString("pos",position+"");
                        bundle.putString("result",new Gson().toJson(successResGetUploadedVideos));
                        Navigation.findNavController(v).navigate(R.id.action_otherUserDetailFragment_to_postsFragment,bundle);
                    }else if(from.equalsIgnoreCase("search"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("key", uploadedPostList);
                        bundle.putString("pos",position+"");
                        bundle.putString("result",new Gson().toJson(successResGetUploadedVideos));
                        Navigation.findNavController(v).navigate(R.id.action_navigation_search_to_postsFragment,bundle);
                    } else if(from.equalsIgnoreCase("newother"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("key", uploadedPostList);
                        bundle.putString("pos",position+"");
                        bundle.putString("result",new Gson().toJson(successResGetUploadedVideos));
                        Navigation.findNavController(v).navigate(R.id.action_newOtherUserDetailFragment_to_postsFragment,bundle);
                    }

                }
        );

        rlLock.setOnClickListener(v ->
                {

                    if(from.equalsIgnoreCase("profile"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("key", uploadedPostList);
                        bundle.putString("pos",position+"");
                        bundle.putString("result",new Gson().toJson(successResGetUploadedVideos));
                        Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_postsFragment,bundle);
                    }
                    else if(from.equalsIgnoreCase("saved"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("key", uploadedPostList);
                        bundle.putString("pos",position+"");
                        bundle.putString("result",new Gson().toJson(successResGetUploadedVideos));
                        Navigation.findNavController(v).navigate(R.id.action_savedImagesAndVideoFragment_to_postsFragment,bundle);
                    }  else if(from.equalsIgnoreCase("other"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("key", uploadedPostList);
                        bundle.putString("pos",position+"");
                        bundle.putString("result",new Gson().toJson(successResGetUploadedVideos));
                        Navigation.findNavController(v).navigate(R.id.action_otherUserDetailFragment_to_postsFragment,bundle);
                    }else if(from.equalsIgnoreCase("search"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("key", uploadedPostList);
                        bundle.putString("pos",position+"");
                        bundle.putString("result",new Gson().toJson(successResGetUploadedVideos));
                        Navigation.findNavController(v).navigate(R.id.action_navigation_search_to_postsFragment,bundle);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return uploadedPostList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(UploadsItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
