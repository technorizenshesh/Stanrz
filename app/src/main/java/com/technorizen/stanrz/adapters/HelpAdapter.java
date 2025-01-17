package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.technorizen.stanrz.R;
import com.technorizen.stanrz.models.SuccessResGetHelp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ravindra Birla on 05,August,2021
 */
public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.SelectTimeViewHolder> {

    private Context context;

    private ArrayList<SuccessResGetHelp.Result> faqsList ;

    private boolean showAns = false;

    public HelpAdapter(Context context, ArrayList<SuccessResGetHelp.Result> faqsList)
    {
        this.context = context;
        this.faqsList = faqsList;
    }

    @NonNull
    @Override
    public SelectTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.help_item, parent, false);
        SelectTimeViewHolder viewHolder = new SelectTimeViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTimeViewHolder holder, int position) {
        
        MaterialCardView cv = holder.itemView.findViewById(R.id.cv);
        TextView tvQues = holder.itemView.findViewById(R.id.tvQs);
        TextView tvAnswer = holder.itemView.findViewById(R.id.tvAns);
        RelativeLayout rlShiftsNote = holder.itemView.findViewById(R.id.rlShiftsNotes);

        ImageView ivAdd =  holder.itemView.findViewById(R.id.plus);
        ImageView ivMinus =  holder.itemView.findViewById(R.id.minus);

        tvQues.setText(faqsList.get(position).getTitle());
        tvAnswer.setText(faqsList.get(position).getDescription());

        rlShiftsNote.setOnClickListener(v ->
                {

                    showAns = !showAns;

                    if(showAns)
                    {
                        tvAnswer.setVisibility(View.VISIBLE);
                        ivAdd.setVisibility(View.GONE);
                        ivMinus.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        tvAnswer.setVisibility(View.GONE);
                        ivAdd.setVisibility(View.VISIBLE);
                        ivMinus.setVisibility(View.GONE);
                    }
                }
        );

    }

    @Override
    public int getItemCount() {
        return faqsList.size();
    }

    public class SelectTimeViewHolder extends RecyclerView.ViewHolder {
        public SelectTimeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
