package com.sansany.theteams.Controler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sansany.theteams.Contents.IncomesContents;
import com.sansany.theteams.Contents.JournalsContents;
import com.sansany.theteams.Contents.SitesContents;
import com.sansany.theteams.Database.DatabaseTeams;

public class IncomeControler {
    private DatabaseTeams teamDB;
    private final Context context;
    private SQLiteDatabase sqLiteDB;

    public IncomeControler(Context context) {
        this.context = context;
    }

    public IncomeControler open() throws SQLException {
        teamDB = new DatabaseTeams(context);
        sqLiteDB = teamDB.getWritableDatabase();
        return this;
    }

    public void close() {
        teamDB.close();
    }

    //☆☆☆☆☆☆☆☆☆☆☆☆☆☆
    //---Income Table start---
    //--------------Table Income
    public void insertIncome(String iid, String date, String leader,
                             String collect, String tax, String memo, String tid) {
        ContentValues incomValues = new ContentValues();
        incomValues.put(IncomesContents.INCOME_IID, iid);
        incomValues.put(IncomesContents.INCOME_DATE, date);
        incomValues.put(IncomesContents.TEAM_LEADER, leader);
        incomValues.put(IncomesContents.INCOME_COLLECT, collect);
        incomValues.put(IncomesContents.INCOME_TAX, tax);
        incomValues.put(IncomesContents.INCOME_MEMO, memo);
        incomValues.put(IncomesContents.TEAM_ID, tid);

        sqLiteDB.insert(IncomesContents.INCOME_TABLE, null, incomValues);

        sqLiteDB.close();
    }

    public void updateIncome(String id, String iid, String date, String leader,
                             String collect, String tax, String memo, String tid) {
        ContentValues incomValues = new ContentValues();
        incomValues.put(IncomesContents.INCOME_ID, id);
        incomValues.put(IncomesContents.INCOME_IID, iid);
        incomValues.put(IncomesContents.INCOME_DATE, date);
        incomValues.put(IncomesContents.TEAM_LEADER, leader);
        incomValues.put(IncomesContents.INCOME_COLLECT, collect);
        incomValues.put(IncomesContents.INCOME_TAX, tax);
        incomValues.put(IncomesContents.INCOME_MEMO, memo);
        incomValues.put(IncomesContents.TEAM_ID, tid);

        sqLiteDB.update(IncomesContents.INCOME_TABLE, incomValues,
                "ID = ?", new String[]{id});

        sqLiteDB.close();
    }

    public void deleteIncome(String id) {
        sqLiteDB.delete(IncomesContents.INCOME_TABLE,
                IncomesContents.INCOME_ID + " = ?", new String[]{id});

        sqLiteDB.close();
    }

    public Cursor incomeAutoId() {
        sqLiteDB = teamDB.getReadableDatabase();
        String autoid = "select " +
                IncomesContents.INCOME_ID + " from " +
                IncomesContents.INCOME_TABLE +
                " ORDER BY id DESC LIMIT 1";
        Cursor csListId = sqLiteDB.rawQuery(autoid, null);
        csListId.moveToFirst();

        return csListId;
    }

