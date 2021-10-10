package com.sansany.theteams.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sansany.theteams.Contents.CostsContents;
import com.sansany.theteams.Contents.JournalsContents;
import com.sansany.theteams.Contents.SitesContents;
import com.sansany.theteams.Database.DatabaseTeams;

import java.util.ArrayList;
import java.util.List;

public class JournalController {
    private DatabaseTeams teamDB;
    private final Context context;
    private SQLiteDatabase sqLiteDB;

    public JournalController(Context context ) {
        this.context = context;
    }

    public JournalController open() throws SQLException {
        teamDB = new DatabaseTeams(context);
        sqLiteDB = teamDB.getWritableDatabase();
        return this;
    }

    public void close() {
        teamDB.close();
    }

    //--- Insert Data Journal
    public void insertJournal(String jId, String date, String site, String day,
                              String leader, String memo, String spay, String jamount, String sid, String tid) {
        ContentValues journalValues = new ContentValues();
        journalValues.put( JournalsContents.JOURNAL_JID, jId );
        journalValues.put( JournalsContents.JOURNAL_DATE, date );
        journalValues.put( JournalsContents.SITE_NAME, site );
        journalValues.put( JournalsContents.JOURNAL_ONE, day );
        journalValues.put( JournalsContents.TEAM_LEADER, leader );
        journalValues.put( JournalsContents.JOURNAL_MEMO, memo );
        journalValues.put( JournalsContents.SITE_PAY, spay );
        journalValues.put( JournalsContents.JOURNAL_AMOUNT, jamount );
        journalValues.put( JournalsContents.SITE_ID, sid );
        journalValues.put( JournalsContents.TEAM_ID, tid );

        sqLiteDB.insert( JournalsContents.JOURNAL_TABLE, null, journalValues );

        sqLiteDB.close();
    }

    //--- Update Data Journal
    public void updateJournalData(String id, String jId, String date, String site, String day,
                                  String leader, String memo, String spay, String jamount, String sid, String tid) {
        ContentValues journalValues = new ContentValues();
        journalValues.put( JournalsContents.JOURNAL_ID, id );
        journalValues.put( JournalsContents.JOURNAL_JID, jId );
        journalValues.put( JournalsContents.JOURNAL_DATE, date );
        journalValues.put( JournalsContents.SITE_NAME, site );
        journalValues.put( JournalsContents.JOURNAL_ONE, day );
        journalValues.put( JournalsContents.TEAM_LEADER, leader );
        journalValues.put( JournalsContents.JOURNAL_MEMO, memo );
        journalValues.put( JournalsContents.SITE_PAY, spay );
        journalValues.put( JournalsContents.JOURNAL_AMOUNT, jamount );
        journalValues.put( JournalsContents.SITE_ID, sid );
        journalValues.put( JournalsContents.TEAM_ID, tid );

        sqLiteDB.update( JournalsContents.JOURNAL_TABLE, journalValues,
                "ID = ?", new String[]{id} );

        sqLiteDB.close();
    }

    //--- Delete Data Journal
    public void deleteJournalData(String id) {
        sqLiteDB.delete( JournalsContents.JOURNAL_TABLE,
                JournalsContents.JOURNAL_ID + " = ?", new String[]{id} );

        sqLiteDB.close();
    }

    //--- AutoId Journal
    public Cursor journalAutoId() {
        sqLiteDB = teamDB.getReadableDatabase();
        String autoId = "select max(" +
                JournalsContents.JOURNAL_ID + ") from " +
                JournalsContents.JOURNAL_TABLE +
                " LIMIT 1";
        Cursor csListId = sqLiteDB.rawQuery( autoId, null );
        csListId.moveToFirst();

        return csListId;
    }

    //--- Select All Data Journal
    public Cursor selectAllJournal() {
        sqLiteDB = teamDB.getReadableDatabase();

        String selectQuery = " SELECT * FROM " + JournalsContents.JOURNAL_TABLE +
                " ORDER BY id DESC ";

        Cursor cusSearch = sqLiteDB.rawQuery( selectQuery, null );
        if (cusSearch != null) {
            int r = cusSearch.getCount();
            for (int i = 0; i < r; i++) {
                cusSearch.moveToFirst();
            }
        } else {
            cusSearch.close();
            return null;
        }
        return cusSearch;

    }

