package com.tp.photo.Model;


import java.util.List;
public class ListData {
    private String time;
    private List<String> dataList;

    public ListData(String time, List<String> dataList) {
        this.time = time;
        this.dataList = dataList;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getDataList() {
        return dataList;
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }
}
