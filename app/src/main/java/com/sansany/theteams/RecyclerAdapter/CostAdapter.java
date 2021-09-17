package com.sansany.theteams.RecyclerAdapter;

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

import com.sansany.theteams.Cost.CostAdd;
import com.sansany.theteams.Cost.CostTable;
import java.text.DecimalFormat;

import sansany.theteams.R;

public class CostAdapter extends RecyclerView.Adapter<CostAdapter.ViewHolder> {
    private final Context context_m;
    private final Cursor cursor_m;
    private DecimalFormat formatPay, formatday;

    public CostAdapter( Context context, Cursor cursor ){
        this.context_m = context;
        cursor_m = cursor;
    }

    @NonNull
    @Override
    public CostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cost_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CostAdapter.ViewHolder holder, int position) {
        formatday = new DecimalFormat("#,###.#");
        formatPay = new DecimalFormat("#,###");
        if (!cursor_m.moveToPosition(position))
            return;
        String site = cursor_m.getString(cursor_m.getColumnIndex("sites"));
        holder.site_name.setText(site);

        String tday = cursor_m.getString(cursor_m.getColumnIndex("Days"));
        float fday = Float.parseFloat(tday);
        String day_format = formatday.format(fday);
        holder.site_total_day.setText(day_format);

        String amount = cursor_m.getString(cursor_m.getColumnIndex("amounts"));
        if (cursor_m.getString(cursor_m.getColumnIndex("amounts")) != null){
            int famount = Integer.parseInt(amount);
            String amount_format = formatPay.format(famount);
            holder.site_amount.setText(amount_format);
        }else {
            holder.site_amount.setText("0");
        }


        String leader = cursor_m.getString(cursor_m.getColumnIndex("leaders"));
        holder.site_leader.setText(leader);

        String cost = cursor_m.getString(cursor_m.getColumnIndex("cost"));
        if (cursor_m.getString(cursor_m.getColumnIndex("cost")) != null) {
            int faCost = Integer.parseInt(cost);
            String fCost = formatPay.format(faCost);
            holder.site_cost.setText(fCost);
        }else {
            holder.site_cost.setText("0");
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent siteTable = new Intent(v.getContext(), CostTable.class);
                siteTable.putExtra("site", holder.site_name.getText().toString());
                v.getContext().startActivity(siteTable);

                ((Activity) v.getContext()).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
      return   (cursor_m.getCount());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView site_name;
        TextView site_total_day;
        TextView site_amount;
        TextView site_leader;
        TextView site_cost;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.site_name = itemView.findViewById(R.id.cost_site_name);
            this.site_total_day = itemView.findViewById(R.id.cost_site_totalday);
            this.site_amount = itemView.findViewById(R.id.cost_site_amount);
            this.site_leader = itemView.findViewById(R.id.cost_site_leader);
            this.site_cost = itemView.findViewById(R.id.cost_site_cost_list);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
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
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1001:
                        Intent addIntent = new Intent(itemView.getContext(), CostAdd.class);
                        itemView.getContext().startActivity(addIntent);

                        ((Activity) itemView.getContext()).finish();//kotlin 은 (context as Activity).finish();

                        break;

                    case 1002:
                        Intent tableIntent = new Intent(itemView.getContext(), CostTable.class);
                        tableIntent.putExtra("site_name", site_name.getText().toString());
                        itemView.getContext().startActivity(tableIntent);

                        ((Activity) itemView.getContext()).finish();//kotlin 은 (context as Activity).finish();

                        break;
                }
                return true;
            }
        };
    }
}
