package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technorizen.stanrz.databinding.YouItemBinding;
import com.technorizen.stanrz.databinding.YouItemBinding;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class YouAdapter extends RecyclerView.Adapter<YouAdapter.StoriesViewHolder> {

    private Context context;
    
    YouItemBinding binding;
    
    public YouAdapter(Context context)
    {
      this.context = context;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= YouItemBinding.inflate(LayoutInflater.from(context));
        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);


        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        if(position==1)
        {
            binding.btnFollow.setVisibility(View.VISIBLE);
            binding.btnMessage.setVisibility(View.GONE);
            binding.tvHistory.setText("martini_rond started following you.");
        }else
        if(position==2)
        {
            binding.btnFollow.setVisibility(View.GONE);
            binding.btnMessage.setVisibility(View.VISIBLE);
            binding.tvHistory.setText("mis_potter started following you.");
        } else
        {
            binding.btnFollow.setVisibility(View.GONE);
            binding.btnMessage.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return 10;
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(YouItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
