package com.technorizen.stanrz.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.PostItemBinding;
import com.technorizen.stanrz.databinding.PostItemBinding;
import com.technorizen.stanrz.models.Result;
import com.technorizen.stanrz.models.SuccessResGetUploadedVideos;
import com.technorizen.stanrz.models.SuccessResGetUploads;
import com.technorizen.stanrz.models.SuccessResGetUser;
import com.technorizen.stanrz.models.UploadedVideos;
import com.technorizen.stanrz.utility.AddFollow;
import com.technorizen.stanrz.utility.AddLike;
import com.technorizen.stanrz.utility.CircleIndicator;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.technorizen.stanrz.retrofit.Constant.USER_ID;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.StoriesViewHolder> {

    private Context context;
    
    PostItemBinding binding;

    private ViewPager viewPager;

    private ArrayList<String> images;

    private ArrayList<Result> list;

    private ArrayList<SuccessResGetUser.Result> usersList;
    private ImagesAndVideosPagerAdapter adapter;
    private String fromWhere ;
    private AddLike addFollow;
    private RadioButton radioButton;
    private RadioGroup radioGroup;
    String quantity = "";

    public void playVideo(int position)
    {
        int pos =  viewPager.getCurrentItem();
        adapter.playVideo(position,list.get(position),pos);
    }

    public void stopVideo(int position)
    {
        int pos =  viewPager.getCurrentItem();
        adapter.stopVideo(position,list.get(position),pos);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull StoriesViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        int pos = holder.getAdapterPosition();

        if(holder.itemView.findViewById(R.id.viewPager)!= null)
        {
            adapter.isAttachedView(true,pos);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public PostsAdapter(Context context, ArrayList<Result> list, AddLike addFollow,String fromWhere,ArrayList<SuccessResGetUser.Result> usersList)
    {
      this.context = context;
      this.images = images;
      this.list = list;
      this.addFollow = addFollow;
      this.fromWhere = fromWhere;
      this.usersList = usersList;
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

         boolean isUser = false;

         String userId = SharedPreferenceUtility.getInstance(context).getString(USER_ID);

         if(list.get(position).getUserId().equalsIgnoreCase(userId))
         {
             isUser = true;
         }
         else
         {
            isUser =  false;
         }

        int myPosition = position;

        ImageView ivOption =  holder.itemView.findViewById(R.id.ivOption);

        CircleImageView imageView  = holder.itemView.findViewById(R.id.ivUploader);

        CircleIndicator indicator = holder.itemView.findViewById(R.id.indicator);

        viewPager = holder.itemView.findViewById(R.id.viewPager);

        ImageView liked = holder.itemView.findViewById(R.id.ivLike);

        ImageView comment = holder.itemView.findViewById(R.id.ivComment);
        ImageView ivSuperLikes = holder.itemView.findViewById(R.id.iv_super_like);

        TextView tvName =holder.itemView.findViewById(R.id.tvUploaderName);
        TextView tvDesc;
        TextView tvTotalLikes ;
        tvDesc = holder.itemView.findViewById(R.id.tvCaption);
        tvTotalLikes = holder.itemView.findViewById(R.id.tv_by);
        TextView tvTotalComment =holder.itemView.findViewById(R.id.tv_totalComment);
        TextView tvSuperLikes = holder.itemView.findViewById(R.id.tvSuperLikes);

        TextView tvTimeAgo = holder.itemView.findViewById(R.id.tvTimeAgo);

        tvName.setText(list.get(position).getUserName());

        if(list.get(position).getDescription().equalsIgnoreCase(""))
        {
            tvDesc.setVisibility(View.GONE);
        }
        else
        {
            tvDesc.setVisibility(View.VISIBLE);
        }

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

        boolean finalIsUser = isUser;

        ivOption.setOnClickListener(v ->
                {
                    addFollow.bottomSheet(v,list.get(position).getUserPost().get(0).getPostId(), finalIsUser,position,list.get(position));
                }
                );

        adapter = new ImagesAndVideosPagerAdapter(context,list.get(position).getUserPost(),list.get(position),list.get(position).getPostIs(),list.get(position).getNsfw(),list.get(position).getUnlockWith(),addFollow);

        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                adapter.playVideoFromViewPager(position,list.get(myPosition),position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //       Toast.makeText(context,"on Page Scroll state changed."+state,Toast.LENGTH_SHORT).show();
            }
        });

        indicator.setViewPager(viewPager);
        ivSuperLikes.setOnClickListener(v ->
                {
                    showDialog(list.get(position).getUserPost().get(0).getPostId(),tvSuperLikes,position);
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
                        addFollow.addLike(list.get(position).getUserPost().get(0).getPostId(),list.get(position).getUserId());
                    }
                    else {
                        liked.setImageResource(R.drawable.ic_like);
                        list.get(position).setLiked("0");
                        int count = list.get(position).getTotalLike();

                        count = count-1;

                        list.get(position).setTotalLike(count);

                        tvTotalLikes.setText(count+" "+context.getString(R.string.likes));

                        addFollow.addLike(list.get(position).getUserPost().get(0).getPostId(),list.get(position).getUserId());
                    }
                }
                );

        comment.setOnClickListener(v ->
                {
                    addFollow.comment(v,list.get(position).getUserPost().get(0).getPostId());
                }
                );

        tvTotalComment.setOnClickListener(v ->
                {
                    addFollow.comment(v,list.get(position).getUserPost().get(0).getPostId());
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

        if(list.get(position).getTotalSuperLike() == 0)
        {
            tvSuperLikes.setVisibility(View.GONE);
        }
        else
        {
            tvSuperLikes.setVisibility(View.VISIBLE);
        }

        tvTimeAgo.setText(list.get(position).getTimeAgo());

        tvSuperLikes.setText(list.get(position).getTotalSuperLike()+" "+context.getString(R.string.super_likes));

        imageView.setOnClickListener(v ->
                {
                    
                    if(fromWhere.equalsIgnoreCase("home"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("otherUser",list.get(position).getUserId());
                        Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_otherUserDetailFragment,bundle);
                    }
                    else if(fromWhere.equalsIgnoreCase("post"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("otherUser",list.get(position).getUserId());
                        Navigation.findNavController(v).navigate(R.id.action_postsFragment_to_otherUserDetailFragment,bundle);

                    }  else if(fromWhere.equalsIgnoreCase("notification"))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("otherUser",list.get(position).getUserId());
                        Navigation.findNavController(v).navigate(R.id.action_notificationPostFragment_to_otherUserDetailFragment,bundle);

                    }
                }
                );

        String text = list.get(position).getDescription();

        SpannableStringBuilder ss = new SpannableStringBuilder(text);

        Pattern p = Pattern.compile("[@][a-zA-Z0-9-.]+");

        Matcher m = p.matcher(ss);

        while(m.find())
        {
            final int s = m.start() + 1; // add 1 to omit the "@" tag
            final int e = m.end();

            int i=0;

            for (SuccessResGetUser.Result result:usersList)
            {
                if(result.getUsername().contains(text.substring(s, e)))
                {

                    if(list.get(position).getTagUsersDetails().contains(result.getId()))
                    {

                        int finalI = i;
                        ClickableSpan clickableSpan  = new ClickableSpan()
                        {
                            @Override
                            public void onClick(View textView)
                            {

                                if(fromWhere.equalsIgnoreCase("home"))
                                {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("otherUser",usersList.get(finalI).getId());
                                    Navigation.findNavController(textView).navigate(R.id.action_navigation_home_to_otherUserDetailFragment,bundle);
                                }
                                else  if(fromWhere.equalsIgnoreCase("post"))
                                {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("otherUser",usersList.get(finalI).getId());
                                    Navigation.findNavController(textView).navigate(R.id.action_postsFragment_to_otherUserDetailFragment,bundle);
                                } else  if(fromWhere.equalsIgnoreCase("notification"))
                                {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("otherUser",usersList.get(finalI).getId());
                                    Navigation.findNavController(textView).navigate(R.id.action_notificationPostFragment_to_otherUserDetailFragment,bundle);
                                }
                            }
                        };
                        ss.setSpan(clickableSpan, m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        break;
                    }

//                    resultArrayList.add(result);

                }

                i++;
            }
        }


        tvDesc.setMovementMethod(LinkMovementMethod.getInstance());
        tvDesc.setText(ss);

    }

    public void showDialog(String postId,TextView tvSuperLikes,int position)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.choose_super_like);
        AppCompatButton btnAdd = dialog.findViewById(R.id.btnAddSuperLikes);

        radioGroup = dialog.findViewById(R.id.radioGroup);

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

        btnAdd.setOnClickListener(v ->
                {

                    int selectedId = radioGroup.getCheckedRadioButtonId();

                    int newQuantity = 0;

                    // find the radiobutton by returned id
                    radioButton = (RadioButton) dialog.findViewById(selectedId);

                    String text = radioButton.getText().toString();

                    if(text.equalsIgnoreCase("01 Super Likes"))
                    {

                        quantity = "1";
                        newQuantity = 1;

                    }else if(text.equalsIgnoreCase("05 Super Likes"))
                    {
                        quantity = "5";
                        newQuantity = 5;
                    }else if(text.equalsIgnoreCase("10 Super Likes"))

                    {
                        quantity = "10";
                        newQuantity = 10;

                    }else if(text.equalsIgnoreCase("50 Super Likes"))

                    {
                        quantity = "50";
                        newQuantity = 50;
                    }else if(text.equalsIgnoreCase("100 Super Likes"))

                    {
                        quantity = "100";
                        newQuantity = 100;
                    }else if(text.equalsIgnoreCase("1000 Super Likes"))

                    {
                        quantity = "1000";
                        newQuantity = 1000;
                    }else if(text.equalsIgnoreCase("10000 Super Likes"))
                    {
                        quantity = "10000";
                        newQuantity = 10000;
                    }

                    int  totalQuantity = list.get(position).getTotalSuperLike()+newQuantity;

                    addFollow.addSuperLikes(v,postId,"",quantity);

                    list.get(position).setTotalSuperLike(totalQuantity);

                    tvSuperLikes.setText(totalQuantity+" "+context.getString(R.string.super_likes));

                    tvSuperLikes.setVisibility(View.VISIBLE);

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
