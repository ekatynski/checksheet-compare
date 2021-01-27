package com.hatci.ccs;

import java.util.ArrayList;

public class CategorySet {

    private ArrayList<String> totalCategories = null;
    private ArrayList<String> categoriesOne = null;
    private ArrayList<String> categoriesTwo = null;
    private ArrayList<ArrayList<String>> totalFeatures = null;
    private ArrayList<ArrayList<String>> featuresOne = null;
    private ArrayList<ArrayList<String>> featuresTwo = null;
    private int totalCategoryCount;
    private int totalFeatureCount;

    CategorySet(Checksheet sheetOne, Checksheet sheetTwo) {
        categoriesOne = sheetOne.getCategoryNames();
        categoriesTwo = sheetTwo.getCategoryNames();

        totalCategories = categoriesOne;
        featuresOne = sheetOne.getAllFeatures();
        featuresTwo = sheetTwo.getAllFeatures();
        totalFeatures = new ArrayList();
        totalFeatureCount = 0;
        totalCategoryCount = 0;

        int inserts = 0;

        for(int i = 0; i < categoriesTwo.size(); i++) {
            // insert categories on sheet two missing from sheet one after last common category
            if (!totalCategories.contains(categoriesTwo.get(i))) {
                totalCategories.add(i + inserts, categoriesTwo.get(i));
                inserts++;
            }
        }

        // compile common feature list
        compareFeaturesLists(sheetOne, sheetTwo);

        // count total number of features
        for(int i = 0; i < totalCategories.size(); i++) {
            totalCategoryCount++;
            totalFeatureCount += totalFeatures.get(i).size();
        }
    }

    public String toString() {
        String output = "\n";
        System.out.println("Total Categories and Features");
        for(int i = 0; i < totalCategories.size(); i++) {
            output += totalCategories.get(i) + "\n";
            for(int j = 0; j < totalFeatures.get(i).size(); j++) {
                output += "\t" + totalFeatures.get(i).get(j) + "\n";
            }
        }
        return(output);
    }

    // compile a master feature list for each category
    private void compareFeaturesLists(Checksheet sheetOne, Checksheet sheetTwo) {
        // iterate through all categories
        for(int i = 0; i < totalCategories.size(); i++) {
            // if first master list category is included in first checksheet, copy over all features
            if (categoriesOne.contains(totalCategories.get(i))) {
                // add feature list of referenced category
                System.out.println("Category Index: " + categoriesOne.indexOf(totalCategories.get(i)));
                System.out.println("CategoryList Size: " + categoriesOne.size());
                totalFeatures.add(featuresOne.get(categoriesOne.indexOf(totalCategories.get(i))));
                // if checklist two contains the same category
                if (categoriesTwo.contains(totalCategories.get(i))) {
                    // iterate through all features for that category of checklist two
                    for(int j = 0; j < featuresTwo.get(categoriesTwo.indexOf(totalCategories.get(i))).size(); j++) {
                        // add any non-redundant features to the master feature-list for this category
                        if (!totalFeatures.get(i).contains(featuresTwo.get(categoriesTwo.indexOf(totalCategories.get(i))).get(j))) {
                            totalFeatures.get(i).add(featuresTwo.get(categoriesTwo.indexOf(totalCategories.get(i))).get(j));
                        }
                    }
                }
            }
            // if the first checksheet lacks this category but the second one has it, copy its corresponding features
            else if (categoriesTwo.contains(totalCategories.get(i))){
                totalFeatures.add(featuresTwo.get(categoriesTwo.indexOf(totalCategories.get(i))));
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
        //System.out.println("FeatureCount i: " + i);
      return totalFeatures.get(i).size();
    }

    public int getTotalFeatureCount() {
        return totalFeatureCount;
    }

    public ArrayList<ArrayList<String>> getTotalFeatureList() {
      return totalFeatures;
    }
}
