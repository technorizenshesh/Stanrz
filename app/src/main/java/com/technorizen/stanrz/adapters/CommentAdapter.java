package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.CommentItemBinding;
import com.technorizen.stanrz.databinding.CommentItemBinding;
import com.technorizen.stanrz.models.SuccessResGetComment;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.StoriesViewHolder> {

    private Context context;

    CommentItemBinding binding;

    private List<SuccessResGetComment.Result> commentList;

    public CommentAdapter(Context context,List<SuccessResGetComment.Result> commentList)
    {
      this.context = context;
      this.commentList = commentList;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= CommentItemBinding.inflate(LayoutInflater.from(context));
        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        CircleImageView imageView;
        TextView tvName,tvComment,tvTimeAgo;

        imageView = holder.itemView.findViewById(R.id.iv_history);
        tvName = holder.itemView.findViewById(R.id.tv_name);

        tvComment = holder.itemView.findViewById(R.id.tv_comment);
        tvTimeAgo = holder.itemView.findViewById(R.id.tvTimeAgo);

        Glide.with(context)
                .load(commentList.get(position).getUserImage())
                .placeholder(R.drawable.ic_user)
                .into(imageView);

        tvName.setText(commentList.get(position).getUserName());
        tvComment.setText(decodeEmoji(commentList.get(position).getComment()));
        tvTimeAgo.setText(commentList.get(position).getTimeAgo());

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
