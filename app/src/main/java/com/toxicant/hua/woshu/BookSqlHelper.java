package com.toxicant.hua.woshu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hua on 2016/3/7.
 */
public class BookSqlHelper extends SQLiteOpenHelper {
    private Context mContext;
    static final String CREATER_BOOK="create table book("
            +"isbn integer primary key,"
            +"image text,"
            +"name text)";
    public BookSqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATER_BOOK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
