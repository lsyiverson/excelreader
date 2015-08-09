package com.lsyiverson.excelreader.bean;

import com.lsyiverson.excelreader.excel.ColumnNumber;

public class Report {
    @ColumnNumber(0)
    private String date;

    @ColumnNumber(1)
    private String value;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "| " + date + " | " + value + " |";
    }
}
