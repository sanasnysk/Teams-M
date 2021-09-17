package com.sansany.theteams.Journals;

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
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.sansany.theteams.Controler.JournalControler;
import com.sansany.theteams.Controler.SitesControler;
import com.sansany.theteams.Database.DatabaseTeams;

import java.text.DecimalFormat;
import java.util.Calendar;

import sansany.theteams.R;

public class JournalTable extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Toolbar toolbar;
    private TableLayout journal_Table;
    private ImageButton imageButton_Backbar;
    private EditText editText_StartDate, editText_EndDate, editText_Search;
    private TextView textView_Day, textView_inputDay, textView_Amount, textView_inputAmount, textView_title;
    private DatabaseTeams teamDB;
    private JournalControler journalControler;
    private SitesControler sitesControler;
    private DecimalFormat formatDouble;
    private  DecimalFormat formatPay;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_table);

        //ToolBar
        toolbar = findViewById( R.id.journal_table_toolbar );
        toolbar.setTitle( "" );
        toolbar.setTitleTextColor( Color.WHITE );
        toolbar.getOverflowIcon().setColorFilter(Color.parseColor( "#FFFFFF" ), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar( toolbar );

        formatDouble = new DecimalFormat("#,###.##");
        formatPay = new DecimalFormat("#,###");

        //--- UI findViewById
        teamDB = new DatabaseTeams( this );
        journalControler = new JournalControler( this );
        sitesControler = new SitesControler( this );

        journal_Table = findViewById( R.id.journal_table );

        editText_StartDate = findViewById( R.id.journal_edit_startDate );
        editText_StartDate.setInputType( InputType.TYPE_NULL );

        editText_EndDate = findViewById( R.id.journal_edit_endDate );
        editText_EndDate.setInputType( InputType.TYPE_NULL );

        editText_Search = findViewById(R.id.journal_table_site_search);

        textView_Day = findViewById( R.id.journal_text_sum_dailyOne );
        textView_inputDay = findViewById( R.id.journal_text_sum_dailyOne_input );
        textView_Amount = findViewById( R.id.journal_text_sum_amount );
        textView_inputAmount = findViewById( R.id.journal_text_sum_amount_input );
        textView_title = findViewById(R.id.txt_journal_table_toolbar);

        imageButton_Backbar = findViewById( R.id.journal_table_back_toolbar );

        DateTime();

        //--- Start Date On Click Listener
        editText_StartDate.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get( Calendar.YEAR );
                mMonth = c.get( Calendar.MONTH );
                mDay = c.get( Calendar.DAY_OF_MONTH );

                startDatePickerDialog();
            }
        } );

        //--- End Date On Click Listener
        editText_EndDate.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get( Calendar.YEAR );
                mMonth = c.get( Calendar.MONTH );
                mDay = c.get( Calendar.DAY_OF_MONTH );

                endDatePickerDialog();
            }
        } );

        //--- Start Date add Text Changed Listener
        editText_StartDate.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void afterTextChanged( Editable s) {
                journalControler.open();
                final Cursor cusLoad = journalControler.selectAllJournal(  );
                final int rowCount = cusLoad.getCount();
                if (rowCount != 0){
                    journal_Table.removeAllViews();
                    if (editText_Search == null){
                        searchDateJournal();
                        sumDateJournal();
                    }else {
                        searchJournalSiteDate();
                        sumSiteDateJournal();
                    }

                }else {
                    //Do nothing
                }
                journalControler.close();
            }
        } );

        //--- End Date add Text Changed Listener
        editText_EndDate.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void afterTextChanged(Editable s) {
                //teamControl.open();
                journalControler.open();
                final Cursor cusLoad = journalControler.selectAllJournal(  );
                final int rowCount = cusLoad.getCount();
                if (rowCount != 0){
                    journal_Table.removeAllViews();
                    if (editText_Search == null){
                        searchDateJournal();
                        sumDateJournal();
                    }else {
                        searchJournalSiteDate();
                        sumSiteDateJournal();
                    }

                    //teamSpinnerItemSearch();
                    //siteSpinnerItemSearch();
                }else {
                    //Do nothing
                }

            }
        } );

        //--- Search Journal Table
        editText_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after ) {

            }

            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count ) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void afterTextChanged( Editable s ) {

                searchJournalSiteDate();
                sumSiteDateJournal();
            }
        });

        //--- ImageButton Back ClickListener
        imageButton_Backbar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent journal_list = new Intent(getApplicationContext(), JournalList.class);
                startActivity(journal_list);
                finish();
            }
        } );

        getIntentResult();//intent result

    }

    private void getIntentResult() {
        Intent posionIntent = getIntent();
        String site = posionIntent.getExtras().getString("site");
        String site_name = posionIntent.getExtras().getString("site_name");
        if (site != null){
            editText_Search.setText(site);
        }else {
            editText_Search.setText(site_name);
        }

    }

    //---EditText Search Journal Data Table
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void searchJournal() {
        journal_Table.removeAllViews();
        //Table Row
        TableRow row0 = new TableRow( this );
        row0.setLayoutParams( new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT ) );

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT );

        tlp.setMargins( 1, 1, 1, 1 );

        //TextView Journal Table Row Id
        TextView tv0 = new TextView( this );
        tv0.setLayoutParams( tlp );

        tv0.setBackgroundColor( Color.BLUE );
        tv0.setTextColor( Color.WHITE );
        tv0.setGravity( Gravity.CENTER );
        tv0.setTextSize( 18 );
        tv0.setPadding( 1, 1, 1, 1 );

        tv0.setText( "ID" );

        row0.addView( tv0 );

        //TextView Journal Table Team ID
        TextView tv1 = new TextView( this );
        tv1.setLayoutParams( tlp );

        tv1.setBackgroundColor( Color.BLUE );
        tv1.setTextColor( Color.WHITE );
        tv1.setGravity( Gravity.CENTER );
        tv1.setTextSize( 18 );
        tv1.setPadding( 1, 1, 1, 1 );

        tv1.setText( " j_id " );

        row0.addView( tv1 );

        //TextView Journal Table Date
        TextView tv2 = new TextView( this );
        tv2.setLayoutParams( tlp );

        tv2.setBackgroundColor( Color.BLUE );
        tv2.setTextColor( Color.WHITE );
        tv2.setGravity( Gravity.CENTER );
        tv2.setTextSize( 18 );
        tv2.setPadding( 1, 1, 1, 1 );

        tv2.setText( " 일  자 " );

        row0.addView( tv2 );

        //TextView Journal Table Site Name
        TextView tv3 = new TextView( this );
        tv3.setLayoutParams( tlp );

        tv3.setBackgroundColor( Color.BLUE );
        tv3.setTextColor( Color.WHITE );
        tv3.setGravity( Gravity.CENTER );
        tv3.setTextSize( 18 );
        tv3.setPadding( 1, 1, 1, 1 );

        tv3.setText( " 현 장 " );

        row0.addView( tv3 );

        //TextView Journal Table One Day
        TextView tv4 = new TextView( this );
        tv4.setLayoutParams( tlp );

        tv4.setBackgroundColor( Color.BLUE );
        tv4.setTextColor( Color.WHITE );
        tv4.setGravity( Gravity.CENTER );
        tv4.setTextSize( 18 );
        tv4.setPadding( 1, 1, 1, 1 );

        tv4.setText( " 일일 " );

        row0.addView( tv4 );

        //TextView Journal Table Daily leader
        TextView tv5 = new TextView( this );
        tv5.setLayoutParams( tlp );

        tv5.setBackgroundColor( Color.BLUE );
        tv5.setTextColor( Color.WHITE );
        tv5.setGravity( Gravity.CENTER );
        tv5.setTextSize( 18 );
        tv5.setPadding( 1, 1, 1, 1 );

        tv5.setText( " 팀 이름 " );

        row0.addView( tv5 );

        //TextView Journal Table memo
        TextView tv6 = new TextView( this );
        tv6.setLayoutParams( tlp );

        tv6.setBackgroundColor( Color.BLUE );
        tv6.setTextColor( Color.WHITE );
        tv6.setGravity( Gravity.CENTER );
        tv6.setTextSize( 18 );
        tv6.setPadding( 1, 1, 1, 1 );

        tv6.setText( " 일일메모 " );

        row0.addView( tv6 );

        //TextView Journal Table Team s_pay
        TextView tv7 = new TextView( this );
        tv7.setLayoutParams( tlp );

        tv7.setBackgroundColor( Color.BLUE );
        tv7.setTextColor( Color.WHITE );
        tv7.setGravity( Gravity.CENTER );
        tv7.setTextSize( 18 );
        tv7.setPadding( 1, 1, 1, 1 );

        tv7.setText( " 일 당 " );

        row0.addView( tv7 );

        //TextView Journal Table j_amount
        TextView tv8 = new TextView( this );
        tv8.setLayoutParams( tlp );

        tv8.setBackgroundColor( Color.BLUE );
        tv8.setTextColor( Color.WHITE );
        tv8.setGravity( Gravity.CENTER );
        tv8.setTextSize( 18 );
        tv8.setPadding( 1, 1, 1, 1 );

        tv8.setText( " 금 액 " );

        row0.addView( tv8 );

        //TextView Journal Table s_id
        TextView tv9 = new TextView( this );
        tv9.setLayoutParams( tlp );

        tv9.setBackgroundColor( Color.BLUE );
        tv9.setTextColor( Color.WHITE );
        tv9.setGravity( Gravity.CENTER );
        tv9.setTextSize( 18 );
        tv9.setPadding( 1, 1, 1, 1 );

        tv9.setText( " 현장ID " );

        row0.addView( tv9 );

        //TextView Journal Table t_id
        TextView tv10 = new TextView( this );
        tv10.setLayoutParams( tlp );

        tv10.setBackgroundColor( Color.BLUE );
        tv10.setTextColor( Color.WHITE );
        tv10.setGravity( Gravity.CENTER );
        tv10.setTextSize( 18 );
        tv10.setPadding( 1, 1, 1, 1 );

        tv10.setText( " 팀ID " );

        row0.addView( tv10 );

        //TableLayout View
        journal_Table.addView( row0 );

        //Data Load
        String editSearchSite = editText_Search.getText().toString();
        String startDay = editText_StartDate.getText().toString();
        String endDay = editText_EndDate.getText().toString();
        journalControler.open();
        final Cursor cus = journalControler.searchJournal( editSearchSite );
        final int rows = cus.getCount();
        final int columns = cus.getColumnCount();

        cus.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            final TableRow row = new TableRow( this );
            row.setLayoutParams( new TableLayout.LayoutParams( TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT ) );
            // inner for loop
            for (int j = 0; j < columns; j++) {

                TextView tv = new TextView( this );
                tv.setLayoutParams( tlp );

                tv.setGravity( Gravity.CENTER );
                tv.setTextSize( 14 );
                tv.setPadding( 3, 1, 1, 3 );

                tv.setText( cus.getString( j ) );

                row.addView( tv );

                //*** TableRow TextView Color *****
                if (i % 2 != 0) {
                    tv.setBackgroundColor( Color.parseColor( "#CFF6FA" ) );
                } else {
                    tv.setBackgroundColor( Color.WHITE );
                }

                //--- comma
                //--- comma
                if (j == 7 ){
                    int rPay = cus.getInt( 7 );
                    String formattedPay = formatPay.format(rPay);
                    tv.setText( formattedPay );
                }
                if (j == 8){
                    int rAmount = cus.getInt( 8 );
                    String formattedAmount = formatPay.format( rAmount );
                    tv.setText( formattedAmount );
                }

                final int p = i;
                final int r = i + 1;

                row.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rows == 0) {
                            return;
                        } else if (cus.moveToPosition( p )) {

                            Intent positionIntent = new Intent( getApplicationContext(), JournalEdit.class );
                            //ID Journal
                            positionIntent.putExtra( "id", cus.getInt( 0 ) );
                            //Journal ID
                            positionIntent.putExtra( "jid", cus.getString( 1 ) );
                            //Journal Date
                            positionIntent.putExtra( "date", cus.getString( 2 ) );
                            //Journal Site Name
                            positionIntent.putExtra( "site", cus.getString( 3 ) );
                            //Journal One Day
                            positionIntent.putExtra( "oneDay", cus.getFloat( 4 ) );
                            //journal Team Leader
                            positionIntent.putExtra( "leader", cus.getString( 5 ) );
                            //Journal Memo
                            positionIntent.putExtra( "memo", cus.getString( 6 ) );
                            //Journal spay
                            positionIntent.putExtra( "spay", cus.getInt( 7 ) );
                            //Journal jamount
                            positionIntent.putExtra( "jamount", cus.getInt( 8 ) );
                            //Journal sid
                            positionIntent.putExtra( "sid", cus.getString( 9 ) );
                            //Journal tid
                            positionIntent.putExtra( "tid", cus.getString( 10 ) );

                            String site = positionIntent.getExtras().getString( "site" );
                            Toast.makeText( JournalTable.this,
                                    site + "를 선택 했습니다. :" ,
                                    Toast.LENGTH_SHORT ).show();

                            startActivity( positionIntent );
                            finish();
                        }
                        cus.close();
                    }
                } );

            }
            cus.moveToNext();
            journal_Table.addView( row );
        }
        journalControler.close();

    }

    //--- Load Journal Data Table
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void LoadJournalData() {
        journal_Table.removeAllViews();
        //Table Row
        TableRow row0 = new TableRow( this );
        row0.setLayoutParams( new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT ) );

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT );

        tlp.setMargins( 1, 0, 1, 0 );

        //TextView Journal Table Row Id
        TextView tv0 = new TextView( this );
        tv0.setLayoutParams( tlp );

        tv0.setBackgroundColor( Color.BLUE );
        tv0.setTextColor( Color.WHITE );
        tv0.setGravity( Gravity.CENTER );
        tv0.setTextSize( 18 );
        tv0.setPadding( 1, 1, 1, 1 );

        tv0.setText( "ID" );

        row0.addView( tv0 );

        //TextView Journal Table Team ID
        TextView tv1 = new TextView( this );
        tv1.setLayoutParams( tlp );

        tv1.setBackgroundColor( Color.BLUE );
        tv1.setTextColor( Color.WHITE );
        tv1.setGravity( Gravity.CENTER );
        tv1.setTextSize( 18 );
        tv1.setPadding( 1, 1, 1, 1 );

        tv1.setText( " j_id " );

        row0.addView( tv1 );

        //TextView Journal Table Date
        TextView tv2 = new TextView( this );
        tv2.setLayoutParams( tlp );

        tv2.setBackgroundColor( Color.BLUE );
        tv2.setTextColor( Color.WHITE );
        tv2.setGravity( Gravity.CENTER );
        tv2.setTextSize( 18 );
        tv2.setPadding( 1, 1, 1, 1 );

        tv2.setText( " 일 자 " );

        row0.addView( tv2 );

        //TextView Journal Table Site Name
        TextView tv3 = new TextView( this );
        tv3.setLayoutParams( tlp );

        tv3.setBackgroundColor( Color.BLUE );
        tv3.setTextColor( Color.WHITE );
        tv3.setGravity( Gravity.CENTER );
        tv3.setTextSize( 18 );
        tv3.setPadding( 1, 1, 1, 1 );

        tv3.setText( " 현 장 " );

        row0.addView( tv3 );

        //TextView Journal Table One Day
        TextView tv4 = new TextView( this );
        tv4.setLayoutParams( tlp );

        tv4.setBackgroundColor( Color.BLUE );
        tv4.setTextColor( Color.WHITE );
        tv4.setGravity( Gravity.CENTER );
        tv4.setTextSize( 18 );
        tv4.setPadding( 1, 1, 1, 1 );

        tv4.setText( " 일일 " );

        row0.addView( tv4 );

        //TextView Journal Table Daily leader
        TextView tv5 = new TextView( this );
        tv5.setLayoutParams( tlp );

        tv5.setBackgroundColor( Color.BLUE );
        tv5.setTextColor( Color.WHITE );
        tv5.setGravity( Gravity.CENTER );
        tv5.setTextSize( 18 );
        tv5.setPadding( 1, 1, 1, 1 );

        tv5.setText( " 팀 이름 " );

        row0.addView( tv5 );

        //TextView Journal Table memo
        TextView tv6 = new TextView( this );
        tv6.setLayoutParams( tlp );

        tv6.setBackgroundColor( Color.BLUE );
        tv6.setTextColor( Color.WHITE );
        tv6.setGravity( Gravity.CENTER );
        tv6.setTextSize( 18 );
        tv6.setPadding( 1, 1, 1, 1 );

        tv6.setText( " 일일메모 " );

        row0.addView( tv6 );

        //TextView Journal Table Team s_pay
        TextView tv7 = new TextView( this );
        tv7.setLayoutParams( tlp );

        tv7.setBackgroundColor( Color.BLUE );
        tv7.setTextColor( Color.WHITE );
        tv7.setGravity( Gravity.CENTER );
        tv7.setTextSize( 18 );
        tv7.setPadding( 1, 1, 1, 1 );

        tv7.setText( " 일 당 " );

        row0.addView( tv7 );

        //TextView Journal Table j_amount
        TextView tv8 = new TextView( this );
        tv8.setLayoutParams( tlp );

        tv8.setBackgroundColor( Color.BLUE );
        tv8.setTextColor( Color.WHITE );
        tv8.setGravity( Gravity.CENTER );
        tv8.setTextSize( 18 );
        tv8.setPadding( 1, 1, 1, 1 );

        tv8.setText( " 금 액 " );

        row0.addView( tv8 );

        //TextView Journal Table s_id
        TextView tv9 = new TextView( this );
        tv9.setLayoutParams( tlp );

        tv9.setBackgroundColor( Color.BLUE );
        tv9.setTextColor( Color.WHITE );
        tv9.setGravity( Gravity.CENTER );
        tv9.setTextSize( 18 );
        tv9.setPadding( 1, 1, 1, 1 );

        tv9.setText( " 현장ID " );

        row0.addView( tv9 );

        //TextView Journal Table t_id
        TextView tv10 = new TextView( this );
        tv10.setLayoutParams( tlp );

        tv10.setBackgroundColor( Color.BLUE );
        tv10.setTextColor( Color.WHITE );
        tv10.setGravity( Gravity.CENTER );
        tv10.setTextSize( 18 );
        tv10.setPadding( 1, 1, 1, 1 );

        tv10.setText( " 팀ID " );

        row0.addView( tv10 );

        //TableLayout View
        journal_Table.addView( row0 );

        //Data Load
        journalControler.open();
        final Cursor cus = journalControler.selectAllJournal();
        final int rows = cus.getCount();
        final int columns = cus.getColumnCount();

        cus.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            final TableRow row = new TableRow( this );
            row.setLayoutParams( new TableLayout.LayoutParams( TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT ) );
            // inner for loop
            for (int j = 0; j < columns; j++) {

                TextView tv = new TextView( this );
                tv.setLayoutParams( tlp );

                tv.setGravity( Gravity.CENTER );
                tv.setTextSize( 14 );
                tv.setPadding( 1, 1, 1, 1 );

                tv.setText( cus.getString( j ) );

                row.addView( tv );

                //*** color
                if (i % 2 != 0) {
                    tv.setBackgroundColor( Color.parseColor( "#CFF6FA" ) );
                } else {
                    tv.setBackgroundColor( Color.WHITE );
                }

                //--- comma
                if (j == 7 ){
                    int rPay = cus.getInt( 7 );
                    String formattedPay = formatPay.format(rPay);
                    tv.setText( formattedPay );
                }
                if (j == 8){
                    int rAmount = cus.getInt( 8 );
                    String formattedAmount = formatPay.format( rAmount );
                    tv.setText( formattedAmount );
                }

                final int p = i;
                final int r = i + 1;

                row.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rows == 0) {
                            return;
                        } else if (cus.moveToPosition( p )) {

                            Intent positionIntent = new Intent( getApplicationContext(), JournalEdit.class );
                            //ID Journal
                            positionIntent.putExtra( "id", cus.getInt( 0 ) );
                            //Journal ID
                            positionIntent.putExtra( "jid", cus.getString( 1 ) );
                            //Journal Date
                            positionIntent.putExtra( "date", cus.getString( 2 ) );
                            //Journal Site Name
                            positionIntent.putExtra( "site", cus.getString( 3 ) );
                            //Journal One Day
                            positionIntent.putExtra( "oneDay", cus.getFloat( 4 ) );
                            //journal Team Leader
                            positionIntent.putExtra( "leader", cus.getString( 5 ) );
                            //Journal Memo
                            positionIntent.putExtra( "memo", cus.getString( 6 ) );
                            //Journal spay
                            positionIntent.putExtra( "spay", cus.getInt( 7 ) );
                            //Journal jamount
                            positionIntent.putExtra( "jamount", cus.getInt( 8 ) );
                            //Journal sid
                            positionIntent.putExtra( "sid", cus.getString( 9 ) );
                            //Journal tid
                            positionIntent.putExtra( "tid", cus.getString( 10 ) );

                            String site = positionIntent.getExtras().getString( "site" );
                            Toast.makeText( JournalTable.this,
                                    site + "를 선택 했습니다. :" ,
                                    Toast.LENGTH_SHORT ).show();

                            startActivity( positionIntent );
                            finish();
                        }
                        cus.close();
                    }
                } );

            }
            cus.moveToNext();
            journal_Table.addView( row );
        }
        journalControler.close();

    }

    //--- Search Site Journal Data Table
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void searchJournalSiteDate() {
        journal_Table.removeAllViews();
        //Table Row
        TableRow row0 = new TableRow( this );
        row0.setLayoutParams( new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT ) );

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT );

        tlp.setMargins( 1, 0, 1, 0 );

        //TextView Journal Table Row Id
        TextView tv0 = new TextView( this );
        tv0.setLayoutParams( tlp );

        tv0.setBackgroundColor( Color.BLUE );
        tv0.setTextColor( Color.WHITE );
        tv0.setGravity( Gravity.CENTER );
        tv0.setTextSize( 18 );
        tv0.setPadding( 1, 1, 1, 1 );

        tv0.setText( "ID" );

        row0.addView( tv0 );

        //TextView Journal Table Team ID
        TextView tv1 = new TextView( this );
        tv1.setLayoutParams( tlp );

        tv1.setBackgroundColor( Color.BLUE );
        tv1.setTextColor( Color.WHITE );
        tv1.setGravity( Gravity.CENTER );
        tv1.setTextSize( 18 );
        tv1.setPadding( 1, 1, 1, 1 );

        tv1.setText( " j_id " );

        row0.addView( tv1 );

        //TextView Journal Table Date
        TextView tv2 = new TextView( this );
        tv2.setLayoutParams( tlp );

        tv2.setBackgroundColor( Color.BLUE );
        tv2.setTextColor( Color.WHITE );
        tv2.setGravity( Gravity.CENTER );
        tv2.setTextSize( 18 );
        tv2.setPadding( 1, 1, 1, 1 );

        tv2.setText( " 일 자 " );

        row0.addView( tv2 );

        //TextView Journal Table Site Name
        TextView tv3 = new TextView( this );
        tv3.setLayoutParams( tlp );

        tv3.setBackgroundColor( Color.BLUE );
        tv3.setTextColor( Color.WHITE );
        tv3.setGravity( Gravity.CENTER );
        tv3.setTextSize( 18 );
        tv3.setPadding( 1, 1, 1, 1 );

        tv3.setText( " 현 장 " );

        row0.addView( tv3 );

        //TextView Journal Table One Day
        TextView tv4 = new TextView( this );
        tv4.setLayoutParams( tlp );

        tv4.setBackgroundColor( Color.BLUE );
        tv4.setTextColor( Color.WHITE );
        tv4.setGravity( Gravity.CENTER );
        tv4.setTextSize( 18 );
        tv4.setPadding( 1, 1, 1, 1 );

        tv4.setText( " 일일 " );

        row0.addView( tv4 );

        //TextView Journal Table Daily leader
        TextView tv5 = new TextView( this );
        tv5.setLayoutParams( tlp );

        tv5.setBackgroundColor( Color.BLUE );
        tv5.setTextColor( Color.WHITE );
        tv5.setGravity( Gravity.CENTER );
        tv5.setTextSize( 18 );
        tv5.setPadding( 1, 1, 1, 1 );

        tv5.setText( " 팀 이름 " );

        row0.addView( tv5 );

        //TextView Journal Table memo
        TextView tv6 = new TextView( this );
        tv6.setLayoutParams( tlp );

        tv6.setBackgroundColor( Color.BLUE );
        tv6.setTextColor( Color.WHITE );
        tv6.setGravity( Gravity.CENTER );
        tv6.setTextSize( 18 );
        tv6.setPadding( 1, 1, 1, 1 );

        tv6.setText( " 일일메모 " );

        row0.addView( tv6 );

        //TextView Journal Table Team s_pay
        TextView tv7 = new TextView( this );
        tv7.setLayoutParams( tlp );

        tv7.setBackgroundColor( Color.BLUE );
        tv7.setTextColor( Color.WHITE );
        tv7.setGravity( Gravity.CENTER );
        tv7.setTextSize( 18 );
        tv7.setPadding( 1, 1, 1, 1 );

        tv7.setText( " 일 당 " );

        row0.addView( tv7 );

        //TextView Journal Table j_amount
        TextView tv8 = new TextView( this );
        tv8.setLayoutParams( tlp );

        tv8.setBackgroundColor( Color.BLUE );
        tv8.setTextColor( Color.WHITE );
        tv8.setGravity( Gravity.CENTER );
        tv8.setTextSize( 18 );
        tv8.setPadding( 1, 1, 1, 1 );

        tv8.setText( " 금 액 " );

        row0.addView( tv8 );

        //TextView Journal Table s_id
        TextView tv9 = new TextView( this );
        tv9.setLayoutParams( tlp );

        tv9.setBackgroundColor( Color.BLUE );
        tv9.setTextColor( Color.WHITE );
        tv9.setGravity( Gravity.CENTER );
        tv9.setTextSize( 18 );
        tv9.setPadding( 1, 1, 1, 1 );

        tv9.setText( " 현장ID " );

        row0.addView( tv9 );

        //TextView Journal Table t_id
        TextView tv10 = new TextView( this );
        tv10.setLayoutParams( tlp );

        tv10.setBackgroundColor( Color.BLUE );
        tv10.setTextColor( Color.WHITE );
        tv10.setGravity( Gravity.CENTER );
        tv10.setTextSize( 18 );
        tv10.setPadding( 1, 1, 1, 1 );

        tv10.setText( " 팀ID " );

        row0.addView( tv10 );

        //TableLayout View
        journal_Table.addView( row0 );

        //Data Load
        //String spinSearchSite = spinner_Site.getSelectedItem().toString();
        String searchSite = editText_Search.getText().toString();
        String startDay = editText_StartDate.getText().toString();
        String endDay = editText_EndDate.getText().toString();
        journalControler.open();
        final Cursor cus = journalControler.searchSiteJournal( searchSite, startDay, endDay );
        final int rows = cus.getCount();
        final int columns = cus.getColumnCount();

        cus.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            final TableRow row = new TableRow( this );
            row.setLayoutParams( new TableLayout.LayoutParams( TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT ) );
            // inner for loop
            for (int j = 0; j < columns; j++) {

                TextView tv = new TextView( this );
                tv.setLayoutParams( tlp );

                tv.setGravity( Gravity.CENTER );
                tv.setTextSize( 14 );
                tv.setPadding( 1, 1, 1, 1 );

                tv.setText( cus.getString( j ) );

                row.addView( tv );

                //*** TableRow TextView Color *****
                if (i % 2 != 0) {
                    tv.setBackgroundColor( Color.parseColor( "#CFF6FA" ) );
                } else {
                    tv.setBackgroundColor( Color.WHITE );
                }

                //--- comma
                if (j == 7 ){
                    int rPay = cus.getInt( 7 );
                    String formattedPay = formatPay.format(rPay);
                    tv.setText( formattedPay );
                }
                if (j == 8){
                    int rAmount = cus.getInt( 8 );
                    String formattedAmount = formatPay.format( rAmount );
                    tv.setText( formattedAmount );
                }

                final int p = i;
                final int r = i + 1;

                row.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rows == 0) {
                            return;
                        } else if (cus.moveToPosition( p )) {

                            Intent positionIntent = new Intent( getApplicationContext(), JournalEdit.class );
                            //ID Journal
                            positionIntent.putExtra( "id", cus.getInt( 0 ) );
                            //Journal ID
                            positionIntent.putExtra( "jid", cus.getString( 1 ) );
                            //Journal Date
                            positionIntent.putExtra( "date", cus.getString( 2 ) );
                            //Journal Site Name
                            positionIntent.putExtra( "site", cus.getString( 3 ) );
                            //Journal One Day
                            positionIntent.putExtra( "oneDay", cus.getFloat( 4 ) );
                            //journal Team Leader
                            positionIntent.putExtra( "leader", cus.getString( 5 ) );
                            //Journal Memo
                            positionIntent.putExtra( "memo", cus.getString( 6 ) );
                            //Journal spay
                            positionIntent.putExtra( "spay", cus.getInt( 7 ) );
                            //Journal jamount
                            positionIntent.putExtra( "jamount", cus.getInt( 8 ) );
                            //Journal sid
                            positionIntent.putExtra( "sid", cus.getString( 9 ) );
                            //Journal tid
                            positionIntent.putExtra( "tid", cus.getString( 10 ) );

                            String site = positionIntent.getExtras().getString( "site" );
                            Toast.makeText( JournalTable.this,
                                    site + "를 선택 했습니다. :" ,
                                    Toast.LENGTH_SHORT ).show();

                            startActivity( positionIntent );
                            finish();
                        }
                        cus.close();
                    }
                } );

            }
            cus.moveToNext();
            journal_Table.addView( row );
        }
        journalControler.close();

    }

    //--- Search Date Journal Data Table
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void searchDateJournal() {
        journal_Table.removeAllViews();
        //Table Row
        TableRow row0 = new TableRow( this );
        row0.setLayoutParams( new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT ) );

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT );

        tlp.setMargins( 1, 0, 1, 0 );

        //TextView Journal Table Row Id
        TextView tv0 = new TextView( this );
        tv0.setLayoutParams( tlp );

        tv0.setBackgroundColor( Color.BLUE );
        tv0.setTextColor( Color.WHITE );
        tv0.setGravity( Gravity.CENTER );
        tv0.setTextSize( 18 );
        tv0.setPadding( 1, 1, 1, 1 );

        tv0.setText( "ID" );

        row0.addView( tv0 );

        //TextView Journal Table Team ID
        TextView tv1 = new TextView( this );
        tv1.setLayoutParams( tlp );

        tv1.setBackgroundColor( Color.BLUE );
        tv1.setTextColor( Color.WHITE );
        tv1.setGravity( Gravity.CENTER );
        tv1.setTextSize( 18 );
        tv1.setPadding( 1, 1, 1, 1 );

        tv1.setText( " j_id " );

        row0.addView( tv1 );

        //TextView Journal Table Date
        TextView tv2 = new TextView( this );
        tv2.setLayoutParams( tlp );

        tv2.setBackgroundColor( Color.BLUE );
        tv2.setTextColor( Color.WHITE );
        tv2.setGravity( Gravity.CENTER );
        tv2.setTextSize( 18 );
        tv2.setPadding( 1, 1, 1, 1 );

        tv2.setText( " 일 자 " );

        row0.addView( tv2 );

        //TextView Journal Table Site Name
        TextView tv3 = new TextView( this );
        tv3.setLayoutParams( tlp );

        tv3.setBackgroundColor( Color.BLUE );
        tv3.setTextColor( Color.WHITE );
        tv3.setGravity( Gravity.CENTER );
        tv3.setTextSize( 18 );
        tv3.setPadding( 1, 1, 1, 1 );

        tv3.setText( " 현 장 " );

        row0.addView( tv3 );

        //TextView Journal Table One Day
        TextView tv4 = new TextView( this );
        tv4.setLayoutParams( tlp );

        tv4.setBackgroundColor( Color.BLUE );
        tv4.setTextColor( Color.WHITE );
        tv4.setGravity( Gravity.CENTER );
        tv4.setTextSize( 18 );
        tv4.setPadding( 1, 1, 1, 1 );

        tv4.setText( " 일일 " );

        row0.addView( tv4 );

        //TextView Journal Table Daily Pay
        TextView tv5 = new TextView( this );
        tv5.setLayoutParams( tlp );

        tv5.setBackgroundColor( Color.BLUE );
        tv5.setTextColor( Color.WHITE );
        tv5.setGravity( Gravity.CENTER );
        tv5.setTextSize( 18 );
        tv5.setPadding( 1, 1, 1, 1 );

        tv5.setText( " 팀 이름 " );

        row0.addView( tv5 );

        //TextView Journal Table Amount
        TextView tv6 = new TextView( this );
        tv6.setLayoutParams( tlp );

        tv6.setBackgroundColor( Color.BLUE );
        tv6.setTextColor( Color.WHITE );
        tv6.setGravity( Gravity.CENTER );
        tv6.setTextSize( 18 );
        tv6.setPadding( 1, 1, 1, 1 );

        tv6.setText( " 일일메모 " );

        row0.addView( tv6 );

        //TextView Journal Table Team s_pay
        TextView tv7 = new TextView( this );
        tv7.setLayoutParams( tlp );

        tv7.setBackgroundColor( Color.BLUE );
        tv7.setTextColor( Color.WHITE );
        tv7.setGravity( Gravity.CENTER );
        tv7.setTextSize( 18 );
        tv7.setPadding( 1, 1, 1, 1 );

        tv7.setText( " 일 당 " );

        row0.addView( tv7 );

        //TextView Journal Table j_amount
        TextView tv8 = new TextView( this );
        tv8.setLayoutParams( tlp );

        tv8.setBackgroundColor( Color.BLUE );
        tv8.setTextColor( Color.WHITE );
        tv8.setGravity( Gravity.CENTER );
        tv8.setTextSize( 18 );
        tv8.setPadding( 1, 1, 1, 1 );

        tv8.setText( " 금 액 " );

        row0.addView( tv8 );

        //TextView Journal Table s_id
        TextView tv9 = new TextView( this );
        tv9.setLayoutParams( tlp );

        tv9.setBackgroundColor( Color.BLUE );
        tv9.setTextColor( Color.WHITE );
        tv9.setGravity( Gravity.CENTER );
        tv9.setTextSize( 18 );
        tv9.setPadding( 1, 1, 1, 1 );

        tv9.setText( " 현장ID " );

        row0.addView( tv9 );

        //TextView Journal Table t_id
        TextView tv10 = new TextView( this );
        tv10.setLayoutParams( tlp );

        tv10.setBackgroundColor( Color.BLUE );
        tv10.setTextColor( Color.WHITE );
        tv10.setGravity( Gravity.CENTER );
        tv10.setTextSize( 18 );
        tv10.setPadding( 1, 1, 1, 1 );

        tv10.setText( " 팀ID " );

        row0.addView( tv10 );

        //TableLayout View
        journal_Table.addView( row0 );

        //Data Load
        String startDay = editText_StartDate.getText().toString();
        String endDay = editText_EndDate.getText().toString();
        journalControler.open();
        final Cursor cus = journalControler.searchDateJournal( startDay, endDay );
        final int rows = cus.getCount();
        final int columns = cus.getColumnCount();

        cus.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            final TableRow row = new TableRow( this );
            row.setLayoutParams( new TableLayout.LayoutParams( TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT ) );
            // inner for loop
            for (int j = 0; j < columns; j++) {

                TextView tv = new TextView( this );
                tv.setLayoutParams( tlp );

                tv.setGravity( Gravity.CENTER );
                tv.setTextSize( 14 );
                tv.setPadding( 1, 1, 1, 1 );

                tv.setText( cus.getString( j ) );

                row.addView( tv );

                //*** TableRow TextView Color
                if (i % 2 != 0) {
                    tv.setBackgroundColor( Color.parseColor( "#CFF6FA" ) );
                } else {
                    tv.setBackgroundColor( Color.WHITE );
                }

                //--- comma
                if (j == 8 ){
                    int rPay = cus.getInt( 8 );
                    String formattedPay = formatPay.format(rPay);
                    tv.setText( formattedPay );
                }
                if (j == 9){
                    int rAmount = cus.getInt( 9 );
                    String formattedAmount = formatPay.format( rAmount );
                    tv.setText( formattedAmount );
                }

                final int p = i;
                final int r = i + 1;

                row.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rows == 0) {
                            return;
                        } else if (cus.moveToPosition( p )) {

                            Intent positionIntent = new Intent( getApplicationContext(), JournalEdit.class );
                            //ID Journal
                            positionIntent.putExtra( "id", cus.getInt( 0 ) );
                            //Journal ID
                            positionIntent.putExtra( "jid", cus.getString( 1 ) );
                            //Journal Date
                            positionIntent.putExtra( "date", cus.getString( 2 ) );
                            //Journal Site Name
                            positionIntent.putExtra( "site", cus.getString( 3 ) );
                            //Journal One Day
                            positionIntent.putExtra( "oneDay", cus.getFloat( 4 ) );
                            //journal Team Leader
                            positionIntent.putExtra( "leader", cus.getString( 5 ) );
                            //Journal Memo
                            positionIntent.putExtra( "memo", cus.getString( 6 ) );
                            //Journal spay
                            positionIntent.putExtra( "spay", cus.getInt( 7 ) );
                            //Journal jamount
                            positionIntent.putExtra( "jamount", cus.getInt( 8 ) );
                            //Journal sid
                            positionIntent.putExtra( "sid", cus.getString( 9 ) );
                            //Journal tid
                            positionIntent.putExtra( "tid", cus.getString( 10 ) );

                            String site = positionIntent.getExtras().getString( "site" );
                            Toast.makeText( JournalTable.this,
                                    site + "를 선택 했습니다. :" ,
                                    Toast.LENGTH_SHORT ).show();

                            startActivity( positionIntent );
                            finish();
                        }
                        cus.close();
                    }
                } );

            }
            cus.moveToNext();
            journal_Table.addView( row );
        }
        journalControler.close();

    }

    //--- Site and start day and end day sum(one day) sum(amount)spinner
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void sumSiteDateJournal(){
        //Data Load
        String startDay = editText_StartDate.getText().toString();
        String endDay = editText_EndDate.getText().toString();
        //String spinItemSite = spinner_Site.getSelectedItem().toString();
        String search_site_date = editText_Search.getText().toString();

        journalControler.open();
        if (search_site_date.equals("현장을 선택 하세요?")){
            //to do nothing
        }else if (!search_site_date.equals("현장을 선택 하세요?")){
            final Cursor cus = journalControler.sumSiteDateJournal( startDay, endDay, search_site_date );
            final int rows = cus.getCount();
            final int columns = cus.getColumnCount();


            float oneDay = cus.getFloat( 0 );
            String formatted_oneday = formatDouble.format( oneDay );
            textView_inputDay.setText( formatted_oneday + "일" );

            int amount = cus.getInt( 1 );
            String formatted_inputAmount = formatPay.format( amount );
            textView_inputAmount.setText( formatted_inputAmount + "원" );

            cus.close();
        }
        journalControler.close();

    }

    //--- startDay and endDay sum(oneDay) sum(amount)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void sumDateJournal(){
        //Data Load
        String startDay = editText_StartDate.getText().toString();
        String endDay = editText_EndDate.getText().toString();

        journalControler.open();
        final Cursor cusLoad = journalControler.selectAllJournal(  );
        final int rowCount = cusLoad.getCount();
        if (rowCount != 0) {
            Cursor cus = journalControler.searchDateJournalSum( startDay, endDay );
            float day = cus.getFloat( 0 );
            String formatted_day = formatDouble.format( day );
            textView_inputDay.setText( formatted_day + "일" );

            int money = cus.getInt( 1 );
            String formatted_inputAmount = formatPay.format( money );
            textView_inputAmount.setText( formatted_inputAmount + "원" );
        }else {
            textView_inputDay.setText( 0 );
            textView_inputAmount.setText( 0 );
        }
    }

    //--- Date Time
    private void DateTime() {
        SQLiteDatabase db = teamDB.getReadableDatabase();

        Cursor csDate, cusDate = null;

        String startQuery = "select date('now', 'start of month', 'localtime')";

        String endQuery = "select date('now', 'start of month', '+1 month', '-1 day','localtime')";
        //String endQuery = "select datetime('now','localtime')";

        csDate = db.rawQuery( startQuery, null );
        cusDate = db.rawQuery( endQuery, null );

        if (csDate.getCount() > 0 || cusDate.getCount() > 0) {
            csDate.moveToFirst();
            cusDate.moveToFirst();

            editText_StartDate.setText( csDate.getString( 0 ) );
            editText_EndDate.setText( cusDate.getString( 0 ) );

            csDate.close();
            cusDate.close();
        }

        db.close();
    }

    //--- Date Picker Dialog Start
    private void startDatePickerDialog() {
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
                        String startDate = String.format( "%d-%02d-%02d", year, month, dayOfMonth );
                        editText_StartDate.setText( startDate );
                        //etxtStartDate.setText( year + "-" + (month + 1) + "-" + dayOfMonth );
                    }
                }, mYear, mMonth, mDay );

        datePickerDialog.show();
    }

    //--- Date Picker Dialog End
    private void endDatePickerDialog() {
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
                        String endDate = String.format( "%d-%02d-%02d", year, month, dayOfMonth );
                        editText_EndDate.setText( endDate );
                        //etxtEndDate.setText( year + "-" + (month + 1) + "-" + dayOfMonth );
                    }
                }, mYear, mMonth, mDay );

        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate(R.menu.journal_menu, menu);

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.journal_add_option:
                Toast.makeText(getApplicationContext(),
                        "일지 쓰기로 이동 합니다.", Toast.LENGTH_SHORT).show();
                Intent intent_journalAdd = new Intent( getApplicationContext(), JournalAdd.class );
                startActivity( intent_journalAdd );
                finish();
                return true;

            case R.id.journal_addimage_option:
                Toast.makeText(getApplicationContext(),
                        "일지쓰기로 이동 합니다.", Toast.LENGTH_SHORT).show();
                Intent intent_journal_add = new Intent( getApplicationContext(), JournalAdd.class );
                startActivity( intent_journal_add );
                finish();
                return true;

            case R.id.journal_newimage_option:
                DateTime();
                editText_Search.setText("");
                return true;

            case R.id.journal_close_option :
                Toast.makeText(getApplicationContext(),
                        "일지 리스트를 닫습니다.", Toast.LENGTH_SHORT).show();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //--- Team Spinner Item Search
    /*
    private void teamSpinnerItemSearch() {
        // database handler
        siteControl = new SiteControl( getApplicationContext() );
        siteControl.open();

        // Spinner Drop down elements
        List<String> data = siteControl.getAllSpinnerTeam();
        data.add( "팀을 선택 하세요?" );

        adapterSpinner1 = new AdapterSpinner1( this, data );
        spinner_Team.setAdapter( adapterSpinner1 );
        spinner_Team.setSelection( adapterSpinner1.getCount() );
    }
    */

    //--- Site Spinner Item Search
    /*
    private void siteSpinnerItemSearch() {
        // database handler
        journalControl = new JournalControl( getApplicationContext() );
        journalControl.open();

        // Spinner Drop down elements
        List<String> data = journalControl.getAllSpinnerSite();
        data.add( "현장을 선택 하세요?" );

        adapterSpinner1 = new AdapterSpinner1( this, data );

        spinner_Site.setAdapter( adapterSpinner1 );
        spinner_Site.setSelection( adapterSpinner1.getCount() );
    }
    */

    //--- Team and start day and end day sum(one day) sum(amount) spinner
    /*
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void sumTeamDateJournal(){
        if (spinner_Team.getSelectedItem() == "팀을 선택 하세요?" &&
                spinner_Site.getSelectedItem() == "현장을 선택 하세요?"){
            //to do nothing
        }else if (spinner_Team.getSelectedItem() != "팀을 선택 하세요?"){
            //Data Load
            String startDay = editText_StartDate.getText().toString();
            String endDay = editText_EndDate.getText().toString();
            String spinItemTeam = spinner_Team.getSelectedItem().toString();

            journalControl.open();
            final Cursor cus = journalControl.sumTeamSpinnerDateJournal( startDay, endDay, spinItemTeam );
            final int rows = cus.getCount();
            final int columns = cus.getColumnCount();


            float oneDay = cus.getFloat( 0 );
            String formatted_oneday = formatDouble.format( oneDay );
            textView_inputDay.setText( formatted_oneday + "일" );

            int money = cus.getInt( 1 );
            String formatted_inputAmount = formatPay.format( money );
            textView_inputAmount.setText( formatted_inputAmount + "원" );

        }

    }
    */

    //--- Search Team Journal Data Table
    /*
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void searchJournalTeam() {
         journal_Table.removeAllViews();
        //Table Row
        TableRow row0 = new TableRow( this );
        row0.setLayoutParams( new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT ) );

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT );

        tlp.setMargins( 1, 0, 1, 0 );

        //TextView Journal Table Row Id
        TextView tv0 = new TextView( this );
        tv0.setLayoutParams( tlp );

        tv0.setBackgroundColor( Color.BLUE );
        tv0.setTextColor( Color.WHITE );
        tv0.setGravity( Gravity.CENTER );
        tv0.setTextSize( 18 );
        tv0.setPadding( 1, 1, 1, 1 );

        tv0.setText( "ID" );

        row0.addView( tv0 );

        //TextView Journal Table Team ID
        TextView tv1 = new TextView( this );
        tv1.setLayoutParams( tlp );

        tv1.setBackgroundColor( Color.BLUE );
        tv1.setTextColor( Color.WHITE );
        tv1.setGravity( Gravity.CENTER );
        tv1.setTextSize( 18 );
        tv1.setPadding( 1, 1, 1, 1 );

        tv1.setText( "J_ID" );

        row0.addView( tv1 );

        //TextView Journal Table Date
        TextView tv2 = new TextView( this );
        tv2.setLayoutParams( tlp );

        tv2.setBackgroundColor( Color.BLUE );
        tv2.setTextColor( Color.WHITE );
        tv2.setGravity( Gravity.CENTER );
        tv2.setTextSize( 18 );
        tv2.setPadding( 1, 1, 1, 1 );

        tv2.setText( "투입일자" );

        row0.addView( tv2 );

        //TextView Journal Table Site Name
        TextView tv3 = new TextView( this );
        tv3.setLayoutParams( tlp );

        tv3.setBackgroundColor( Color.BLUE );
        tv3.setTextColor( Color.WHITE );
        tv3.setGravity( Gravity.CENTER );
        tv3.setTextSize( 18 );
        tv3.setPadding( 1, 1, 1, 1 );

        tv3.setText( "현장이름" );

        row0.addView( tv3 );

        //TextView Journal Table One Day
        TextView tv4 = new TextView( this );
        tv4.setLayoutParams( tlp );

        tv4.setBackgroundColor( Color.BLUE );
        tv4.setTextColor( Color.WHITE );
        tv4.setGravity( Gravity.CENTER );
        tv4.setTextSize( 18 );
        tv4.setPadding( 1, 1, 1, 1 );

        tv4.setText( "일일" );

        row0.addView( tv4 );

        //TextView Journal Table Daily Pay
        TextView tv5 = new TextView( this );
        tv5.setLayoutParams( tlp );

        tv5.setBackgroundColor( Color.BLUE );
        tv5.setTextColor( Color.WHITE );
        tv5.setGravity( Gravity.CENTER );
        tv5.setTextSize( 18 );
        tv5.setPadding( 1, 1, 1, 1 );

        tv5.setText( "임금" );

        row0.addView( tv5 );

        //TextView Journal Table Amount
        TextView tv6 = new TextView( this );
        tv6.setLayoutParams( tlp );

        tv6.setBackgroundColor( Color.BLUE );
        tv6.setTextColor( Color.WHITE );
        tv6.setGravity( Gravity.CENTER );
        tv6.setTextSize( 18 );
        tv6.setPadding( 1, 1, 1, 1 );

        tv6.setText( "금액" );

        row0.addView( tv6 );

        //TextView Journal Table Team Leader
        TextView tv7 = new TextView( this );
        tv7.setLayoutParams( tlp );

        tv7.setBackgroundColor( Color.BLUE );
        tv7.setTextColor( Color.WHITE );
        tv7.setGravity( Gravity.CENTER );
        tv7.setTextSize( 18 );
        tv7.setPadding( 1, 1, 1, 1 );

        tv7.setText( "팀장/회사" );

        row0.addView( tv7 );

        //TextView Journal Table Memo
        TextView tv8 = new TextView( this );
        tv8.setLayoutParams( tlp );

        tv8.setBackgroundColor( Color.BLUE );
        tv8.setTextColor( Color.WHITE );
        tv8.setGravity( Gravity.CENTER );
        tv8.setTextSize( 18 );
        tv8.setPadding( 1, 1, 1, 1 );

        tv8.setText( "일일메모" );

        row0.addView( tv8 );

        //TableLayout View
        journal_Table.addView( row0 );

        //Data Load
        String spinSearchTeam = spinner_Team.getSelectedItem().toString();
        String startDay = editText_StartDate.getText().toString();
        String endDay = editText_EndDate.getText().toString();
        journalControl.open();
        final Cursor cus = journalControl.searchTeamJournal( spinSearchTeam, startDay, endDay );
        final int rows = cus.getCount();
        final int columns = cus.getColumnCount();

        cus.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            final TableRow row = new TableRow( this );
            row.setLayoutParams( new TableLayout.LayoutParams( TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT ) );
            // inner for loop
            for (int j = 0; j < columns; j++) {

                TextView tv = new TextView( this );
                tv.setLayoutParams( tlp );

                //tv.setBackgroundResource( R.drawable.cell_shape );
                tv.setGravity( Gravity.CENTER );
                tv.setTextSize( 14 );
                tv.setPadding( 1, 1, 1, 1 );

                tv.setText( cus.getString( j ) );

                row.addView( tv );

                //*** TableRow TextView Color
                if (i % 2 != 0) {
                    tv.setBackgroundColor( Color.parseColor( "#CFF6FA" ) );
                } else {
                    tv.setBackgroundColor( Color.WHITE );
                }

                //--- comma
                if (j == 5 ){
                    int rPay = cus.getInt( 5 );
                    String formattedPay = formatPay.format(rPay);
                    tv.setText( formattedPay );
                }
                if (j == 6){
                    int rAmount = cus.getInt( 6 );
                    String formattedAmount = formatPay.format( rAmount );
                    tv.setText( formattedAmount );
                }

                final int p = i;
                final int r = i + 1;

                row.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rows == 0) {
                            return;
                        } else if (cus.moveToPosition( p )) {

                            Intent positionIntent = new Intent( getApplicationContext(), JournalEdit.class );
                            //ID Journal
                            positionIntent.putExtra( "id", cus.getInt( 0 ) );
                            //Journal ID
                            positionIntent.putExtra( "jid", cus.getString( 1 ) );
                            //Journal Date
                            positionIntent.putExtra( "date", cus.getString( 2 ) );
                            //Journal Site Name
                            positionIntent.putExtra( "site", cus.getString( 3 ) );
                            //Journal One Day
                            positionIntent.putExtra( "oneDay", cus.getFloat( 4 ) );
                            //journal Daily pay
                            positionIntent.putExtra( "pay", cus.getInt( 5 ) );
                            //Journal Amount
                            positionIntent.putExtra( "amount", cus.getInt( 6 ) );
                            //Journal Team Leader
                            positionIntent.putExtra( "leader", cus.getString( 7 ) );
                            //Journal Memo
                            positionIntent.putExtra( "memo", cus.getString( 8 ) );

                            String site = positionIntent.getExtras().getString( "site" );
                            Toast.makeText( JournalTable.this,
                                    site + "를 선택 했습니다. :" ,
                                    Toast.LENGTH_SHORT ).show();

                            startActivity( positionIntent );
                            finish();
                        }
                        cus.close();
                    }
                } );

            }
            cus.moveToNext();
            journal_Table.addView( row );
        }
        journalControl.close();

    }
    */
}