package com.technorizen.stanrz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.technorizen.stanrz.R;
import com.technorizen.stanrz.databinding.SearchItemBinding;

/**
 * Created by Ravindra Birla on 06,July,2021
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.StoriesViewHolder> {

    private Context context;

    SearchItemBinding binding;

    public MessageAdapter(Context context)
    {
      this.context = context;
    }
    
    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= SearchItemBinding.inflate(LayoutInflater.from(context));
        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);

        return new StoriesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {

        binding.rlParent.setOnClickListener(v ->
                {
                    Navigation.findNavController(v).navigate(R.id.action_chatFragment_to_one2OneChatFragment);
                }
                );

    }


    @Override
    public int getItemCount() {
        return 6;
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {

        public StoriesViewHolder(SearchItemBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
