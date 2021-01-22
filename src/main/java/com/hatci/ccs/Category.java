package com.hatci.ccs;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.ArrayList;

public class Category {

    private int rows;
    private Configurator config = null;
    private XSSFSheet sheet = null;
    private ArrayList<Feature> features = null;
    private ArrayList<String> featureNames = null;
    private String categoryName;

    Category() {

    }

    Category(XSSFSheet sheet, Configurator config) {
        this.config = config;
        this.sheet = sheet;
        this.rows = sheet.getPhysicalNumberOfRows();
        this.features = new ArrayList<>();
        this.featureNames = new ArrayList<>();
        this.categoryName = sheet.getSheetName();
        System.out.println(this.getCategoryName() + " has " +
                this.rows + " rows.");
        this.setFeatures();
    }

    // scrape checksheet tab for feature names
    private void setFeatures() {
        String rowFeatureName;
        for(int i = 0; i < this.rows; i++) {
            rowFeatureName = (String) sheet.getRow(i).getCell(0).toString();
            // check to see if feature is already being tracked
            if (!featureNames.contains(rowFeatureName)) {
                featureNames.add(rowFeatureName);
                System.out.println("Added feature: " + rowFeatureName);
            }
        }

    }

    // get category name of current sheet
    public String getCategoryName() {
        return(this.categoryName);
    }

    public String getProgramName() {
        return ("Program");
    }

}