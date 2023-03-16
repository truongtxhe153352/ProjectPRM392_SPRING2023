package com.example.projectprm392_spring2023;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class Paging extends RecyclerView.OnScrollListener {
    private LinearLayoutManager linearLayoutManager;

    public Paging(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = linearLayoutManager.getChildCount();
        int totalItemCount =  linearLayoutManager.getItemCount();
        int firstVisibleItemPostion = linearLayoutManager.findFirstVisibleItemPosition();

        if(isLoading() || isLastPage()){
            return;
        }
        if(firstVisibleItemPostion >= 0 && (visibleItemCount + firstVisibleItemPostion) >= totalItemCount){
            loadMoreHistory();
        }
    }

    public abstract void loadMoreHistory();
    public abstract boolean isLoading();
    public abstract boolean isLastPage();
}
