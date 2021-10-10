package com.sansany.theteams.Incomes;

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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sansany.theteams.Controler.IncomeControler;
import com.sansany.theteams.Controler.JournalControler;
import com.sansany.theteams.Controler.SitesControler;
import com.sansany.theteams.Database.DatabaseTeams;

import java.text.DecimalFormat;
import java.util.Calendar;

import sansany.theteams.R;

public class IncomeTable extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Toolbar toolbar;
    private EditText editText_StartDate, editText_EndDate, editText_search;
    private TextView textView_day, textView_amount, textView_balance,
            textView_Collect, textView_Tax, textView_BalanceDay, text_title;
    private ImageButton imageButton_Backbar;
    private TableLayout income_table;
    private DatabaseTeams teamDB;
    private JournalControler journalControler;
    private SitesControler sitesControler;
    private IncomeControler incomeControler;
    private DecimalFormat formatDouble;
    private DecimalFormat formatPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_table);

        //ToolBar
        toolbar = findViewById(R.id.income_table_toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getOverflowIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);

        formatDouble = new DecimalFormat("#,###.##");
        formatPay = new DecimalFormat("#,###");

        teamDB = new DatabaseTeams(this);
        journalControler = new JournalControler(this);
        sitesControler = new SitesControler(this);
        incomeControler = new IncomeControler(this);


        //--- UI findViewById
        income_table = findViewById(R.id.income_table);
        text_title = findViewById(R.id.txt_income_table_toolbar);

        editText_StartDate = findViewById(R.id.income_table_startDate);
        editText_EndDate = findViewById(R.id.income_table_endDate);

        textView_day = findViewById(R.id.income_text_day_total);
        textView_amount = findViewById(R.id.income_text_amount_total);
        textView_balance = findViewById(R.id.income_text_balance_total);
        textView_Collect = findViewById(R.id.income_text_collect_total);
        textView_Tax = findViewById(R.id.income_text_tax_total);
        textView_BalanceDay = findViewById(R.id.income_text_day_balance);

        imageButton_Backbar = findViewById(R.id.income_table_image_back_toolbar);
        //spinner_site = findViewById( R.id.income_table_site_spinner );

        //--> EditText 항목 쓰기방지(read-only) or android:focusable="false"
        editText_StartDate.setInputType(InputType.TYPE_NULL);
        editText_StartDate.setFocusable(false);
        editText_EndDate.setInputType(InputType.TYPE_NULL);
        editText_EndDate.setFocusable(false);
        editText_search = findViewById(R.id.income_table_site_search);

        //--- ImageButton Back ClickListener
        imageButton_Backbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent income_table = new Intent(getApplicationContext(), IncomeList.class);
                startActivity(income_table);
                finish();
            }
        });

        //------ Start Date OnClick Listener -------------------------------
        editText_StartDate.setOnClickListener(new View.OnClickListener() {
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

        editText_EndDate.setOnClickListener(new View.OnClickListener() {
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

        editText_StartDate.addTextChangedListener(textWatcher);
        editText_EndDate.addTextChangedListener(textWatcher);
        editText_search.addTextChangedListener(textWatcher);

        DateTime();
        getIntentResult();

    }

    private void getIntentResult() {
        Intent posionIntent = getIntent();
        String leader = posionIntent.getExtras().getString("leader");
        String leader_name = posionIntent.getExtras().getString("leader");
        if (leader != null) {
            editText_search.setText(leader);
        } else {
            editText_search.setText(leader_name);
        }

    }

    //--- addTextChangedListener TextWatcher
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void afterTextChanged(Editable editable) {
            income_table.removeAllViews();
            searchTeamDateLoad();
            //sumSearchTeamDate();
            sumDateJournal();
            sumDateIncome();
        }
    };

    //--- Income Table Site Search ---
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void incomeSearchLoadData() {
        income_table.removeAllViews();
        //Table Row
        TableRow row0 = new TableRow(this);
        row0.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);

        tlp.setMargins(1, 0, 1, 0);

        //TextView Income Table Row Id
        TextView tv0 = new TextView(this);
        tv0.setLayoutParams(tlp);

        tv0.setBackgroundColor(Color.BLUE);
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.CENTER);
        tv0.setTextSize(18);
        tv0.setLines(1);
        tv0.setPadding(3, 1, 3, 1);

        tv0.setText("ID");

        row0.addView(tv0);

        //TextView Income Table income ID
        TextView tv1 = new TextView(this);
        tv1.setLayoutParams(tlp);

        tv1.setBackgroundColor(Color.BLUE);
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextSize(18);
        tv1.setLines(1);
        tv1.setPadding(3, 1, 3, 1);

        tv1.setText(" i_id ");

        row0.addView(tv1);

        //TextView Income Table Date
        TextView tv2 = new TextView(this);
        tv2.setLayoutParams(tlp);

        tv2.setBackgroundColor(Color.BLUE);
        tv2.setTextColor(Color.WHITE);
        tv2.setGravity(Gravity.CENTER);
        tv2.setTextSize(18);
        tv2.setLines(1);
        tv2.setPadding(3, 1, 3, 1);

        tv2.setText(" 일 자 ");

        row0.addView(tv2);

        //TextView Income Table Leader Name
        TextView tv3 = new TextView(this);
        tv3.setLayoutParams(tlp);

        tv3.setBackgroundColor(Color.BLUE);
        tv3.setTextColor(Color.WHITE);
        tv3.setGravity(Gravity.CENTER);
        tv3.setTextSize(18);
        tv3.setLines(1);
        tv3.setPadding(3, 1, 3, 1);

        tv3.setText(" 팀 이름 ");

        row0.addView(tv3);

        //TextView Income Table Collect
        TextView tv4 = new TextView(this);
        tv4.setLayoutParams(tlp);

        tv4.setBackgroundColor(Color.BLUE);
        tv4.setTextColor(Color.WHITE);
        tv4.setGravity(Gravity.CENTER);
        tv4.setTextSize(18);
        tv4.setLines(1);
        tv4.setPadding(3, 1, 3, 1);

        tv4.setText(" 입 금 ");//수입금액 입금액

        row0.addView(tv4);

        //TextView Income Table Tax
        TextView tv5 = new TextView(this);
        tv5.setLayoutParams(tlp);

        tv5.setBackgroundColor(Color.BLUE);
        tv5.setTextColor(Color.WHITE);
        tv5.setGravity(Gravity.CENTER);
        tv5.setTextSize(18);
        tv5.setLines(1);
        tv5.setPadding(3, 1, 3, 1);

        tv5.setText("세 금 ");//세금공제

        row0.addView(tv5);

        //TextView Income Table Cost
        TextView tv6 = new TextView(this);
        tv6.setLayoutParams(tlp);

        tv6.setBackgroundColor(Color.BLUE);
        tv6.setTextColor(Color.WHITE);
        tv6.setGravity(Gravity.CENTER);
        tv6.setTextSize(18);
        tv6.setLines(1);
        tv6.setPadding(3, 1, 3, 1);

        tv6.setText(" 메 모 ");//memo
        row0.addView(tv6);

        //TextView Income Table Memo
        TextView tv7 = new TextView(this);
        tv7.setLayoutParams(tlp);

        tv7.setBackgroundColor(Color.BLUE);
        tv7.setTextColor(Color.WHITE);
        tv7.setGravity(Gravity.CENTER);
        tv7.setTextSize(18);
        tv7.setLines(1);
        tv7.setPadding(3, 1, 3, 1);

        tv7.setText(" t_id ");

        row0.addView(tv7);

        //TableLayout View
        income_table.addView(row0);

        //Data Load
        String search = editText_search.getText().toString();

        incomeControler.open();
        final Cursor cus = incomeControler.incomeSearchSelect(search);
        final int rows = cus.getCount();
        final int clums = cus.getColumnCount();

        cus.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            final TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 0; j < clums; j++) {

                TextView tv = new TextView(this);
                tv.setLayoutParams(tlp);

                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(14);
                tv.setLines(1);
                tv.setPadding(1, 1, 1, 1);

                tv.setText(cus.getString(j));

                row.addView(tv);

                //--- BackgroundColor
                if (i % 2 != 0) {
                    tv.setBackgroundColor(Color.parseColor("#CFF6FA"));
                } else {
                    tv.setBackgroundColor(Color.WHITE);
                }

                //--- comma collect
                if (j == 4) {
                    int rcollect = cus.getInt(4);
                    String collect_Format = formatPay.format(rcollect);
                    tv.setText(collect_Format);
                }
                //--- comma tax
                if (j == 5) {
                    int rtax = cus.getInt(5);
                    String tax_Format = formatPay.format(rtax);
                    tv.setText(tax_Format);
                }

                final int p = i;
                final int r = i + 1;

                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rows == 0) {
                            return;
                        } else if (cus.moveToPosition(p)) {

                            Intent positionIntent = new Intent(getApplicationContext(), IncomeEdit.class);
                            //Income Table rowid
                            positionIntent.putExtra("id", cus.getInt(0));
                            //Income Table Incomeid
                            positionIntent.putExtra("iid", cus.getString(1));
                            //Income Table Date
                            positionIntent.putExtra("date", cus.getString(2));
                            //Income Table Team Leader
                            positionIntent.putExtra("leader", cus.getString(3));
                            //Income Table Collect
                            positionIntent.putExtra("collect", cus.getInt(4));
                            //Income Table Tax
                            positionIntent.putExtra("tax", cus.getInt(5));
                            //Income Table Cost
                            positionIntent.putExtra("memo", cus.getInt(6));
                            //Income Table Memo
                            positionIntent.putExtra("tid", cus.getString(7));

                            String leader = positionIntent.getExtras().getString("leader");
                            Toast.makeText(IncomeTable.this, "선택 팀 :" + leader,
                                    Toast.LENGTH_SHORT).show();

                            startActivity(positionIntent);
                            finish();
                        }
                        cus.close();
                    }
                });

            }
            cus.moveToNext();
            income_table.addView(row);
        }
        incomeControler.close();

    }

    //------- Data Load Table ------
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void searchDateLoad() {
        income_table.removeAllViews();
        //Table Row
        TableRow row0 = new TableRow(this);
        row0.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);

        tlp.setMargins(1, 0, 1, 0);

        //TextView Income Table Row Id
        TextView tv0 = new TextView(this);
        tv0.setLayoutParams(tlp);

        tv0.setBackgroundColor(Color.BLUE);
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.CENTER);
        tv0.setTextSize(18);
        tv0.setPadding(1, 1, 1, 1);

        tv0.setText("ID");

        row0.addView(tv0);

        //TextView Income Table income ID
        TextView tv1 = new TextView(this);
        tv1.setLayoutParams(tlp);

        tv1.setBackgroundColor(Color.BLUE);
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextSize(18);
        tv1.setPadding(1, 1, 1, 1);

        tv1.setText(" i_id ");

        row0.addView(tv1);

        //TextView Income Table Date
        TextView tv2 = new TextView(this);
        tv2.setLayoutParams(tlp);

        tv2.setBackgroundColor(Color.BLUE);
        tv2.setTextColor(Color.WHITE);
        tv2.setGravity(Gravity.CENTER);
        tv2.setTextSize(18);
        tv2.setPadding(1, 1, 1, 1);

        tv2.setText(" 일 자 ");

        row0.addView(tv2);

        //TextView Income Table Leader Name
        TextView tv3 = new TextView(this);
        tv3.setLayoutParams(tlp);

        tv3.setBackgroundColor(Color.BLUE);
        tv3.setTextColor(Color.WHITE);
        tv3.setGravity(Gravity.CENTER);
        tv3.setTextSize(18);
        tv3.setPadding(1, 1, 1, 1);

        tv3.setText(" 팀 이름 ");

        row0.addView(tv3);

        //TextView Income Table Collect
        TextView tv4 = new TextView(this);
        tv4.setLayoutParams(tlp);

        tv4.setBackgroundColor(Color.BLUE);
        tv4.setTextColor(Color.WHITE);
        tv4.setGravity(Gravity.CENTER);
        tv4.setTextSize(18);
        tv4.setPadding(1, 1, 1, 1);

        tv4.setText(" 입 금 ");//수입금액 입금액

        row0.addView(tv4);

        //TextView Income Table Tax
        TextView tv5 = new TextView(this);
        tv5.setLayoutParams(tlp);

        tv5.setBackgroundColor(Color.BLUE);
        tv5.setTextColor(Color.WHITE);
        tv5.setGravity(Gravity.CENTER);
        tv5.setTextSize(18);
        tv5.setPadding(1, 1, 1, 1);

        tv5.setText(" 세 금 ");//세금공제

        row0.addView(tv5);

        //TextView Income Table Cost
        TextView tv6 = new TextView(this);
        tv6.setLayoutParams(tlp);

        tv6.setBackgroundColor(Color.BLUE);
        tv6.setTextColor(Color.WHITE);
        tv6.setGravity(Gravity.CENTER);
        tv6.setTextSize(18);
        tv6.setPadding(1, 1, 1, 1);

        tv6.setText(" 메 모 ");//경비

        row0.addView(tv6);

        //TextView Income Table Memo
        TextView tv7 = new TextView(this);
        tv7.setLayoutParams(tlp);

        tv7.setBackgroundColor(Color.BLUE);
        tv7.setTextColor(Color.WHITE);
        tv7.setGravity(Gravity.CENTER);
        tv7.setTextSize(18);
        tv7.setPadding(1, 1, 1, 1);

        tv7.setText(" t_id ");

        row0.addView(tv7);

        //TableLayout View
        income_table.addView(row0);

        String sDate = editText_StartDate.getText().toString();
        String eDate = editText_EndDate.getText().toString();
        incomeControler.open();
        final Cursor cus = incomeControler.selectDateLoad(sDate, eDate);
        final int rows = cus.getCount();
        final int clums = cus.getColumnCount();

        cus.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            final TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 0; j < clums; j++) {

                TextView tv = new TextView(this);
                tv.setLayoutParams(tlp);

                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(14);
                tv.setPadding(1, 1, 1, 1);

                tv.setText(cus.getString(j));

                row.addView(tv);

                //--- TableRow TextView BackgroundColor
                if (i % 2 != 0) {
                    tv.setBackgroundColor(Color.parseColor("#CFF6FA"));
                } else {
                    tv.setBackgroundColor(Color.WHITE);
                }

                //--- comma collect
                if (j == 4) {
                    int rcollect = cus.getInt(4);
                    String collect_Format = formatPay.format(rcollect);
                    tv.setText(collect_Format);
                }
                //--- comma tax
                if (j == 5) {
                    int rtax = cus.getInt(5);
                    String tax_Format = formatPay.format(rtax);
                    tv.setText(tax_Format);
                }

                final int p = i;
                final int r = i + 1;

                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(IncomeTable.this, "Click Data row ID :" + r,
                                Toast.LENGTH_SHORT).show();

                        if (rows == 0) {
                            return;
                        } else if (cus.moveToPosition(p)) {

                            Intent positionIntent = new Intent(getApplicationContext(), IncomeEdit.class);
                            //Income Table rowid
                            positionIntent.putExtra("id", cus.getInt(0));
                            //Income Table Incomeid
                            positionIntent.putExtra("iid", cus.getString(1));
                            //Income Table Date
                            positionIntent.putExtra("date", cus.getString(2));
                            //Income Table Team Leader
                            positionIntent.putExtra("leader", cus.getString(3));
                            //Income Table Collect
                            positionIntent.putExtra("collect", cus.getInt(4));
                            //Income Table Tax
                            positionIntent.putExtra("tax", cus.getInt(5));
                            //Income Table Cost
                            positionIntent.putExtra("memo", cus.getString(6));
                            //Income Table Memo
                            positionIntent.putExtra("tid", cus.getString(7));

                            String leader = positionIntent.getExtras().getString("leader");
                            Toast.makeText(IncomeTable.this, "선택 팀 :" + leader,
                                    Toast.LENGTH_SHORT).show();

                            startActivity(positionIntent);
                            finish();
                        }
                        cus.close();
                    }
                });

            }
            cus.moveToNext();
            income_table.addView(row);
        }
        incomeControler.close();

    }

    //------Income startday and endday total(days) sum(amount) sum(collect) sum(tax) ----------------
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint({"Recycle", "SetTextI18n"})
    private void sumSearchDate() {
        //-- search
        String stDate = editText_StartDate.getText().toString();
        String enDate = editText_EndDate.getText().toString();

        incomeControler.open();
        Cursor curinc = incomeControler.sumDateSearch(stDate, enDate);

        if (curinc.getString(curinc.getColumnIndex("ione")) != null) {
            float day = curinc.getFloat(0);
            String one_format = formatDouble.format(day);
            textView_day.setText(one_format + " 일 ");//--> oneDay
        } else {
            textView_day.setText("0 일");
        }

        if (curinc.getString(curinc.getColumnIndex("iamount")) != null) {
            int amounts = curinc.getInt(1);
            String amt_format = formatPay.format(amounts);
            textView_amount.setText(amt_format + " 원 ");//--> amount
        } else {
            textView_amount.setText("0 원 ");//--> amount
        }

        if (curinc.getString(curinc.getColumnIndex("icollect")) != null) {
            int collects = curinc.getInt(2);
            String col_format = formatPay.format(collects);
            textView_Collect.setText(col_format + " 원 ");//--> collect
        } else {
            textView_Collect.setText("0 원");
        }

        if (curinc.getString(curinc.getColumnIndex("itax")) != null) {
            int taxs = curinc.getInt(3);
            String ta_format = formatPay.format(taxs);
            textView_Tax.setText(ta_format + " 원 ");//--> tax
        } else {
            textView_Tax.setText("0 원");
        }

        if (curinc.getString(curinc.getColumnIndex("balance")) != null) {
            int balance = curinc.getInt(4);
            String bal_format = formatPay.format(balance);
            textView_balance.setText(bal_format + " 원 ");//--> balance

            if (balance < 0) {
                textView_balance.setTextColor(Color.parseColor("#BA0513"));
                textView_balance.setText(bal_format.replace("-", "") + "원 ");

            } else {
                textView_balance.setTextColor(Color.parseColor("#075797"));
                textView_balance.setText(bal_format + " 원 ");
            }
        } else {
            textView_balance.setText("0 원");
        }

        if (curinc.getString(curinc.getColumnIndex("balance_day")) != null) {
            float bday = curinc.getFloat(5);
            String bday_format = formatDouble.format(bday);
            textView_BalanceDay.setText(bday_format + " 일 ");//--> day balance
        } else {
            textView_BalanceDay.setText("0 일 ");//--> day balance
        }

        curinc.close();
        incomeControler.close();

    }

    @SuppressLint("SetTextI18n")
    private void sumDateJournal(){
        //-- search
        String stDate = editText_StartDate.getText().toString().trim();
        String enDate = editText_EndDate.getText().toString().trim();
        String search = editText_search.getText().toString().trim();

        incomeControler.open();
        Cursor curinc = incomeControler.sumDateSearchJournal(stDate, enDate,search);

        if (curinc.getString(curinc.getColumnIndex("ione")) != null) {
            float day = curinc.getFloat(0);
            String one_format = formatDouble.format(day);
            textView_day.setText(one_format + " 일 ");//--> oneDay
        } else {
            textView_day.setText("0 일");
        }

        if (curinc.getString(curinc.getColumnIndex("iamount")) != null) {
            int amounts = curinc.getInt(1);
            String amt_format = formatPay.format(amounts);
            textView_amount.setText(amt_format + " 원 ");//--> amount
        } else {
            textView_amount.setText("0 원 ");//--> amount
        }

        incomeControler.close();
        curinc.close();
    }

    @SuppressLint("SetTextI18n")
    private void sumDateIncome(){
        //-- search
        String stDate = editText_StartDate.getText().toString().trim();
        String enDate = editText_EndDate.getText().toString().trim();
        String search = editText_search.getText().toString().trim();

        incomeControler.open();
        Cursor curinc = incomeControler.sumDateSearchIncome(stDate, enDate,search);

        if (curinc.getString(curinc.getColumnIndex("icollect")) != null) {
            int collects = curinc.getInt(2);
            String col_format = formatPay.format(collects);
            textView_Collect.setText(col_format + " 원 ");//--> collect
        } else {
            textView_Collect.setText("0 원");
        }

        if (curinc.getString(curinc.getColumnIndex("itax")) != null) {
            int taxs = curinc.getInt(3);
            String ta_format = formatPay.format(taxs);
            textView_Tax.setText(ta_format + " 원 ");//--> tax
        } else {
            textView_Tax.setText("0 원");
        }

        if (curinc.getString(curinc.getColumnIndex("balance")) != null) {
            int balance = curinc.getInt(4);
            String bal_format = formatPay.format(balance);
            textView_balance.setText(bal_format + " 원 ");//--> balance
            if (balance < 0) {
                textView_balance.setTextColor(Color.parseColor("#BA0513"));
                textView_balance.setText(bal_format.replace("-", "") + "원 ");

            } else {
                textView_balance.setTextColor(Color.parseColor("#075797"));
                textView_balance.setText(bal_format + " 원 ");
            }
        } else {
            textView_balance.setText("0 원");
        }

        if (curinc.getString(curinc.getColumnIndex("balance_day")) != null) {
            float b_day = curinc.getFloat(5);
            String bald_format = formatDouble.format(b_day);
            textView_BalanceDay.setText(bald_format + " 일 ");//--> day balance
        } else {
            textView_BalanceDay.setText("0 일 ");//--> day balance
        }

        incomeControler.close();
        curinc.close();
    }

    //------ Date Selected Search -----------------------------------------------------
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private void searchTeamDateLoad() {
        income_table.removeAllViews();
        //Table Row
        TableRow row0 = new TableRow(this);
        row0.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);

        tlp.setMargins(1, 0, 1, 0);

        //TextView Income Table Row Id
        TextView tv0 = new TextView(this);
        tv0.setLayoutParams(tlp);

        tv0.setBackgroundColor(Color.BLUE);
        tv0.setTextColor(Color.WHITE);
        tv0.setGravity(Gravity.CENTER);
        tv0.setTextSize(18);
        tv0.setPadding(1, 1, 1, 1);

        tv0.setText("ID");

        row0.addView(tv0);

        //TextView Income Table income ID
        TextView tv1 = new TextView(this);
        tv1.setLayoutParams(tlp);

        tv1.setBackgroundColor(Color.BLUE);
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextSize(18);
        tv1.setPadding(1, 1, 1, 1);

        tv1.setText(" i_id ");

        row0.addView(tv1);

        //TextView Income Table Date
        TextView tv2 = new TextView(this);
        tv2.setLayoutParams(tlp);

        tv2.setBackgroundColor(Color.BLUE);
        tv2.setTextColor(Color.WHITE);
        tv2.setGravity(Gravity.CENTER);
        tv2.setTextSize(18);
        tv2.setPadding(1, 1, 1, 1);

        tv2.setText(" 일 자 ");

        row0.addView(tv2);

        //TextView Income Table Leader Name
        TextView tv3 = new TextView(this);
        tv3.setLayoutParams(tlp);

        tv3.setBackgroundColor(Color.BLUE);
        tv3.setTextColor(Color.WHITE);
        tv3.setGravity(Gravity.CENTER);
        tv3.setTextSize(18);
        tv3.setPadding(1, 1, 1, 1);

        tv3.setText(" 팀 이름 ");

        row0.addView(tv3);

        //TextView Income Table Collect
        TextView tv4 = new TextView(this);
        tv4.setLayoutParams(tlp);

        tv4.setBackgroundColor(Color.BLUE);
        tv4.setTextColor(Color.WHITE);
        tv4.setGravity(Gravity.CENTER);
        tv4.setTextSize(18);
        tv4.setPadding(1, 1, 1, 1);

        tv4.setText(" 입 금 ");//수입금액 입금액

        row0.addView(tv4);

        //TextView Income Table Tax
        TextView tv5 = new TextView(this);
        tv5.setLayoutParams(tlp);

        tv5.setBackgroundColor(Color.BLUE);
        tv5.setTextColor(Color.WHITE);
        tv5.setGravity(Gravity.CENTER);
        tv5.setTextSize(18);
        tv5.setPadding(1, 1, 1, 1);

        tv5.setText(" 세 금 ");//세금공제

        row0.addView(tv5);

        //TextView Income Table Cost
        TextView tv6 = new TextView(this);
        tv6.setLayoutParams(tlp);

        tv6.setBackgroundColor(Color.BLUE);
        tv6.setTextColor(Color.WHITE);
        tv6.setGravity(Gravity.CENTER);
        tv6.setTextSize(18);
        tv6.setPadding(1, 1, 1, 1);

        tv6.setText(" 메 모 ");//경비

        row0.addView(tv6);

        //TextView Income Table Memo
        TextView tv7 = new TextView(this);
        tv7.setLayoutParams(tlp);

        tv7.setBackgroundColor(Color.BLUE);
        tv7.setTextColor(Color.WHITE);
        tv7.setGravity(Gravity.CENTER);
        tv7.setTextSize(18);
        tv7.setPadding(1, 1, 1, 1);

        tv7.setText(" t_id ");

        row0.addView(tv7);

        //TableLayout View
        income_table.addView(row0);


        //Data Load
        String search = editText_search.getText().toString();
        String sDate = editText_StartDate.getText().toString();
        String eDate = editText_EndDate.getText().toString();

        incomeControler.open();
        final Cursor cus = incomeControler.selectTeamDate(search, sDate, eDate);
        final int rows = cus.getCount();
        final int clums = cus.getColumnCount();

        cus.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            final TableRow row = new TableRow(this);
            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            // inner for loop
            for (int j = 0; j < clums; j++) {

                TextView tv = new TextView(this);
                tv.setLayoutParams(tlp);

                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(14);
                tv.setPadding(1, 1, 1, 1);

                tv.setText(cus.getString(j));

                row.addView(tv);

                //--- TableRow TextView BackgroundColor
                if (i % 2 != 0) {
                    tv.setBackgroundColor(Color.parseColor("#CFF6FA"));
                } else {
                    tv.setBackgroundColor(Color.WHITE);
                }

                //--- comma collect
                if (j == 4) {
                    int rcollect = cus.getInt(4);
                    String collect_Format = formatPay.format(rcollect);
                    tv.setText(collect_Format);
                }
                //--- comma tax
                if (j == 5) {
                    int rtax = cus.getInt(5);
                    String tax_Format = formatPay.format(rtax);
                    tv.setText(tax_Format);
                }

                final int p = i;
                final int r = i + 1;

                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rows == 0) {
                            return;
                        } else if (cus.moveToPosition(p)) {

                            Intent positionIntent = new Intent(getApplicationContext(), IncomeEdit.class);
                            //Income Table rowid
                            positionIntent.putExtra("id", cus.getInt(0));
                            //Income Table Incomeid
                            positionIntent.putExtra("iid", cus.getString(1));
                            //Income Table Date
                            positionIntent.putExtra("date", cus.getString(2));
                            //Income Table Team Leader
                            positionIntent.putExtra("leader", cus.getString(3));
                            //Income Table Collect
                            positionIntent.putExtra("collect", cus.getInt(4));
                            //Income Table Tax
                            positionIntent.putExtra("tax", cus.getInt(5));
                            //Income Table Cost
                            positionIntent.putExtra("memo", cus.getString(6));
                            //Income Table Memo
                            positionIntent.putExtra("tid", cus.getString(7));

                            String leader = positionIntent.getExtras().getString("leader");
                            Toast.makeText(IncomeTable.this, "선택 팀 :" + leader,
                                    Toast.LENGTH_SHORT).show();

                            startActivity(positionIntent);
                            finish();
                        }
                        cus.close();
                    }
                });

            }
            cus.moveToNext();
            income_table.addView(row);
        }
        incomeControler.close();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint({"Recycle", "SetTextI18n"})
    private void sumSearchTeamDate() {
        //-- search
        String search = editText_search.getText().toString().trim();
        String stDate = editText_StartDate.getText().toString().trim();
        String enDate = editText_EndDate.getText().toString().trim();

        incomeControler.open();
        final Cursor ctdsi = incomeControler.sumTeamDateSearch(search, stDate, enDate);

        if (ctdsi.getString(ctdsi.getColumnIndex("ione")) == null) {
            textView_day.setText("0 일");
        } else {
            String day = ctdsi.getString(ctdsi.getColumnIndex("ione"));
            //String one_format = formatDouble.format(day);
            textView_day.setText(day + " 일 ");//--> oneDay
        }

        if (ctdsi.getString(ctdsi.getColumnIndex("iamount")) == null) {
            textView_amount.setText("0 원 ");//--> amount
        } else {
            int amounts = ctdsi.getInt(1);
            String amt_format = formatPay.format(amounts);
            textView_amount.setText(amt_format + " 원 ");//--> amount
        }

        if (ctdsi.getString(ctdsi.getColumnIndex("icollect")) == null) {
            textView_Collect.setText("0 원");
        } else {
            int collects = ctdsi.getInt(3);
            String col_format = formatPay.format(collects);
            textView_Collect.setText(col_format + " 원 ");//--> collect
        }

        if (ctdsi.getString(ctdsi.getColumnIndex("itax")) == null) {
            textView_Tax.setText("0 원");
        } else {
            int taxs = ctdsi.getInt(4);
            String ta_format = formatPay.format(taxs);
            textView_Tax.setText(ta_format + " 원 ");//--> tax
        }

        if (ctdsi.getString(ctdsi.getColumnIndex("balance")) == null) {
            textView_balance.setText("0 원");
        } else {
            int balance = ctdsi.getInt(5);
            String bal_format = formatPay.format(balance);
            textView_balance.setText(bal_format + " 원 ");//--> balance

            if (balance < 0) {
                textView_balance.setTextColor(Color.parseColor("#BA0513"));
                textView_balance.setText(bal_format.replace("-", "") + "원 ");

            } else {
                textView_balance.setTextColor(Color.parseColor("#075797"));
                textView_balance.setText(bal_format + " 원 ");
            }
        }

        if (ctdsi.getString(ctdsi.getColumnIndex("balance_day")) == null) {
            textView_BalanceDay.setText("0 일 ");//--> day balance
        } else {
            String daybal = ctdsi.getString(ctdsi.getColumnIndex("balance_day"));
            textView_BalanceDay.setText(daybal + " 일 ");//--> day balance
        }

        ctdsi.close();
        incomeControler.close();
    }

    //------- Income Table DateTime ---------------------------------------------------
    private void DateTime() {
        SQLiteDatabase db = teamDB.getReadableDatabase();

        Cursor csDate, cusDate = null;

        String startQuery = "select date('now', 'start of month', '-1 month', 'localtime')";
        String endQuery = "select date('now', 'start of month', '+1 month', '-1 day','localtime')";
        //String endQuery = "select datetime('now','localtime')";

        //----- 전월 -----
        /*
        Cursor cusdate, cuedate = null;

        //String startQuery = "select date('"+ stDate +"', 'start of month', 'localtime')";
        String startQuery = "select date('"+ stDate +"', 'start of month', '-1 month', 'localtime')";
        String endQuery = "select date('"+ enDate +"', 'start of month', '+1 month', '-1 day','localtime')";
        //String stmonthQuery = "select date( " + stDate + ", 'start of month', '-1 month', 'localtime')";

        cusdate = db.rawQuery( startQuery, null );
        cuedate = db.rawQuery( endQuery, null );

        if (cusdate.getCount() > 0 || cuedate.getCount() > 0) {
            cusdate.moveToFirst();
            cuedate.moveToFirst();
        }
        //Data Load
        String startday = cusdate.getString( 0 );
        String endday = cuedate.getString( 0 );
        */

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

    //------- Income Table Start DatePickerDialog ------------------------------------
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

    //------- Income Table End DatePickerDialog --------------------------------------
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
        getMenuInflater().inflate(R.menu.income_table_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.income_table_addImage_option:
                Toast.makeText(getApplicationContext(),
                        "일지 쓰기로 이동 합니다.", Toast.LENGTH_SHORT).show();
                Intent intent_incomeAddImage = new Intent(getApplicationContext(), IncomeAdd.class);
                startActivity(intent_incomeAddImage);
                finish();
                return true;

            case R.id.income_table_newImage_option:
                editText_search.setText("");
                textView_day.setText("");
                textView_amount.setText("");
                textView_balance.setText("");
                textView_Tax.setText("");
                textView_Collect.setText("");
                textView_BalanceDay.setText("");

                DateTime();

                return true;

            case R.id.income_table_close_option:
                Toast.makeText(getApplicationContext(),
                        "일지 리스트를 닫습니다.", Toast.LENGTH_SHORT).show();
                Intent income_table = new Intent(getApplicationContext(), IncomeList.class);
                startActivity(income_table);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}