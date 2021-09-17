package com.sansany.theteams.Controler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sansany.theteams.Contents.CostsContents;
import com.sansany.theteams.Contents.JournalsContents;
import com.sansany.theteams.Contents.SitesContents;
import com.sansany.theteams.Database.DatabaseTeams;

public class CostControler {
    private DatabaseTeams teamDB;
    private final Context context;
    private SQLiteDatabase sqLiteDB;

    public CostControler(Context context) {
        this.context = context;
    }

    public CostControler open() throws SQLException {
        teamDB = new DatabaseTeams(context);
        sqLiteDB = teamDB.getWritableDatabase();
        return this;
    }

    public void close() {
        teamDB.close();
    }

    //--- Insert Data Cost
    public void insertCost(String cid, String date, String site,
                              String contents, String amount, String memo, String sid) {
        ContentValues costValues = new ContentValues();
        costValues.put( CostsContents.COST_IID, cid );
        costValues.put( CostsContents.COST_DATE, date );
        costValues.put( CostsContents.SITE_NAME, site );
        costValues.put( CostsContents.COST_CONTENTS, contents );
        costValues.put( CostsContents.COST_AMOUNT, amount );
        costValues.put( CostsContents.COST_MEMO, memo );
        costValues.put( CostsContents.SITE_ID, sid );

        sqLiteDB.insert( CostsContents.COST_TABLE, null, costValues );

        sqLiteDB.close();
    }

    //--- Update Data Cost
    public void updateCost(String id, String cid, String date, String site,
                           String contents, String amount, String memo, String sid) {
        ContentValues costValues = new ContentValues();
        costValues.put(CostsContents.COST_ID, id);
        costValues.put( CostsContents.COST_IID, cid );
        costValues.put( CostsContents.COST_DATE, date );
        costValues.put( CostsContents.SITE_NAME, site );
        costValues.put( CostsContents.COST_CONTENTS, contents );
        costValues.put( CostsContents.COST_AMOUNT, amount );
        costValues.put( CostsContents.COST_MEMO, memo );
        costValues.put( CostsContents.SITE_ID, sid );

        sqLiteDB.update( CostsContents.COST_TABLE, costValues,
                "ID = ?", new String[]{id} );

        sqLiteDB.close();
    }

    //--- Delete Data Cost
    public void deleteCost(String id) {
        sqLiteDB.delete( CostsContents.COST_TABLE,
                CostsContents.COST_ID + " = ?", new String[]{id} );

        sqLiteDB.close();
    }

    //--- AutoId Cost
    public Cursor costAutoId() {
        sqLiteDB = teamDB.getReadableDatabase();
        String autoId = "select max(" +
                CostsContents.COST_ID + ") from " +
                CostsContents.COST_TABLE +
                " LIMIT 1";
        Cursor csListId = sqLiteDB.rawQuery( autoId, null );
        csListId.moveToFirst();

        return csListId;
    }

    //--- Select site and date Cost
    public Cursor searchSiteDate(String site,String stdate,String endate) {
        sqLiteDB = teamDB.getReadableDatabase();

        String selectQuery = " SELECT * FROM " + CostsContents.COST_TABLE +
                " WHERE " + CostsContents.SITE_NAME +
                " LIKE '%" + site + "%' AND " + CostsContents.COST_DATE +
                " BETWEEN date('" + stdate + "') AND date('" + endate + "')" +
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

    //--- Select site and date Cost sum
    public Cursor sumSearchSiteDate(String site,String stdate,String endate) {
        sqLiteDB = teamDB.getReadableDatabase();

        String selectQuery = " SELECT sum(" + CostsContents.COST_AMOUNT +
                ")as camount FROM " + CostsContents.COST_TABLE +
                " WHERE " + CostsContents.SITE_NAME +
                " LIKE '%" + site + "%' AND " + CostsContents.COST_DATE +
                " BETWEEN date('" + stdate + "') AND date('" + endate + "')";

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

    //--- Select date Data Cost
    public Cursor selectDateCost(String std,String endt) {
        sqLiteDB = teamDB.getReadableDatabase();

        String selectQuery = " SELECT * FROM " + CostsContents.COST_TABLE +
                " WHERE "  + CostsContents.COST_DATE +
                " BETWEEN date('" + std + "') AND date('" + endt + "')" +
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

    //--- Select date Data Cost sum
    public Cursor sumSelectDateCost(String std,String endt) {
        sqLiteDB = teamDB.getReadableDatabase();

        String selectQuery = " SELECT sum(" + CostsContents.COST_AMOUNT +
                ") as camount FROM " + CostsContents.COST_TABLE +
                " WHERE " + CostsContents.COST_DATE +
                " BETWEEN date('" + std + "') AND date('" + endt + "')";

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

    //--- site list startDay and endDay sum(oneDay) sum(amount)
    public Cursor sumSearchDateCost(String site, String start,String end) {
        sqLiteDB = teamDB.getReadableDatabase();

        String searchSumQuery = "SELECT sum(j." + CostsContents.COST_AMOUNT +
                " as days,Total(j." + JournalsContents.JOURNAL_ONE +
                ") as amount FROM " + JournalsContents.JOURNAL_TABLE +
                " WHERE " + CostsContents.SITE_NAME +
                " LIKE '%" + site + "%' AND " + CostsContents.COST_DATE +
                " BETWEEN date('" + start + "') AND date('" + end + "')";

        Cursor cusSearch = sqLiteDB.rawQuery(searchSumQuery, null);
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

    //--- Cost RecyclweView Data
    public Cursor recyclweViewData(){
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
