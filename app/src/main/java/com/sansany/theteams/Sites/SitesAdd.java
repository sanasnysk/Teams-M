package com.sansany.theteams.Sites;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sansany.theteams.Controler.SitesControler;
import com.sansany.theteams.Database.DatabaseTeams;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sansany.theteams.R;

public class SitesAdd extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    private TextView textView_Bar;//
    private Toolbar toolbar;
    private ImageButton imageButton_backbar;
    private Spinner spinner_team;
//    private AdapterSpinner1 adapterSpinner1;
    private EditText editText_sid,editText_name,editText_leader,editText_pay,
            editText_manager,editText_date,editText_memo,editText_tid;
    private Button button_save;
    private DatabaseTeams teamDB;
    private SitesControler sitesControler;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private String result="";

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
        setContentView(R.layout.activity_sites_add);

//ToolBar
        toolbar = findViewById(R.id.site_add_toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getOverflowIcon().setColorFilter(
                Color.parseColor("#FFFFFF"),
                PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);

        teamDB = new DatabaseTeams(this );
       sitesControler = new SitesControler(this);

       //findViewById
                imageButton_backbar = findViewById( R.id.site_add_back_imgbtn_toolbar );

        editText_sid = findViewById( R.id.site_add_edit_sId );
        editText_name = findViewById( R.id.site_add_edit_name );
        editText_leader = findViewById( R.id.site_add_edit_leader );
        editText_pay = findViewById( R.id.site_add_edit_pay );
        editText_manager = findViewById( R.id.site_add_edit_manager );
        editText_date = findViewById( R.id.site_add_edit_date );
        editText_memo = findViewById( R.id.site_add_edit_memo );
        editText_tid = findViewById(R.id.site_add_edit_tid);

        editText_name.requestFocus();

        //Image Button Back Click
        imageButton_backbar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_back = new Intent( getApplicationContext(), SitesList.class );
                startActivity( intent_back );
                finish();
            }
        } );

        //Edit Text Date Click
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

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after ) {

            }
            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count ) {
                if(!TextUtils.isEmpty(s.toString()) && !s.toString().equals(result)){
                    result = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",","")));
                    editText_pay.setText(result);
                    editText_pay.setSelection(result.length());
                }
            }
            @Override
            public void afterTextChanged( Editable s ) {

            }
        };

        editText_pay.addTextChangedListener(watcher);

        //ListView Dialog
        textview_result = findViewById(R.id.textview_main_text);
        Button button_run = findViewById(R.id.button_main_run);
        button_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        siteAutoId();
        DateTime();

    }

    @SuppressLint("SetTextI18n")
    public void siteAutoId() {
        sitesControler.open();
        final Cursor cus = sitesControler.siteAutoId();
        final int rows = cus.getCount();
        String siteId = "s_";
        int idNo = 1;

        if (rows == 0) {
            editText_sid.setText( "s_" + idNo );
        } else {
            int r = cus.getInt( 0 );
            int rid = idNo + r;
            editText_sid.setText(siteId + rid);
        }

    }

    @SuppressLint("ResourceType")
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog,null);
        builder.setView(view);

        final TextView title = view.findViewById(R.id.textview_main_text);
        final ListView listview = view.findViewById(R.id.listview_alterdialog_list);
        final AlertDialog dialog = builder.create();

        // database handler
        sitesControler = new SitesControler( getApplicationContext() );
        sitesControler.open();

        // ListView Dropdown elements
        List<String> data = sitesControler.getAllSpinnerTeam();
        //int[] image_data = {R.drawable.img_s0002};
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
                String leader = textview_result.getText().toString();
                if (leader.equals("현장을 선택 하세요?")) {
                    //Do nothing....
                } else {
                    // Showing selected spinner item
                    Toast.makeText(parent.getContext(), "You selected: " + leader,
                            Toast.LENGTH_SHORT).show();

                    editText_leader.setText(leader);

                    // outer for loop
                    //---Data Edit Site_Name Team_Leader Daily_Pay 출력
                    //sitesControler.open();
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

                                String tleader = cus.getString(0);
                                editText_tid.setText(tleader);

                                editText_name.requestFocus();

                            }
                        }
                    }
                }

                dialog.dismiss();
            }


        });
        sitesControler.close();

        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        //dialog.getWindow().setLayout(1000,1500);
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
        editText_date = findViewById( R.id.site_add_edit_date );

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.site_add_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.site_add_saveimage_option:
                if (editText_name.length() == 0 || editText_leader.length() == 0) {
                    Toast.makeText( SitesAdd.this,
                            "현장/팀장 이름을 입력하세요?", Toast.LENGTH_LONG ).show();
                }
                else if (editText_pay.length() == 0) {
                    Toast.makeText( SitesAdd.this,
                            "일당을 입력하세요?", Toast.LENGTH_SHORT ).show();
                } else {
                    String siteId = editText_sid.getText().toString();
                    String name = editText_name.getText().toString();
                    String leader = editText_leader.getText().toString();
                    String pay = editText_pay.getText().toString().replace( ",", "" );
                    String manager = editText_manager.getText().toString();
                    String date = editText_date.getText().toString();
                    String memo = editText_memo.getText().toString();
                    String tid = editText_tid.getText().toString();

                    sitesControler.open();
                    sitesControler.insertSite( siteId, name, leader, pay, manager, date, memo,tid );

                    Toast.makeText(getApplicationContext(),
                            "새 현장을 추가 했습니다.", Toast.LENGTH_SHORT).show();

                    Intent intent_save = new Intent( getApplicationContext(), SitesList.class );
                    startActivity( intent_save );

                    finish();
                }

                return true;

            case R.id.site_add_close_option:
                Toast.makeText(getApplicationContext(),
                        "현장 추가를 닫습니다.", Toast.LENGTH_SHORT).show();

                Intent intent_close = new Intent( getApplicationContext(), SitesList.class );
                startActivity( intent_close );
                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}