package com.example.derekchiu.q;

import java.util.ArrayList;

/**
 * Created by tomo on 12/6/15.
 */
public class CompanyPlace {
    private String name;
    private int place;

    public CompanyPlace(String name, int place) {
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

    private static ArrayList<CompanyPlace> mockdata;

    public static ArrayList<CompanyPlace> getMockData() {
        if (mockdata == null) {
            mockdata = new ArrayList<CompanyPlace>();
            mockdata.add(new CompanyPlace("Walmart", 14));
            mockdata.add(new CompanyPlace("Disney", 30));
            mockdata.add(new CompanyPlace("Goldman Sachs", 4));
        }
        return mockdata;
    }

}
