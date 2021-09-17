package com.sansany.theteams.Cost;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import com.sansany.theteams.Controler.CostControler;
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

public class CostEdit extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Toolbar toolbar;
    private EditText edit_cid, edit_date,
            edit_site, edit_contents, edit_amount,edit_memo,edit_id, edit_sid;
    private ImageButton imageButton_backbar;
    private DatabaseTeams teamDB;
    private CostControler costControler;
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
        setContentView(R.layout.activity_cost_edit);

        //ToolBar
        toolbar = findViewById( R.id.cost_edit_toolbar );
        toolbar.setTitle( "" );
        toolbar.setTitleTextColor( Color.WHITE );
        toolbar.getOverflowIcon().setColorFilter(Color.parseColor( "#FFFFFF" ), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar( toolbar );

        //--- UI findViewById
        teamDB = new DatabaseTeams( this );
        costControler = new CostControler( this );
        journalControler = new JournalControler( this );
        sitesControler = new SitesControler(this);

        edit_cid = findViewById( R.id.cost_up_edit_cid );
        edit_date = findViewById( R.id.cost_up_edit_date );
        edit_site = findViewById( R.id.cost_up_edit_site );
        edit_contents = findViewById( R.id.cost_up_edit_contents );
        edit_amount = findViewById( R.id.cost_up_edit_amount );
        edit_memo = findViewById( R.id.cost_up_edit_memo );
        edit_id = findViewById(R.id.cost_up_edit_id);
        edit_sid = findViewById( R.id.cost_up_edit_sid );

        imageButton_backbar = findViewById( R.id.cost_edit_back_toolbar );

        //--> EditText 항목 쓰기방지(read-only) or android:focusable="false"
        edit_cid.setInputType( InputType.TYPE_NULL );
        edit_site.setInputType( InputType.TYPE_NULL );
        edit_date.setInputType( InputType.TYPE_NULL );
        edit_date.setFocusable( false );
        edit_contents.requestFocus();

        //---- 숫자 콤마
        TextWatcher watcher_amount = new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after ) {

            }
            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count ) {
                if(!TextUtils.isEmpty(s.toString()) && !s.toString().equals(result_collect)){
                    result_collect = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",","")));
                    edit_amount.setText(result_collect);
                    edit_amount.setSelection(result_collect.length());
                }
            }
            @Override
            public void afterTextChanged( Editable s ) {
            }
        };


        edit_amount.addTextChangedListener(watcher_amount);

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
                Intent intentBack = new Intent( getApplicationContext(), CostList.class );
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

        costDateTime();
        getIntentResult();
    }

    private void getIntentResult() {
        //--- Intent Result Data -----
        Intent positionIntent = getIntent();

        int id = positionIntent.getExtras().getInt( "id" ); // int 형
        String cid = positionIntent.getExtras().getString( "cid" ); //String 형
        String date = positionIntent.getExtras().getString( "date" );
        String site = positionIntent.getExtras().getString( "site" );
        String contents = positionIntent.getExtras().getString( "contents" );
        int amount = positionIntent.getExtras().getInt( "amount" );
        String memo = positionIntent.getExtras().getString( "memo" );
        String sid = positionIntent.getExtras().getString( "sid" );

        edit_id.setText( String.valueOf( id ) );
        edit_cid.setText( cid );
        edit_date.setText( date );
        edit_site.setText( site );
        edit_contents.setText( contents );
        edit_amount.setText( String.valueOf( amount ) );
        edit_memo.setText( memo );
        edit_sid.setText( sid );
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
        journalControler = new JournalControler(getApplicationContext());
        journalControler.open();

        // Spinner Drop down elements
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
                String site = textview_result.getText().toString();
                if (site.equals("현장을 선택 하세요?")) {
                    //Do nothing....
                } else {
                    // Showing selected spinner item
                    Toast.makeText(parent.getContext(), "You selected: " + site,
                            Toast.LENGTH_SHORT).show();

                    edit_site.setText(site);

                    // outer for loop
                    //---Data Edit Site_Name Team_Leader Daily_Pay 출력
                    final Cursor cus = journalControler.siteSpinnerResult(site);
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

                                String sid = cus.getString(2);
                                edit_sid.setText(sid);

                                edit_contents.requestFocus();

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

    //--- Date Time
    private void costDateTime() {
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
        getMenuInflater().inflate(R.menu.cost_edit_menu, menu);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cost_up_edit_image_option:
                if (edit_site.length() == 0 || edit_contents.length() == 0){
                    Toast.makeText( CostEdit.this,
                            "현장 또는 수입금액을 입력 하세요?", Toast.LENGTH_SHORT ).show();
                }else {
                    String cId = edit_cid.getText().toString();
                    String cDate = edit_date.getText().toString();
                    String cSite = edit_site.getText().toString();
                    String contents = edit_contents.getText().toString();
                    String cAmount = edit_amount.getText().toString().replace( ",", "" );
                    String cMemo = edit_memo.getText().toString();
                    String id = edit_id.getText().toString();
                    String sid = edit_sid.getText().toString();

                    costControler.open();
                    costControler.updateCost(id, cId, cDate, cSite, contents, cAmount, cMemo, sid );

                    Toast.makeText(getApplicationContext(),
                            "수입/경비 내용을  수정", Toast.LENGTH_SHORT).show();

                    Intent savecostintent = new Intent( getApplicationContext(), CostList.class );
                    startActivity( savecostintent );

                    finish();
                }

                return true;

            case R.id.cost_up_delete_image_option:
                String rid = edit_id.getText().toString();
                String cid = edit_cid.getText().toString();
                if (edit_id.length() > 0){
                    costControler.open();
                    costControler.deleteCost(rid);

                    Toast.makeText( CostEdit.this,
                            "Deleted" + cid, Toast.LENGTH_LONG ).show();
                    Intent updateintent = new Intent( getApplicationContext(), CostList.class );
                    startActivity( updateintent );

                    finish();
                }else {
                    Toast.makeText( CostEdit.this,
                            "Not Deleted" + cid, Toast.LENGTH_LONG ).show();
                }
                return true;
            case R.id.cost_up_edit_text_option:
                if (edit_site.length() == 0 || edit_contents.length() == 0){
                    Toast.makeText( CostEdit.this,
                            "현장 또는 수입금액을 입력 하세요?", Toast.LENGTH_SHORT ).show();
                }else {
                    String cId = edit_cid.getText().toString();
                    String cDate = edit_date.getText().toString();
                    String cSite = edit_site.getText().toString();
                    String contents = edit_contents.getText().toString();
                    String cAmount = edit_amount.getText().toString().replace( ",", "" );
                    String cMemo = edit_memo.getText().toString();
                    String id = edit_id.getText().toString();
                    String sid = edit_sid.getText().toString();

                    costControler.open();
                    costControler.updateCost(id, cId, cDate, cSite, contents, cAmount, cMemo, sid );

                    Toast.makeText(getApplicationContext(),
                            "수입/경비 내용을  수정 했습니다.", Toast.LENGTH_SHORT).show();

                    Intent saveincomeintent = new Intent( getApplicationContext(), CostList.class );
                    startActivity( saveincomeintent );

                    finish();
                }
                return true;

            case R.id.cost_add_close_option:
                Toast.makeText(getApplicationContext(),
                        "수입/경비 수정 끝내기", Toast.LENGTH_SHORT).show();
                Intent intent_addClose = new Intent( getApplicationContext(), CostList.class );
                startActivity( intent_addClose );
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}