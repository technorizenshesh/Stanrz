package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.StanrzItemBinding;
import com.technorizen.stanrz.databinding.StanrzItemBinding;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class StanrzAdapter extends RecyclerView.Adapter<StanrzAdapter.StoriesViewHolder> {

    private Context context;
    
    StanrzItemBinding binding;
    
    public StanrzAdapter(Context context)
    {
      this.context = context;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= StanrzItemBinding.inflate(LayoutInflater.from(context));
        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);

        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        TextView textView = holder.itemView.findViewById(R.id.tvRanking);

        if(position==0)
        {
            textView.setText("1");
            textView.setBackgroundResource(R.drawable.badge_circle);
        }

        if(position==1)
        {
            textView.setText("2");
            textView.setBackgroundResource(R.drawable.silver_badge_circle);
        }

        if(position==2)
        {
            textView.setText("3");
            textView.setBackgroundResource(R.drawable.brondge_badge_circle);
        }

        if(position==3)
        {
            textView.setText("4");
            textView.setBackgroundResource(R.drawable.pink_badge_circle);
        }

        if(position==4)
        {
            textView.setText("5");
            textView.setBackgroundResource(R.drawable.pink_badge_circle);
        }

        if(position==5)
        {
            textView.setText("6");
            textView.setBackgroundResource(R.drawable.pink_badge_circle);
        }

        if(position==6)
        {
            textView.setText("7");
            textView.setBackgroundResource(R.drawable.pink_badge_circle);
        }
        if(position==7)
        {
            textView.setText("8");
            textView.setBackgroundResource(R.drawable.pink_badge_circle);
        }
        if(position==8)
        {
            textView.setText("9");
            textView.setBackgroundResource(R.drawable.pink_badge_circle);
        }
        if(position==9)
        {
            textView.setText("10");
            textView.setBackgroundResource(R.drawable.pink_badge_circle);
        }

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(StanrzItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
