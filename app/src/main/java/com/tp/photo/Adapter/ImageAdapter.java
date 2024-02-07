package com.tp.photo.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tp.photo.App.ImageActivity;
import com.tp.photo.R;

import java.util.List;

public class ImageAdapter  extends RecyclerView.Adapter<ImageAdapter.CustomViewHolder>{

    private final List<String> mFileList;
    private final Activity mActivity;

    public ImageAdapter(Activity activity, List<String> fileList) {
        mActivity = activity;
        mFileList = fileList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
        return new ImageAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        if(mFileList.get(position).trim().toString().contains("mp4")){
            Glide
                    .with(mActivity)
                    .load(mFileList.get(position))
                    .override(350, 350)
                    .centerCrop()
                    .into(holder.imageResource);
            final int itemPosition = holder.getAdapterPosition();
            holder.imageResource.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, ImageActivity.class);
                    intent.putExtra("Image",mFileList.get(itemPosition));
                    mActivity.startActivity(intent);
                }
            });
        }else {
            Glide
                    .with(mActivity)
                    .load(mFileList.get(position))
                    .override(350, 350)
                    .centerCrop()
                    .into(holder.imageResource);
            final int itemPosition = holder.getAdapterPosition();
            holder.imageResource.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, ImageActivity.class);
                    intent.putExtra("Image",mFileList.get(itemPosition));
                    mActivity.startActivity(intent);
                }
            });
            holder.image_play.setVisibility(View.INVISIBLE);
            holder.textView_video.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageResource;
        final ImageView image_play;
        final TextView textView_video;

        CustomViewHolder(View itemView) {
            super(itemView);
            this.imageResource = (ImageView) itemView.findViewById(R.id.image_view);
            this.textView_video=(TextView) itemView.findViewById(R.id.text_time_video);
            this.image_play=(ImageView) itemView.findViewById(R.id.image_lpay);
        }
    }
}
