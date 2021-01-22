package com.hatci.ccs;

import org.apache.poi.ss.usermodel.Cell;
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
        this.rows = sheet.getLastRowNum();
        this.features = new ArrayList<>();
        this.featureNames = new ArrayList<>();
        this.categoryName = sheet.getSheetName();
        System.out.println("\n" + this.getCategoryName() + " in program " +
                this.getProgramName() + " has " + this.rows + " rows.");
        this.setFeatures();
    }

    // scrape checksheet tab for feature names
    private void setFeatures() {
        String rowFeatureName;
        // start catalog at index of top row according to config file
        for (int i = this.config.getRowStart() - 1; i <= this.rows; i++) {
            try {
                // ensure row isn't null
                if (this.sheet.getRow(i) != null){
                    // ensure cell isn't null
                    if (sheet.getRow(i).getCell(this.config.getColFeature() - 1) != null) {
                        rowFeatureName = this.sheet.getRow(i).getCell(this.config.getColFeature() - 1).toString();
                        // check to see if feature is already being tracked or if the cell is blank
                        if ((!featureNames.contains(rowFeatureName)) && (!rowFeatureName.isEmpty())) {
                            featureNames.add(rowFeatureName);
                            System.out.println("Added feature: " + rowFeatureName);
                        }
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    // get category name of current sheet
    public String getCategoryName() {
        return(this.categoryName);
    }

    public String getProgramName() {
        String program = "";
        if (this.sheet.getRow(this.config.getPlatformRow() - 1) != null) {
            if (this.sheet.getRow(this.config.getPlatformRow() - 1).getCell(this.config.getPlatformCol() - 1) != null) {
                program = this.sheet.getRow(this.config.getPlatformRow() - 1).getCell(this.config.getPlatformCol() - 1).toString();
            }
            else {
                System.out.println("Missing program info, check sheet " + this.getCategoryName() + ".");
            }
        }
        else {
            System.out.println("Missing program info, check sheet " + this.getCategoryName() + ".");
        }
        return(program);
    }

}