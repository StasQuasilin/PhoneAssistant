package ua.quasilin.assistant.utils;

import android.support.annotation.NonNull;

/**
 * Created by szpt_user045 on 19.12.2018.
 */

public class ArchiveData implements Comparable<ArchiveData> {
    private long date;
    private HistoryType type;
    private String number;
    private String data;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public HistoryType getType() {
        return type;
    }

    public void setType(String type){
        setType(HistoryType.valueOf(type));
    }

    public void setType(HistoryType type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int compareTo(@NonNull ArchiveData o) {
        return Long.compare(date, o.date);
    }
}
