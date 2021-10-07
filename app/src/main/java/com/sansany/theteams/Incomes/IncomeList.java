package com.sansany.theteams.Incomes;

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
import com.sansany.theteams.Controler.IncomeControler;
import com.sansany.theteams.Controler.JournalControler;
import com.sansany.theteams.Database.DatabaseTeams;
import com.sansany.theteams.RecyclerAdapter.IncomeAdapter;

import java.util.ArrayList;

import sansany.theteams.R;

public class IncomeList extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageButton imageButton_Back;
    private ArrayList<Journals> jList;
    private ArrayList<Incomes> iList;
    private ArrayList<String> sjList;
    private ArrayList<String> siList;
    private RecyclerView rcv_journal;
    private IncomeAdapter incomeAdapter;
    DatabaseTeams db;
    SQLiteDatabase sql;
    JournalControler journalControler;
    IncomeControler incomeControler;
    private final int count = -1;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_list);

        //ToolBar
        toolbar = findViewById( R.id.income_list_toolbar );
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getOverflowIcon().setColorFilter(
                Color.parseColor("#FFFFFF"),
                PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar( toolbar );

        db = new DatabaseTeams(this);
        journalControler = new JournalControler(this);
        incomeControler = new IncomeControler(this);
        sql = db.getReadableDatabase();

        //ImageButton Back Click
        imageButton_Back = findViewById( R.id.income_list_back_imgbtn_toolbar );
        imageButton_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                finish();
            }
        });

        rcv_journal = findViewById(R.id.recycler_income);
        rcv_journal.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
//        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
//        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);

        rcv_journal.setLayoutManager(layoutManager);

        //getJournalRecyclerView();
        getAllJournalRecyclerView();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getAllJournalRecyclerView(){
        incomeControler.open();
        Cursor cursor = incomeControler.incomeRecyclerViewListData();

        incomeAdapter = new IncomeAdapter(this,cursor);
        rcv_journal.setAdapter(incomeAdapter);
        incomeAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        getMenuInflater().inflate(R.menu.income_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        if (item.getItemId() == R.id.income_list_image_option){
            Toast.makeText(getApplicationContext(),
                    "일지 쓰기로 이동 합니다.", Toast.LENGTH_SHORT).show();
            Intent intent_incomeAddImage = new Intent( getApplicationContext(), IncomeAdd.class );
            startActivity( intent_incomeAddImage );
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}