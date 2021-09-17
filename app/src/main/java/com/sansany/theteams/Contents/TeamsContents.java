package com.sansany.theteams.Contents;

public class TeamsContents {
    //--- Create Table Name Team ---
    public static final String TEAM_TABLE = "team_table";

    //-- Contacts Team Columns Name
    public static final String TEAM_ID = "id";
    public static final String TEAM_TID = "t_id" ;
    public static final String TEAM_LEADER = "t_leader";
    public static final String TEAM_MOBILE = "t_mobile";
    public static final String TEAM_DATE = "t_date";
    public static final String TEAM_MEMO = "t_memo";

    //-- Create Table Tam
    public static final String CREATE_TABLE_TEAM = "CREATE TABLE " + TEAM_TABLE +
            "(" + TEAM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TEAM_TID + "  TEXT ," + TEAM_LEADER + "  TEXT ," + TEAM_MOBILE + " TEXT ,"
            + TEAM_DATE + "  TEXT ," + TEAM_MEMO + " TEXT)";
}
