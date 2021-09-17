package com.sansany.theteams.Teams;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sansany.theteams.Controler.TeamsControler;
import com.sansany.theteams.Database.DatabaseTeams;
import java.util.Objects;

import sansany.theteams.R;

public class TeamsEdit extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Toolbar toolbar;
    private ImageButton imageButton_backbar,imageButton_updatebar,imageButton_deletebar;
    private EditText editText_tid,editText_leader,editText_phone,
            editText_date,editText_memo,editText_id;
    private DatabaseTeams teamDB;
    private TeamsControler teamsControler;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams_edit);

        //ToolBar
        toolbar = findViewById( R.id.update_team_toolbar );
        toolbar.setTitle( "" );
        toolbar.setTitleTextColor( Color.WHITE );
        Objects.requireNonNull(toolbar.getOverflowIcon()).setColorFilter( Color.parseColor( "#FFFFFF" ), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar( toolbar );

        teamDB = new DatabaseTeams( this );
        teamsControler = new TeamsControler( this );

        editText_tid = findViewById( R.id.edit_team_update_tid );
        editText_leader = findViewById( R.id.edit_team_update_leader );
        editText_phone = findViewById( R.id.edit_team_update_phone );
        editText_date = findViewById( R.id.edit_team_update_date );
        editText_memo = findViewById( R.id.edit_team_update_memo );
        editText_id = findViewById( R.id.edit_team_update_id );

        imageButton_updatebar = findViewById( R.id.team_update_edit_btn_toolbar );
        imageButton_deletebar = findViewById( R.id.team_update_delete_btn_toolbar );
        imageButton_updatebar.setVisibility( View.INVISIBLE );
        imageButton_deletebar.setVisibility( View.INVISIBLE );

        //InputType
        editText_tid.setInputType( InputType.TYPE_NULL );
        editText_leader.setInputType( InputType.TYPE_NULL );
        editText_phone.setInputType( InputType.TYPE_NULL );
        editText_date.setInputType( InputType.TYPE_NULL );
        editText_memo.setInputType( InputType.TYPE_NULL );
        editText_id.setInputType( InputType.TYPE_NULL );

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

        //Result Intent Data
        Intent positionIntent = getIntent();

        int rid = positionIntent.getExtras().getInt( "id" ); // int 형
        String tId = positionIntent.getExtras().getString( "tId" ); //String 형
        String leader = positionIntent.getExtras().getString( "leader" ); //String 형
        String phone = positionIntent.getExtras().getString( "phone" ); //String 형
        String tDate = positionIntent.getExtras().getString( "date" ); //String 형
        String memo = positionIntent.getExtras().getString( "memo" ); //String 형

        editText_tid.setText( tId );
        editText_leader.setText( leader );
        editText_phone.setText( phone );
        editText_date.setText( tDate );
        editText_memo.setText( memo );
        editText_id.setText( String.valueOf( rid ) );

        //Image Button Back Click
        imageButton_backbar = findViewById( R.id.team_update_back_btn_toolbar );
        imageButton_backbar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_back = new Intent( getApplicationContext(), TeamsList.class );
                startActivity( intent_back );
                finish();
            }
        } );
        //Image Button update Click
        imageButton_updatebar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = editText_id.getText().toString();
                String tid = editText_tid.getText().toString();
                String leader = editText_leader.getText().toString();
                String phone = editText_phone.getText().toString();
                String date = editText_date.getText().toString();
                String memo = editText_memo.getText().toString();

                if (editText_id.length() > 0) {
                    teamsControler.open();
                    teamsControler.updateTeam( id, tid, leader, phone, date, memo );

                    Toast.makeText( TeamsEdit.this,
                            "팀 정보를 수정 했습니다.", Toast.LENGTH_LONG ).show();

                    Intent intent_update = new Intent( getApplicationContext(), TeamsList.class );
                    startActivity( intent_update );
                    finish();

                } else {
                    Toast.makeText( TeamsEdit.this,
                            "팀 정보를 수정하지 못 했습니다.", Toast.LENGTH_LONG ).show();

                }
            }
        } );

        //Image Button Delete Click
        imageButton_deletebar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = editText_id.getText().toString();
                String tid = editText_tid.getText().toString();
                if (editText_id.length() > 0) {
                    teamsControler.open();
                    teamsControler.deleteTeam( id );

                    Toast.makeText( TeamsEdit.this,
                            "팀 정보를 삭제 했습니다." + tid, Toast.LENGTH_LONG ).show();

                    Intent intent_update = new Intent( getApplicationContext(), TeamsList.class );
                    startActivity( intent_update );
                    finish();

                } else {
                    Toast.makeText( TeamsEdit.this,
                            "팀 정보를 삭제하지 못 했습니다." + tid, Toast.LENGTH_LONG ).show();
                }
            }
        } );
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
        getMenuInflater().inflate(R.menu.team_update_menu, menu);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update_text_team_option :

                Toast.makeText(getApplicationContext(),
                        "팀 정보를 수정 합니다.", Toast.LENGTH_SHORT).show();

                editText_leader.setInputType( InputType.TYPE_TEXT_VARIATION_PERSON_NAME );
                editText_phone.setInputType( InputType.TYPE_TEXT_VARIATION_PERSON_NAME );
                editText_date.setInputType( InputType.TYPE_NULL );
                editText_memo.setInputType( InputType.TYPE_TEXT_VARIATION_PERSON_NAME );

                imageButton_updatebar.setVisibility( View.VISIBLE );

                imageButton_deletebar.setVisibility( View.INVISIBLE );
                return true;

            case R.id.delete_text_team_option:
                Toast.makeText(getApplicationContext(),
                        "팀 정보를 삭제 합니다.", Toast.LENGTH_SHORT).show();

                imageButton_deletebar.setVisibility( View.VISIBLE );

                imageButton_updatebar.setVisibility( View.INVISIBLE );
                return true;

            case R.id.back_text_team_option :
                Toast.makeText(getApplicationContext(),
                        "팀 리스트로 돌아 갑니다.", Toast.LENGTH_SHORT).show();
                Intent intent_update_back = new Intent( getApplicationContext(), TeamsList.class );
                startActivity( intent_update_back );

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}