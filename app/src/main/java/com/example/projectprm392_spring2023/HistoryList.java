package com.example.projectprm392_spring2023;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class HistoryList extends AppCompatActivity {


    private RecyclerView rcvHistory;
    private HistoryApdapter apdapter;
    private void BindingView(){
        rcvHistory = findViewById(R.id.rcv_history);
    }

    private void loadData(){
        List<History> list = HistoryDatabase.getInstance(this).historyDAO().getListHistory();

        list.add(new History(1,R.drawable.camchatgpt, "14/3/2023", "Test 1"));

        apdapter = new HistoryApdapter(list, this);
        rcvHistory.setAdapter(apdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);
        BindingView();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvHistory.setLayoutManager(linearLayoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvHistory.addItemDecoration(itemDecoration);

        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(apdapter != null){
            apdapter.release();
        }
    }
}