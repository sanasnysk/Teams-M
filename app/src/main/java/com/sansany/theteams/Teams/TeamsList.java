package com.sansany.theteams.Teams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sansany.theteams.Contents.Teams;
import com.sansany.theteams.Controller.TeamsController;
import com.sansany.theteams.Database.DatabaseTeams;
import com.sansany.theteams.RecyclerAdapter.TeamsAdapter;

import java.util.ArrayList;

import sansany.theteams.R;

public class TeamsList extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageButton imageButton_Back;
    private ArrayList<Teams> list;
    private RecyclerView mRecyclerView;
    private TeamsAdapter teamsAdapter;
    DatabaseTeams db;
    SQLiteDatabase sql;
    TeamsController teamsController;
    private final int count = -1;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams_list);

        //ToolBar
        toolbar = findViewById( R.id.team_list_toolbar );
        toolbar.setTitle( "" );
        toolbar.setTitleTextColor( Color.WHITE );
        toolbar.getOverflowIcon().setColorFilter(
                Color.parseColor( "#FFFFFF" ),
                PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar( toolbar );

        //ImageButton Back Click
        imageButton_Back = findViewById( R.id.team_list_back_btn_toolbar );
        imageButton_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                finish();
            }
        });

        db = new DatabaseTeams(this);
        teamsController = new TeamsController(this);
        sql = db.getReadableDatabase();

        mRecyclerView = findViewById(R.id.recycler_team);
        mRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);

        mRecyclerView.setLayoutManager(layoutManager);

        getTeamRecyclerView();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getTeamRecyclerView(){
        list = new ArrayList<>();
        teamsController.open();
        list = teamsController.getList();

        teamsAdapter = new TeamsAdapter(this.list);
        mRecyclerView.setAdapter(teamsAdapter);
        teamsAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate(R.menu.team_list_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected( MenuItem item) {
        if (item.getItemId() == R.id.team_list_option) {
            Toast.makeText(getApplicationContext(),
                    "팀 등록 추가하기로 이동합니다.", Toast.LENGTH_SHORT).show();
            Intent intent_teamAdd = new Intent(getApplicationContext(), TeamsAdd.class);
            startActivity(intent_teamAdd);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}