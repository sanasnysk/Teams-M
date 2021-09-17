package com.sansany.theteams.Cost;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sansany.theteams.Contents.Incomes;
import com.sansany.theteams.Contents.Journals;
import com.sansany.theteams.Controler.CostControler;
import com.sansany.theteams.Database.DatabaseTeams;
import com.sansany.theteams.RecyclerAdapter.CostAdapter;

import java.util.ArrayList;

import sansany.theteams.R;

public class CostList extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageButton imageButton_Back;
    private ArrayList<Journals> jList;
    private ArrayList<Incomes> iList;
    private ArrayList<String> sjList;
    private ArrayList<String> siList;
    private RecyclerView rcv_cost;
    private CostAdapter costAdapter;
    DatabaseTeams db;
    SQLiteDatabase sql;
    CostControler costControler;
    private final int count = -1;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_list);

        //ToolBar
        toolbar = findViewById( R.id.cost_list_toolbar );
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getOverflowIcon().setColorFilter(
                Color.parseColor("#FFFFFF"),
                PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar( toolbar );

        db = new DatabaseTeams(this);
        costControler = new CostControler(this);
        sql = db.getReadableDatabase();

        //ImageButton Back Click
        imageButton_Back = findViewById( R.id.cost_list_back_imgbtn_toolbar );
        imageButton_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                finish();
            }
        });

        rcv_cost = findViewById(R.id.recycler_cost);
        rcv_cost.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
//        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
//        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);

        rcv_cost.setLayoutManager(layoutManager);

        getAllJournalRecyclerView();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getAllJournalRecyclerView(){
        costControler.open();

        Cursor cursor = costControler.recyclweViewData();
        costAdapter = new CostAdapter(this,cursor);
        rcv_cost.setAdapter(costAdapter);
        costAdapter.notifyDataSetChanged();
        costControler.close();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        getMenuInflater().inflate(R.menu.cost_list_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        if (item.getItemId() == R.id.cost_list_image_option) {
            Toast.makeText(getApplicationContext(),
                    "일지쓰기로 이동 합니다.", Toast.LENGTH_SHORT).show();
            Intent intent_cost_add = new Intent(getApplicationContext(), CostAdd.class);
            startActivity(intent_cost_add);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}