package com.hatci.ccs;

import java.util.ArrayList;

public class CategorySet {

    private ArrayList<String> totalCategories = null;
    private ArrayList<String> categoriesOne = null;
    private ArrayList<String> categoriesTwo = null;
    private ArrayList<ArrayList<String>> totalFeatures = null;
    private ArrayList<ArrayList<String>> featuresOne = null;
    private ArrayList<ArrayList<String>> featuresTwo = null;
    private int totalFeatureCount;

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

        // count total number of features
        for(int i = 0; i < totalCategories.size(); i++) {
            totalFeatureCount += totalFeatures.get(i).size();
        }
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

    public int getCategoryCount() {
        return totalCategories.size();
    }

    public int getFeatureCount(int i) {
      return totalFeatures.get(i).size();
    }

    public int getTotalFeatureCount() {
        return totalFeatureCount;
    }

    public ArrayList<ArrayList<String>> getTotalFeatureList() {
      return totalFeatures;
    }
}
