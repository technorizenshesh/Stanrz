package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.CardItemBinding;
import com.technorizen.stanrz.models.SuccessResGetCards;
import com.technorizen.stanrz.utility.UpdateAndDeleteAddress;
import com.vinaygaba.creditcardview.CardType;
import com.vinaygaba.creditcardview.CreditCardView;

import java.util.List;

/**
 * Created by Ravindra Birla on 10,June,2021
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CategoryViewHolder> {

    private Context context;
    CardItemBinding binding;

     
    
    private List<SuccessResGetCards.Result> cardList;
    private UpdateAndDeleteAddress updateAndDeleteAddress;

    private int selectedPosition = -1;
    private boolean from;

    public CardAdapter(Context context, List<SuccessResGetCards.Result> cardList, UpdateAndDeleteAddress updateAndDeleteAddress, boolean from)
    {
        this.context = context;
        this.cardList= cardList;
        this.updateAndDeleteAddress = updateAndDeleteAddress;
        this.from = from;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= CardItemBinding.inflate(LayoutInflater.from(context));

        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new CategoryViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        TextView tvAddress,tvName,tvPhone;
        AppCompatButton btnEdit;
        ImageView ivDelete;

        CreditCardView llParent = holder.itemView.findViewById(R.id.card1);

        String cardNumber = cardList.get(position).getCardNo();

        llParent.setCardName(cardList.get(position).getCardHolderName());
        llParent.setCardNumber(cardList.get(position).getCardNo());
        llParent.setExpiryDate(cardList.get(position).getExpMonth()+"/"+cardList.get(position).getExpYear());

        llParent.setType(CardType.AUTO);

        if(position == selectedPosition)
        {
            llParent.setBackgroundResource(R.drawable.light_blue_fill);
        }
        else
        {
            llParent.setBackgroundResource(R.drawable.light_gray_fill);
        }


        if (from)
        {
            llParent.setOnClickListener(v ->
                    {

                        selectedPosition = position;
                        updateAndDeleteAddress.isSelected(true,position);
                        notifyDataSetChanged();

                    }
            );
        }

    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {

        public CategoryViewHolder(CardItemBinding binding1) {
            super(binding1.getRoot());
        }
    }

}
