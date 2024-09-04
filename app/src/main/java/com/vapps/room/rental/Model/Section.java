package com.vapps.room.rental.Model;

import java.util.ArrayList;

public class Section {

    String title;
    ArrayList<HistoryData> items;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<HistoryData> getItems() {
        return items;
    }

    public void setItems(ArrayList<HistoryData> items) {
        this.items = items;
    }
}
