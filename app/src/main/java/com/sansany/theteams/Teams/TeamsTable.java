package com.sansany.theteams.Teams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.sansany.theteams.Controler.TeamsControler;
import com.sansany.theteams.Database.DatabaseTeams;

import sansany.theteams.R;

public class TeamsTable extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txt_title;
    private EditText editText_Search;
    private ImageButton imageButton_Back;
    public TableLayout tableTeam;
    private DatabaseTeams teamDB;
    private TeamsControler teamsControler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams_table);

        //ToolBar
        toolbar = findViewById( R.id.team_table_toolbar );
        toolbar.setTitle( "" );
        toolbar.setTitleTextColor( Color.WHITE );
        toolbar.getOverflowIcon().setColorFilter(
                Color.parseColor( "#FFFFFF" ),
                PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar( toolbar );

        teamDB = new DatabaseTeams( this );
        teamsControler = new TeamsControler( this );

        txt_title = findViewById(R.id.txt_main_toolbar);
        tableTeam = findViewById( R.id.table_team );

        editText_Search = findViewById( R.id.etxt_team_search );

        //EdieText Search Add Text
        editText_Search.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged( Editable s) {
                tableTeam.removeAllViews();
                searchTeamData();
            }
        } );

        //ImageButton Back Click
        imageButton_Back = findViewById( R.id.team_back_btn_toolbar );
        imageButton_Back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_back = new Intent(getApplicationContext(), TeamsList.class);
                startActivity(intent_back);

                finish();
            }
        } );

        getIntentResult();
        //loadTeamData();
    }

    private void getIntentResult() {
        Intent posionIntent = getIntent();
        String name = posionIntent.getExtras().getString("name");
        String leader = posionIntent.getExtras().getString("leader");
        if (leader == null){
            editText_Search.setText(name);
        }else {
            editText_Search.setText(leader);
        }
    }

    @SuppressLint("SetTextI18n")
    private void loadTeamData() {
        //Table Row
        TableRow row0 = new TableRow( this );
        row0.setLayoutParams( new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT ) );

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT );

        tlp.setMargins( 1, 0, 1, 0 );

        //TextView Team Id
        TextView tv0 = new TextView( this );
        tv0.setLayoutParams( tlp );

        tv0.setBackgroundColor( Color.BLUE );
        tv0.setTextColor( Color.WHITE );
        tv0.setGravity( Gravity.CENTER );
        tv0.setTextSize( 18 );
        tv0.setPadding( 0, 1, 0, 1 );

        tv0.setText( " ID " );

        row0.addView( tv0 );

        //TextView Team ID
        TextView tv1 = new TextView( this );
        tv1.setLayoutParams( tlp );

        tv1.setBackgroundColor( Color.BLUE );
        tv1.setTextColor( Color.WHITE );
        tv1.setGravity( Gravity.CENTER );
        tv1.setTextSize( 18 );
        tv1.setPadding( 0, 1, 0, 1 );

        tv1.setText( " T_ID " );

        row0.addView( tv1 );

        //TextView Team Leader
        TextView tv2 = new TextView( this );
        tv2.setLayoutParams( tlp ) ;

        tv2.setBackgroundColor( Color.BLUE );
        tv2.setTextColor( Color.WHITE );
        tv2.setGravity( Gravity.CENTER );
        tv2.setTextSize( 18 );
        tv2.setPadding( 0, 1, 0, 1 );

        tv2.setText( " 팀 이름 " );

        row0.addView( tv2 );

        //TextView Team Leader Phone No
        TextView tv3 = new TextView( this );
        tv3.setLayoutParams(tlp );

        tv3.setBackgroundColor( Color.BLUE );
        tv3.setTextColor( Color.WHITE );
        tv3.setGravity( Gravity.CENTER );
        tv3.setTextSize( 18 );
        tv3.setPadding( 0, 1, 0, 1 );

        tv3.setText( " 전화번호 " );

        row0.addView( tv3 );

        //TextView Team Start Date
        TextView tv4 = new TextView( this );
        tv4.setLayoutParams( tlp );

        tv4.setBackgroundColor( Color.BLUE );
        tv4.setTextColor( Color.WHITE );
        tv4.setGravity( Gravity.CENTER );
        tv4.setTextSize( 18 );
        tv4.setPadding( 0, 1, 0, 1 );

        tv4.setText( " 등록일자 " );

        row0.addView( tv4 );

        //TextView Team Leader
        TextView tv5 = new TextView( this );
        tv5.setLayoutParams( tlp );

        tv5.setBackgroundColor( Color.BLUE );
        tv5.setTextColor( Color.WHITE );
        tv5.setGravity( Gravity.CENTER );
        tv5.setTextSize( 18 );
        tv5.setPadding( 0, 1, 0, 1 );

        tv5.setText( " 팀 메모 " );

        row0.addView( tv5 );

        //TableLayout View
        tableTeam.addView( row0 );

        //Data Load
        String search = editText_Search.getText().toString();
        teamsControler.open();
        final Cursor cus = teamsControler.selectAllTeam();
        final int rows = cus.getCount();
        int columns = cus.getColumnCount();

        cus.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            final TableRow row = new TableRow( this );
            row.setLayoutParams( new TableLayout.LayoutParams( TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT ) );

            // inner for loop
            for (int j = 0; j < columns; j++) {

                TextView tv = new TextView( this );
                tv.setLayoutParams( tlp );

                tv.setGravity( Gravity.CENTER );
                tv.setTextSize( 14 );
                tv.setPadding( 0, 1, 0, 1 );

                tv.setText( cus.getString( j ) );

                //********
                if (i % 2 != 0) {
                    tv.setBackgroundColor( Color.parseColor( "#CFF6FA" ) );
                } else {
                    tv.setBackgroundColor( Color.WHITE );
                }

                row.addView( tv );

                final int p = i;
                final int r = i + 1;
                final int rId = i + 2;

                row.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rows == 0) {
                            return;
                        } else if (cus.moveToPosition( p )) {
                            Intent positionIntent = new Intent( getApplicationContext(), TeamsEdit.class );

                            positionIntent.putExtra( "id", cus.getInt( 0 ) );
                            positionIntent.putExtra( "tId", cus.getString( 1 ) );
                            positionIntent.putExtra( "leader", cus.getString( 2 ) );
                            positionIntent.putExtra( "phone", cus.getString( 3 ) );
                            positionIntent.putExtra( "date", cus.getString( 4 ) );
                            positionIntent.putExtra( "memo", cus.getString( 5 ) );

                            String leader = positionIntent.getExtras().getString( "leader" );
                            Toast.makeText( TeamsTable.this, "팀 선택 :" + leader,
                                    Toast.LENGTH_SHORT ).show();

                            startActivity( positionIntent );
                            finish();

                        }

                        cus.close();
                    }
                } );
            }
            cus.moveToNext();
            tableTeam.addView( row );
        }

        teamsControler.close();
    }

    @SuppressLint("SetTextI18n")
    private void searchTeamData() {
        //Table Row
        TableRow row0 = new TableRow( this );
        row0.setLayoutParams( new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT ) );

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT );

        tlp.setMargins( 1, 0, 1, 0 );

        //TextView Team Id
        TextView tv0 = new TextView( this );
        tv0.setLayoutParams( tlp );

        tv0.setBackgroundColor( Color.BLUE );
        tv0.setTextColor( Color.WHITE );
        tv0.setGravity( Gravity.CENTER );
        tv0.setTextSize( 18 );
        tv0.setPadding( 0, 1, 0, 1 );

        tv0.setText( " ID " );

        row0.addView( tv0 );

        //TextView Team ID
        TextView tv1 = new TextView( this );
        tv1.setLayoutParams( tlp );

        tv1.setBackgroundColor( Color.BLUE );
        tv1.setTextColor( Color.WHITE );
        tv1.setGravity( Gravity.CENTER );
        tv1.setTextSize( 18 );
        tv1.setPadding( 0, 1, 0, 1 );

        tv1.setText( " t_id " );

        row0.addView( tv1 );

        //TextView Team Leader
        TextView tv2 = new TextView( this );
        tv2.setLayoutParams( tlp ) ;

        tv2.setBackgroundColor( Color.BLUE );
        tv2.setTextColor( Color.WHITE );
        tv2.setGravity( Gravity.CENTER );
        tv2.setTextSize( 18 );
        tv2.setPadding( 0, 1, 0, 1 );

        tv2.setText( " 팀 이름 " );

        row0.addView( tv2 );

        //TextView Team Leader Phone No
        TextView tv3 = new TextView( this );
        tv3.setLayoutParams(tlp );

        tv3.setBackgroundColor( Color.BLUE );
        tv3.setTextColor( Color.WHITE );
        tv3.setGravity( Gravity.CENTER );
        tv3.setTextSize( 18 );
        tv3.setPadding( 0, 1, 0, 1 );

        tv3.setText( " 전화번호 " );

        row0.addView( tv3 );

        //TextView Team Start Date
        TextView tv4 = new TextView( this );
        tv4.setLayoutParams( tlp );

        tv4.setBackgroundColor( Color.BLUE );
        tv4.setTextColor( Color.WHITE );
        tv4.setGravity( Gravity.CENTER );
        tv4.setTextSize( 18 );
        tv4.setPadding( 0, 1, 0, 1 );

        tv4.setText( " 등록일자 " );

        row0.addView( tv4 );

        //TextView Team Leader
        TextView tv5 = new TextView( this );
        tv5.setLayoutParams( tlp );

        tv5.setBackgroundColor( Color.BLUE );
        tv5.setTextColor( Color.WHITE );
        tv5.setGravity( Gravity.CENTER );
        tv5.setTextSize( 18 );
        tv5.setPadding( 0, 1, 0, 1 );

        tv5.setText( " 팀 메모 " );

        row0.addView( tv5 );

        //TableLayout View
        tableTeam.addView( row0 );

        //Data Load
        String search = editText_Search.getText().toString();
        teamsControler.open();
        final Cursor cus = teamsControler.searchTeam(search);
        final int rows = cus.getCount();
        int columns = cus.getColumnCount();

        cus.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            final TableRow row = new TableRow( this );
            row.setLayoutParams( new TableLayout.LayoutParams( TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT ) );

            // inner for loop
            for (int j = 0; j < columns; j++) {

                TextView tv = new TextView( this );
                tv.setLayoutParams( tlp );

                tv.setGravity( Gravity.CENTER );
                tv.setTextSize( 14 );
                tv.setPadding( 0, 1, 0, 1 );

                tv.setText( cus.getString( j ) );

                //********
                if (i % 2 != 0) {
                    tv.setBackgroundColor( Color.parseColor( "#CFF6FA" ) );
                } else {
                    tv.setBackgroundColor( Color.WHITE );
                }

                row.addView( tv );

                final int p = i;
                final int r = i + 1;
                final int rId = i + 2;

                row.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rows == 0) {
                            return;
                        } else if (cus.moveToPosition( p )) {
                            Intent positionIntent = new Intent( getApplicationContext(), TeamsEdit.class );

                            positionIntent.putExtra( "id", cus.getInt( 0 ) );
                            positionIntent.putExtra( "tId", cus.getString( 1 ) );
                            positionIntent.putExtra( "leader", cus.getString( 2 ) );
                            positionIntent.putExtra( "phone", cus.getString( 3 ) );
                            positionIntent.putExtra( "date", cus.getString( 4 ) );
                            positionIntent.putExtra( "memo", cus.getString( 5 ) );

                            String leader = positionIntent.getExtras().getString( "leader" );
                            Toast.makeText( TeamsTable.this, "팀 선택 :" + leader,
                                    Toast.LENGTH_SHORT ).show();

                            startActivity( positionIntent );
                            finish();
                        }

                        cus.close();
                    }
                } );
            }
            cus.moveToNext();
            tableTeam.addView( row );
        }

        teamsControler.close();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu)
    {
        getMenuInflater().inflate(R.menu.team_menu, menu);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected( MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.team_newimage_option:
                editText_Search.setText("");
                return true;

            case R.id.team_close_option:
                Intent intent_back = new Intent(getApplicationContext(), TeamsList.class);
                startActivity(intent_back);

                Toast.makeText(getApplicationContext(),
                        "팀 리스트 닫기", Toast.LENGTH_SHORT).show();
                finish();
                return true;

            case R.id.taem_add_image_option:
                Toast.makeText(getApplicationContext(),
                        "추가하기로 이동", Toast.LENGTH_SHORT).show();
                Intent intent_teamImageAdd = new Intent( getApplicationContext(), TeamsAdd.class );
                startActivity( intent_teamImageAdd );
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}