    public Cursor incomeAllSelect() {
        sqLiteDB = teamDB.getReadableDatabase();
        String selectQuery = " SELECT * FROM " + IncomesContents.INCOME_TABLE +
                " ORDER BY id DESC ";
        Cursor cusSearch = sqLiteDB.rawQuery(selectQuery, null);
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

    public Cursor incomeSearchSelect(String search) {
        sqLiteDB = teamDB.getReadableDatabase();

        String selectQuery = " SELECT * FROM " + IncomesContents.INCOME_TABLE +
                " WHERE " + IncomesContents.TEAM_LEADER +
                "  LIKE  '%" + search + "%'" +
                " ORDER BY id DESC ";

        Cursor cusSearch = sqLiteDB.rawQuery(selectQuery, null);
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

    //--Income Search sum(collect) sum(tax) sum(cost) ---
    public Cursor sumSearchIncome(String search) {
        sqLiteDB = teamDB.getReadableDatabase();

        String selectDateQuery = "SELECT sum(" + IncomesContents.INCOME_COLLECT + ")," +
                " sum(" + IncomesContents.INCOME_TAX + "), " +
                "sum(" + IncomesContents.TEAM_ID + ") " +
                "FROM " + IncomesContents.INCOME_TABLE +
                " WHERE " + IncomesContents.TEAM_LEADER + "  LIKE  '%" + search +
                "%' ORDER BY id DESC ";

        Cursor cusSearch = sqLiteDB.rawQuery(selectDateQuery, null);
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

    public Cursor selectTeamDate(String site, String start, String end) {
        sqLiteDB = teamDB.getReadableDatabase();

        String selectQuery = " SELECT * FROM " + IncomesContents.INCOME_TABLE +
                " WHERE " + IncomesContents.TEAM_LEADER +
                " LIKE '%" + site + "%' AND " +
                IncomesContents.INCOME_DATE +
                " BETWEEN Date('" + start + "') AND Date('" + end + "') ORDER BY id DESC ";

        Cursor cusSearch = sqLiteDB.rawQuery(selectQuery, null);
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

    //--Income and startday and endday sum(collect) sum(tax) sum(cost) ----------------------
    public Cursor sumTeamDateSearch(String search, String start, String end) {
        sqLiteDB = teamDB.getReadableDatabase();
        String sumtdSearchQuery = "SELECT i." + IncomesContents.TEAM_LEADER +
                " as ileader,js.amounts as iamount,js.ones ione,sum(i." + IncomesContents.INCOME_COLLECT +
                ") as icollect,sum(i." + IncomesContents.INCOME_TAX +
                ") as itax,js.amounts-sum(" + IncomesContents.INCOME_COLLECT +
                ")-sum(" + IncomesContents.INCOME_TAX +
                ") as balance,ROUND((js.amounts-sum(" + IncomesContents.INCOME_COLLECT +
                ")-sum(" + IncomesContents.INCOME_TAX +
                "))/js.avgspay,1) as balance_day FROM " + IncomesContents.INCOME_TABLE +
                " as i LEFT JOIN (SELECT j." + JournalsContents.TEAM_ID +
                " as tid,total(j." + JournalsContents.JOURNAL_ONE +
                ") as ones,sum(j." + JournalsContents.JOURNAL_AMOUNT +
                ") as amounts,avg(j." + JournalsContents.SITE_PAY +
                ") as avgspay FROM " + JournalsContents.JOURNAL_TABLE +
                " as j LEFT JOIN " + SitesContents.SITE_TABLE +
                " as s ON j." + JournalsContents.SITE_ID +
                " = s." + SitesContents.SITE_SID +
                " WHERE j." + JournalsContents.TEAM_LEADER +
                " LIKE '%" + search + "%' AND j." + JournalsContents.JOURNAL_DATE +
                " BETWEEN date('" + start + "') AND date('" + end + "')) as js ON i." + IncomesContents.TEAM_ID +
                " = js.tid WHERE i." + IncomesContents.TEAM_LEADER +
                " LIKE '%" + search + "%' AND i." + IncomesContents.INCOME_DATE +
                " BETWEEN date('" + start + "') AND date('" + end + "') ";

        /*
        String selectTeDaQuery = "SELECT js.days as days,js.amount as amount,sum(i.i_collect) as collects,sum(i.i_tax) as taxs," +
                "(js.amount-sum(i.i_collect)-sum(i.i_tax)) as balance," +
                "round(((js.amount - sum(i.i_collect) - sum(i.i_tax))/js.pay),1) as dayBalance" +
                " FROM income_table as i" +
                " LEFT JOIN (SELECT j.t_id as tid,TOTAL(j.j_one) as days,round((TOTAL(j.j_one) * avg(s.s_pay)),1) as amount,s.s_pay as pay" +
                " FROM journal_table as j" +
                " LEFT JOIN site_table as s" +
                " ON j.t_id = s.t_id " +
                " WHERE j.t_leader LIKE '%" + search + "%'" +
                " AND j.j_date BETWEEN Date('" + start +"') AND Date('" + end + "')" +
                ") as js " +
                " ON i.t_id = js.tid" +
                " WHERE i.t_leader LIKE '%" + search + "%'" +
                " AND i.i_date BETWEEN Date('" + start +"') AND Date('" + end + "')";
        */

        //leader=0,iamount=1,ione=2,icollect=3,taxi=4,balance=5,balance_day=6

        Cursor cusSearch = sqLiteDB.rawQuery(sumtdSearchQuery, null);
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

    public Cursor selectDateLoad(String start, String end) {
        sqLiteDB = teamDB.getReadableDatabase();

        String selectDateQuery = "SELECT * FROM " + IncomesContents.INCOME_TABLE +
                " WHERE " + IncomesContents.INCOME_DATE +
                " BETWEEN Date('" + start + "') AND Date('" + end + "') ORDER BY id DESC ";

        Cursor cusSearch = sqLiteDB.rawQuery(selectDateQuery, null);
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

    //--startday and endday sum(collect) sum(tax) sum(cost) ------------------------------------
    public Cursor sumDateSearch(String start, String end) {
        sqLiteDB = teamDB.getReadableDatabase();

        String sumDateSearchQuery = "SELECT js.ones as ione,js.amounts as iamount,sum(i." + IncomesContents.INCOME_COLLECT +
                ") as icollect,sum(i." + IncomesContents.INCOME_TAX +
                ") as itax,js.amounts-sum(" + IncomesContents.INCOME_COLLECT +
                ")-sum(" + IncomesContents.INCOME_TAX +
                ") as balance,ROUND((js.amounts-sum(" + IncomesContents.INCOME_COLLECT +
                ")-sum(" + IncomesContents.INCOME_TAX +
                "))/js.avgspay,1) as balance_day FROM " + IncomesContents.INCOME_TABLE +
                " as i LEFT JOIN (SELECT j." + JournalsContents.TEAM_ID +
                " as tid,total(j." + JournalsContents.JOURNAL_ONE +
                ") as ones,sum(j." + JournalsContents.JOURNAL_AMOUNT +
                ") as amounts,avg(j." + JournalsContents.SITE_PAY +
                ") as avgspay FROM " + JournalsContents.JOURNAL_TABLE +
                " as j WHERE j." + JournalsContents.JOURNAL_DATE +
                " BETWEEN date('" + start + "') AND date('" + end +"')" +
                ") as js ON i." + IncomesContents.TEAM_ID +
                " = js.tid WHERE i." + IncomesContents.INCOME_DATE +
                " BETWEEN date('" + start + "') AND date('" + end + "')";


        String sumSearchDateQuery = "SELECT total(j." + JournalsContents.JOURNAL_ONE +
                ") as ione,sum(j." + JournalsContents.JOURNAL_AMOUNT +
                ") as iamount,i.collects as icollect,i.taxs as itax,sum(j." + JournalsContents.JOURNAL_ONE +
                ")-i.collects-i.taxs as balance,round((sum(j." + JournalsContents.JOURNAL_AMOUNT +
                ")-i.collects-i.taxs)/avg(j." + JournalsContents.SITE_PAY +
                "),1) as balance_day FROM " + JournalsContents.JOURNAL_TABLE +
                " as j LEFT JOIN (SELECT " + IncomesContents.TEAM_ID +
                " as tid,sum(" + IncomesContents.INCOME_COLLECT +
                ") as collects,sum(" + IncomesContents.INCOME_TAX +
                ") as taxs FROM " + IncomesContents.INCOME_TABLE +
                " WHERE " + IncomesContents.INCOME_DATE +
                " >= date('" + start + "') AND " + IncomesContents.INCOME_DATE +
                " <= date('" + end + "')" +
                ") as i ON i.tid = j." + JournalsContents.TEAM_ID +
                " WHERE j." + JournalsContents.JOURNAL_DATE +
                " >= date('" + start + "') AND j." + JournalsContents.JOURNAL_DATE +
                " <= date('" + end + "')";


        //ione=0,iamount=1,icollect=2,itax=3,balance=4,balance_day=5

        Cursor cusDate = sqLiteDB.rawQuery(sumSearchDateQuery, null);
        if (cusDate != null) {
            int r = cusDate.getCount();
            for (int i = 0; i < r; i++) {
                cusDate.moveToNext();
            }
        } else {
            cusDate.close();
            return null;
        }
        return cusDate;
    }

    public Cursor sumDateSearchJournal(String start, String end, String search){
        sqLiteDB = teamDB.getReadableDatabase();
        String journalDatesum = "SELECT total(" + JournalsContents.JOURNAL_ONE +
                ") as ione,sum(" + JournalsContents.JOURNAL_AMOUNT +
                ") as iamount FROM " + JournalsContents.JOURNAL_TABLE +
                " WHERE " + JournalsContents.JOURNAL_DATE +
                " BETWEEN date('" + start +"') AND date('" + end + "') AND " + JournalsContents.TEAM_LEADER +
                " LIKE '%" + search + "%' ";
        Cursor curjd = sqLiteDB.rawQuery(journalDatesum,null);
        if (curjd != null) {
            int r = curjd.getCount();
            for (int i = 0; i < r; i++) {
                curjd.moveToNext();
            }
        } else {
            curjd.close();
            return null;
        }

        return curjd;
    }

    public Cursor sumDateSearchIncome(String start, String end,String search){
        sqLiteDB = teamDB.getReadableDatabase();
        /*
        String incomeDatesum = "SELECT sum(" + IncomesContents.INCOME_COLLECT +
                ") as icollect,sum(" + IncomesContents.INCOME_TAX +
                ") as itax FROM " + IncomesContents.INCOME_TABLE +
                " WHERE " + IncomesContents.INCOME_DATE +
                " BETWEEN date('" + start + "') AND date('" + end + "') AND " + IncomesContents.TEAM_LEADER +
                " LIKE '%" + search + "%'";

        String sumTeamDateSearchQuery = "SELECT sum(i." + IncomesContents.INCOME_COLLECT +
                ") as icollect,sum(i." + IncomesContents.INCOME_TAX +
                ") as itax,js.amounts-sum(" + IncomesContents.INCOME_COLLECT +
                ")-sum(" + IncomesContents.INCOME_TAX +
                ") as balance,ROUND((js.amounts-sum(" + IncomesContents.INCOME_COLLECT +
                ")-sum(" + IncomesContents.INCOME_TAX +
                "))/js.avgspay,1) as balance_day FROM " + IncomesContents.INCOME_TABLE +
                " as i LEFT JOIN (SELECT " + JournalsContents.TEAM_ID +
                " as tid,sum(" + JournalsContents.JOURNAL_AMOUNT +
                ") as amounts,avg(" + JournalsContents.SITE_PAY +
                ") as avgspay FROM " + JournalsContents.JOURNAL_TABLE +
                " WHERE " + JournalsContents.TEAM_LEADER +
                " LIKE '%" + search + "%' AND " + JournalsContents.JOURNAL_DATE +
                " BETWEEN date('" + start + "') AND date('" + end + "')) as js ON i." + IncomesContents.TEAM_ID +
                " = js.tid WHERE i." + IncomesContents.TEAM_LEADER +
                " LIKE '%" + search + "%' AND i." + IncomesContents.INCOME_DATE +
                " BETWEEN date('" + start + "') AND date('" + end + "') ";
        */

        String sumSearchDateQuery = "SELECT total(j." + JournalsContents.JOURNAL_ONE +
                ") as ione,sum(j." + JournalsContents.JOURNAL_AMOUNT +
                ") as iamount,i.collects as icollect,i.taxs as itax,sum(j." + JournalsContents.JOURNAL_AMOUNT +
                ")-i.collects-i.taxs as balance,round((sum(j." + JournalsContents.JOURNAL_AMOUNT +
                ")-i.collects-i.taxs)/avg(j." + JournalsContents.SITE_PAY +
                "),1) as balance_day FROM " + JournalsContents.JOURNAL_TABLE +
                " as j LEFT JOIN (SELECT " + IncomesContents.TEAM_ID +
                " as tid,sum(" + IncomesContents.INCOME_COLLECT +
                ") as collects,sum(" + IncomesContents.INCOME_TAX +
                ") as taxs FROM " + IncomesContents.INCOME_TABLE +
                " WHERE " + IncomesContents.TEAM_LEADER +
                " LIKE '%" + search + "%' AND " + IncomesContents.INCOME_DATE +
                " BETWEEN date('" + start + "') AND date('" + end + "')) as i ON i.tid = j." + JournalsContents.TEAM_ID +
                " WHERE j." + JournalsContents.TEAM_LEADER +
                " LIKE '%" + search + "%' AND j." + JournalsContents.JOURNAL_DATE +
                " >= date('" + start + "') AND j." + JournalsContents.JOURNAL_DATE +
                " <= date('" + end + "')";

        Cursor curid = sqLiteDB.rawQuery(sumSearchDateQuery,null);
        if (curid != null) {
            int r = curid.getCount();
            for (int i = 0; i < r; i++) {
                curid.moveToNext();
            }
        } else {
            curid.close();
            return null;
        }

        return curid;
    }

    //--Income and startday and endday sum(collect) sum(tax) sum(cost) ----------------------
    public Cursor sumSiteIncome(String search) {
        sqLiteDB = teamDB.getReadableDatabase();

        String selectDateQuery = "SELECT sum(" + IncomesContents.INCOME_COLLECT + ")," +
                " sum(" + IncomesContents.INCOME_TAX + ") " +
                "FROM " + IncomesContents.INCOME_TABLE +
                " WHERE " + IncomesContents.TEAM_LEADER + "  LIKE  '%" + search +
                "%' ORDER BY id DESC ";

        Cursor cusSearch = sqLiteDB.rawQuery(selectDateQuery, null);
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

    //--- Income Total sum
    public Cursor sumTotalIncome() {
        sqLiteDB = teamDB.getReadableDatabase();

        String selectDateQuery = "SELECT sum(" + IncomesContents.INCOME_COLLECT + ")," +
                " sum(" + IncomesContents.INCOME_TAX + ") " +
                "FROM " + IncomesContents.INCOME_TABLE +
                " ORDER BY id DESC ";

        Cursor cusSearch = sqLiteDB.rawQuery(selectDateQuery, null);
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

    public Cursor incomeRecyclerViewListData(){
        sqLiteDB = teamDB.getReadableDatabase();
        String rcvListQuery = "SELECT i." + IncomesContents.TEAM_LEADER +
                " as ileader,js.amounts as iamount,sum(i." + IncomesContents.INCOME_COLLECT +
                ") as icollect,sum(i." + IncomesContents.INCOME_TAX +
                ") as itax,js.amounts-sum(" + IncomesContents.INCOME_COLLECT +
                ")-sum(" + IncomesContents.INCOME_TAX +
                ") as balance,round((js.amounts-sum(" + IncomesContents.INCOME_COLLECT +
                ")-sum(" + IncomesContents.INCOME_TAX +
                "))/js.avgspay,1) as balance_day,js.ones as iones FROM " + IncomesContents.INCOME_TABLE +
                " as i LEFT JOIN (SELECT j." + JournalsContents.TEAM_ID +
                " as tid,total(j." + JournalsContents.JOURNAL_ONE +
                ") as ones,sum(j." + JournalsContents.JOURNAL_AMOUNT +
                ") as amounts,avg(j." + JournalsContents.SITE_PAY +
                ") as avgspay FROM " + JournalsContents.JOURNAL_TABLE +
                " as j LEFT JOIN " + SitesContents.SITE_TABLE +
                " as s ON j." + JournalsContents.SITE_ID +
                " = s." + SitesContents.SITE_SID +
                " GROUP BY j." + JournalsContents.TEAM_ID +
                ") as js ON i." + IncomesContents.TEAM_ID +
                " = js.tid GROUP BY i." + IncomesContents.TEAM_ID +
                " ORDER BY i." + IncomesContents.INCOME_ID +" DESC ";

        Cursor curv = sqLiteDB.rawQuery(rcvListQuery, null);

        if (curv != null) {
            int r = curv.getCount();
            for (int i = 0; i < r; i++) {
                curv.moveToNext();
            }
        } else {
            curv.close();
            return null;
        }
        return curv;
    }

}
