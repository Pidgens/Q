package com.example.derekchiu.q;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by tomo on 12/6/15.
 */
public class CompanyQueue {
    private String name;
    private int place;

    public CompanyQueue(String name, int place) {
        this.name = name;
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public int getPlace() {
        return place;
    }

    public void bumpQueue() {
        // TODO Integrate with DB
        place += 5;
    }

    private static ArrayList<CompanyQueue> mockdata;

    public static ArrayList<CompanyQueue> getMockData() {
        if (mockdata == null) {
            mockdata = new ArrayList<CompanyQueue>();
            mockdata.add(new CompanyQueue("Walmart", 14));
            mockdata.add(new CompanyQueue("Disney", 23));
            mockdata.add(new CompanyQueue("Goldman Sachs", 30));
        }
        return mockdata;
    }

}
