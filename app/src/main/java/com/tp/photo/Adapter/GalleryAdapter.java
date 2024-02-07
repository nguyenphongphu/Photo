package com.tp.photo.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.tp.photo.App.ImageActivity;
import com.tp.photo.App.MainActivity;
import com.tp.photo.Model.ListData;
import com.tp.photo.R;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.CustomViewHolder> {

    private final List<ListData> mData;
    private final Activity mActivity;
    private static final String TAG="MainActivity";

    public GalleryAdapter(Activity activity, List<ListData> fileList) {
        mActivity = activity;
        mData = fileList;
    }



    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_recyclerview, parent, false);
        return new GalleryAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        ListData listData=mData.get(position);
        if(listData==null){
            return;
        }
        holder.Time.setText(listData.getTime());
        GridLayoutManager gridLayoutManager=new GridLayoutManager(mActivity,3);
        holder.recyclerView.setLayoutManager(gridLayoutManager);
        ImageAdapter imageAdapter=new ImageAdapter(mActivity,listData.getDataList());
        holder.recyclerView.setAdapter(imageAdapter);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        final TextView Time;
        final RecyclerView recyclerView;

        CustomViewHolder(View itemView) {
            super(itemView);
            this.recyclerView = (RecyclerView) itemView.findViewById(R.id.item_data_recyclerview);
            this.Time=(TextView) itemView.findViewById(R.id.text_time_item);
        }
    }

}
