package com.hatci.ccs;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.ArrayList;

public class Category {

    private int sheetNum;
    private int rows;
    private Configurator config = null;
    private XSSFSheet sheet = null;
    private ArrayList<Feature> features = null;
    private ArrayList<String> featureNames = null;
    private String categoryName;

    Category() {

    }

    Category(XSSFSheet sheet, Configurator config, int sheetNum) {
        this.config = config;
        this.sheet = sheet;
        this.sheetNum = sheetNum;
        this.rows = sheet.getLastRowNum();
        this.features = new ArrayList<>();
        this.featureNames = new ArrayList<>();
        this.categoryName = sheet.getSheetName();
        System.out.println("\n" + this.getCategoryName() + " in program " +
                this.getProgramName() + " has " + this.rows + " rows.");
        this.setFeatures();
        System.out.println(this.toString());
    }

    // scrape checksheet tab for feature names
    private void setFeatures() {
        String rowFeatureName = "";
        Feature currentFeature = null;
        // start catalog at index of top row according to config file
        for (int i = this.config.getRowStart() - 1; i <= this.rows; i++) {
            try {
                // ensure row isn't null
                if (this.sheet.getRow(i) != null){
                    // ensure cell isn't null
                    if (this.sheet.getRow(i).getCell(this.config.getColFeature() - 1) != null) {
                        rowFeatureName = this.sheet.getRow(i).getCell(this.config.getColFeature() - 1).toString();
                        // check to see if feature is already being tracked or if the cell is blank
                        if ((!this.featureNames.contains(rowFeatureName)) && (!rowFeatureName.isEmpty())) {
                            // log feature name
                            this.featureNames.add(rowFeatureName);
                            // log feature
                            this.features.add(new Feature(rowFeatureName, this.sheetNum));
                            System.out.println("Added feature: " + rowFeatureName);
                        }
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // track test case outcomes per row
            currentFeature = this.features.get(this.featureNames.indexOf(rowFeatureName));
            if (this.sheet.getRow(i).getCell(this.config.getColUs())!= null && this.sheet.getRow(i).getCell(this.config.getColCan())!= null) {
                this.setOutcomes(currentFeature, i);
            }
            else {
                System.out.println("Null test case result in row: " + (i + 1));
            }
        }

    }

    // track results for test cases encompassed by feature
    private void setOutcomes(Feature currentFeature, int i) {
        boolean invalid = false;
        //pull US/CAN test case results per row
        String usResult = this.sheet.getRow(i).getCell(this.config.getColUs()-1).toString().toUpperCase();
        String canResult = this.sheet.getRow(i).getCell(this.config.getColCan()-1).toString().toUpperCase();

        // check if case is invalid -- BROKEN
        if (this.sheet.getRow(i).getCell(13) != null) {
            if (this.sheet.getRow(i).getCell(13).toString().toUpperCase() == "INVALID" ) {
                System.out.println("Invalid case detected.");
                invalid = true;
            }
        }

        if (this.sheet.getRow(i).getCell(14) != null) {
            if (this.sheet.getRow(i).getCell(14).toString().toUpperCase() == "INVALID" ) {
                System.out.println("Invalid case detected.");
                invalid = true;
            }
        }

        // invalidate invalid test cases
        if (invalid) {
            usResult = "INVALID";
            canResult = "INVALID";
        }

        System.out.println(this.sheet.getRow(i).getCell(14).toString().toUpperCase());

        // process test results
        currentFeature.processUsCase(usResult);
        currentFeature.processCanCase(canResult);

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

    public String toString() {
        String output = ("Category: " + this.getCategoryName() + "\n");

        for (int i = 0; i < this.features.size(); i++) {
            output += features.get(i).toString();
        }

        output += "\n";
        return(output);
    }
}
