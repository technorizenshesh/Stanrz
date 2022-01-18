package com.technorizen.stanrz.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.FollowersItemBinding;
import com.technorizen.stanrz.models.SuccessResGetFollowings;
import com.technorizen.stanrz.utility.AddFollow;
import com.technorizen.stanrz.utility.FollowNFollowingNRemove;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class OtherUserFollowingAdapter extends RecyclerView.Adapter<OtherUserFollowingAdapter.StoriesViewHolder> {

    private Context context;

    FollowersItemBinding binding;
    private List<SuccessResGetFollowings.Result> followersList ;
    private AddFollow addFollow;

    public OtherUserFollowingAdapter(Context context, List<SuccessResGetFollowings.Result> followersList, AddFollow addFollow)
    {
      this.context = context;
      this.followersList = followersList;
      this.addFollow = addFollow;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= FollowersItemBinding.inflate(LayoutInflater.from(context));
        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        CircleImageView ivProfile = holder.itemView.findViewById(R.id.iv_profile);
        TextView tvName = holder.itemView.findViewById(R.id.tvName);
        AppCompatButton btnFollowing = holder.itemView.findViewById(R.id.btnFollowing);
        AppCompatButton btnFollow = holder.itemView.findViewById(R.id.btnFollow);

            btnFollow.setVisibility(View.VISIBLE);
            btnFollowing.setVisibility(View.VISIBLE);

            Glide
                    .with(context)
                    .load(followersList.get(position).getUserDetails().getImage())
                    .centerCrop()
                    .placeholder(R.drawable.ic_user)
                    .into(ivProfile);
            tvName.setText(followersList.get(position).getUserDetails().getUsername());

            if(followersList.get(position).getUserDetails().getFollow().equalsIgnoreCase("Following"))
            {

                  btnFollow.setVisibility(View.GONE);
                  btnFollowing.setVisibility(View.VISIBLE);

            } else
            {
                btnFollowing.setVisibility(View.GONE);
                btnFollow.setVisibility(View.VISIBLE);
            }
            binding.btnFollowing.setOnClickListener(v ->
                    {

                        AppCompatButton btnCancel,btnRemove1;
                        TextView tvName1,tvRemoveText;
                        CircleImageView ivProfile1;

                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
                        dialog.setContentView(R.layout.unfollow_diaog);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        Window window = dialog.getWindow();
                        lp.copyFrom(window.getAttributes());
                        //This makes the dialog take up the full width
                        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        window.setAttributes(lp);

                        btnCancel = dialog.findViewById(R.id.btnCancel);
                        btnRemove1 = dialog.findViewById(R.id.btnRemove);
                        tvName1 = dialog.findViewById(R.id.tvName);
                        tvRemoveText = dialog.findViewById(R.id.tvRemoveText);
                        ivProfile1 = dialog.findViewById(R.id.ivOtherUserProfile);

                        Glide
                                .with(context)
                                .load(followersList.get(position).getUserDetails().getImage())
                                .centerCrop()
                                .placeholder(R.drawable.ic_user)
                                .into(ivProfile1);

                        btnCancel.setOnClickListener(v1 ->
                                {
                                    dialog.dismiss();
                                }
                        );

                        btnRemove1.setOnClickListener(v1 ->
                                {
                                    addFollow.addFollow(followersList.get(position).getUserDetails().getId());

                                    btnFollow.setVisibility(View.VISIBLE);
                                    btnFollowing.setVisibility(View.GONE);

                                    dialog.dismiss();
                                }
                        );
                        tvName.setText(followersList.get(position).getUserDetails().getUsername());
                        tvRemoveText.setText("Stanrz won't tell "+followersList.get(position).getUserDetails().getUsername()+"they were removed from your following");
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                    }
            );

            btnFollow.setOnClickListener(v ->
                    {
                        addFollow.addFollow(followersList.get(position).getUserDetails().getId());
                        btnFollow.setVisibility(View.GONE);
                        btnFollowing.setVisibility(View.VISIBLE);
                    }
                    );
        ivProfile.setOnClickListener(view ->
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("otherUser",followersList.get(position).getUserDetails().getId());
                    Navigation.findNavController(view).navigate(R.id.action_otherUserFollowersFollowingFragment_to_otherUserDetailFragment,bundle);

                }
        );
    }

    @Override
    public int getItemCount() {
        return followersList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {
        public StoriesViewHolder(FollowersItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
