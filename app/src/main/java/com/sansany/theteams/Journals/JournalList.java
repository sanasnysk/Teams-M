package com.sansany.theteams.Journals;

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
import com.sansany.theteams.Controller.JournalController;
import com.sansany.theteams.Database.DatabaseTeams;
import com.sansany.theteams.RecyclerAdapter.JournalAdapter;

import java.util.ArrayList;

import sansany.theteams.R;

public class JournalList extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageButton imageButton_Back;
    private ArrayList<Journals> jList;
    private ArrayList<Incomes> iList;
    private ArrayList<String> sjList;
    private ArrayList<String> siList;
    private RecyclerView rcv_journal;
    private JournalAdapter journalAdapter;
    DatabaseTeams db;
    SQLiteDatabase sql;
    JournalController journalController;
    private final int count = -1;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);

        //ToolBar
        toolbar = findViewById( R.id.journal_list_toolbar );
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getOverflowIcon().setColorFilter(
                Color.parseColor("#FFFFFF"),
                PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar( toolbar );

        db = new DatabaseTeams(this);
        journalController = new JournalController(this);
        sql = db.getReadableDatabase();

        //ImageButton Back Click
        imageButton_Back = findViewById( R.id.journal_list_back_imgbtn_toolbar );
        imageButton_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                finish();
            }
        });

        rcv_journal = findViewById(R.id.recycler_journal);
        rcv_journal.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
//        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
//        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);

        rcv_journal.setLayoutManager(layoutManager);

        getAllJournalRecyclerView();
    }


    @SuppressLint("NotifyDataSetChanged")
    private void getAllJournalRecyclerView(){
        journalController.open();
        Cursor cursor = journalController.recyclerViewData();

        journalAdapter = new JournalAdapter(this,cursor);
        rcv_journal.setAdapter(journalAdapter);
        journalAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        getMenuInflater().inflate(R.menu.journal_list_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        if (item.getItemId() == R.id.journal_list_image_option) {
            Toast.makeText(getApplicationContext(),
                    "일지쓰기로 이동 합니다.", Toast.LENGTH_SHORT).show();
            Intent intent_journal_add = new Intent(getApplicationContext(), JournalAdd.class);
            startActivity(intent_journal_add);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}