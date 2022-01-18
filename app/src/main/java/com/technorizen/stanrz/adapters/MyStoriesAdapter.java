package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.MyStoriesItemBinding;
import com.technorizen.stanrz.databinding.MyStoriesItemBinding;
import com.technorizen.stanrz.models.SuccessResGetStories;
import com.technorizen.stanrz.utility.DeleteStory;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;
import com.technorizen.stanrz.utility.ShowStory;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.technorizen.stanrz.retrofit.Constant.USER_ID;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class MyStoriesAdapter extends RecyclerView.Adapter<MyStoriesAdapter.StoriesViewHolder> {

    private Context context;

    private DeleteStory deleteStory;

    MyStoriesItemBinding binding;

    private ArrayList<SuccessResGetStories.UserStory> storyList = new ArrayList<>();

    public MyStoriesAdapter(Context context, ArrayList<SuccessResGetStories.UserStory> storyList,DeleteStory deleteStory)
    {
      this.context = context;
      this.storyList = storyList;
      this.deleteStory = deleteStory;
    }

    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= MyStoriesItemBinding.inflate(LayoutInflater.from(context));
        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);

        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        ImageView ivStory = holder.itemView.findViewById(R.id.img1);
        ImageView ivDelete = holder.itemView.findViewById(R.id.ivDelete);

                Glide.with(context)
                        .load(storyList.get(position).getStoryData())
                        .centerCrop()
                        .placeholder(R.drawable.ic_user)
                        .into(ivStory);

                ivDelete.setOnClickListener(v ->
                        {
                            new AlertDialog.Builder(context)
                                    .setTitle("Remove Story")
                                    .setMessage("Are you sure you want to remove story?")

                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Continue with delete operation

                                            deleteStory.deleteStory(storyList.get(position).getId(),storyList.get(position).getStoryId());

                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                        );

    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(MyStoriesItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
