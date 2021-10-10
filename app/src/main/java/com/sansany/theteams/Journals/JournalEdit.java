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

import com.sansany.theteams.Controller.JournalController;
import com.sansany.theteams.Database.DatabaseTeams;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sansany.theteams.R;

public class JournalEdit extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Toolbar toolbar;
    private EditText editText_id, editText_jid,editText_date, editText_Site, editText_oneday,
            editText_leader, editText_Memo,editText_spay,editText_jamount,editText_sid, editText_tid;
    private ImageButton imageButton_backbar, imageButton_update, imageButton_delete;
    private String result_pay,result_amount = "";
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private DatabaseTeams teamDB;
    private JournalController journalController;
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
        setContentView(R.layout.activity_journal_edit);

        //ToolBar
        toolbar = findViewById( R.id.journal_edit_toolbar );
        toolbar.setTitle( "" );
        toolbar.setTitleTextColor( Color.WHITE );
        toolbar.getOverflowIcon().setColorFilter(Color.parseColor( "#FFFFFF" ), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar( toolbar );

        teamDB = new DatabaseTeams( this );
        journalController = new JournalController( this );

        //--- EditText findViewById
        editText_jid = findViewById( R.id.journal_update_edit_jid );
        editText_date = findViewById( R.id.journal_update_edit_Date );
        editText_Site = findViewById( R.id.journal_update_edit_site );
        editText_oneday = findViewById( R.id.journal_update_edit_oneday );
        editText_leader = findViewById( R.id.journal_update_edit_leader );
        editText_Memo = findViewById( R.id.journal_update_edit_memo );
        editText_id = findViewById( R.id.journal_update_edit_rowid );
        editText_spay = findViewById( R.id.journal_update_edit_spay );
        editText_jamount = findViewById( R.id.journal_update_edit_jamount );
        editText_sid = findViewById( R.id.journal_update_edit_sid );
        editText_tid = findViewById( R.id.journal_update_edit_tid );

        getIntentResult();

        //--- ImageButton findViewById
        imageButton_backbar = findViewById( R.id.journal_edit_back_toolbar );
        //--- Image Button Back
        imageButton_backbar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack = new Intent( getApplicationContext(), JournalList.class );
                startActivity( intentBack );
                finish();
            }
        } );

        //--- EditText Date ClickListener
        editText_date.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // Get Current Date
                @SuppressLint({"NewApi", "LocalSuppress"})
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog();
            }
        } );

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

        //ListView Dialog
        textview_result = findViewById(R.id.textview_main_text);
        Button button_run = findViewById(R.id.button_main_run);
        button_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }
    private void getIntentResult(){
        //--- Result Intent Data
        Intent positionIntent = getIntent();

        int id = positionIntent.getExtras().getInt( "id" ); // int 형
        String jid = positionIntent.getExtras().getString( "jid" ); //String 형
        String date = positionIntent.getExtras().getString( "date" );
        String site = positionIntent.getExtras().getString( "site" );
        float oneday = positionIntent.getExtras().getFloat( "oneDay" );
        String leader = positionIntent.getExtras().getString( "leader" );
        String memo = positionIntent.getExtras().getString( "memo" );
        int spay = positionIntent.getExtras().getInt( "spay" );
        int jamount = positionIntent.getExtras().getInt( "jamount" );
        String sid = positionIntent.getExtras().getString( "sid" );
        String tid = positionIntent.getExtras().getString( "tid" );


        editText_id.setText( String.valueOf( id ) );
        editText_jid.setText( jid );
        editText_date.setText( date );
        editText_Site.setText( site );
        editText_oneday.setText( String.valueOf( oneday ) );
        editText_leader.setText( leader );
        editText_Memo.setText( memo );
        editText_spay.setText( String.valueOf(spay));
        editText_jamount.setText( String.valueOf(jamount));
        editText_sid.setText( sid );
        editText_tid.setText( tid );
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
        journalController = new JournalController( getApplicationContext() );
        journalController.open();

        // ListView Dropdown elements
        List<String> data = journalController.getAllSpinnerSite();

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

//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
//        listview.setAdapter(adapter);
        SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(), dialogItemList, R.layout.alert_dialog_row,
                new String[]{TAG_IMAGE, TAG_TEXT},
                new int[]{R.id.alertDialogItemImageView, R.id.alertDialogItemTextView} );

        listview.setAdapter(simpleAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //textview_result.setText(parent.getItemAtPosition(position).toString());
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
                    journalController.open();
                    final Cursor cus = journalController.siteSpinnerResult( site );
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
                                editText_leader.setText( tleader );
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

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate(R.menu.journal_update_menu, menu);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()) {
            case R.id.journal_image_edit_option:
                if (editText_Site.length() == 0){
                    Toast.makeText( JournalEdit.this, "수정할 내용이 없습니다.",
                            Toast.LENGTH_LONG ).show();
                    editText_oneday.requestFocus();
                }else if (editText_oneday.length() == 0){
                    Toast.makeText( JournalEdit.this, "수정할 내용이 없습니다.",
                            Toast.LENGTH_SHORT ).show();
                    editText_oneday.requestFocus();
                }else {
                    //Update Data
                    String id = editText_id.getText().toString();
                    String jid = editText_jid.getText().toString();
                    String date = editText_date.getText().toString();
                    String site = editText_Site.getText().toString();
                    String oneday = editText_oneday.getText().toString();
                    String leader = editText_leader.getText().toString();
                    String memo = editText_Memo.getText().toString();
                    String spay = editText_spay.getText().toString().replace(",","");
                    String jamount = editText_jamount.getText().toString().replace(",","");
                    String sid = editText_sid.getText().toString();
                    String tid = editText_tid.getText().toString();

                    //db open
                    journalController.open();
                    journalController.updateJournalData(id, jid, date, site, oneday, leader ,memo,spay,jamount, sid,tid  );

                    Toast.makeText( JournalEdit.this, site + "내용을 수정 햇습니다.",
                            Toast.LENGTH_SHORT ).show();

                    Intent updateIntent = new Intent( getApplicationContext(), JournalList.class );
                    startActivity( updateIntent );
                    finish();
                }
                return true;

            case R.id.journal_image_delete_option:
                String id = editText_id.getText().toString();
                String jourid = editText_jid.getText().toString();
                if (editText_id.length() > 0){
                    journalController.open();
                    journalController.deleteJournalData(id);

                    Toast.makeText( JournalEdit.this,
                            jourid + "를 삭제 했습니다." , Toast.LENGTH_LONG ).show();
                    Intent updateintent = new Intent( getApplicationContext(), JournalList.class );
                    startActivity( updateintent );

                    finish();
                }else {
                    Toast.makeText( JournalEdit.this,
                            jourid + "가 삭제 되지 않았습니다." , Toast.LENGTH_LONG ).show();

                }
                return true;

            case R.id.journal_update_close_option :
                Toast.makeText(getApplicationContext(),
                        "일지 수정을 종료합니다.", Toast.LENGTH_SHORT).show();
                Intent intent_journalUpdateClose = new Intent( getApplicationContext(), JournalList.class );
                startActivity( intent_journalUpdateClose );
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}