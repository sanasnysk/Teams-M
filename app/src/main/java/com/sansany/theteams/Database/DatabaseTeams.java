package com.sansany.theteams.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sansany.theteams.Contents.CostsContents;
import com.sansany.theteams.Contents.IncomesContents;
import com.sansany.theteams.Contents.JournalsContents;
import com.sansany.theteams.Contents.SitesContents;
import com.sansany.theteams.Contents.TeamsContents;

import java.io.File;

public class DatabaseTeams extends SQLiteOpenHelper {
    //Database Create
    public static String DATABASE_NAME = "teams_db";
    public static final int DATABASE_VERSION = 4;

    public DatabaseTeams(Context context) {
        super(context, context.getExternalFilesDir(DATABASE_NAME) +
                File.separator + "/database/" +
                File.separator + DATABASE_NAME, null, DATABASE_VERSION);
        //super(context, DATABASE_NAME, null, DATABASE_VERSION );
        this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TeamsContents.CREATE_TABLE_TEAM);
        db.execSQL(SitesContents.CREATE_TABLE_SITE);
        db.execSQL(JournalsContents.CREATE_TABLE_JOURNAL);
        db.execSQL(IncomesContents.CREATE_TABLE_INCOME);
        db.execSQL(CostsContents.CREATE_TABLE_COST);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS '" + TeamsContents.TEAM_TABLE + "'");
            db.execSQL("DROP TABLE IF EXISTS '" + SitesContents.SITE_TABLE + "'");
            db.execSQL("DROP TABLE IF EXISTS '" + JournalsContents.JOURNAL_TABLE + "'");
            db.execSQL("DROP TABLE IF EXISTS '" + IncomesContents.INCOME_TABLE + "'");
            db.execSQL("DROP TABLE IF EXISTS '" + CostsContents.COST_TABLE + "'");
        }
        onCreate(db);

    }
}
