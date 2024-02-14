package com.tp.photo.Utility;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {
    private  LinearLayoutManager linearLayoutManager;
    public   PaginationScrollListener(LinearLayoutManager linearLayoutManager){
       this.linearLayoutManager=linearLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount=linearLayoutManager.getItemCount();
        int totalItemCount=linearLayoutManager.getItemCount();
        int firstVisibleItemPosition=linearLayoutManager.findFirstVisibleItemPosition();
        if(isloading()||isLastPage()){
            return;
        }
        if(firstVisibleItemPosition>=0&&(visibleItemCount+firstVisibleItemPosition)>+totalItemCount){
            loadMoreItem();
        }
    }
    public abstract void loadMoreItem();
    public abstract boolean isloading();
    public abstract boolean isLastPage();
}
