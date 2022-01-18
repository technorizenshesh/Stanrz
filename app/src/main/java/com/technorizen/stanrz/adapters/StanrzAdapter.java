package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.StanrzItemBinding;
import com.technorizen.stanrz.databinding.StanrzItemBinding;
import com.technorizen.stanrz.models.SuccessResGetStanrzOf;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class StanrzAdapter extends RecyclerView.Adapter<StanrzAdapter.StoriesViewHolder> {

    private Context context;
    
    StanrzItemBinding binding;

    private ArrayList<SuccessResGetStanrzOf.Result> topStanrzList ;
    private String from;
    public StanrzAdapter(Context context,ArrayList<SuccessResGetStanrzOf.Result> topStanrzList, String from)
    {
      this.context = context;
      this.topStanrzList=topStanrzList;
      this.from = from;
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

        CircleImageView circleImageView = holder.itemView.findViewById(R.id.iv_profile);

        TextView textView = holder.itemView.findViewById(R.id.tvRanking);

        Glide
                .with(context)
                .load(topStanrzList.get(position).getUserImage())
                .centerCrop()
                .into(circleImageView);

        circleImageView.setOnClickListener(view ->
                {
                    if(from.equalsIgnoreCase("profile"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("otherUser",topStanrzList.get(position).getUserId());
                        Navigation.findNavController(view).navigate(R.id.action_navigation_profile_to_otherUserDetailFragment,bundle);
                    }
                    else if(from.equalsIgnoreCase("other"))
                    {

                        Bundle bundle = new Bundle();
                        bundle.putString("otherUser",topStanrzList.get(position).getUserId());
                        Navigation.findNavController(view).navigate(R.id.action_otherUserDetailFragment_to_newOtherUserDetailFragment,bundle);

                    }  else if(from.equalsIgnoreCase("newother"))
                    {

                        Bundle bundle = new Bundle();
                        bundle.putString("otherUser",topStanrzList.get(position).getUserId());
                        Navigation.findNavController(view).navigate(R.id.action_newOtherUserDetailFragment_to_otherUserDetailFragment,bundle);

                    }
                }
        );

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
        return topStanrzList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(StanrzItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
