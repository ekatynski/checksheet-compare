package com.hatci.ccs;

import java.util.ArrayList;

public class CategorySet {

    ArrayList<String> totalCategories = null;
    ArrayList<String> namesTwo = null;
    

    CategorySet(Checksheet sheetOne, Checksheet sheetTwo) {
        totalCategories = new ArrayList();
        totalCategories = sheetOne.getCategoryNames();
        namesTwo = sheetTwo.getCategoryNames();

        for(int i = 0; i < namesTwo.size(); i++) {
            if (!totalCategories.contains(namesTwo.get(i))) {
                totalCategories.add(namesTwo.get(i));
            }
        }
    }



}
