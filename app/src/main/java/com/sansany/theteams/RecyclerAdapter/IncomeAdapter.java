package com.sansany.theteams.RecyclerAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sansany.theteams.Incomes.IncomeAdd;
import com.sansany.theteams.Incomes.IncomeTable;

import java.text.DecimalFormat;

import sansany.theteams.R;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.ViewHolder> {
    private final Context context_m;
    private final Cursor cursor_m;
    private DecimalFormat formatPay, formatday;

    public IncomeAdapter( Context context, Cursor cursor ){
        this.context_m = context;
        cursor_m = cursor;
    }

    @NonNull
    @Override
    public IncomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.income_list_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        formatPay = new DecimalFormat("#,###");
        formatday = new DecimalFormat("#,###.#");
        if (!cursor_m.moveToPosition(position))
            return;

        String leader = cursor_m.getString(cursor_m.getColumnIndex("ileader"));
        holder.team_leader.setText(leader);

        if (cursor_m.getString(cursor_m.getColumnIndex("iones")) != null) {
            String days = cursor_m.getString(cursor_m.getColumnIndex("iones"));
            float fday = Float.parseFloat(days);
            String day_format = formatday.format(fday);
            holder.team_days.setText(day_format + " 일");
        }else {
            holder.team_days.setText("0 일");
        }

        if (cursor_m.getString(cursor_m.getColumnIndex("iamount")) != null) {
            String amount = cursor_m.getString(cursor_m.getColumnIndex("iamount"));
            int famount = Integer.parseInt(amount);
            String amount_format = formatPay.format(famount);
            holder.team_amount.setText(amount_format + " 원");
        }else {
            holder.team_amount.setText("0 원");
        }

        if (cursor_m.getString(cursor_m.getColumnIndex("icollect")) != null) {
            String collect = cursor_m.getString(cursor_m.getColumnIndex("icollect"));
            int fcollect = Integer.parseInt(collect);
            String collect_format = formatPay.format(fcollect);
            holder.team_collect.setText(collect_format + " 원");
        }else {
            holder.team_collect.setText("0 원");
        }

        if (cursor_m.getString(cursor_m.getColumnIndex("itax")) != null) {
            String tax = cursor_m.getString(cursor_m.getColumnIndex("itax"));
            int ftax = Integer.parseInt(tax);
            String tax_format = formatPay.format(ftax);
            holder.team_tax.setText(tax_format + " 원");
        }else {
            holder.team_tax.setText("0 원");
        }

        if (cursor_m.getString(cursor_m.getColumnIndex("balance")) != null) {
            String balance = cursor_m.getString(cursor_m.getColumnIndex("balance"));
            int fbalance = Integer.parseInt(balance);
            String balance_format = formatPay.format(fbalance);
            holder.team_balance_amount.setText(balance_format + " 원");
        }else {
            holder.team_balance_amount.setText("0 원");
        }

        if (cursor_m.getString(cursor_m.getColumnIndex("balance_day")) != null) {
            String balance_day = cursor_m.getString(cursor_m.getColumnIndex("balance_day"));
            float fbalance_day = Float.parseFloat(balance_day);
            String balanceday_format = formatday.format(fbalance_day);
            holder.team_balance_day.setText(balanceday_format + " 일");
        }else {
            holder.team_balance_day.setText("0 일");
        }

//        String cost = cursor_m.getString(cursor_m.getColumnIndex("costs"));
//        if (cursor_m.getString(cursor_m.getColumnIndex("costs")) != null) {
//            int fcost = Integer.parseInt(cost);
//            String cost_format = formatPay.format(fcost);
//            holder.site_cost.setText(cost_format);
//        }else {
//            holder.site_cost.setText("0");
//        }

        /*
        if (cursor_m.getString(cursor_m.getColumnIndex("collects")) != null) {
            String balance_day = cursor_m.getString(cursor_m.getColumnIndex("days"));
            holder.site_balance_day.setText(balance_day);
        }else {
            String balance_day = cursor_m.getString(cursor_m.getColumnIndex("oneDays"));
            holder.site_balance_day.setText(balance_day);
        }

        String site_balance = cursor_m.getString(cursor_m.getColumnIndex("balances"));
        if (cursor_m.getString(cursor_m.getColumnIndex("collects")) != null) {
            int balances = Integer.parseInt(site_balance);
            String balance_format = formatPay.format(balances);
            holder.site_balance_amount.setText(balance_format);
        }else {
            holder.site_balance_amount.setText("0");
        }
        */

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent siteTable = new Intent(v.getContext(), IncomeTable.class);
                siteTable.putExtra("leader", holder.team_leader.getText().toString());
                v.getContext().startActivity(siteTable);

                ((Activity) v.getContext()).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return (cursor_m.getCount());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView team_leader;
        TextView team_days;
        TextView team_amount;
        TextView team_collect;
        TextView team_tax;
        TextView team_balance_day;
        TextView team_balance_amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.team_leader = itemView.findViewById(R.id.income_team_name);
            this.team_days = itemView.findViewById(R.id.income_team_days);
            this.team_amount = itemView.findViewById(R.id.income_team_amount);
            this.team_collect = itemView.findViewById(R.id.income_team_collect);
            this.team_tax = itemView.findViewById(R.id.income_team_tax);
            this.team_balance_day = itemView.findViewById(R.id.income_team_balance_day);
            this.team_balance_amount = itemView.findViewById(R.id.income_team_balance_amount);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu( ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo ) {
            //다이얼로그 3. 메뉴 추가
            //편집 3. 컨텍스트 메뉴를 생성하고 메뉴 항목 선택시 호출되는 리스너를 등록해줍니다.
            // ID 1001, 1002로 어떤 메뉴를 선택했는지 리스너에서 구분하게 됩니다.
            MenuItem addMenu = menu.add(Menu.NONE, 1001, 1, "추가(ADD)");
            MenuItem tableMenu = menu.add(Menu.NONE, 1002, 2, "테이블 보기(TABLE SHOW)");

            addMenu.setOnMenuItemClickListener(onAddMenu);

            tableMenu.setOnMenuItemClickListener(onAddMenu);

        }

        private final MenuItem.OnMenuItemClickListener onAddMenu = new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick( MenuItem item ) {
                switch (item.getItemId()){
                    case 1001:
                        Intent addIntent = new Intent(itemView.getContext(), IncomeAdd.class);
                        itemView.getContext().startActivity(addIntent);

                        ((Activity)itemView.getContext()).finish();//kotlin 은 (context as Activity).finish();

                        break;

                    case 1002:
                        Intent tableIntent = new Intent(itemView.getContext(), IncomeTable.class);
                        tableIntent.putExtra("leader", team_leader.getText().toString());
                        itemView.getContext().startActivity(tableIntent);

                        ((Activity)itemView.getContext()).finish();//kotlin 은 (context as Activity).finish();

                        break;
                }
                return true;
            }

        };
    }
}
