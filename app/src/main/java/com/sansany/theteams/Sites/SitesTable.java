package com.sansany.theteams.Sites;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sansany.theteams.Controler.SitesControler;
import com.sansany.theteams.Database.DatabaseTeams;
import java.text.DecimalFormat;

import sansany.theteams.R;

public class SitesTable extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txt_title;
    private EditText editText_Search;
    private ImageButton imageButton_Backbar;
    public TableLayout site_Table;
    private DatabaseTeams teamDB;
    private SitesControler sitesControler;
    private DecimalFormat formatDouble;
    private  DecimalFormat formatPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sites_table);

        //ToolBar
        toolbar = findViewById( R.id.site_table_toolbar );
        toolbar.setTitle( "" );
        toolbar.setTitleTextColor( Color.WHITE );
        toolbar.getOverflowIcon().setColorFilter(
                Color.parseColor( "#FFFFFF" ),
                PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar( toolbar );

        formatDouble = new DecimalFormat("#,###.##");//금액 콤마 소숫점
        formatPay = new DecimalFormat("#,###");//금액 콤마 소숫점 없음

        teamDB = new DatabaseTeams( this );
        sitesControler = new SitesControler( this );

        //-- findViewById --
        txt_title = findViewById(R.id.txt_site_toolbar);
        site_Table = findViewById( R.id.site_table );
        editText_Search = findViewById( R.id.site_edit_search );
        imageButton_Backbar = findViewById( R.id.site_back_btn_toolbar );

        //Image Button Back Click
        imageButton_Backbar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_table = new Intent(getApplicationContext(), SitesList.class);
                startActivity(intent_table);
                finish();
            }
        } );

        // Search Edit Text Changed
        editText_Search.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged( Editable s) {
                site_Table.removeAllViews();
                siteSearchData();

            }
        } );

        getIntentResultSite();

    }

    private void getIntentResultSite() {
        Intent positionIntent = getIntent();
        String site = positionIntent.getExtras().getString("site");
        String name = positionIntent.getExtras().getString("name");
        if (site == null){
            editText_Search.setText(name);
        }else {
            editText_Search.setText(site);
        }
    }

    @SuppressLint({"SetTextI18n", "Range"})
    private void siteLoadData() {
        //Table Row
        TableRow row0 = new TableRow( this );
        row0.setLayoutParams( new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT ) );

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT );

        tlp.setMargins( 1, 0, 1, 0 );

        //TextView Site Id
        TextView tv0 = new TextView( this );
        tv0.setLayoutParams( tlp );

        tv0.setBackgroundColor( Color.BLUE );
        tv0.setTextColor( Color.WHITE );
        tv0.setGravity( Gravity.CENTER );
        tv0.setTextSize( 18 );
        tv0.setLines(1);
        tv0.setPadding( 1, 1, 1, 1 );

        tv0.setText( "ID" );

        row0.addView( tv0 );

        //TextView Site ID
        TextView tv1 = new TextView( this );
        tv1.setLayoutParams( tlp );

        tv1.setBackgroundColor( Color.BLUE );
        tv1.setTextColor( Color.WHITE );
        tv1.setGravity( Gravity.CENTER );
        tv1.setTextSize( 18 );
        tv1.setLines(1);
        tv1.setPadding( 1, 1, 1, 1 );

        tv1.setText( " s_id " );

        row0.addView( tv1 );

        //TextView Team Leader
        TextView tv2 = new TextView( this );
        tv2.setLayoutParams( tlp );

        tv2.setBackgroundColor( Color.BLUE );
        tv2.setTextColor( Color.WHITE );
        tv2.setGravity( Gravity.CENTER );
        tv2.setTextSize( 18 );
        tv2.setLines(1);
        tv2.setPadding( 1, 1, 1, 1 );

        tv2.setText( " 현 장 " );

        row0.addView( tv2 );

        //TextView Team Leader Phone No
        TextView tv3 = new TextView( this );
        tv3.setLayoutParams( tlp );

        tv3.setBackgroundColor( Color.BLUE );
        tv3.setTextColor( Color.WHITE );
        tv3.setGravity( Gravity.CENTER );
        tv3.setTextSize( 18 );
        tv3.setLines(1);
        tv3.setPadding( 1, 1, 1, 1 );

        tv3.setText( " 팀 이름 " );

        row0.addView( tv3 );

        //TextView Team Leader Phone No
        TextView tv4 = new TextView( this );
        tv4.setLayoutParams( tlp );

        tv4.setBackgroundColor( Color.BLUE );
        tv4.setTextColor( Color.WHITE );
        tv4.setGravity( Gravity.CENTER );
        tv4.setTextSize( 18 );
        tv4.setLines(1);
        tv4.setPadding( 1, 1, 1, 1 );

        tv4.setText( " 임금(일당) " );

        row0.addView( tv4 );

        //TextView Team Leader Phone No
        TextView tv5 = new TextView( this );
        tv5.setLayoutParams( tlp );

        tv5.setBackgroundColor( Color.BLUE );
        tv5.setTextColor( Color.WHITE );
        tv5.setGravity( Gravity.CENTER );
        tv5.setTextSize( 18 );
        tv5.setLines(1);
        tv5.setPadding( 1, 1, 1, 1 );

        tv5.setText( " 팀 반장 " );

        row0.addView( tv5 );

        //TextView Team Start Date
        TextView tv6 = new TextView( this );
        tv6.setLayoutParams( tlp );

        tv6.setBackgroundColor( Color.BLUE );
        tv6.setTextColor( Color.WHITE );
        tv6.setGravity( Gravity.CENTER );
        tv6.setTextSize( 18 );
        tv6.setLines(1);
        tv6.setPadding( 1, 1, 1, 1 );

        tv6.setText( " 등록일자 " );

        row0.addView( tv6 );

        //TextView Team Leader
        TextView tv7 = new TextView( this );
        tv7.setLayoutParams( tlp );

        tv7.setBackgroundColor( Color.BLUE );
        tv7.setTextColor( Color.WHITE );
        tv7.setGravity( Gravity.CENTER );
        tv7.setTextSize( 18 );
        tv7.setLines(1);
        tv7.setPadding( 1, 1, 1, 1 );

        tv7.setText( " 현장 메모 " );

        row0.addView( tv7 );

        //TextView Team Leader
        TextView tv8 = new TextView( this );
        tv8.setLayoutParams( tlp );

        tv8.setBackgroundColor( Color.BLUE );
        tv8.setTextColor( Color.WHITE );
        tv8.setGravity( Gravity.CENTER );
        tv8.setTextSize( 18 );
        tv8.setLines(1);
        tv8.setPadding( 1, 1, 1, 1 );

        tv8.setText( " 팀 ID " );

        row0.addView( tv8 );

        //TableLayout View
        site_Table.addView( row0 );

        //Data Load
        sitesControler.open();
        final Cursor cus = sitesControler.selectAllSite();
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
                tv.setLines(1);
                tv.setPadding( 0, 1, 0, 1 );

                tv.setText( cus.getString( j ) );

                row.addView( tv );

                //********
                if (i % 2 != 0) {
                    tv.setBackgroundColor( Color.parseColor( "#CFF6FA" ) );
                } else {
                    tv.setBackgroundColor( Color.WHITE );
                }

                //--- comma
                if (j == 4 ){
                    int rPay = cus.getInt( 4 );
                    String formattedPay = formatPay.format(rPay);
                    tv.setText( formattedPay );
                }

                final int p = i;
                final int r = i + 1;
                final int rId = i + 2;

                row.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rows == 0) {
                            return;
                        } else if (cus.moveToPosition( p )) {
                            Intent positionIntent = new Intent( getApplicationContext(), SitesEdit.class );

                            positionIntent.putExtra( "id", cus.getInt( 0 ) );
                            positionIntent.putExtra( "sid", cus.getString( 1 ) );
                            positionIntent.putExtra( "name", cus.getString( 2 ) );
                            positionIntent.putExtra( "leader", cus.getString( 3 ) );
                            positionIntent.putExtra( "pay", cus.getInt( 4 ) );
                            positionIntent.putExtra( "manager", cus.getString( 5 ) );
                            positionIntent.putExtra( "date", cus.getString( 6 ) );
                            positionIntent.putExtra( "memo", cus.getString( 7 ) );
                            positionIntent.putExtra( "tid", cus.getString(8 ));

                            String name = positionIntent.getExtras().getString( "name" );

                            Toast.makeText( SitesTable.this, "선택 현장 :" + name,
                                    Toast.LENGTH_SHORT ).show();

                            startActivity( positionIntent );
                            finish();
                        }

                        cus.close();
                    }
                } );
            }
            cus.moveToNext();
            site_Table.addView( row );
        }

        sitesControler.close();
    }

    @SuppressLint("SetTextI18n")
    private void siteSearchData() {
        //Table Row
        TableRow row0 = new TableRow( this );
        row0.setLayoutParams( new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT ) );

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT );

        tlp.setMargins( 1, 0, 1, 0 );

        //TextView Site Id
        TextView tv0 = new TextView( this );
        tv0.setLayoutParams( tlp );

        tv0.setBackgroundColor( Color.BLUE );
        tv0.setTextColor( Color.WHITE );
        tv0.setGravity( Gravity.CENTER );
        tv0.setTextSize( 18 );
        tv0.setLines(1);
        tv0.setPadding( 1, 1, 1, 1 );

        tv0.setText( "ID" );

        row0.addView( tv0 );

        //TextView Site ID
        TextView tv1 = new TextView( this );
        tv1.setLayoutParams( tlp );

        tv1.setBackgroundColor( Color.BLUE );
        tv1.setTextColor( Color.WHITE );
        tv1.setGravity( Gravity.CENTER );
        tv1.setTextSize( 18 );
        tv1.setLines(1);
        tv1.setPadding( 1, 1, 1, 1 );

        tv1.setText( " s_id " );

        row0.addView( tv1 );

        //TextView Team Leader
        TextView tv2 = new TextView( this );
        tv2.setLayoutParams( tlp );

        tv2.setBackgroundColor( Color.BLUE );
        tv2.setTextColor( Color.WHITE );
        tv2.setGravity( Gravity.CENTER );
        tv2.setTextSize( 18 );
        tv2.setLines(1);
        tv2.setPadding( 1, 1, 1, 1 );

        tv2.setText( " 현 장 " );

        row0.addView( tv2 );

        //TextView Team Leader Phone No
        TextView tv3 = new TextView( this );
        tv3.setLayoutParams( tlp );

        tv3.setBackgroundColor( Color.BLUE );
        tv3.setTextColor( Color.WHITE );
        tv3.setGravity( Gravity.CENTER );
        tv3.setTextSize( 18 );
        tv3.setLines(1);
        tv3.setPadding( 1, 1, 1, 1 );

        tv3.setText( " 팀 이름 " );

        row0.addView( tv3 );

        //TextView Team Leader Phone No
        TextView tv4 = new TextView( this );
        tv4.setLayoutParams( tlp );

        tv4.setBackgroundColor( Color.BLUE );
        tv4.setTextColor( Color.WHITE );
        tv4.setGravity( Gravity.CENTER );
        tv4.setTextSize( 18 );
        tv4.setLines(1);
        tv4.setPadding( 1, 1, 1, 1 );

        tv4.setText( " 임금(일당) " );

        row0.addView( tv4 );

        //TextView Team Leader Phone No
        TextView tv5 = new TextView( this );
        tv5.setLayoutParams( tlp );

        tv5.setBackgroundColor( Color.BLUE );
        tv5.setTextColor( Color.WHITE );
        tv5.setGravity( Gravity.CENTER );
        tv5.setTextSize( 18 );
        tv5.setLines(1);
        tv5.setPadding( 1, 1, 1, 1 );

        tv5.setText( " 팀 반장 " );

        row0.addView( tv5 );

        //TextView Team Start Date
        TextView tv6 = new TextView( this );
        tv6.setLayoutParams( tlp );

        tv6.setBackgroundColor( Color.BLUE );
        tv6.setTextColor( Color.WHITE );
        tv6.setGravity( Gravity.CENTER );
        tv6.setTextSize( 18 );
        tv6.setLines(1);
        tv6.setPadding( 1, 1, 1, 1 );

        tv6.setText( " 등록일자 " );

        row0.addView( tv6 );

        //TextView Team Leader
        TextView tv7 = new TextView( this );
        tv7.setLayoutParams( tlp );

        tv7.setBackgroundColor( Color.BLUE );
        tv7.setTextColor( Color.WHITE );
        tv7.setGravity( Gravity.CENTER );
        tv7.setTextSize( 18 );
        tv7.setLines(1);
        tv7.setPadding( 1, 1, 1, 1 );

        tv7.setText( " 현장 메모 " );

        row0.addView( tv7 );

        //TextView Team Leader
        TextView tv8 = new TextView( this );
        tv8.setLayoutParams( tlp );

        tv8.setBackgroundColor( Color.BLUE );
        tv8.setTextColor( Color.WHITE );
        tv8.setGravity( Gravity.CENTER );
        tv8.setTextSize( 18 );
        tv8.setLines(1);
        tv8.setPadding( 1, 1, 1, 1 );

        tv8.setText( " 팀 ID " );

        row0.addView( tv8 );

        //TableLayout View
        site_Table.addView( row0 );

        //Data Load
        String searchSite = editText_Search.getText().toString();
        sitesControler.open();
        final Cursor cus = sitesControler.searchSite( searchSite );
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
                tv.setLines(1);
                tv.setPadding( 0, 1, 0, 1 );

                tv.setText( cus.getString( j ) );

                row.addView( tv );

                //********
                if (i % 2 != 0) {
                    tv.setBackgroundColor( Color.parseColor( "#CFF6FA" ) );
                } else {
                    tv.setBackgroundColor( Color.WHITE );
                }

                //--- comma
                if (j == 4 ){
                    int rPay = cus.getInt( 4 );
                    String formattedPay = formatPay.format(rPay);
                    tv.setText( formattedPay );
                }

                final int p = i;
                final int r = i + 1;
                final int rId = i + 2;

                row.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rows == 0) {
                            return;
                        } else if (cus.moveToPosition( p )) {
                            Intent positionIntent = new Intent( getApplicationContext(), SitesEdit.class );

                            positionIntent.putExtra( "id", cus.getInt( 0 ) );
                            positionIntent.putExtra( "sid", cus.getString( 1 ) );
                            positionIntent.putExtra( "name", cus.getString( 2 ) );
                            positionIntent.putExtra( "leader", cus.getString( 3 ) );
                            positionIntent.putExtra( "pay", cus.getInt( 4 ) );
                            positionIntent.putExtra( "manager", cus.getString( 5 ) );
                            positionIntent.putExtra( "date", cus.getString( 6 ) );
                            positionIntent.putExtra( "memo", cus.getString( 7 ) );
                            positionIntent.putExtra( "tid", cus.getString(8 ) );

                            String name = positionIntent.getExtras().getString( "name" );

                            Toast.makeText( SitesTable.this, "선택 현장 :" + name,
                                    Toast.LENGTH_SHORT ).show();

                            startActivity( positionIntent );
                            finish();
                        }

                        cus.close();
                    }
                } );
            }
            cus.moveToNext();
            site_Table.addView( row );
        }

        sitesControler.close();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate(R.menu.site_menu, menu);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.site_add_image_option:
                Toast.makeText(getApplicationContext(),
                        "현장 추가 등록으로 이동 합니다.", Toast.LENGTH_SHORT).show();
                Intent intent_siteAdd = new Intent( getApplicationContext(), SitesAdd.class );
                startActivity( intent_siteAdd );
                finish();
                return true;

            case R.id.site_newimage_option:
                editText_Search.setText("");
                return true;

            case R.id.site_close_option :
                Toast.makeText(getApplicationContext(),
                        "종료 합니다.", Toast.LENGTH_SHORT).show();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}