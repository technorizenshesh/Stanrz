package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technorizen.stanrz.R;

import com.technorizen.stanrz.databinding.SubscriptionPlanItemBinding;
import com.technorizen.stanrz.models.SuccessResGetPackages;
import com.technorizen.stanrz.utility.SubscriptionClick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ravindra Birla on 10,June,2021
 */
public class SubscriptionPlanAdapter extends RecyclerView.Adapter<SubscriptionPlanAdapter.CategoryViewHolder> {

    private Context context;

    SubscriptionPlanItemBinding binding;

    private boolean from;

    private ArrayList<SuccessResGetPackages.Result> packagesList;

    private SubscriptionClick click;

    public SubscriptionPlanAdapter(Context context, ArrayList<SuccessResGetPackages.Result> packagesList,SubscriptionClick click,boolean from)
    {
        this.context = context;
        this.packagesList= packagesList;
        this.click = click;
        this.from = from;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= SubscriptionPlanItemBinding.inflate(LayoutInflater.from(context));
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        TextView tvSuperlikes,tvMonths,tv200Edit,tvJoin;
        tvSuperlikes = holder.itemView.findViewById(R.id.tvSuperLikes);
        tvMonths = holder.itemView.findViewById(R.id.tvMonth4);
        tv200Edit = holder.itemView.findViewById(R.id.tv200Edit);
        tvJoin = holder.itemView.findViewById(R.id.tvJoin);
        RelativeLayout ll200 = holder.itemView.findViewById(R.id.ll200);
        tvSuperlikes.setText(packagesList.get(position).getSuperlike()+" /");
        if(packagesList.get(position).getForMonth().equalsIgnoreCase("") || packagesList.get(position).getForMonth().equalsIgnoreCase(context.getString(R.string.monthly)))
        {
            tvMonths.setText(packagesList.get(position).getForMonth());
            if(packagesList.get(position).getViewStatus().equalsIgnoreCase(context.getString(R.string.hide)))
            {
                ll200.setBackground(context.getResources().getDrawable(R.drawable.ic_blank_black));
                tv200Edit.setBackground(context.getResources().getDrawable(R.drawable.black_button_bg));
                tvJoin.setBackground(context.getResources().getDrawable(R.drawable.blue_button_bg));
            }
            else
            {
                ll200.setBackground(context.getResources().getDrawable(R.drawable.ic_blank_blue));
                tv200Edit.setBackground(context.getResources().getDrawable(R.drawable.blue_button_bg));
                tvJoin.setBackground(context.getResources().getDrawable(R.drawable.blue_button_bg));
            }
        }
        else
        {
            ll200.setBackground(context.getResources().getDrawable(R.drawable.ic_blank_orange));
            tv200Edit.setBackground(context.getResources().getDrawable(R.drawable.orange_button_bg));
            tvJoin.setBackground(context.getResources().getDrawable(R.drawable.orange_button_bg));
            tvMonths.setText(packagesList.get(position).getForMonth()+" "+context.getResources().getString(R.string.months));
        }

        tv200Edit.setOnClickListener(v ->
                {
                    click.superLikeClick(v,position,"true");
                }
                );

        tvJoin.setOnClickListener(v ->
                {
                    click.superLikeClick(v,position,"true");
                }
                );

        if(from)
        {
            tv200Edit.setVisibility(View.VISIBLE);
            tvJoin.setVisibility(View.GONE);
        }
        else
        {
            tv200Edit.setVisibility(View.GONE);
            tvJoin.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        return packagesList.size();
    }
    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {
        public CategoryViewHolder(SubscriptionPlanItemBinding binding1) {
            super(binding1.getRoot());
        }
    }
}
