package com.sansany.theteams.Contents;

public class SitesContents {
    //--- Create Table Name Site ---
    public static final String SITE_TABLE = "site_table";

    //-- Contacts Site Table Columns Name
    public static final String SITE_ID = "id";
    public static final String SITE_SID = "s_id" ;
    public static final String SITE_NAME = "s_Name";
    public static final String TEAM_LEADER = "t_leader";
    public static final String SITE_PAY = "s_pay";
    public static final String SITE_MANAGER = "s_manager";
    public static final String SITE_DATE = "s_date";
    public static final String SITE_MEMO = "s_memo";
    public static final String TEAM_ID = "t_id";

    //Create Table Site
    public static final String CREATE_TABLE_SITE = "CREATE TABLE "
            + SITE_TABLE + "(" + SITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + SITE_SID + "  TEXT ," + SITE_NAME + "  TEXT ," + TEAM_LEADER + " TEXT ,"
            + SITE_PAY + "  INTEGER ," + SITE_MANAGER + " TEXT,"
            + SITE_DATE + " TEXT," + SITE_MEMO + " TEXT ," + TEAM_ID + " TEXT )";
}
