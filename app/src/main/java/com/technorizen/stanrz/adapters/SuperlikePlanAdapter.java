package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.SuperlikePlanItemBinding;
import com.technorizen.stanrz.databinding.SuperlikePlanItemBinding;
import com.technorizen.stanrz.models.SuccessResGetChat;
import com.technorizen.stanrz.models.SuccessResGetSuperLikePlan;
import com.technorizen.stanrz.utility.SuperlikeClick;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by Ravindra Birla on 18,March,2021
 */
public class SuperlikePlanAdapter extends RecyclerView.Adapter<SuperlikePlanAdapter.ChatViewHolder> {

    SuperlikePlanItemBinding binding;

    private int selected = -1 ;
    private RadioButton lastCheckedRB = null;

   private Context context;
   private List<SuccessResGetSuperLikePlan.Result> superLikeList = new LinkedList<>();
   String myId;

   private SuperlikeClick superlikeClick;

 public SuperlikePlanAdapter(Context context, List<SuccessResGetSuperLikePlan.Result> superLikeList,SuperlikeClick superlikeClick)
 {
     this.context = context;
     this.superLikeList = superLikeList;
     this.myId = myId;
     this.superlikeClick = superlikeClick;
 }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.superlike_plan_item, parent, false);
        return new ChatViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

     RadioButton rbButton = holder.itemView.findViewById(R.id.btnSuperLike);

     if(position == selected)
     {
         rbButton.setChecked(true);
     }
     else
     {
         rbButton.setChecked(false);
     }

     rbButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             selected = position;

             notifyDataSetChanged();

             superlikeClick.superLikeClick(position,true);

         }
     });

     rbButton.setText(superLikeList.get(position).getSuperlike()+" Super Likes"+" ( "+superLikeList.get(position).getPrice()+"$ )");

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
        return superLikeList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder
    {

        public ChatViewHolder(SuperlikePlanItemBinding SuperlikePlanItemBinding) {
            super(SuperlikePlanItemBinding.getRoot());
        }
    }

}
