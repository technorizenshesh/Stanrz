package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technorizen.stanrz.databinding.AdapterChatBinding;
import com.technorizen.stanrz.databinding.AdapterChatBinding;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class One2OneChatAdapter extends RecyclerView.Adapter<One2OneChatAdapter.StoriesViewHolder> {

    private Context context;
    
    AdapterChatBinding binding;
    
    public One2OneChatAdapter(Context context)
    {
      this.context = context;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= AdapterChatBinding.inflate(LayoutInflater.from(context));
        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);

        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 0;
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(AdapterChatBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
