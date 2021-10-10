package com.sansany.theteams.Controller;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sansany.theteams.Contents.Sites;
import com.sansany.theteams.Contents.SitesContents;
import com.sansany.theteams.Contents.TeamsContents;
import com.sansany.theteams.Database.DatabaseTeams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SitesController {
    private DatabaseTeams teamDB;
    private final Context context;
    private SQLiteDatabase sqLiteDB;

    public SitesController(Context context) {
        this.context = context;
    }

    public SitesController open() throws SQLException {
        teamDB = new DatabaseTeams( context );
        sqLiteDB = teamDB.getWritableDatabase();
        return this;
    }

    public void close() {
        teamDB.close();
    }

    //☆☆☆☆☆☆☆☆☆☆☆☆☆☆
    //--- Site Table start ---
    public Cursor siteAutoId() {
        sqLiteDB = teamDB.getReadableDatabase();
        String autoId = " select " +
                SitesContents.SITE_ID + " from " +
                SitesContents.SITE_TABLE +
                " ORDER BY id DESC LIMIT 1";
        //Cursor csListId = sqLiteDB.rawQuery( autoId, null );
        Cursor csListId = sqLiteDB.rawQuery( autoId,null );
        csListId.moveToFirst();

        return csListId;
    }

    //----Sites add and update Site Result oneday and dailypay
    public Cursor teamSpinnerResult( String spinteam) {
        sqLiteDB = teamDB.getReadableDatabase();
        String siteItemResultQuery = "SELECT " + TeamsContents.TEAM_TID +
                " FROM " + TeamsContents.TEAM_TABLE +
                " where " + TeamsContents.TEAM_LEADER + " LIKE '%" + spinteam + "%'";

        Cursor cusAllSite = sqLiteDB.rawQuery( siteItemResultQuery, null );
        if (cusAllSite.moveToFirst()) {
            do {
                cusAllSite.getString( 0 );
            } while (cusAllSite.moveToNext());
        }
        cusAllSite.moveToFirst();
        sqLiteDB.close();
        return cusAllSite;
    }

    public void insertSite(String sid, String name, String leader, String pay, String manager, String date, String memo,String tid) {
        ContentValues siteValues = new ContentValues();
        siteValues.put( SitesContents.SITE_SID, sid );
        siteValues.put( SitesContents.SITE_NAME, name );
        siteValues.put( SitesContents.TEAM_LEADER, leader );
        siteValues.put( SitesContents.SITE_PAY, pay );
        siteValues.put( SitesContents.SITE_MANAGER, manager );
        siteValues.put( SitesContents.SITE_DATE, date );
        siteValues.put( SitesContents.SITE_MEMO, memo );
        siteValues.put( SitesContents.TEAM_ID, tid );

        sqLiteDB.insert( SitesContents.SITE_TABLE, null, siteValues );

        sqLiteDB.close();
    }

    public Cursor selectAllSite() {
        sqLiteDB = teamDB.getReadableDatabase();
        String selectAllSiteQuery = " SELECT * FROM " + SitesContents.SITE_TABLE +
                " ORDER BY id DESC limit 10";
        Cursor cusAllSite = sqLiteDB.rawQuery(selectAllSiteQuery, null );
        /*String[] allColumns = new String[]{SiteConstants.SITE_ID, SiteConstants.SITE_SID,
                SiteConstants.SITE_NAME, SiteConstants.SITE_LEADER,SiteConstants.SITE_PAY,
                SiteConstants.SITE_MANAGER,SiteConstants.SITE_DATE, SiteConstants.SITE_MEMO};

        Cursor cusAllSite = sqLiteDB.query( SiteConstants.SITE_TABLE, allColumns,
                null, null, null,
                null, SiteConstants.SITE_ID + " DESC " );*/

        if (cusAllSite != null){
            cusAllSite.moveToFirst();
        }
        sqLiteDB.close();
        return cusAllSite;
    }

    public Cursor searchSite(String search) {
        sqLiteDB = teamDB.getReadableDatabase();
        String selectQuery = "SELECT  " +
                SitesContents.SITE_ID + "," +
                SitesContents.SITE_SID + "," +
                SitesContents.SITE_NAME + "," +
                SitesContents.TEAM_LEADER + "," +
                SitesContents.SITE_PAY + "," +
                SitesContents.SITE_MANAGER + "," +
                SitesContents.SITE_DATE + "," +
                SitesContents.SITE_MEMO + "," +
                SitesContents.TEAM_ID +
                " FROM " + SitesContents.SITE_TABLE +
                " WHERE " + SitesContents.SITE_NAME +
                "  LIKE  '%" + search + "%' order by id desc ";

        Cursor cusSearch = sqLiteDB.rawQuery( selectQuery, null );
        if (cusSearch != null) {
            int r = cusSearch.getCount();
            for (int i = 0; i < r; i++) {
                cusSearch.moveToNext();
            }
        } else {
            cusSearch.close();
            return null;
        }
        return cusSearch;
    }

    public void updateSite(String id, String sid, String name, String leader, String pay, String manager, String date, String memo,String tid) {
        ContentValues siteValues = new ContentValues();
        siteValues.put(SitesContents.SITE_ID, id );
        siteValues.put( SitesContents.SITE_SID, sid );
        siteValues.put( SitesContents.SITE_NAME, name );
        siteValues.put( SitesContents.TEAM_LEADER, leader );
        siteValues.put( SitesContents.SITE_PAY, pay );
        siteValues.put( SitesContents.SITE_MANAGER, manager );
        siteValues.put( SitesContents.SITE_DATE, date );
        siteValues.put( SitesContents.SITE_MEMO, memo );
        siteValues.put( SitesContents.TEAM_ID, tid );

        sqLiteDB.update( SitesContents.SITE_TABLE, siteValues, "ID = ?", new String[]{id} );

        sqLiteDB.close();
    }

    public void deleteSite(String id) {
        sqLiteDB.delete( SitesContents.SITE_TABLE,
                SitesContents.SITE_ID + " = ?", new String[]{id} );

        sqLiteDB.close();
    }

    @SuppressLint("Recycle")
    public ArrayList<Sites> getAllSiteList(){
        // 읽기가 가능하게 DB 열기
        //database = dbHelper.getReadableDatabase();
        String id;
        String siteId;
        String siteName;
        String leader;
        String pay;
        String manager;
        String date;
        String memo;
        String tid;

        String[] allColumns = new String[] {
                SitesContents.SITE_ID,
                SitesContents.SITE_SID,
                SitesContents.SITE_NAME,
                SitesContents.TEAM_LEADER,
                SitesContents.SITE_PAY,
                SitesContents.SITE_MANAGER,
                SitesContents.SITE_DATE,
                SitesContents.SITE_MEMO,
                SitesContents.TEAM_ID};

        String[] orderby = new String[]{SitesContents.SITE_ID};
        int[] limit = new int[10];

        ArrayList<Sites> list = new ArrayList<>();

//        String listQuery = "select * from " + DatabaseHelper.TABLE_SITE;
//        Cursor cursor = sql.rawQuery(listQuery, null);
        Cursor cursor = sqLiteDB.query(SitesContents.SITE_TABLE, allColumns,
                null,
                null,
                null,
                null,
                Arrays.toString(orderby));
        if (cursor.moveToFirst()){
            do{
                id = cursor.getString(0);
                siteId = cursor.getString(1);
                siteName = cursor.getString(2);
                leader = cursor.getString(3);
                pay = cursor.getString(4);
                manager = cursor.getString(5);
                date = cursor.getString(6);
                memo = cursor.getString(7);
                tid = cursor.getString(8);

                Sites results = new Sites(id, siteId, siteName, leader, pay, manager, date, memo,tid);
                list.add(results);
            }while (cursor.moveToNext());

        }
        return list;
    }

    //--- Spinner ---
    //Spinner Team_Spinner
    public List<String> getAllSpinnerTeam() {
        List<String> listItem = new ArrayList<>();
        // Select All Query
        String spinnerTeamQuery = " SELECT " + TeamsContents.TEAM_LEADER +
                " FROM " + TeamsContents.TEAM_TABLE +
                " ORDER BY ID DESC limit 10";
        sqLiteDB = teamDB.getReadableDatabase();
        Cursor curSpinner = sqLiteDB.rawQuery( spinnerTeamQuery, null );
        // looping through all rows and adding to list
        if (curSpinner.moveToFirst()) {
            do {
                listItem.add( curSpinner.getString( 0 ) );
            } while (curSpinner.moveToNext());
        }
        // closing connection
        curSpinner.close();
        sqLiteDB.close();
        // returning ables
        return listItem;
    }
    //--- Site Table end ---
}
