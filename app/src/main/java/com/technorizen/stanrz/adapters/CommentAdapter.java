package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.CommentItemBinding;
import com.technorizen.stanrz.models.SuccessResGetComment;
import com.technorizen.stanrz.models.SuccessResGetUser;
import com.technorizen.stanrz.utility.DeleteComment;
import com.technorizen.stanrz.utility.SharedPreferenceUtility;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.technorizen.stanrz.activites.LoginActivity.TAG;
import static com.technorizen.stanrz.retrofit.Constant.USER_ID;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.StoriesViewHolder> {

    private Context context;

    CommentItemBinding binding;

    private DeleteComment deleteComment;

    private ArrayList<SuccessResGetUser.Result> usersList;

    private List<SuccessResGetComment.Result> commentList;

    public CommentAdapter(Context context,List<SuccessResGetComment.Result> commentList,ArrayList<SuccessResGetUser.Result> usersList,DeleteComment deleteComment)
    {
      this.context = context;
      this.commentList = commentList;
      this.usersList = usersList;
      this.deleteComment = deleteComment;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= CommentItemBinding.inflate(LayoutInflater.from(context));
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        CircleImageView imageView;
        TextView tvName,tvComment,tvTimeAgo;

        RelativeLayout rlParent = holder.itemView.findViewById(R.id.rlParent);

        imageView = holder.itemView.findViewById(R.id.iv_history);
        tvName = holder.itemView.findViewById(R.id.tv_name);

       ImageView ivDelete = holder.itemView.findViewById(R.id.ivDelete);

        tvComment = holder.itemView.findViewById(R.id.tv_comment);
        tvTimeAgo = holder.itemView.findViewById(R.id.tvTimeAgo);

        Glide.with(context)
                .load(commentList.get(position).getUserImage())
                .placeholder(R.drawable.ic_user)
                .into(imageView);

        tvName.setText(commentList.get(position).getUserName());
//        tvComment.setText(decodeEmoji(commentList.get(position).getComment()));
        tvTimeAgo.setText(commentList.get(position).getTimeAgo());

        imageView.setOnClickListener(view ->
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("otherUser",commentList.get(position).getUserId());
                    Navigation.findNavController(view).navigate(R.id.action_addCommentFragment_to_otherUserDetailFragment,bundle);
                }
                );

        String text = decodeEmoji(commentList.get(position).getComment());

        SpannableStringBuilder ss = new SpannableStringBuilder(text);

        Pattern p = Pattern.compile("[@][a-zA-Z0-9-.]+");

        Matcher m = p.matcher(ss);

        while(m.find())
        {
            final int s = m.start() + 1; // add 1 to omit the "@" tag

            final int e = m.end();

            int i=0;

            Log.d(TAG, "onBindViewHolder: usersList"+usersList.size());

            for (SuccessResGetUser.Result result:usersList)
            {

                Log.d(TAG, "SuccessResGetUser.Result "+usersList.get(i).getUsername());

                if(result.getUsername().contains(text.substring(s, e)))
                {
                    if(commentList.get(position).getTagUsersDetails().contains(result.getId()))
                    {

//                    resultArrayList.add(result);
                        Log.d(TAG, "Content: "+usersList.get(i).getUsername());

                        int finalI = i;
                        ClickableSpan clickableSpan  = new ClickableSpan()
                        {
                            @Override
                            public void onClick(View textView)
                            {
                                Log.d(TAG, "onClick: "+usersList.get(finalI).getUsername());
                                Bundle bundle = new Bundle();
                                bundle.putString("otherUser",usersList.get(finalI).getId());
                                Navigation.findNavController(textView).navigate(R.id.action_addCommentFragment_to_otherUserDetailFragment,bundle);
                            }
                        };
                        ss.setSpan(clickableSpan, m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    makeLinkClickable(m.start(), m.end(),ss, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,finalI);
                        break;
                    }
                }
                i++;
            }
        }

        tvComment.setText(ss);
        tvComment.setMovementMethod(LinkMovementMethod.getInstance());

        String userId = SharedPreferenceUtility.getInstance(context).getString(USER_ID);

        ivDelete.setOnClickListener(view ->
                {
                    new AlertDialog.Builder(context)
                            .setTitle("Delete Comment")
                            .setMessage("Are you sure you want to delete Comment?")

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    deleteComment.deleteComment(commentList.get(position).getUserId(),commentList.get(position).getSid());

                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
        );

        if(userId.equalsIgnoreCase(commentList.get(position).getUserId()))
        {

            ivDelete.setVisibility(View.VISIBLE);

        } else if(userId.equalsIgnoreCase(commentList.get(position).getPostUser()))
        {

            ivDelete.setVisibility(View.VISIBLE);
        }
        else
        {
            ivDelete.setVisibility(View.GONE);
        }

//
//        tvComment.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                tvComment.setText(ss);
//                tvComment.setMovementMethod(LinkMovementMethod.getInstance());
//            }
//        },1000);
    }

    protected void makeLinkClickable(int start,int end,SpannableStringBuilder strBuilder, int span,int position)
    {

        ClickableSpan clickable = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Bundle bundle = new Bundle();
                bundle.putString("otherUser",usersList.get(position).getId());
                Navigation.findNavController(widget).navigate(R.id.action_addCommentFragment_to_otherUserDetailFragment,bundle);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
            }
        };

        strBuilder.setSpan(clickable, start, end, span);
        strBuilder.removeSpan(span);
    }

    public static String decodeEmoji (String message) {
        String myString= null;
        try {
            return URLDecoder.decode(
                    message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }
    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(CommentItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
