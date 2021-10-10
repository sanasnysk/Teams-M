package com.sansany.theteams.Teams;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sansany.theteams.Controller.TeamsController;
import com.sansany.theteams.Database.DatabaseTeams;

import sansany.theteams.R;


public class TeamsAdd extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Toolbar toolbar;
    private ImageButton imageButton_back;
    private EditText editText_tid,editText_leader,editText_phone,
            editText_date,editText_memo;
    private Button button_save;
    private DatabaseTeams teamDB;
    private TeamsController teamsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams_add);

        //--- ToolBar ---
        toolbar = findViewById( R.id.team_add_toolbar );
        toolbar.setTitle( "  " );
        toolbar.setTitleTextColor( Color.WHITE );
        toolbar.getOverflowIcon().setColorFilter( Color.parseColor( "#FFFFFF" ),
                PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar( toolbar );


        teamDB = new DatabaseTeams( this );
        teamsController = new TeamsController( this );

        imageButton_back = findViewById( R.id.team_add_back_btn_toolbar );

        editText_tid = findViewById( R.id.edit_team_tid );
        editText_leader = findViewById( R.id.edit_team_leader );
        editText_phone = findViewById( R.id.edit_team_phone );
        editText_date = findViewById( R.id.edit_team_date );
        editText_memo = findViewById( R.id.edit_team_memo );


        //Focus
        editText_tid.setFocusable( false );
        editText_leader.requestFocus();
        editText_date.setFocusable( false );

        //ImageButton Back toolbar Click
        imageButton_back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_back = new Intent( getApplicationContext(), TeamsList.class );
                startActivity( intent_back );
                finish();

            }
        } );

        //EditText Date Click
        editText_date.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // Get Current Date
                @SuppressLint({"NewApi", "LocalSuppress"}) final Calendar c = Calendar.getInstance();
                mYear = c.get( Calendar.YEAR );
                mMonth = c.get( Calendar.MONTH );
                mDay = c.get( Calendar.DAY_OF_MONTH );

                DatePickerDialog();
            }
        } );

        teamAutoId();
        DateTime();
    }

    @SuppressLint("SetTextI18n")
    public void teamAutoId() {
        teamsController.open();
        final Cursor cus = teamsController.teamAutoId();
        final int rows = cus.getCount();
        String teamId = "t_";
        int idNo = 1;

        if (rows == 0){
            editText_tid.setText( "t_" + idNo );
        }else {
            int r = cus.getInt( 0 );
            int rid = idNo + r;
            editText_tid.setText(teamId + rid);
        }
        teamsController.close();

    }

    private void DatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog( this,
                new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set( year, month, dayOfMonth );
                        year = calendar.get( Calendar.YEAR );
                        month = calendar.get( Calendar.MONTH ) + 1;
                        dayOfMonth = calendar.get( Calendar.DAY_OF_MONTH );
                        @SuppressLint("DefaultLocale")
                        String teamDate = String.format( "%d-%02d-%02d", year, month, dayOfMonth );
                        editText_date.setText( teamDate );
                        //etxtDate.setText( year + "-" + (month + 1) + "-" + dayOfMonth );
                    }
                }, mYear, mMonth, mDay );

        datePickerDialog.show();
    }

    private void DateTime() {
        editText_date = findViewById( R.id.edit_team_date );

        SQLiteDatabase db = teamDB.getWritableDatabase();

        Cursor csDate;

        String startQuery = "select date('now','localtime')";

        csDate = db.rawQuery( startQuery, null );

        if (csDate.getCount() > 0 ) {
            csDate.moveToFirst();

            editText_date.setText( csDate.getString( 0 ) );

            csDate.close();
        }

        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate(R.menu.taem_add_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.team_add_saveimage_option:
                if (editText_tid.length() == 0 || editText_leader.length() == 0) {
                    Toast.makeText( TeamsAdd.this,
                            "팀장/회사 이름을 입력하세요?", Toast.LENGTH_LONG ).show();
                } else if (editText_phone.length() == 0) {
                    Toast.makeText( TeamsAdd.this,
                            "전화번호를 입력하세요?", Toast.LENGTH_SHORT ).show();
                } else {

                    String tid = editText_tid.getText().toString();
                    String leader = editText_leader.getText().toString();
                    String phone = editText_phone.getText().toString();
                    String date = editText_date.getText().toString();
                    String memo = editText_memo.getText().toString();

                    teamsController.open();
                    teamsController.insertTeam( tid, leader, phone, date, memo );

                    Toast.makeText( getApplicationContext(),
                            "입력한 데이터를 저장 했습니다.", Toast.LENGTH_SHORT ).show();

                    Intent intent_Save = new Intent( getApplicationContext(), TeamsList.class );
                    startActivity( intent_Save );

                    finish();
                }

                return true;

            case R.id.team_add_close_option:
                Intent intent_close = new Intent(getApplicationContext(), TeamsList.class);
                startActivity(intent_close);

                Toast.makeText(getApplicationContext(),
                        "팀 추가를 닫습니다.", Toast.LENGTH_SHORT).show();

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}