package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.MySubscribedPlanBinding;
import com.technorizen.stanrz.databinding.MySubscribedPlanBinding;
import com.technorizen.stanrz.models.SuccessResGetMyPurchasedPlan;
import com.technorizen.stanrz.models.SuccessResGetSuperLikePlan;
import com.technorizen.stanrz.utility.SuperlikeClick;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by Ravindra Birla on 18,March,2021
 */
public class MyPurchasedSubscriptionAdapter extends RecyclerView.Adapter<MyPurchasedSubscriptionAdapter.ChatViewHolder> {

    MySubscribedPlanBinding binding;


   private Context context;
   private List<SuccessResGetMyPurchasedPlan.Result> planList ;


 public MyPurchasedSubscriptionAdapter(Context context, List<SuccessResGetMyPurchasedPlan.Result> planList)
 {
     this.context = context;
     this.planList = planList;
 }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.my_subscribed_plan, parent, false);
        return new ChatViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        TextView tvName,tvMonth,tvSuperLike,tvStartDate,tvEndDate,tvStatus;
        TextView btnDetail;

        tvName = holder.itemView.findViewById(R.id.tvName);
        tvStatus = holder.itemView.findViewById(R.id.tvStatus);
        tvMonth = holder.itemView.findViewById(R.id.tvMonth);
        tvSuperLike = holder.itemView.findViewById(R.id.tvSuperLike);
        tvStartDate = holder.itemView.findViewById(R.id.tvStartDate);
        tvEndDate = holder.itemView.findViewById(R.id.tvEndDate);

        ImageView ivProfile =holder.itemView.findViewById(R.id.ivProfile);

        Glide.with(context)
                .load(planList.get(position).getUserDetails().getImage())
                .fitCenter()
                .into(ivProfile);

        tvName.setText(planList.get(position).getUserDetails().getUsername());
        tvStatus.setText("Status : "+planList.get(position).getPlanIs());
        tvMonth.setText("Months : "+planList.get(position).getForMonth());
        tvSuperLike.setText("Superlikes : "+planList.get(position).getSuperlike());
        tvStartDate.setText(planList.get(position).getSubscriptionDate());
        tvEndDate.setText(planList.get(position).getEndDate());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder
    {

        public ChatViewHolder(MySubscribedPlanBinding MySubscribedPlanBinding) {
            super(MySubscribedPlanBinding.getRoot());
        }
    }

}
