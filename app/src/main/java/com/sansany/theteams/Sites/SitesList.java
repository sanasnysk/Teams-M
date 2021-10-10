package com.sansany.theteams.Sites;

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

import com.sansany.theteams.Contents.Sites;
import com.sansany.theteams.Controller.SitesController;
import com.sansany.theteams.Database.DatabaseTeams;

import com.sansany.theteams.RecyclerAdapter.SitesAdapter;

import java.util.ArrayList;

import sansany.theteams.R;

public class SitesList extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageButton imageButton_Back;
    private ArrayList<Sites> list;
    private RecyclerView mRecyclerView;
    private SitesAdapter sitesAdapter;
    DatabaseTeams db;
    SQLiteDatabase sql;
    SitesController sitesController;
    private final int count = -1;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sites_list);

        //ToolBar
        toolbar = findViewById( R.id.site_list_toolbar );
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getOverflowIcon().setColorFilter(
                Color.parseColor("#FFFFFF"),
                PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar( toolbar );

        //ImageButton Back Click
        imageButton_Back = findViewById( R.id.site_list_back_imgbtn_toolbar );
        imageButton_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                finish();
            }
        });

        db = new DatabaseTeams(this);
        sitesController = new SitesController(this);
        sql = db.getReadableDatabase();

        mRecyclerView = findViewById(R.id.recycler_site);
        mRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);

        mRecyclerView.setLayoutManager(layoutManager);

        getSiteRecyclerView();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getSiteRecyclerView(){
        list = new ArrayList<>();
        sitesController.open();
        list = sitesController.getAllSiteList();

        sitesAdapter = new SitesAdapter(this.list);
        mRecyclerView.setAdapter(sitesAdapter);
        sitesAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate(R.menu.site_list_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected( MenuItem item) {
        if (item.getItemId() == R.id.site_list_option) {
            Toast.makeText(getApplicationContext(),
                    "팀 등록 추가하기로 이동합니다.", Toast.LENGTH_SHORT).show();
            Intent intent_siteAdd = new Intent(getApplicationContext(), SitesAdd.class);
            startActivity(intent_siteAdd);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}