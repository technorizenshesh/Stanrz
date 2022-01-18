package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.StanrzItemBinding;
import com.technorizen.stanrz.databinding.TopStanrzItemBinding;
import com.technorizen.stanrz.models.SuccessResGetStanrzOf;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class TopAllStanrzAdapter extends RecyclerView.Adapter<TopAllStanrzAdapter.StoriesViewHolder> {

    private Context context;

    TopStanrzItemBinding binding;

    private ArrayList<SuccessResGetStanrzOf.Result> topStanrzList;

    public TopAllStanrzAdapter(Context context, ArrayList<SuccessResGetStanrzOf.Result> topStanrzList)
    {
      this.context = context;
      this.topStanrzList=topStanrzList;
    }

    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= TopStanrzItemBinding.inflate(LayoutInflater.from(context));
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        CircleImageView circleImageView = holder.itemView.findViewById(R.id.iv_profile);

        TextView textView = holder.itemView.findViewById(R.id.tvRanking);
        TextView textName = holder.itemView.findViewById(R.id.tv_name);
        TextView tvSuperlikes = holder.itemView.findViewById(R.id.tvSuperLikes);

        Glide
                .with(context)
                .load(topStanrzList.get(position).getUserImage())
                .centerCrop()
                .into(circleImageView);

        tvSuperlikes.setText(topStanrzList.get(position).getTopSuperlike());

        textName.setText(topStanrzList.get(position).getUserName());

        circleImageView.setOnClickListener(view ->
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("otherUser",topStanrzList.get(position).getUserId());
                    Navigation.findNavController(view).navigate(R.id.action_seeMoreFragment_to_otherUserDetailFragment,bundle);

                }
        );

        if(position==0)
        {
            textView.setBackgroundResource(R.drawable.badge_circle);
        }

        if(position==1)
        {
            textView.setBackgroundResource(R.drawable.silver_badge_circle);
        }

        if(position==2)
        {
            textView.setBackgroundResource(R.drawable.brondge_badge_circle);
        }

        if(position==3)
        {

            textView.setBackgroundResource(R.drawable.pink_badge_circle);
        }

        textView.setText(topStanrzList.get(position).getUserRanking());

    }

    @Override
    public int getItemCount() {
        return topStanrzList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(TopStanrzItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
