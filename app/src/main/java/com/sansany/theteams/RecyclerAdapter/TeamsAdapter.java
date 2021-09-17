package com.sansany.theteams.RecyclerAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sansany.theteams.Contents.Teams;

import com.sansany.theteams.Teams.TeamsAdd;
import com.sansany.theteams.Teams.TeamsTable;

import java.util.ArrayList;

import sansany.theteams.R;

public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.ViewHolder> {
    private final ArrayList<Teams> mList;

    public TeamsAdapter(ArrayList<Teams> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public TeamsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.teams_list_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    public void onBindViewHolder(@NonNull TeamsAdapter.ViewHolder holder, int position) {
        holder.team_leader.setText(mList.get(position).getT_leader());
        holder.team_mobile.setText(mList.get(position).getT_mobile());
        holder.team_date.setText(mList.get(position).getT_date());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent teamTable = new Intent(v.getContext(), TeamsTable.class);
                teamTable.putExtra("leader", mList.get(position).getT_leader());
                v.getContext().startActivity(teamTable);

                ((Activity) v.getContext()).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size():0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView team_leader;
        TextView team_mobile;
        TextView team_date;

        public ViewHolder( @NonNull View itemView ) {
            super(itemView);
            this.team_leader = itemView.findViewById(R.id.team_leader_name);
            this.team_mobile = itemView.findViewById(R.id.team_mobile_no);
            this.team_date = itemView.findViewById(R.id.team_date_day);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //다이얼로그 3. 메뉴 추가
            //편집 3. 컨텍스트 메뉴를 생성하고 메뉴 항목 선택시 호출되는 리스너를 등록해줍니다.
            // ID 1001, 1002로 어떤 메뉴를 선택했는지 리스너에서 구분하게 됩니다.
            MenuItem addMenu = menu.add(Menu.NONE, 1001, 1, "ADD(추가)");
            MenuItem tableMenu = menu.add(Menu.NONE, 1002, 2, "TABLE(테이블 보기)");

            addMenu.setOnMenuItemClickListener(onAddMenu);

            tableMenu.setOnMenuItemClickListener(onAddMenu);

        }

        private final MenuItem.OnMenuItemClickListener onAddMenu = new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick( MenuItem item ) {
                switch (item.getItemId()){
                    case 1001:
                        Intent addIntent = new Intent(itemView.getContext(), TeamsAdd.class);
                        itemView.getContext().startActivity(addIntent);

                        ((Activity)itemView.getContext()).finish();//kotlin 은 (context as Activity).finish();

                        break;

                    case 1002:
                        Intent tableIntent = new Intent(itemView.getContext(), TeamsTable.class);
                        tableIntent.putExtra("name", team_leader.getText().toString());
                        itemView.getContext().startActivity(tableIntent);

                        ((Activity)itemView.getContext()).finish();//kotlin 은 (context as Activity).finish();

                        break;
                }
                return true;
            }

        };
    }

}
