package com.sansany.theteams.Incomes;

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
import android.text.InputType;
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

import com.sansany.theteams.Controler.IncomeControler;
import com.sansany.theteams.Controler.JournalControler;
import com.sansany.theteams.Controler.SitesControler;
import com.sansany.theteams.Database.DatabaseTeams;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sansany.theteams.R;

public class IncomeAdd extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Toolbar toolbar;
    private EditText edit_iid, edit_date,
            edit_leader, edit_collect, edit_tax,edit_memo, edit_tid;
    private ImageButton imageButton_backbar;
    private DatabaseTeams teamDB;
    private IncomeControler incomeControler;
    private JournalControler journalControler;
    private SitesControler sitesControler;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private String result_collect, result_tax = "";
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
        setContentView(R.layout.activity_income_add);

        //ToolBar
        toolbar = findViewById( R.id.income_add_toolbar );
        toolbar.setTitle( "" );
        toolbar.setTitleTextColor( Color.WHITE );
        toolbar.getOverflowIcon().setColorFilter(Color.parseColor( "#FFFFFF" ), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar( toolbar );

        //--- UI findViewById
        teamDB = new DatabaseTeams( this );
        incomeControler = new IncomeControler( this );
        journalControler = new JournalControler( this );
        sitesControler = new SitesControler(this);

        edit_iid = findViewById( R.id.income_add_edit_iid );
        edit_date = findViewById( R.id.income_add_edit_date );
        edit_leader = findViewById( R.id.income_add_edit_leader );
        edit_collect = findViewById( R.id.income_add_edit_collect );
        edit_tax = findViewById( R.id.income_add_edit_tax );
        edit_memo = findViewById( R.id.income_add_edit_memo );
        edit_tid = findViewById( R.id.income_add_edit_tid );

        imageButton_backbar = findViewById( R.id.income_add_back_toolbar );

        //--> EditText 항목 쓰기방지(read-only) or android:focusable="false"
        edit_iid.setInputType( InputType.TYPE_NULL );
        edit_leader.setInputType( InputType.TYPE_NULL );
        edit_date.setInputType( InputType.TYPE_NULL );
        edit_date.setFocusable( false );
        edit_collect.requestFocus();

        //---- 숫자 콤마
        TextWatcher watcher_collect = new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after ) {

            }
            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count ) {
                if(!TextUtils.isEmpty(s.toString()) && !s.toString().equals(result_collect)){
                    result_collect = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",","")));
                    edit_collect.setText(result_collect);
                    edit_collect.setSelection(result_collect.length());
                }
            }
            @Override
            public void afterTextChanged( Editable s ) {
            }
        };
        TextWatcher watcher_tax = new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after ) {

            }

            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count ) {
                if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(result_tax)){
                    result_tax = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",", "")));
                    edit_tax.setText(result_tax);
                    edit_tax.setSelection(result_tax.length());
                }
            }

            @Override
            public void afterTextChanged( Editable s ) {
            }
        };

        edit_collect.addTextChangedListener(watcher_collect);
        edit_tax.addTextChangedListener(watcher_tax);

        //--- Income Date Selected Changed
        edit_date.setOnClickListener( new View.OnClickListener() {
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
        imageButton_backbar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack = new Intent( getApplicationContext(), IncomeList.class );
                startActivity( intentBack );
                finish();
            }
        } );

        //ListView Dialog
        textview_result = findViewById(R.id.textview_main_text);
        Button button_run = findViewById(R.id.button_main_run);
        button_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        incomeDateTime();
        incomeAutoId();
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
        sitesControler = new SitesControler(getApplicationContext());
        sitesControler.open();

        // Spinner Drop down elements
        List<String> data = sitesControler.getAllSpinnerTeam();

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
                String leader = textview_result.getText().toString();
                if (leader.equals("현장을 선택 하세요?")) {
                    //Do nothing....
                } else {
                    // Showing selected spinner item
                    Toast.makeText(parent.getContext(), "You selected: " + leader,
                            Toast.LENGTH_SHORT).show();

                    edit_leader.setText(leader);

                    // outer for loop
                    //---Data Edit Site_Name Team_Leader Daily_Pay 출력
                    final Cursor cus = sitesControler.teamSpinnerResult(leader);
                    final int rows = cus.getCount();
                    final int clums = cus.getColumnCount();

                    cus.moveToFirst();
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < clums; j++) {

                            final int p = i;
                            final int r = i + 1;

                            if (rows == 0) {
                                return;
                            } else if (cus.moveToPosition(p)) {

                                String tid = cus.getString(0);
                                edit_tid.setText(tid);

                                edit_collect.requestFocus();

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

    //--- Income Auto Id
    @SuppressLint("SetTextI18n")
    private void incomeAutoId() {
        //Data Load
        incomeControler.open();
        final Cursor cus = incomeControler.incomeAutoId();
        final int rows = cus.getCount();
        String journalid = "i_";
        int idNo = 1;

        if (rows == 0) {
            edit_iid.setText( journalid + idNo );
        } else {
            int r = cus.getInt( 0 );
            int rid = idNo + r;
            edit_iid.setText(journalid + rid);
        }
    }

    //--- Date Time
    private void incomeDateTime() {
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
            edit_date.setText( csdate.getString( 0 ) );
            // etxtEndDate.setText( cusdate.getString( 0 ));
            //etedate.setText( cusdate.getString( 0 ) );
            csdate.close();
            //cusdate.close();
        }

        db.close();
    }

    //--- Date Picker Dialog
    private void DatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog( this,
                new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //for
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        year = calendar.get( Calendar.YEAR );
                        month = calendar.get(Calendar.MONTH) + 1;
                        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                        @SuppressLint("DefaultLocale")
                        String startDate = String.format("%d-%02d-%02d", year, month, dayOfMonth);

                        edit_date.setText( startDate );
                    }
                }, mYear, mMonth, mDay );

        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate(R.menu.income_add_menu, menu);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()) {
            case R.id.income_add_saveImage_option:
                if (edit_leader.length() == 0 || edit_collect.length() == 0){
                    Toast.makeText( IncomeAdd.this,
                            "팀장/회사 또는 수입금액을 입력 하세요?", Toast.LENGTH_SHORT ).show();
                }else {
                    String incId = edit_iid.getText().toString();
                    String incDate = edit_date.getText().toString();
                    String incLeader = edit_leader.getText().toString();
                    String incCollect = edit_collect.getText().toString().replace( ",", "" );
                    String incTax = edit_tax.getText().toString().replace( ",", "" );
                    String incTid = edit_tid.getText().toString();
                    String incMemo = edit_memo.getText().toString();

                    incomeControler.open();
                    incomeControler.insertIncome( incId, incDate, incLeader, incCollect, incTax, incMemo, incTid );

                    Toast.makeText(getApplicationContext(),
                            "수입/경비 내용을  추가", Toast.LENGTH_SHORT).show();

                    Intent saveincomeintent = new Intent( getApplicationContext(), IncomeList.class );
                    startActivity( saveincomeintent );

                    finish();
                }

                return true;

            case R.id.income_add_close_option :
                Toast.makeText(getApplicationContext(),
                        "수입/경비 추가 끝내기", Toast.LENGTH_SHORT).show();
                Intent intent_addClose = new Intent( getApplicationContext(), IncomeList.class );
                startActivity( intent_addClose );
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}