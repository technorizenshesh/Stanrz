package com.technorizen.stanrz.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.HistroyItemBinding;
import com.technorizen.stanrz.databinding.HistroyItemBinding;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.StoriesViewHolder> {

    private Context context;
    
    HistroyItemBinding binding;
    
    public HistoryAdapter(Context context)
    {
      this.context = context;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= HistroyItemBinding.inflate(LayoutInflater.from(context));
        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);


        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 6;
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(HistroyItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
