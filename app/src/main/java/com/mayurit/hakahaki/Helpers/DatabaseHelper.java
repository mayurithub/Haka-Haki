package com.mayurit.hakahaki.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mayurit.hakahaki.Model.NewsListModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    static String dbName = "hakahaki";
    static int version = 1;

    static String tbl_news = " CREATE TABLE if not exists tbl_news (\n" +
            "    category_id  INTEGER,\n" +
            "    post_title TEXT,\n" +
            "    post_date TEXT,\n" +
            "    post_description TEXT\n" +
            ");\n";

    public DatabaseHelper(Context context) {
        super(context, dbName, null, version);
        getWritableDatabase().execSQL(tbl_news);
    }

    public void insertMainNews(ContentValues cv) {
        getWritableDatabase().insert("tbl_news", "", cv);
        Log.i("inserted", "data");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public long countRowTable(String table_name) {
        long count = DatabaseUtils.queryNumEntries(getWritableDatabase(), table_name);
        return count;
    }

    public ArrayList<NewsListModel> getQAList(String category_id) {

        String sql = "Select * from tbl_news WHERE category_id = " + category_id;
        Cursor c = getWritableDatabase().rawQuery(sql, null);
        ArrayList<NewsListModel> list = new ArrayList<NewsListModel>();
        while (c.moveToNext()) {
            NewsListModel info = new NewsListModel();
            info.setID(c.getString(c.getColumnIndex("category_id")));
            info.setPostTitle(c.getString(c.getColumnIndex("post_title")));
            info.setPostDate(c.getString(c.getColumnIndex("post_date")));
            info.setPostExcerpt(c.getString(c.getColumnIndex("post_description")));
            Log.i("msz", "post t" + info.getID());
            list.add(info);
        }
        c.close();
        return list;
    }

    public void delete(String id){
        getWritableDatabase().execSQL("delete from tbl_news where category_id = "+id);
    }
}
