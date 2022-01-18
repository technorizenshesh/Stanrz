package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.IAmStanrzItemBinding;
import com.technorizen.stanrz.models.SuccessResGetStanrzOf;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class IamStanAdapter extends RecyclerView.Adapter<IamStanAdapter.StoriesViewHolder> {

    private Context context;

    IAmStanrzItemBinding binding;
    private ArrayList<SuccessResGetStanrzOf.Result> stanrzOfList;

    private String from;

    public IamStanAdapter(Context context,ArrayList<SuccessResGetStanrzOf.Result> stanrzOfList,String from)
    {
      this.context = context;
      this.stanrzOfList = stanrzOfList;
      this.from = from;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= IAmStanrzItemBinding.inflate(LayoutInflater.from(context));
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        CircleImageView circleImageView = holder.itemView.findViewById(R.id.ivProfile);

        Glide
                .with(context)
                .load(stanrzOfList.get(position).getUserImage())
                .centerCrop()
                .into(circleImageView);

        circleImageView.setOnClickListener(view ->
                {
                    if(from.equalsIgnoreCase("profile"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("otherUser",stanrzOfList.get(position).getPostUserId());
                        Navigation.findNavController(view).navigate(R.id.action_navigation_profile_to_otherUserDetailFragment,bundle);
                    }
                   else if(from.equalsIgnoreCase("other"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("otherUser",stanrzOfList.get(position).getPostUserId());
                        Navigation.findNavController(view).navigate(R.id.action_otherUserDetailFragment_to_newOtherUserDetailFragment,bundle);

                    } else if(from.equalsIgnoreCase("newother"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("otherUser",stanrzOfList.get(position).getPostUserId());
                        Navigation.findNavController(view).navigate(R.id.action_newOtherUserDetailFragment_to_otherUserDetailFragment,bundle);

                    }
                }
                );

    }

    @Override
    public int getItemCount() {
        return stanrzOfList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(IAmStanrzItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
