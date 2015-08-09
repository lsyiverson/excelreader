package com.lsyiverson.excelreader.bean;

import com.lsyiverson.excelreader.excel.ColumnNumber;

public class Content {
    @ColumnNumber(0)
    private String time;

    @ColumnNumber(1)
    private Number releaseLoad;

    @ColumnNumber(2)
    private Number declareLoad;

    @ColumnNumber(3)
    private Number difference;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Number getReleaseLoad() {
        return releaseLoad;
    }

    public void setReleaseLoad(Number releaseLoad) {
        this.releaseLoad = releaseLoad;
    }

    public Number getDeclareLoad() {
        return declareLoad;
    }

    public void setDeclareLoad(Number declareLoad) {
        this.declareLoad = declareLoad;
    }

    public Number getDifference() {
        return difference;
    }

    public void setDifference(Number difference) {
        this.difference = difference;
    }
}
