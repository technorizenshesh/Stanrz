package com.technorizen.stanrz.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.ConversationItemBinding;
import com.technorizen.stanrz.databinding.ConversationItemBinding;
import com.technorizen.stanrz.models.SuccessResGetConversation;
import com.technorizen.stanrz.utility.BottomSheet;
import com.technorizen.stanrz.utility.ReportInterface;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.StoriesViewHolder> {

    private Context context;

    ConversationItemBinding binding;

    private ReportInterface reportInterface;

    private ArrayList<SuccessResGetConversation.Result> conversationList;

    public MessageAdapter(Context context,ArrayList<SuccessResGetConversation.Result> conversationList,ReportInterface reportInterface)
    {
      this.context = context;
      this.conversationList = conversationList;
      this.reportInterface = reportInterface;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= ConversationItemBinding.inflate(LayoutInflater.from(context));
        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        CircleImageView circleImageView = holder.itemView.findViewById(R.id.ivProfile);

        TextView tvUserName,tvvFullName;

        ImageView ivOption = holder.itemView.findViewById(R.id.ivOption);

        ImageView ivActive = holder.itemView.findViewById(R.id.ivActive);

        RelativeLayout rlParent = holder.itemView.findViewById(R.id.rlParent);

        tvUserName = holder.itemView.findViewById(R.id.tvUserName);

        tvvFullName = holder.itemView.findViewById(R.id.tvMessage);

        tvUserName.setText(conversationList.get(position).getUsername());

        tvvFullName.setText(conversationList.get(position).getLastMessage());

        if(conversationList.get(position).getMsgStatus().equalsIgnoreCase("NOTSEEN"))
        {
            ivActive.setVisibility(View.VISIBLE);
        }
        else
        {
            ivActive.setVisibility(View.GONE);
        }

        Glide
                .with(context)
                .load(conversationList.get(position).getImage())
                .centerCrop()
                .into(circleImageView);

     rlParent.setOnClickListener(v ->
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("name",conversationList.get(position).getUsername());
                    bundle.putString("image",conversationList.get(position).getImage());
                    bundle.putString("id",conversationList.get(position).getId());
                    Navigation.findNavController(v).navigate(R.id.action_chatFragment_to_one2OneChatFragment,bundle);
                }
                );

     ivOption.setOnClickListener(v ->
             {

                 final Dialog dialog = new Dialog(context);
                 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                 dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
                 dialog.setContentView(R.layout.dialog_conversation_option);
                 WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                 Window window = dialog.getWindow();
                 lp.copyFrom(window.getAttributes());

                 lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                 lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                 window.setAttributes(lp);

                 TextView tvDelete = dialog.findViewById(R.id.tvDelete);
                 TextView tvBlock = dialog.findViewById(R.id.tvBlock);
                 TextView tvReport = dialog.findViewById(R.id.tvReport);
                 TextView tvViewProfile = dialog.findViewById(R.id.tvViewProfile);

                 if(conversationList.get(position).getBlockUser().equalsIgnoreCase("Block"))
                 {
                     tvBlock.setText("Unblock");
                 }
                 else
                 {
                     tvBlock.setText("Block");
                 }

                 tvDelete.setOnClickListener(v1 ->
                         {
                             reportInterface.deleteChat(conversationList.get(position).getId());
                             dialog.dismiss();
                         }
                         );

                 tvViewProfile.setOnClickListener(v1 ->
                         {

                             Bundle bundle = new Bundle();
                             bundle.putString("otherUser",conversationList.get(position).getId());
                             Navigation.findNavController(v).navigate(R.id.action_chatFragment_to_otherUserDetailFragment,bundle);
                             dialog.dismiss();
                         }
                         );

                 tvBlock
                         .setOnClickListener(v1 ->
                                 {

                                     dialog.dismiss();
                                     reportInterface.blockUser(conversationList.get(position).getReceiverId(),conversationList.get(position).getSenderId());
                                 }
                                 );

                 tvReport
                         .setOnClickListener(v1 ->
                                 {

                                     dialog.dismiss();
                                     reportInterface.onReport(conversationList.get(position).getReceiverId(),conversationList.get(position).getSenderId());
                                 }
                         );

                 dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                 dialog.show();
             }
             );

    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(ConversationItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
