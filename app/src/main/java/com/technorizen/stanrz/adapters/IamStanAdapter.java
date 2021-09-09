package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technorizen.stanrz.databinding.IAmStanrzItemBinding;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class IamStanAdapter extends RecyclerView.Adapter<IamStanAdapter.StoriesViewHolder> {

    private Context context;

    IAmStanrzItemBinding binding;
     
    public IamStanAdapter(Context context)
    {
      this.context = context;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= IAmStanrzItemBinding.inflate(LayoutInflater.from(context));
        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);

        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {




    }


    @Override
    public int getItemCount() {
        return 10;
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(IAmStanrzItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
