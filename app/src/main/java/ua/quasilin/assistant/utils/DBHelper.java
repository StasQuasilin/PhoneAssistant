package ua.quasilin.assistant.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by szpt_user045 on 19.12.2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "hdb3";
    public static final String TABLE_NAME = "history";

    DBHelper(Context context){
        super(context, DB_NAME,null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +
                " (date long, type integer, number text primary key, data text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
