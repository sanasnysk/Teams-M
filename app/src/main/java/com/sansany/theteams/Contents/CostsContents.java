package com.sansany.theteams.Contents;

public class CostsContents {
    //--- Create Table Name Cost
    public static final String COST_TABLE = "cost_table";

    //--- Contacts Journal Columns Name
    public static final String COST_ID = "id";
    public static final String COST_IID = "c_id";
    public static final String COST_DATE = "c_date";
    public static final String SITE_NAME = "s_name";
    public static final String COST_CONTENTS = "c_contents";
    public static final String COST_AMOUNT = "c_amount";
    public static final String COST_MEMO = "c_memo";
    public static final String SITE_ID = "s_id";

    //--- Create Table Income
    public static final String CREATE_TABLE_COST = "CREATE TABLE " +
            COST_TABLE + "(" +
            COST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COST_IID + " TEXT," + COST_DATE + " TEXT," +
            SITE_NAME + " TEXT," + COST_CONTENTS + " TEXT," +
            COST_AMOUNT + " INTEGER," + COST_MEMO + " TEXT," +
            SITE_ID + " TEXT )";
}
