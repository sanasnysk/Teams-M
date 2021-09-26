package com.sansany.theteams.Journals;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sansany.theteams.Controler.JournalControler;
import com.sansany.theteams.Database.DatabaseTeams;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sansany.theteams.R;

public class JournalAdd extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Toolbar toolbar;
    private EditText editText_jId, editText_date, editText_Site, editText_oneday,
            editText_Leader, editText_Memo,editText_spay,editText_jamount,editText_sid, editText_tid;
    private String result_pay,result_amount = "";
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private ImageButton imageButton_backbar;
    private DatabaseTeams teamDB;
    private JournalControler journalControler;
    //ListView Dialog
    private Button button_run;
    private TextView textview_result;
    private static final String TAG_TEXT = "text";
    private static final String TAG_IMAGE = "image";
    List<Map<String, Object>> dialogItemList;
    int[] image = {R.drawable.img_s0002};
    String[] text = {""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_add);

        //ToolBar
        toolbar = findViewById( R.id.journal_add_toolbar );
        toolbar.setTitle( "" );
        toolbar.setTitleTextColor( Color.WHITE );
        toolbar.getOverflowIcon().setColorFilter(Color.parseColor( "#FFFFFF" ), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar( toolbar );

        teamDB = new DatabaseTeams( this );
        journalControler = new JournalControler( this );

        //---
        editText_jId = findViewById( R.id.journal_add_edit_jid );
        editText_date = findViewById( R.id.journal_add_edit_Date );
        editText_Site = findViewById( R.id.journal_add_edit_site );
        editText_oneday = findViewById( R.id.journal_add_edit_oneday );
        editText_oneday.requestFocus();
        editText_Leader = findViewById( R.id.journal_add_edit_leader );
        editText_Memo = findViewById( R.id.journal_add_edit_memo );
        editText_spay = findViewById( R.id.journal_add_edit_spay );
        editText_jamount = findViewById( R.id.journal_add_edit_jamount );
        editText_sid = findViewById( R.id.journal_add_edit_sid );
        editText_tid = findViewById( R.id.journal_add_edit_tid );

        //----- EditText Journal Date On Click Listener -----
        editText_date.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get( Calendar.YEAR );
                mMonth = c.get( Calendar.MONTH );
                mDay = c.get( Calendar.DAY_OF_MONTH );

                DatePickerDialog();
            }
        } );

        //--- Image Button Back
        imageButton_backbar = findViewById( R.id.journal_add_back_toolbar );
        imageButton_backbar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack = new Intent( getApplicationContext(), JournalList.class );
                startActivity( intentBack );
                finish();
            }
        } );

        //--- 숫자 콤마
        TextWatcher watcher_spay = new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after ) {

            }

            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count ) {
                if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(result_pay)){
                    result_pay = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",", "")));
                    editText_spay.setText(result_pay);
                    editText_spay.setSelection(result_pay.length());
                }
            }

            @Override
            public void afterTextChanged( Editable s ) {
            }
        };
        TextWatcher watcher_amount = new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after ) {

            }

            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count ) {
                if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(result_amount)){
                    result_amount = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",", "")));
                    editText_jamount.setText(result_amount);
                    editText_jamount.setSelection(result_amount.length());
                }
            }

            @Override
            public void afterTextChanged( Editable s ) {
            }
        };
        editText_spay.addTextChangedListener(watcher_spay);
        editText_jamount.addTextChangedListener(watcher_amount);

        editText_oneday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (editText_oneday == null) {
                    //Do nothing
                    float soneday = 0;
                    int pay = Integer.parseInt( editText_spay.getText().toString() );
                    int amount = (int) (soneday * pay);
                    editText_jamount.setText( String.valueOf( amount ) );

                } else if (editText_oneday.length() > 0) {

                    float oneday = Float.parseFloat( s.toString() );

                    String sPay = editText_spay.getText().toString().replace( ",", "" );
                    int pay = Integer.parseInt(sPay);

                    int amount = (int) (oneday * pay);
                    editText_jamount.setText( String.valueOf( amount ) );
                } else {
                    //Do nothing
                }


            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //ListView Dialog
        textview_result = findViewById(R.id.textview_main_text);
        Button button_run = findViewById(R.id.button_main_run);
        button_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        dateTimeJournal();
        jourAutoId(); //--- Journal Table AutoId and DateTime 실행
    }

    //----- Journal Table AutoId -----
    @SuppressLint("SetTextI18n")
    private void jourAutoId() {
        //Data Load
        journalControler.open();
        final Cursor cus = journalControler.journalAutoId();
        final int rows = cus.getCount();
        String journalid = "j_";
        int idNo = 1;
        int r = cus.getInt( 0 );

        if (cus == null) {
            editText_jId.setText( journalid + idNo );
        } else {
            int rid = r + idNo;
            editText_jId.setText(journalid + rid);
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog, null);
        builder.setView(view);

        final TextView title = view.findViewById(R.id.textview_main_text);
        final ListView listview = view.findViewById(R.id.listview_alterdialog_list);
        final AlertDialog dialog = builder.create();
        //title.setText("현장을 선택 하세요?");

        // database handler
        journalControler = new JournalControler( getApplicationContext() );
        journalControler.open();

        // ListView Dropdown elements
        List<String> data = journalControler.getAllSpinnerSite();

        String[] item_data = data.toArray(new String[0]);
        int size = 0;
        for (String temp:data){
            item_data[size++] = temp;
        }
        text = item_data;

        dialogItemList = new ArrayList<>();

        for(int i=0;i<data.size();i++)
        {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put(TAG_IMAGE, image[0]);
            itemMap.put(TAG_TEXT, text[i]);

            dialogItemList.add(itemMap);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(), dialogItemList, R.layout.alert_dialog_row,
                new String[]{TAG_IMAGE, TAG_TEXT},
                new int[]{R.id.alertDialogItemImageView, R.id.alertDialogItemTextView} );

        listview.setAdapter(simpleAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textview_result.setText(text[position]);
                // Showing selected  item
                // On selecting a spinner item
                String site = textview_result.getText().toString();
                if (site.equals("현장을 선택 하세요?")) {
                    //Do nothing....
                } else {
                    // Showing selected spinner item
                    Toast.makeText( parent.getContext(), "You selected: " + site,
                            Toast.LENGTH_SHORT ).show();

                    editText_Site.setText( site );

                    // outer for loop
                    //---Data Edit Site_Name Team_Leader Daily_Pay 출력
                    journalControler.open();
                    final Cursor cus = journalControler.siteSpinnerResult( site );
                    final int rows = cus.getCount();
                    final int clums = cus.getColumnCount();

                    cus.moveToFirst();
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < clums; j++) {

                            final int p = i;
                            final int r = i + 1;

                            if (rows == 0) {
                                return;
                            } else if (cus.moveToPosition( p )) {

                                String tleader = cus.getString( 0 );
                                editText_Leader.setText( tleader );
                                String spay = cus.getString(1);
                                editText_spay.setText( spay );
                                String sid = cus.getString(2);
                                editText_sid.setText( sid );
                                String tid = cus.getString(3);
                                editText_tid.setText(tid);

                                editText_oneday.requestFocus();

                            }
                        }
                    }
                }
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        //dialog.getWindow().setLayout(900,1200);
    }

    //----- DateTime Journal -----
    private void dateTimeJournal() {
        SQLiteDatabase db = teamDB.getReadableDatabase();

        Cursor csdate, cusdate = null;

        String startQuery = "select date('now','localtime')";
        //String endQuery = "select date('now','localtime')";
        //String endQuery = "select datetime('now','localtime')";

        csdate = db.rawQuery( startQuery, null );
        //cusdate = db.rawQuery( endQuery, null );
        if (csdate.getCount() > 0) {
            csdate.moveToFirst();
            //cusdate.moveToFirst();

            editText_date.setText( csdate.getString( 0 ) );
            // etxtEndDate.setText( cusdate.getString( 0 ));
            //etedate.setText( cusdate.getString( 0 ) );

            csdate.close();
            //cusdate.close();
        }

        db.close();
    }

    //----- DatePickerDialog -----
    private void DatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog( this,
                new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //for
                        Calendar calendar = Calendar.getInstance();
                        calendar.set( year, month, dayOfMonth );
                        year = calendar.get( Calendar.YEAR );
                        month = calendar.get( Calendar.MONTH ) + 1;
                        dayOfMonth = calendar.get( Calendar.DAY_OF_MONTH );

                        @SuppressLint("DefaultLocale")
                        String startDate = String.format( "%d-%02d-%02d", year, month, dayOfMonth );

                        editText_date.setText( startDate );
                    }
                }, mYear, mMonth, mDay );

        datePickerDialog.show();
    }

    //----- Site Spinner Item Load -----
    /*
    private void siteSpinnerItem() {
        // database handler
        journalControler = new JournalControler( getApplicationContext() );
        journalControler.open();

        /// Spinner Drop down elements
        List<String> data = journalControler.getAllSpinnerSite();
        data.add( "현장을 선택 하세요?" );

        adapterSpinner1 = new AdapterSpinner1( this, data );

        spinner_site.setAdapter( adapterSpinner1 );
        spinner_site.setSelection( adapterSpinner1.getCount() );

    }

     */

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate(R.menu.journal_add_menu, menu);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.journal_add_saveimage_option:
                if (editText_date.length() == 0 || editText_Site.length() == 0) {
                    Toast.makeText( JournalAdd.this,
                            "날짜와 이름을 입력하세요?", Toast.LENGTH_LONG ).show();
                } else if (editText_oneday.length() == 0) {
                    Toast.makeText( JournalAdd.this,
                            "일량을 입력하세요?", Toast.LENGTH_SHORT ).show();
                } else {
                    String jid = editText_jId.getText().toString();
                    String date = editText_date.getText().toString();
                    String site = editText_Site.getText().toString();
                    String day = editText_oneday.getText().toString();
                    String leader = editText_Leader.getText().toString();
                    String memo = editText_Memo.getText().toString();
                    String spay = editText_spay.getText().toString().replace(",","");
                    String jamount = editText_jamount.getText().toString().replace(",","");
                    String sid = editText_sid.getText().toString();
                    String tid = editText_tid.getText().toString();

                    journalControler.open();
                    journalControler.insertJournal( jid,date,site,day,leader,memo,spay,jamount,sid,tid );

                    Toast.makeText(getApplicationContext(),
                            "일지 내용을 저장 했습니다.", Toast.LENGTH_SHORT).show();

                    Intent inSavejournal = new Intent( getApplicationContext(), JournalList.class );
                    startActivity( inSavejournal );
                    finish();
                }
                return true;

            case R.id.journal_add_close_option :
                Toast.makeText(getApplicationContext(),
                        "일지 쓰기를 종료합니다.", Toast.LENGTH_SHORT).show();
                Intent intentAddClose = new Intent( getApplicationContext(), JournalList.class );
                startActivity( intentAddClose );
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}