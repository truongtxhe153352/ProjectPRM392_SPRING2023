package com.example.projectprm392_spring2023;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryApdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_HISTORY = 1;
    private static final int TYPE_LOADING = 2;

    private List<History> list;
    Context context;
    private boolean isLoadingAdd;

    @Override
    public int getItemViewType(int position) {
        if(list != null && position == list.size() - 1 &&  isLoadingAdd){
            return TYPE_LOADING;
        }
        return TYPE_HISTORY;
    }

    public HistoryApdapter(List<History> historyList, Context context){
        this.list = historyList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(TYPE_HISTORY == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
            return new HistoryViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_loading, parent, false);
            return new LoadingViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == TYPE_HISTORY){
            History history = list.get(position);
            if(history == null){
                return;
            }
            HistoryViewHolder historyViewHolder = (HistoryViewHolder) holder;
            historyViewHolder.img_cam.setImageResource(history.getResourceId());
            historyViewHolder.txt_date.setText(history.getDate());
            historyViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickGotoDetail(history);
                }
            });
        }
    }


    private void onClickGotoDetail(History history){
        Intent intent = new Intent(context, HistoryDetail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_history", history);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    public void release(){
        context = null;
    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{

        private ImageView img_cam;
        private TextView txt_date;
        private RelativeLayout btnDetail;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            img_cam = itemView.findViewById(R.id.img_cam);
            txt_date = itemView.findViewById(R.id.txt_date);
            btnDetail = itemView.findViewById(R.id.btnDetail);
        }
    }
    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        private ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }

    public void addFooterLoading(){
        isLoadingAdd = true;
        list.add(new History(1, R.drawable.camchatgpt, "",""));

    }
    public void removeFooterLoading(){
        isLoadingAdd = false;
        int postion = list.size() - 1;
        History history = list.get(postion);
        if(history != null){
            list.remove(postion);
            notifyItemRemoved(postion);

        }
    }

}