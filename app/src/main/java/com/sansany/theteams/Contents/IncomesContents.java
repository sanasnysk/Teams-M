package com.sansany.theteams.Contents;

public class IncomesContents {
    //--- Create Table Name Income
    public static final String INCOME_TABLE = "income_table";

    //--- Contacts Journal Columns Name
    public static final String INCOME_ID = "id";
    public static final String INCOME_IID = "i_id";
    public static final String INCOME_DATE = "i_date";
    public static final String TEAM_LEADER = "t_leader";
    public static final String INCOME_COLLECT = "i_collect";
    public static final String INCOME_TAX = "i_tax";
    public static final String INCOME_MEMO = "i_memo";
    public static final String TEAM_ID = "t_id";

    //--- Create Table Income
    public static final String CREATE_TABLE_INCOME = "CREATE TABLE " +
            INCOME_TABLE + "(" +
            INCOME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            INCOME_IID + " TEXT," + INCOME_DATE + " TEXT," +
            TEAM_LEADER + " TEXT," + INCOME_COLLECT + " INTEGER," +
            INCOME_TAX + " INTEGER," + INCOME_MEMO + " TEXT," +
            TEAM_ID + " TEXT )";
}
