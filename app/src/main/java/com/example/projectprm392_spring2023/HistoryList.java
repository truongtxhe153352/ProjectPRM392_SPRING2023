package com.example.projectprm392_spring2023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HistoryList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    private RecyclerView rcvHistory;
    private HistoryApdapter apdapter;

    private List<History> list;
    private int id = 1;

    private History history;

    private boolean isLoading;
    private boolean isLastPage;
    private int totalPage = 1;
    private int currentPage = 1;

    private void BindingView(){
        rcvHistory = findViewById(R.id.rcv_history);
        navigationView =  findViewById(R.id.nav_view);
    }

    private void loadData(List<History> listSearch){

        if(listSearch != null){
            list = new ArrayList<>();
            list = listSearch;

        }else{
            //list = HistoryDatabase.getInstance(this).historyDAO().getListHistory();
            //list.add(new History(1,R.drawable.camchatgpt, "14/3/2023", "Test 1"));

            list = getListHistory(0);
        }
        apdapter = new HistoryApdapter(list, this);
        rcvHistory.setAdapter(apdapter);


    }

    private List<History> getListHistory(int offset){

        List<History> historyList = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            //historyList.add(new History(i,R.drawable.camchatgpt, "14/3/2023", "Test 1"));
            historyList = HistoryDatabase.getInstance(this).historyDAO().getListHistory(i, offset);
        }
        if(historyList.size() > 0){
            Toast.makeText(this, "Loading data page" + currentPage, Toast.LENGTH_SHORT).show();
            id = historyList.size() + 1;

            return historyList;
        }
        return null;
    }

    private void New(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        EditText editSearch = dialogView.findViewById(R.id.edtSearch);

        new AlertDialog.Builder(this)
                .setTitle("Enter new History")
                .setView(dialogView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String searchText = editSearch.getText().toString().trim();
                        history = new History(searchText, searchText, searchText, searchText);
                        HistoryDatabase.getInstance(HistoryList.this).historyDAO().insertHistory(history);
                        id++;
                        loadData(null);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void Search(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        EditText editSearch = dialogView.findViewById(R.id.edtSearch);

        new AlertDialog.Builder(this)
                .setTitle("Enter what do you want to search")
                .setView(dialogView)
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String searchText = editSearch.getText().toString().trim();
                        List<History> listSearch = HistoryDatabase.getInstance(HistoryList.this).historyDAO().searchHistory(searchText);
                        loadData(listSearch);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);
        BindingView();
        navigationView.setNavigationItemSelectedListener(this);
        // Sidebar menu
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        drawerLayout.bringToFront();
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvHistory.setLayoutManager(linearLayoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvHistory.addItemDecoration(itemDecoration);

        loadData(null);

        rcvHistory.addOnScrollListener(new Paging(linearLayoutManager) {
            @Override
            public void loadMoreHistory() {
                isLoading = true;
                currentPage += 1;
                if(list.size() >= 8){
                    loadNextPage();
                }

            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_new_photo: {
                Intent i = new Intent(HistoryList.this, TextDetectionActivity.class);
                startActivity(i);
                Log.d("thaiduongme", "New photo chose");
                break;
            }
            case R.id.nav_history: {
                Log.d("thaiduongme", "New history chose");
            }
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.optSearch:
                Search();
                break;
//            case R.id.optNew:
//                New();
//                break;
        }
        super.onOptionsItemSelected(item);
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
    private void loadNextPage(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<History> historyList = getListHistory(list.size() - currentPage);
                apdapter.removeFooterLoading();
                list.addAll(historyList);
                apdapter.notifyDataSetChanged();

                isLoading = false;
                if(currentPage < totalPage){
                    apdapter.addFooterLoading();
                }else {
                    isLastPage = true;
                }
            }
        }, 2000);

    }

}