    //--- Search Site Journal
    public Cursor searchJournal(String search) {
        sqLiteDB = teamDB.getReadableDatabase();
        String selectQuery = " SELECT " +
                JournalsContents.JOURNAL_ID + "," +
                JournalsContents.JOURNAL_JID + "," +
                JournalsContents.JOURNAL_DATE + ", " +
                JournalsContents.SITE_NAME + "," +
                JournalsContents.JOURNAL_ONE + "," +
                JournalsContents.TEAM_LEADER + ", " +
                JournalsContents.JOURNAL_MEMO + "," +
                JournalsContents.SITE_PAY + "," +
                JournalsContents.JOURNAL_AMOUNT + "," +
                JournalsContents.SITE_ID + "," +
                JournalsContents.TEAM_ID +
                " FROM " + JournalsContents.JOURNAL_TABLE +
                " WHERE " + JournalsContents.SITE_NAME +
                "  LIKE  '%" + search + "%'" +
                "ORDER BY " + JournalsContents.JOURNAL_ID + " DESC ";

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

    //--- Search Site Journal
    public Cursor searchSiteJournal(String search, String start, String end) {
        sqLiteDB = teamDB.getReadableDatabase();
        String selectQuery = " SELECT * FROM " + JournalsContents.JOURNAL_TABLE +
                " WHERE " + JournalsContents.SITE_NAME +
                "  LIKE  '%" + search + "%' AND " + JournalsContents.JOURNAL_DATE +
                " BETWEEN Date('" + start + "') AND Date('" + end + "') " +
                "ORDER BY " + JournalsContents.JOURNAL_ID + " DESC ";

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

    //--- Site and startDay and endDay sum(oneDay) sum(amount)
    public Cursor sumSiteDateJournal(String start, String end, String search) {
        sqLiteDB = teamDB.getReadableDatabase();

        String searchDateQuery = "SELECT Total(j." + JournalsContents.JOURNAL_ONE +
                ") as days,Total(j." + JournalsContents.JOURNAL_ONE +
                ") * s." + SitesContents.SITE_PAY +
                " as amount,s." + SitesContents.SITE_PAY +
                " FROM " + JournalsContents.JOURNAL_TABLE +
                " as j LEFT JOIN " + SitesContents.SITE_TABLE +
                " as s ON j." + JournalsContents.SITE_ID +
                "=s." + SitesContents.SITE_SID +
                " WHERE s." + SitesContents.SITE_NAME +
                " LIKE '%" + search +
                "%' AND j." + JournalsContents.JOURNAL_DATE +
                " BETWEEN date('" + start + "') AND date('" + end +
                "')ORDER BY j." + JournalsContents.JOURNAL_ID + " DESC";

        Cursor cusSearch = sqLiteDB.rawQuery( searchDateQuery, null );
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

    //--- Search Date Journal
    public Cursor searchDateJournal(String start, String end) {
        sqLiteDB = teamDB.getReadableDatabase();

        String selectDateQuery = "SELECT * FROM " + JournalsContents.JOURNAL_TABLE +
                " WHERE j." + JournalsContents.JOURNAL_DATE +
                " BETWEEN Date('" + start + "') AND Date('" + end + "') " +
                "ORDER BY j." + JournalsContents.JOURNAL_ID + " DESC ";

        Cursor cusSearch = sqLiteDB.rawQuery( selectDateQuery, null );
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

    //--- startDay and endDay sum(oneDay) sum(amount)
    public Cursor searchDateJournalSum(String start, String end) {
        sqLiteDB = teamDB.getReadableDatabase();

        String dateSumQuery = "SELECT Total(" + JournalsContents.JOURNAL_ONE +
                " as days,sum(" + JournalsContents.JOURNAL_AMOUNT +
                ") as amount FROM " + JournalsContents.JOURNAL_TABLE +
                " WHERE j." + JournalsContents.JOURNAL_DATE +
                " BETWEEN date('" + start + "') AND date('" + end + "')";

        Cursor cusSearch = sqLiteDB.rawQuery(dateSumQuery, null);
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

    //--- Site Spinner Item Journal add & update Result oneDay & dailyPay
    public List<String> getAllSpinnerSite() {
        List<String> listItem = new ArrayList<>();
        //-- Select All Query
        String spinnerQuery = "SELECT * FROM site_table  ORDER BY ID DESC limit 10";
        sqLiteDB = teamDB.getReadableDatabase();
        Cursor curSpinner = sqLiteDB.rawQuery( spinnerQuery, null );

        //-- looping through all rows and adding to list
        if (curSpinner.moveToFirst()) {
            do {
                listItem.add( curSpinner.getString( 2 ) );
            } while (curSpinner.moveToNext());
        }

        //-- closing connection
        curSpinner.close();
        sqLiteDB.close();

        //-- returning Items
        return listItem;
    }

    //----Journal add and update Site Result oneday and dailypay
    public Cursor siteSpinnerResult( String spinSite) {
        sqLiteDB = teamDB.getReadableDatabase();
        String siteItemResultQuery = "SELECT " + SitesContents.TEAM_LEADER +
                ", " + SitesContents.SITE_PAY +
                ", " + SitesContents.SITE_SID +
                ", " +SitesContents.TEAM_ID +
                " FROM " + SitesContents.SITE_TABLE +
                " where " + SitesContents.SITE_NAME + " LIKE '%" + spinSite + "%'";

        Cursor cusAllSite = sqLiteDB.rawQuery( siteItemResultQuery, null );
        if (cusAllSite.moveToFirst()) {
            do {
                cusAllSite.getString( 0 );//leader
                cusAllSite.getString( 1 );//pay
                cusAllSite.getString(2);//sid
                cusAllSite.getString(3);//tid
            } while (cusAllSite.moveToNext());
        }
        cusAllSite.moveToFirst();
        sqLiteDB.close();
        return cusAllSite;
    }

    //--- Search Team Journal
    public Cursor searchTeamJournal(String search, String start, String end) {
        sqLiteDB = teamDB.getReadableDatabase();
        String selectQuery = " SELECT * FROM " + JournalsContents.JOURNAL_TABLE +
                " WHERE " + JournalsContents.TEAM_LEADER +
                "  LIKE  '%" + search + "%' AND " + JournalsContents.JOURNAL_DATE +
                " BETWEEN Date('" + start + "') AND Date('" + end + "') " +
                "ORDER BY " + JournalsContents.JOURNAL_ID + " DESC ";

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

    //--- Site and startDay and endDay sum(oneDay) sum(amount)
    public Cursor sumTeamDateJournal(String start, String end, String search) {
        sqLiteDB = teamDB.getReadableDatabase();

        String searchDateQuery = "SELECT Total(j." + JournalsContents.JOURNAL_ONE +
                ") as days,Total(j." + JournalsContents.JOURNAL_ONE +
                ") * s." + SitesContents.SITE_PAY +
                " as amount,s." + SitesContents.SITE_PAY +
                " FROM " + JournalsContents.JOURNAL_TABLE +
                " as j LEFT JOIN " + SitesContents.SITE_TABLE +
                " as s ON j." + JournalsContents.SITE_ID +
                "=s." + SitesContents.SITE_SID +
                " WHERE j." + JournalsContents.TEAM_LEADER +
                " LIKE '%" + search +
                "%' AND j." + JournalsContents.JOURNAL_DATE +
                " BETWEEN date('" + start + "') AND date('" + end +
                "')ORDER BY j." + JournalsContents.JOURNAL_ID + " DESC";

        Cursor cusSearch = sqLiteDB.rawQuery( searchDateQuery, null );
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

    //--- journal RecyclerView Adapter
    public Cursor recyclerViewData(){
        String listRecyclerQuery = "SELECT j." + JournalsContents.SITE_NAME +
                " as sites, TOTAL(j." + JournalsContents.JOURNAL_ONE +
                ") as Days, sum(j."+ JournalsContents.JOURNAL_AMOUNT +
                ") as amounts,j." + JournalsContents.TEAM_LEADER +
                " as leaders,cs.camount as cost FROM " + JournalsContents.JOURNAL_TABLE +
                " as j LEFT JOIN (SELECT c." + CostsContents.SITE_ID +
                " as sid,sum(" + CostsContents.COST_AMOUNT +
                ") as camount FROM " + CostsContents.COST_TABLE +
                " as c LEFT JOIN " + SitesContents.SITE_TABLE +
                " as s ON c." + CostsContents.SITE_ID +
                " = s." + SitesContents.SITE_SID +
                " GROUP BY c." + CostsContents.SITE_ID +
                ") as cs ON j." + JournalsContents.SITE_ID +
                " = cs.sid GROUP BY j." + JournalsContents.SITE_ID +
                " ORDER BY j." + CostsContents.COST_ID +
                " DESC limit 10";

        Cursor curRc = sqLiteDB.rawQuery(listRecyclerQuery, null);
        if (curRc != null) {
            int r = curRc.getCount();
            for (int i = 0; i < r; i++) {
                curRc.moveToNext();
            }
        } else {
            curRc.close();
            return null;
        }

        return curRc;
    }

}
