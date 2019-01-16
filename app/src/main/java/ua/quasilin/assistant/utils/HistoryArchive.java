package ua.quasilin.assistant.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import ua.quasilin.assistant.R;

/**
 * Created by szpt_user045 on 19.12.2018.
 */

public class HistoryArchive {

    private static HistoryArchive archive;
    private LinearLayout list = null;
    LayoutInflater layoutInflater;

    public static HistoryArchive getArchive(Context context) {
        if (archive == null){
            archive = new HistoryArchive(context);
        }
        return archive;
    }

    private SQLiteDatabase db;

    private HistoryArchive(Context context) {
        db = new DBHelper(context).getWritableDatabase();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private String getDateTime(long millis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd.MM.yy HH:mm", Locale.getDefault());
        Date date = new Date(millis);
        return dateFormat.format(date);
    }

    public void addToArchive(HistoryType type, String number, String contact){

        Log.i(HistoryArchive.class.getName(), "Add to archive " + number + ":" + contact);
        ContentValues cv = new ContentValues();
        cv.put("date", System.currentTimeMillis());
        cv.put("type", type.toString());
        cv.put("data", contact);
        Cursor cursor = db.rawQuery("select * from " + DBHelper.TABLE_NAME + " where number=\'" + number + "\'", null);
        long rowsCount = getRowsCount();

        if (cursor.moveToFirst()) {
            db.update(DBHelper.TABLE_NAME, cv, "number=?", new String[]{number});
            updateMe(list);
        } else {
            cv.put("number", number);
            db.insert(DBHelper.TABLE_NAME, null, cv);

            if (list != null) {
                if (rowsCount == 0) {
                    list.removeAllViews();
                }
                add(getDateTime(System.currentTimeMillis()), type, number, contact);
            }
        }
    }

    private List<ArchiveData> getFromArchive() {
        List<ArchiveData> list = new LinkedList<>();
        Cursor query = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        if (query.moveToFirst()){
            int date = query.getColumnIndex("date");
            int type = query.getColumnIndex("type");
            int number = query.getColumnIndex("number");
            int data = query.getColumnIndex("data");

            do {
                ArchiveData archiveData = new ArchiveData();
                archiveData.setDate(query.getLong(date));
                archiveData.setType(query.getString(type));
                archiveData.setNumber(query.getString(number));
                archiveData.setData(query.getString(data));
                list.add(archiveData);
            } while (query.moveToNext());
        }

        Collections.sort(list);

        return list;
    }

    public void updateMe(LinearLayout list) {
        this.list = list;

        if (list != null) {

            List<ArchiveData> fromArchive = getFromArchive();
            if (fromArchive.size() > 0) {
                list.removeAllViews();

                for (int i = 0; i < fromArchive.size(); i++) {
                    ArchiveData archiveData = fromArchive.get(i);

                    add(getDateTime(archiveData.getDate()), archiveData.getType(), archiveData.getNumber(), archiveData.getData());

                }
            }

        }
    }

    private void add(String date, HistoryType type, String number, String contact){
        ViewGroup inflate = getInflate(type);
        TextView dateView = inflate.findViewById(R.id.date);
        dateView.setText(date);

        TextView numberView = inflate.findViewById(R.id.number);
        numberView.setText(number);

        TextView contactView = inflate.findViewById(R.id.contact);
        contactView.setText(contact);

        list.addView(inflate, 0);
    }

    public long getRowsCount() {
        return DatabaseUtils.queryNumEntries(db, DBHelper.TABLE_NAME);
    }

    private ViewGroup getInflate(HistoryType type){
        switch (type){
            default:
                return (ViewGroup) layoutInflater.inflate(R.layout.custom_list, null);
            case income:
                return (ViewGroup) layoutInflater.inflate(R.layout.income_list, null);
        }
    }

}
