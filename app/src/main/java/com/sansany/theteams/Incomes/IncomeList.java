package com.sansany.theteams.Incomes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.sansany.theteams.Contents.Incomes;
import com.sansany.theteams.Contents.IncomesContents;
import com.sansany.theteams.Contents.Journals;
import com.sansany.theteams.Contents.JournalsContents;
import com.sansany.theteams.Contents.SitesContents;
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
        String rcvListQuery = "SELECT i." + IncomesContents.TEAM_LEADER +
                " as ileader,js.amounts as iamount,sum(i." + IncomesContents.INCOME_COLLECT +
                ") as icollect,sum(i." + IncomesContents.INCOME_TAX +
                ") as itax,js.amounts-sum(" + IncomesContents.INCOME_COLLECT +
                ")-sum(" + IncomesContents.INCOME_TAX +
                ") as balance,ROUND((js.amounts-sum(" + IncomesContents.INCOME_COLLECT +
                ")-sum(" + IncomesContents.INCOME_TAX +
                "))/js.avgspay,1) as balance_day FROM " + IncomesContents.INCOME_TABLE +
                " as i LEFT JOIN (SELECT j." + JournalsContents.TEAM_ID +
                " as tid,total(j." + JournalsContents.JOURNAL_ONE +
                ") as ones,sum(j." + JournalsContents.JOURNAL_AMOUNT +
                ") as amounts,avg(j." + JournalsContents.SITE_PAY +
                ") as avgspay FROM " + JournalsContents.JOURNAL_TABLE +
                " as j LEFT JOIN " + SitesContents.SITE_TABLE +
                " as s ON j." + JournalsContents.SITE_ID +
                " = s." + SitesContents.SITE_SID +
                " GROUP BY j." + JournalsContents.TEAM_ID +
                ") as js ON i." + IncomesContents.TEAM_ID +
                " = js.tid GROUP BY i." + IncomesContents.TEAM_ID +
                " ORDER BY i." + IncomesContents.INCOME_ID +" DESC ";

        Cursor cursor = sql.rawQuery(rcvListQuery, null);
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