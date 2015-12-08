package com.example.derekchiu.q;

public class DataItem {
    private String company;
    private int queue;

    public DataItem() {

    }

    public DataItem(String n, int r) {
        this.company = n;
        this.queue = r;
    }


    public String getCompany() {
        return company;
    }

    public int getQueue(){
        return queue;
    }
}
