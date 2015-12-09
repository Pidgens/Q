package com.example.derekchiu.q;

import java.util.ArrayList;

/**
 * Created by tomo on 12/8/15.
 */
public class CompanyPlaceList {


    public static ArrayList<CompanyPlace> getList() {
        init();
        return list;
    }

    private static void init() {
        if (list == null) {
            list = new ArrayList<CompanyPlace>();
        }
    }


    private static ArrayList<CompanyPlace> list;

    public static void updateList(ArrayList<String> cps) {
        ArrayList<CompanyPlace> replace = new ArrayList<CompanyPlace>();
        for (String cp: cps) {
            String[] cpsplit = cp.split(" ");
            replace.add(new CompanyPlace(cpsplit[0], Integer.parseInt(cpsplit[1])));
        }
        list = replace;
    }

}
