package com.erikagtierrez.multiple_media_picker.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.erikagtierrez.multiple_media_picker.R;

import java.util.List;

public class BucketsAdapter extends RecyclerView.Adapter<BucketsAdapter.MyViewHolder>{
    private List<String> bucketNames,bitmapList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        protected RequestOptions requestOptions;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail=(ImageView) view.findViewById(R.id.image);
            requestOptions =new RequestOptions().transforms(new CenterCrop(),new RoundedCorners(15));

        }
    }

    public BucketsAdapter(List<String> bucketNames,List<String> bitmapList,Context context) {
        this.bucketNames=bucketNames;
        this.bitmapList = bitmapList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        bucketNames.get(position);
        holder.title.setText(bucketNames.get(position));
        Glide.with(context).load("file://"+bitmapList.get(position)).apply(holder.requestOptions).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return bucketNames.size();
    }
}

