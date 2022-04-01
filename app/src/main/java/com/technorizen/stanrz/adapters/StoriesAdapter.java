package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.StoryItemBinding;
import com.technorizen.stanrz.models.SuccessResGetStories;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;
import com.technorizen.stanrz.utility.ShowStory;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.technorizen.stanrz.retrofit.Constant.USER_ID;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoriesViewHolder> {

    private Context context;

    StoryItemBinding binding;
    private ArrayList<SuccessResGetStories.Result> storyList = new ArrayList<>();

    private ShowStory showStory;

    public StoriesAdapter(Context context,ArrayList<SuccessResGetStories.Result> storyList,ShowStory showStory)
    {
      this.context = context;
      this.storyList = storyList;
      this.showStory = showStory;
    }

    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= StoryItemBinding.inflate(LayoutInflater.from(context));
        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);

        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        CircleImageView ivStory = holder.itemView.findViewById(R.id.ivStory);
        TextView tvName = holder.itemView.findViewById(R.id.tvuserName);
/*
        if (position==0)
        {
            showStory.showStory(position,"","",new ArrayList<>());
        }
        */

        String userId = SharedPreferenceUtility.getInstance(context).getString(USER_ID);

                Glide.with(context)
                        .load(storyList.get(position).getUserImage())
                        .centerCrop()
                        .placeholder(R.drawable.ic_user)
                        .into(ivStory);

                if (storyList.get(position).getUserId().equalsIgnoreCase(userId))
                {

                    tvName.setText("Your Story");

                }
                else
                {
                    tvName.setText(storyList.get(position).getUserName());
                }


                ivStory.setOnClickListener(v ->
                        {

                            showStory.showStory(v,position,storyList.get(position).getUserId(),"",storyList.get(position));

                        }
                        );


    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(StoryItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
