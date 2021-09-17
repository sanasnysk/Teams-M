package com.sansany.theteams.Controler;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sansany.theteams.Contents.Teams;
import com.sansany.theteams.Contents.TeamsContents;
import com.sansany.theteams.Database.DatabaseTeams;

import java.util.ArrayList;

public class TeamsControler {
    private DatabaseTeams teamDB;
    private final Context context;
    private SQLiteDatabase sqLiteDB;

    public TeamsControler(Context context) {
        this.context = context;
    }

    public TeamsControler open() throws SQLException {
        teamDB = new DatabaseTeams( context );
        sqLiteDB = teamDB.getWritableDatabase();
        return this;
    }

    public void close() {
        teamDB.close();
    }

    //☆☆☆☆☆☆☆☆☆☆☆☆☆☆
    //---Team Table start---
    public void insertTeam(String tid, String leader, String mobile, String date, String memo) {
        ContentValues teamValues = new ContentValues();
        teamValues.put( TeamsContents.TEAM_TID, tid );
        teamValues.put( TeamsContents.TEAM_LEADER, leader );
        teamValues.put( TeamsContents.TEAM_MOBILE, mobile );
        teamValues.put( TeamsContents.TEAM_DATE, date );
        teamValues.put( TeamsContents.TEAM_MEMO, memo );

        sqLiteDB.insert( TeamsContents.TEAM_TABLE, null, teamValues );

        sqLiteDB.close();
    }

    public Cursor teamAutoId() {
        sqLiteDB = teamDB.getReadableDatabase();
        String autoId = "select " +
                TeamsContents.TEAM_ID + " from " +
                TeamsContents.TEAM_TABLE +
                " ORDER BY id DESC LIMIT 1";
        Cursor csListId = sqLiteDB.rawQuery( autoId, null );
        csListId.moveToFirst();

        return csListId;
    }

    public Cursor selectAllTeam() {
        String[] allColumns = new String[]{TeamsContents.TEAM_ID, TeamsContents.TEAM_TID,
                TeamsContents.TEAM_LEADER, TeamsContents.TEAM_MOBILE,
                TeamsContents.TEAM_DATE, TeamsContents.TEAM_MEMO};

        Cursor cusTeam = sqLiteDB.query( TeamsContents.TEAM_TABLE, allColumns,
                null, null, null,
                null, TeamsContents.TEAM_ID + " DESC " );
        if (cusTeam != null) {
            cusTeam.moveToFirst();
        }
        sqLiteDB.close();

        return cusTeam;
    }

    public Cursor searchTeam(String search) {
        sqLiteDB = teamDB.getReadableDatabase();
        String selectQuery = "SELECT " +
                TeamsContents.TEAM_ID + "," +
                TeamsContents.TEAM_TID + "," +
                TeamsContents.TEAM_LEADER + "," +
                TeamsContents.TEAM_MOBILE + "," +
                TeamsContents.TEAM_DATE + "," +
                TeamsContents.TEAM_MEMO +
                " FROM " + TeamsContents.TEAM_TABLE +
                " WHERE " + TeamsContents.TEAM_LEADER +
                "  LIKE  '%" + search + "%' order by id desc";

        Cursor cusSearch = sqLiteDB.rawQuery( selectQuery, null );
        if (cusSearch != null) {
            int r = cusSearch.getCount();
            for (int i = 0; i < r; i++) {
                cusSearch.moveToNext();

            }
        } else {
            assert false;
            cusSearch.close();
            return null;
        }
        return cusSearch;
    }

    public void updateTeam(String id, String tid, String leader, String mobile, String date, String memo) {
        ContentValues teamValues = new ContentValues();
        teamValues.put( TeamsContents.TEAM_ID, id );
        teamValues.put( TeamsContents.TEAM_TID, tid );
        teamValues.put( TeamsContents.TEAM_LEADER, leader );
        teamValues.put( TeamsContents.TEAM_MOBILE, mobile );
        teamValues.put( TeamsContents.TEAM_DATE, date );
        teamValues.put( TeamsContents.TEAM_MEMO, memo );
        sqLiteDB.update( TeamsContents.TEAM_TABLE, teamValues,
                "ID = ?", new String[]{id} );

        sqLiteDB.close();
    }

    public void deleteTeam(String id) {
        sqLiteDB.delete( TeamsContents.TEAM_TABLE,
                TeamsContents.TEAM_ID + " = ?", new String[]{id} );

        sqLiteDB.close();
    }

    public ArrayList<Teams> getList(){
        // 읽기가 가능하게 DB 열기
        //database = dbHelper.getReadableDatabase();
        String id;
        String tid;
        String leader;
        String mobile;
        String date;
        String memo;

        String[] allColumns = new String[] {
                TeamsContents.TEAM_ID,
                TeamsContents.TEAM_TID,
                TeamsContents.TEAM_LEADER,
                TeamsContents.TEAM_MOBILE,
                TeamsContents.TEAM_DATE,
                TeamsContents.TEAM_MEMO };

        ArrayList<Teams> list = new ArrayList<>();

//        String listQuery = "select * from " + DatabaseHelper.TABLE_SITE;
//        Cursor cursor = sql.rawQuery(listQuery, null);
        @SuppressLint("Recycle")
        Cursor cursor = sqLiteDB.query(TeamsContents.TEAM_TABLE, allColumns,
                null,
                null,
                null,
                null,
                null);
        if (cursor.moveToFirst()){
            do{
                id = cursor.getString(0);
                tid = cursor.getString(1);
                leader = cursor.getString(2);
                mobile = cursor.getString(3);
                date = cursor.getString(4);
                memo = cursor.getString(5);

                Teams results = new Teams(id, tid, leader, mobile, date, memo);
                list.add(results);
            }while (cursor.moveToNext());

        }
        return list;
    }
    //--- Team Table end ---
}
