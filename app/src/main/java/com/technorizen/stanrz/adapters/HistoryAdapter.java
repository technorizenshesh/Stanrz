package com.technorizen.stanrz.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.HistroyItemBinding;
import com.technorizen.stanrz.databinding.HistroyItemBinding;
import com.technorizen.stanrz.models.SuccessResGetTransactionHistory;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.StoriesViewHolder> {
    private Context context;
    HistroyItemBinding binding;
    private ArrayList<SuccessResGetTransactionHistory.Result> transactionHistory;
    public HistoryAdapter(Context context,ArrayList<SuccessResGetTransactionHistory.Result> transactionHistory)
    {
      this.context = context;
      this.transactionHistory = transactionHistory;
    }
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= HistroyItemBinding.inflate(LayoutInflater.from(context));
        return new StoriesViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {
        TextView tvMessage = holder.itemView.findViewById(R.id.tvMessage);
        TextView tvDate = holder.itemView.findViewById(R.id.tv_date);
        tvMessage.setText(transactionHistory.get(position).getMessage());
        tvDate.setText(transactionHistory.get(position).getTimeAgo());
    }
    @Override
    public int getItemCount() {
        return transactionHistory.size();
    }
    public class StoriesViewHolder extends RecyclerView.ViewHolder {
        public StoriesViewHolder(HistroyItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
