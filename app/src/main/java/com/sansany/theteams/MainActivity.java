package com.sansany.theteams;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import com.sansany.theteams.Cost.CostList;
import com.sansany.theteams.Incomes.IncomeList;
import com.sansany.theteams.Journals.JournalList;
import com.sansany.theteams.Sites.SitesList;
import com.sansany.theteams.Teams.TeamsList;

import java.util.Objects;

import sansany.theteams.R;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ToolBar
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        Objects.requireNonNull(toolbar.getOverflowIcon()).setColorFilter(
                Color.parseColor("#FFFFFF"),
                PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);

        //ImageButton Close Click
        ImageButton imageButton_toolbarMenu = findViewById(R.id.main_image_menu_toolbar);
        imageButton_toolbarMenu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick( View v ) {
                PopupMenu popup = new PopupMenu(MainActivity.this, v);
                popup.setOnMenuItemClickListener(MainActivity.this);
                popup.inflate(R.menu.main_menu);
                popup.show();
            }
        });

        ImageButton imageButton_close = findViewById(R.id.main_close_toolbar);
        imageButton_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                finish();
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.journal_list_option:
                Toast.makeText(getApplicationContext(),
                        "일지 리스트로 이동 합나다.", Toast.LENGTH_SHORT).show();
                Intent journal_list = new Intent(getApplicationContext(), JournalList.class);
                startActivity(journal_list);
                return true;

            case R.id.income_list_option:
                Toast.makeText(getApplicationContext(),
                        "수입 리스트로 이동 합나다.", Toast.LENGTH_SHORT).show();
                Intent income_list = new Intent( getApplicationContext(), IncomeList.class );
                startActivity( income_list );
                return true;

            case R.id.site_list_option:
                Toast.makeText(getApplicationContext(),
                        "현장 리스트로 이동 합나다.", Toast.LENGTH_SHORT).show();
                Intent site_list = new Intent(MainActivity.this, SitesList.class);
                startActivity(site_list);
                return true;

            case R.id.team_list_option:
                Toast.makeText(getApplicationContext(),
                        "팀 리스트로 이동 합나다.", Toast.LENGTH_SHORT).show();
                Intent teams_list = new Intent(getApplicationContext(), TeamsList.class);
                startActivity(teams_list);
                return true;

            case R.id.settling_table_option:
                Toast.makeText(getApplicationContext(),
                        "비용 리포트로 이동 합나다.", Toast.LENGTH_SHORT).show();

                Intent intent_cost = new Intent( getApplicationContext(), CostList.class );
                startActivity( intent_cost );
                return true;

            case R.id.main_close_option:
                Toast.makeText(getApplicationContext(),
                        "종료 합나다.", Toast.LENGTH_SHORT).show();
                finish();
                return true;

            default:
                return false;
        }
    }
}

