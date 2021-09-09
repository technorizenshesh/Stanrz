package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technorizen.stanrz.databinding.StoryItemBinding;
import com.technorizen.stanrz.models.SuccessResGetStories;
import com.technorizen.stanrz.utility.ShowStory;

import java.util.ArrayList;

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



        if (position==0)
        {
            showStory.showStory(position,"","",new ArrayList<>());
        }

    }

    @Override
    public int getItemCount() {
        return storyList.size()+1;
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(StoryItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
