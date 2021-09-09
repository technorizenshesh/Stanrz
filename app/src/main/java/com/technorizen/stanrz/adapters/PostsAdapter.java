package com.technorizen.stanrz.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.PostItemBinding;
import com.technorizen.stanrz.databinding.PostItemBinding;
import com.technorizen.stanrz.models.SuccessResGetUploads;
import com.technorizen.stanrz.utility.AddFollow;
import com.technorizen.stanrz.utility.AddLike;
import com.technorizen.stanrz.utility.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.StoriesViewHolder> {

    private Context context;
    
    PostItemBinding binding;

    private ArrayList<String> images;

    private ArrayList<SuccessResGetUploads.Result> list;

    private AddLike addFollow;

    public PostsAdapter(Context context,ArrayList<SuccessResGetUploads.Result> list,AddLike addFollow)
    {
      this.context = context;
      this.images = images;
      this.list = list;
      this.addFollow = addFollow;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= PostItemBinding.inflate(LayoutInflater.from(context));
        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        CircleImageView imageView  = holder.itemView.findViewById(R.id.ivUploader);

        CircleIndicator indicator = holder.itemView.findViewById(R.id.indicator);

        ViewPager viewPager = holder.itemView.findViewById(R.id.viewPager);

        ImageView liked = holder.itemView.findViewById(R.id.ivLike);
        ImageView comment = holder.itemView.findViewById(R.id.ivComment);

        TextView tvName =holder.itemView.findViewById(R.id.tvUploaderName);
        TextView tvDesc;
        TextView tvTotalLikes ;
        tvDesc = holder.itemView.findViewById(R.id.tvCaption);
        tvTotalLikes = holder.itemView.findViewById(R.id.tv_by);
        TextView tvTotalComment =holder.itemView.findViewById(R.id.tv_totalComment);

        tvName.setText(list.get(position).getUserName());

        tvDesc.setText(list.get(position).getDescription());
        tvTotalLikes.setText(list.get(position).getTotalLike()+" "+context.getString(R.string.likes));

        Glide.with(context)
                .load(list.get(position).getUserImage())
                .centerCrop()
                .placeholder(R.drawable.ic_user)
                .into(imageView);

        if(list.get(position).getUserPost().size()==0){
            indicator.setVisibility(View.GONE);

        }
        else if(list.get(position).getUserPost().size()==1){
            indicator.setVisibility(View.GONE);

        }
        else{
            indicator.setVisibility(View.VISIBLE);
        }

        ImagesAndVideosPagerAdapter adapter = new ImagesAndVideosPagerAdapter(context,list.get(position).getUserPost());

        viewPager.setAdapter(adapter);

        indicator.setViewPager(viewPager);

        binding.ivSuperLike.setOnClickListener(v ->
                {
                    showDialog();
                }
        );

        if(list.get(position).getLiked().equalsIgnoreCase("0"))
        {
            liked.setImageResource(R.drawable.ic_like);
        }else
        {
            liked.setImageResource(R.drawable.ic_like1);
        }

        liked.setOnClickListener(v ->
                {
                    if(list.get(position).getLiked().equalsIgnoreCase("0"))
                    {
                        liked.setImageResource(R.drawable.ic_like1);
                        int count = list.get(position).getTotalLike();
                        count = count+1;
                        list.get(position).setTotalLike(count);
                        tvTotalLikes.setText(count+" "+context.getString(R.string.likes));
                        list.get(position).setLiked("1");
                        addFollow.addLike(list.get(position).getId(),list.get(position).getUserId());
                    }
                    else {
                        liked.setImageResource(R.drawable.ic_like);
                        list.get(position).setLiked("0");
                        int count = list.get(position).getTotalLike();

                        count = count-1;

                        list.get(position).setTotalLike(count);

                        tvTotalLikes.setText(count+" "+context.getString(R.string.likes));

                        addFollow.addLike(list.get(position).getId(),list.get(position).getUserId());

                    }
                }
                );

        comment.setOnClickListener(v ->
                {
                    addFollow.comment(v,list.get(position).getId());
                }
                );

        tvTotalComment.setOnClickListener(v ->
                {
                    addFollow.comment(v,list.get(position).getId());
                }
        );

        if(list.get(position).getTotalComment() == 0)
        {
            tvTotalComment.setVisibility(View.GONE);
        }
        else
        {
            tvTotalComment.setVisibility(View.VISIBLE);
        }

        tvTotalComment.setText(context.getString(R.string.view_all1)+" "+list.get(position).getTotalComment()+" "+context.getString(R.string.comment));


    }

    public void showDialog()
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.choose_super_like);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        ImageView ivCross = dialog.findViewById(R.id.ivCross);
        ivCross.setOnClickListener(v ->
                {
                    dialog.dismiss();
                }
                );


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(PostItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
