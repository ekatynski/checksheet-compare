package com.hatci.ccs;

import java.util.ArrayList;

public class CategorySet {

    ArrayList<String> totalCategories = null;
    ArrayList<String> categoriesOne = null;
    ArrayList<String> categoriesTwo = null;
    ArrayList<ArrayList<String>> totalFeatures = null;
    ArrayList<ArrayList<String>> featuresOne = null;
    ArrayList<ArrayList<String>> featuresTwo = null;

    CategorySet(Checksheet sheetOne, Checksheet sheetTwo) {
        categoriesOne = sheetOne.getCategoryNames();
        categoriesTwo = sheetTwo.getCategoryNames();
        totalCategories = categoriesOne;
        featuresOne = sheetOne.getAllFeatures();
        featuresTwo = sheetTwo.getAllFeatures();
        totalFeatures = new ArrayList();

        int inserts = 0;

        for(int i = 0; i < categoriesTwo.size(); i++) {
            // insert categories on sheet two missing from sheet one after last common category
            if (!totalCategories.contains(categoriesTwo.get(i))) {
                totalCategories.add(i + inserts, categoriesTwo.get(i));
                inserts++;
            }
        }

        compareFeaturesLists(sheetOne, sheetTwo);
    }

    public String toString() {
        String output = "\n";

        for(int i = 0; i < totalCategories.size(); i++) {
            output += totalCategories.get(i) + "\n";
            for(int j = 0; j < totalFeatures.get(i).size(); j++) {
                output += "\t" + totalFeatures.get(i).get(j) + "\n";
            }
        }
        return(output);
    }

    private void compareFeaturesLists(Checksheet sheetOne, Checksheet sheetTwo) {
        // iterate through all categories
        int inserts;
        for(int i = 0; i < totalCategories.size(); i++) {
            inserts = 0;
            // copy over first category feature list if present in joined list
            if (categoriesOne.contains(totalCategories.get(i))) {
                // add category feature arraylist from category corresponding to current master category list item
                totalFeatures.add(sheetOne.getCategoryFeatures(categoriesOne.indexOf(totalCategories.get(i))));
                // filter in non-redundant features from same category on checksheet two if present
                if (categoriesTwo.contains(totalCategories.get(i))) {
                    // iterate through features in checklist two category feature list to find non-redundant features
                    for(int j = 0; j < featuresTwo.get(categoriesTwo.indexOf(totalCategories.get(i))).size(); j++) {
                        if (!totalFeatures.get(i).contains(featuresTwo.get(j))) {
                            totalFeatures.get(i).add(j + inserts, featuresTwo.get(i).get(j));
                            inserts++;
                        }
                    }
                }
            }
            // if checklist one doesn't contain the category but checklist two does, copy its category feature list
            else if (categoriesTwo.contains(totalCategories.get(i))) {
                // add category feature arraylist from category corresponding to current master category list item
                totalFeatures.add(sheetTwo.getCategoryFeatures(categoriesOne.indexOf(totalCategories.get(i))));
            }
        }

    }

    public ArrayList<String> getAllCategories() {
        return totalCategories;
    }
}
