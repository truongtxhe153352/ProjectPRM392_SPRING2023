package com.example.projectprm392_spring2023;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryApdapter extends RecyclerView.Adapter<HistoryApdapter.HistoryViewHolder>{

    private List<History> list;
    Context context;

    public HistoryApdapter(List<History> historyList, Context context){
        this.list = historyList;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);

        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        History history = list.get(position);
        if(history == null){
            return;
        }
        holder.img_cam.setImageResource(history.getResourceId());
        holder.txt_date.setText(history.getDate());
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGotoDetail(history);
            }
        });

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

}