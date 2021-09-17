package com.sansany.theteams.Contents;

public class JournalsContents {
    //--- Create Table Name Journal
    public static final String JOURNAL_TABLE= "journal_table";

    //--- Contacts Journal Columns Name
    public static final String JOURNAL_ID = "id";
    public static final String JOURNAL_JID = "j_id";
    public static final String JOURNAL_DATE = "j_date";
    public static final String SITE_NAME = "s_name";
    public static final String JOURNAL_ONE = "j_one";
    public static final String TEAM_LEADER = "t_leader";
    public static final String JOURNAL_MEMO = "j_memo";
    public static final String SITE_PAY = "s_pay";
    public static final String JOURNAL_AMOUNT = "j_amount";
    public static final String SITE_ID = "s_id";
    public static final String TEAM_ID = "t_id";

    //--- Create Table Journal
    public static final String CREATE_TABLE_JOURNAL = "CREATE TABLE "
            + JOURNAL_TABLE + "(" + JOURNAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + JOURNAL_JID + "  TEXT ," + JOURNAL_DATE + "  TEXT ,"
            + SITE_NAME + " TEXT ," + JOURNAL_ONE + "  INTEGER ,"
            + TEAM_LEADER + " TEXT ," + JOURNAL_MEMO + " TEXT ,"
            + SITE_PAY + " TEXT ," + JOURNAL_AMOUNT + " TEXT ,"
            + SITE_ID + " TEXT ," + TEAM_ID + " TEXT )";
}
