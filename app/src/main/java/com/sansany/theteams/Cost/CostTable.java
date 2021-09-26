package com.sansany.theteams.Cost;

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
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sansany.theteams.Controler.CostControler;
import com.sansany.theteams.Controler.SitesControler;
import com.sansany.theteams.Database.DatabaseTeams;

import java.text.DecimalFormat;
import java.util.Calendar;

import sansany.theteams.R;

public class CostTable extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Toolbar toolbar;
    private TableLayout cost_table;
    private ImageButton imageButton_Backbar;
    private EditText editText_StartDate, editText_EndDate, editText_Search;
    private TextView textView_Amount, textView_inputAmount, textView_title;
    private DatabaseTeams teamDB;
    private CostControler costControler;
    private SitesControler sitesControler;
    private DecimalFormat formatDouble;
    private DecimalFormat formatPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_table);

        //ToolBar
        toolbar = findViewById(R.id.cost_table_toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getOverflowIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);

        formatDouble = new DecimalFormat("#,###.##");
        formatPay = new DecimalFormat("#,###");

        //--- UI findViewById
        teamDB = new DatabaseTeams(this);
        costControler = new CostControler(this);
        sitesControler = new SitesControler(this);

        cost_table = findViewById(R.id.cost_table);

        editText_StartDate = findViewById(R.id.cost_edit_startDate);
        editText_StartDate.setInputType(InputType.TYPE_NULL);

        editText_EndDate = findViewById(R.id.cost_edit_endDate);
        editText_EndDate.setInputType(InputType.TYPE_NULL);

        editText_Search = findViewById(R.id.cost_table_site_search);

        textView_Amount = findViewById(R.id.cost_text_sum_amount);
        textView_inputAmount = findViewById(R.id.cost_text_sum_amount_input);
        textView_title = findViewById(R.id.txt_cost_table_toolbar);

        imageButton_Backbar = findViewById(R.id.cost_table_back_toolbar);
        //--- ImageButton Back ClickListener
        imageButton_Backbar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cost_list = new Intent(getApplicationContext(), CostList.class);
                startActivity(cost_list);
                finish();
            }
        });

        DateTime();

        //--- Start Date On Click Listener
        editText_StartDate.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                startDatePickerDialog();
            }
        });

        //--- End Date On Click Listener
        editText_EndDate.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                endDatePickerDialog();
            }
        });

        //--- Start Date add Text Changed Listener
        editText_StartDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void afterTextChanged(Editable s) {
                cost_table.removeAllViews();
                if (editText_Search == null) {
                    searchDateCost();
                    sumDateCost();
                } else {
                    searchSiteDate();
                    sumSearchSiteDate();

                }
            }
        });

        //--- End Date add Text Changed Listener
        editText_EndDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void afterTextChanged(Editable s) {
                cost_table.removeAllViews();
                if (editText_Search == null) {
                    searchDateCost();
                    sumDateCost();
                } else {
                    searchSiteDate();
                    sumSearchSiteDate();
                }

            }
        });

        //--- Search Journal Table
        editText_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void afterTextChanged(Editable s) {

                if (editText_Search.getText().toString().equals("")) {
                    cost_table.removeAllViews();
                    searchDateCost();
                    sumDateCost();
                } else {
                    cost_table.removeAllViews();
                    searchSiteDate();
                    sumSearchSiteDate();
                }

            }
        });

        getIntentResult();//intent result
    }

    private void getIntentResult() {
        Intent posionIntent = getIntent();
        String site = posionIntent.getExtras().getString("site");
        String site_name = posionIntent.getExtras().getString("site_name");
        if (site != null) {
            editText_Search.setText(site);
        } else {
            editText_Search.setText(site_name);
        }

    }

    //---EditText Search Journal Data Table
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void searchDateCost() {
        cost_table.removeAllViews();
        //Table Row
        TableRow row0 = new TableRow(this);
        row0.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);

        tlp.setMargins(1, 1, 1, 1);

        //TextView Journal Table Row Id
        TextView tv0 = new TextView(this);
        tv0.setLayoutParams(tlp);

        tv0.setBackgroundColor(Color.BLUE);
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.CENTER);
        tv0.setTextSize(18);
        tv0.setPadding(1, 1, 1, 1);

        tv0.setText("ID");

        row0.addView(tv0);

        //TextView Journal Table Team ID
        TextView tv1 = new TextView(this);
        tv1.setLayoutParams(tlp);

        tv1.setBackgroundColor(Color.BLUE);
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextSize(18);
        tv1.setPadding(1, 1, 1, 1);

        tv1.setText(" c_id ");

        row0.addView(tv1);

        //TextView Journal Table Date
        TextView tv2 = new TextView(this);
        tv2.setLayoutParams(tlp);

        tv2.setBackgroundColor(Color.BLUE);
        tv2.setTextColor(Color.WHITE);
        tv2.setGravity(Gravity.CENTER);
        tv2.setTextSize(18);
        tv2.setPadding(1, 1, 1, 1);

        tv2.setText(" 일 자 ");

        row0.addView(tv2);

        //TextView Journal Table Site Name
        TextView tv3 = new TextView(this);
        tv3.setLayoutParams(tlp);

        tv3.setBackgroundColor(Color.BLUE);
        tv3.setTextColor(Color.WHITE);
        tv3.setGravity(Gravity.CENTER);
        tv3.setTextSize(18);
        tv3.setPadding(1, 1, 1, 1);

        tv3.setText(" 현 장 ");

        row0.addView(tv3);

        //TextView Journal Table One Day
        TextView tv4 = new TextView(this);
        tv4.setLayoutParams(tlp);

        tv4.setBackgroundColor(Color.BLUE);
        tv4.setTextColor(Color.WHITE);
        tv4.setGravity(Gravity.CENTER);
        tv4.setTextSize(18);
        tv4.setPadding(1, 1, 1, 1);

        tv4.setText(" 내 용 ");

        row0.addView(tv4);

        //TextView Journal Table Daily leader
        TextView tv5 = new TextView(this);
        tv5.setLayoutParams(tlp);

        tv5.setBackgroundColor(Color.BLUE);
        tv5.setTextColor(Color.WHITE);
        tv5.setGravity(Gravity.CENTER);
        tv5.setTextSize(18);
        tv5.setPadding(1, 1, 1, 1);

        tv5.setText(" 금 액 ");

        row0.addView(tv5);

        //TextView Journal Table memo
        TextView tv6 = new TextView(this);
        tv6.setLayoutParams(tlp);

        tv6.setBackgroundColor(Color.BLUE);
        tv6.setTextColor(Color.WHITE);
        tv6.setGravity(Gravity.CENTER);
        tv6.setTextSize(18);
        tv6.setPadding(1, 1, 1, 1);

        tv6.setText(" 경비 메모 ");

        row0.addView(tv6);

        //TextView Journal Table Team s_id
        TextView tv7 = new TextView(this);
        tv7.setLayoutParams(tlp);

        tv7.setBackgroundColor(Color.BLUE);
        tv7.setTextColor(Color.WHITE);
        tv7.setGravity(Gravity.CENTER);
        tv7.setTextSize(18);
        tv7.setPadding(1, 1, 1, 1);

        tv7.setText(" s_id ");

        row0.addView(tv7);

        //TableLayout View
        cost_table.addView(row0);

        //Data Load
        String startDay = editText_StartDate.getText().toString();
        String endDay = editText_EndDate.getText().toString();
        costControler.open();
        final Cursor cus = costControler.selectDateCost(startDay,endDay);
        final int rows = cus.getCount();
        final int columns = cus.getColumnCount();

        cus.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            final TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 0; j < columns; j++) {

                TextView tv = new TextView(this);
                tv.setLayoutParams(tlp);

                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(14);
                tv.setPadding(3, 1, 1, 3);

                tv.setText(cus.getString(j));

                row.addView(tv);

                //*** TableRow TextView Color *****
                if (i % 2 != 0) {
                    tv.setBackgroundColor(Color.parseColor("#CFF6FA"));
                } else {
                    tv.setBackgroundColor(Color.WHITE);
                }

                //--- comma

                if (j == 5 ){
                    int rPay = cus.getInt( 5 );
                    String formattedPay = formatPay.format(rPay);
                    tv.setText( formattedPay );
                }

                /*
                if (j == 6) {
                    int rAmount = cus.getInt(6);
                    String formattedAmount = formatPay.format(rAmount);
                    tv.setText(formattedAmount);
                }
                */

                final int p = i;
                final int r = i + 1;

                row.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rows == 0) {
                            return;
                        } else if (cus.moveToPosition(p)) {

                            Intent positionIntent = new Intent(getApplicationContext(), CostEdit.class);
                            //id
                            positionIntent.putExtra("id", cus.getInt(0));
                            //cost cid
                            positionIntent.putExtra("cid", cus.getString(1));
                            //Date
                            positionIntent.putExtra("date", cus.getString(2));
                            //Site Name
                            positionIntent.putExtra("site", cus.getString(3));
                            //contents
                            positionIntent.putExtra("contents", cus.getString(4));
                            //amount
                            positionIntent.putExtra("amount", cus.getInt(5));
                            //memo
                            positionIntent.putExtra("memo", cus.getString(6));
                            //sid
                            positionIntent.putExtra("sid", cus.getString(7));


                            String site = positionIntent.getExtras().getString("site");
                            Toast.makeText(CostTable.this,
                                    site + "를 선택 했습니다. :",
                                    Toast.LENGTH_SHORT).show();

                            startActivity(positionIntent);
                            finish();
                        }
                        cus.close();
                    }
                });

            }
            cus.moveToNext();
            cost_table.addView(row);
        }
        costControler.close();

    }

    //--- search date cost sum
    @SuppressLint("SetTextI18n")
    private void sumDateCost(){
        String stda = editText_StartDate.getText().toString();
        String enda = editText_EndDate.getText().toString();

        costControler.open();
        Cursor cur = costControler.sumSelectDateCost(stda,enda);

        if (cur.getString(cur.getColumnIndex("camount")) != null){
            int amounts = cur.getInt(0);
            String amt_format = formatPay.format(amounts);
            textView_inputAmount.setText(amt_format + " 원 ");//--> amount
        }else {
            textView_inputAmount.setText("0 원 ");//--> amount
        }
    }

    //---EditText Search Journal Data Table
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void searchSiteDate() {
        cost_table.removeAllViews();
        //Table Row
        TableRow row0 = new TableRow(this);
        row0.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);

        tlp.setMargins(1, 1, 1, 1);

        //TextView Journal Table Row Id
        TextView tv0 = new TextView(this);
        tv0.setLayoutParams(tlp);

        tv0.setBackgroundColor(Color.BLUE);
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.CENTER);
        tv0.setTextSize(18);
        tv0.setPadding(1, 1, 1, 1);

        tv0.setText("ID");

        row0.addView(tv0);

        //TextView Journal Table Team ID
        TextView tv1 = new TextView(this);
        tv1.setLayoutParams(tlp);

        tv1.setBackgroundColor(Color.BLUE);
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextSize(18);
        tv1.setPadding(1, 1, 1, 1);

        tv1.setText(" c_id ");

        row0.addView(tv1);

        //TextView Journal Table Date
        TextView tv2 = new TextView(this);
        tv2.setLayoutParams(tlp);

        tv2.setBackgroundColor(Color.BLUE);
        tv2.setTextColor(Color.WHITE);
        tv2.setGravity(Gravity.CENTER);
        tv2.setTextSize(18);
        tv2.setPadding(1, 1, 1, 1);

        tv2.setText(" 일 자 ");

        row0.addView(tv2);

        //TextView Journal Table Site Name
        TextView tv3 = new TextView(this);
        tv3.setLayoutParams(tlp);

        tv3.setBackgroundColor(Color.BLUE);
        tv3.setTextColor(Color.WHITE);
        tv3.setGravity(Gravity.CENTER);
        tv3.setTextSize(18);
        tv3.setPadding(1, 1, 1, 1);

        tv3.setText(" 현 장 ");

        row0.addView(tv3);

        //TextView Journal Table One Day
        TextView tv4 = new TextView(this);
        tv4.setLayoutParams(tlp);

        tv4.setBackgroundColor(Color.BLUE);
        tv4.setTextColor(Color.WHITE);
        tv4.setGravity(Gravity.CENTER);
        tv4.setTextSize(18);
        tv4.setPadding(1, 1, 1, 1);

        tv4.setText(" 내 용 ");

        row0.addView(tv4);

        //TextView Journal Table Daily leader
        TextView tv5 = new TextView(this);
        tv5.setLayoutParams(tlp);

        tv5.setBackgroundColor(Color.BLUE);
        tv5.setTextColor(Color.WHITE);
        tv5.setGravity(Gravity.CENTER);
        tv5.setTextSize(18);
        tv5.setPadding(1, 1, 1, 1);

        tv5.setText(" 금 액 ");

        row0.addView(tv5);

        //TextView Journal Table memo
        TextView tv6 = new TextView(this);
        tv6.setLayoutParams(tlp);

        tv6.setBackgroundColor(Color.BLUE);
        tv6.setTextColor(Color.WHITE);
        tv6.setGravity(Gravity.CENTER);
        tv6.setTextSize(18);
        tv6.setPadding(1, 1, 1, 1);

        tv6.setText(" 경비 메모 ");

        row0.addView(tv6);

        //TextView Journal Table Team s_id
        TextView tv7 = new TextView(this);
        tv7.setLayoutParams(tlp);

        tv7.setBackgroundColor(Color.BLUE);
        tv7.setTextColor(Color.WHITE);
        tv7.setGravity(Gravity.CENTER);
        tv7.setTextSize(18);
        tv7.setPadding(1, 1, 1, 1);

        tv7.setText(" s_id ");

        row0.addView(tv7);

        //TableLayout View
        cost_table.addView(row0);

        //Data Load
        String searchSite = editText_Search.getText().toString();
        String startDay = editText_StartDate.getText().toString();
        String endDay = editText_EndDate.getText().toString();
        costControler.open();
        final Cursor cus = costControler.searchSiteDate(searchSite, startDay, endDay);
        final int rows = cus.getCount();
        final int columns = cus.getColumnCount();

        cus.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            final TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 0; j < columns; j++) {

                TextView tv = new TextView(this);
                tv.setLayoutParams(tlp);

                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(14);
                tv.setPadding(3, 1, 1, 3);

                tv.setText(cus.getString(j));

                row.addView(tv);

                //*** TableRow TextView Color *****
                if (i % 2 != 0) {
                    tv.setBackgroundColor(Color.parseColor("#CFF6FA"));
                } else {
                    tv.setBackgroundColor(Color.WHITE);
                }

                //--- comma

                if (j == 5 ){
                    int rPay = cus.getInt( 5 );
                    String formattedPay = formatPay.format(rPay);
                    tv.setText( formattedPay );
                }

                 /*
                if (j == 6) {
                    int rAmount = cus.getInt(6);
                    String formattedAmount = formatPay.format(rAmount);
                    tv.setText(formattedAmount);
                }
                */

                final int p = i;
                final int r = i + 1;

                row.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rows == 0) {
                            return;
                        } else if (cus.moveToPosition(p)) {

                            Intent positionIntent = new Intent(getApplicationContext(), CostEdit.class);
                            //id
                            positionIntent.putExtra("id", cus.getInt(0));
                            //cost cid
                            positionIntent.putExtra("cid", cus.getString(1));
                            //Date
                            positionIntent.putExtra("date", cus.getString(2));
                            //Site Name
                            positionIntent.putExtra("site", cus.getString(3));
                            //contents
                            positionIntent.putExtra("contents", cus.getString(4));
                            //amount
                            positionIntent.putExtra("amount", cus.getInt(5));
                            //memo
                            positionIntent.putExtra("memo", cus.getString(6));
                            //sid
                            positionIntent.putExtra("sid", cus.getString(7));


                            String site = positionIntent.getExtras().getString("site");
                            Toast.makeText(CostTable.this,
                                    site + "를 선택 했습니다. :",
                                    Toast.LENGTH_SHORT).show();

                            startActivity(positionIntent);
                            finish();
                        }
                        cus.close();
                    }
                });

            }
            cus.moveToNext();
            cost_table.addView(row);
        }
        costControler.close();

    }

    //--- search date cost sum
    @SuppressLint("SetTextI18n")
    private void sumSearchSiteDate(){
        String site = editText_Search.getText().toString();
        String stda = editText_StartDate.getText().toString();
        String enda = editText_EndDate.getText().toString();

        costControler.open();
        Cursor cur = costControler.sumSearchSiteDate(site,stda,enda);

        if (cur.getString(cur.getColumnIndex("camount")) != null){
            int amounts = cur.getInt(0);
            String amt_format = formatPay.format(amounts);
            textView_inputAmount.setText(amt_format + " 원 ");//--> amount
        }else {
            textView_inputAmount.setText("0 원 ");//--> amount
        }
    }

    //--- Date Time
    private void DateTime() {
        SQLiteDatabase db = teamDB.getReadableDatabase();

        Cursor csDate, cusDate = null;

        String startQuery = "select date('now', 'start of month', 'localtime')";

        String endQuery = "select date('now', 'start of month', '+1 month', '-1 day','localtime')";
        //String endQuery = "select datetime('now','localtime')";

        csDate = db.rawQuery(startQuery, null);
        cusDate = db.rawQuery(endQuery, null);

        if (csDate.getCount() > 0 || cusDate.getCount() > 0) {
            csDate.moveToFirst();
            cusDate.moveToFirst();

            editText_StartDate.setText(csDate.getString(0));
            editText_EndDate.setText(cusDate.getString(0));

            csDate.close();
            cusDate.close();
        }

        db.close();
    }

    //--- Date Picker Dialog Start
    private void startDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH) + 1;
                        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                        @SuppressLint("DefaultLocale")
                        String startDate = String.format("%d-%02d-%02d", year, month, dayOfMonth);
                        editText_StartDate.setText(startDate);
                        //etxtStartDate.setText( year + "-" + (month + 1) + "-" + dayOfMonth );
                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    //--- Date Picker Dialog End
    private void endDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH) + 1;
                        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                        @SuppressLint("DefaultLocale")
                        String endDate = String.format("%d-%02d-%02d", year, month, dayOfMonth);
                        editText_EndDate.setText(endDate);
                        //etxtEndDate.setText( year + "-" + (month + 1) + "-" + dayOfMonth );
                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cost_table_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cost_table_addImage_option:
                Toast.makeText(getApplicationContext(),
                        "경비 쓰기로 이동 합니다.", Toast.LENGTH_SHORT).show();
                Intent intent_costAddImage = new Intent(getApplicationContext(), CostAdd.class);
                startActivity(intent_costAddImage);
                finish();
                return true;

            case R.id.cost_table_newImage_option:
                DateTime();
                editText_Search.setText("");

                return true;

            case R.id.cost_table_close_option:
                Toast.makeText(getApplicationContext(),
                        "경비 쓰기를 닫습니다.", Toast.LENGTH_SHORT).show();
                Intent income_table = new Intent(getApplicationContext(), CostList.class);
                startActivity(income_table);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}