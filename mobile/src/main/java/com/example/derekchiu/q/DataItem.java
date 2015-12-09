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

    public void setCompany(String c) {
        this.company = c;
    }

    public int getQueue(){
        return queue;
    }

    public void setQueue(int r) {
        this.queue = r;
    }
}
