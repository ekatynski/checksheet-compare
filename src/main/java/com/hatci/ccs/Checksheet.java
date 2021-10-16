package com.hatci.ccs;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Checksheet {

    private Configurator config = null;
    private XSSFWorkbook wb = null;
    private ArrayList<XSSFSheet> sheets = null;
    private ArrayList<Category> categories = null;
    private ArrayList<String> categoryNames = null;

    Checksheet(String fileName, Configurator config) {
        // open checksheet file
        this.config = config;
        this.categoryNames = new ArrayList();
        File file = new File("../input/" + fileName);

        // open sheets from workbook
        this.sheets = new ArrayList<>();

        // open category array
        this.categories = new ArrayList<>();

        // confirm file is open
        if (file.isFile() && file.exists()) {
            System.out.println("\nOpened " + fileName);
        } else {
            System.out.println("\nCould not open file " + fileName);
        }

        try {
            // open input stream from new file
            FileInputStream fis = new FileInputStream(file);
            // open workbook from file input stream
            wb = new XSSFWorkbook(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (wb.getNumberOfSheets() < (this.config.getLeadingSheets() + this.config.getSheetCount())) {
            System.out.print("Checksheet " + file.getName() + " has insufficient sheet count." +
                    " Verify that this checksheet is properly formatted.\n");
        } else {
            this.scrapePages();
            for (Category category : categories) {
                categoryNames.add(category.getCategoryName());
            }
        }
    }

    // retrieve checksheet pages as XSSFSheets
    private void scrapePages() {
        System.out.println("Importing pages from checksheet.");
        // construct categories using loaded sheets from checksheet
        for (int i = 0; i < this.config.getSheetCount(); i++) {
            sheets.add(wb.getSheetAt(this.config.getLeadingSheets() + i));
            categories.add(new Category(sheets.get(i), config, i));
        }
    }

    // compile a category name list
    public ArrayList<String> getCategoryNames() {
      return (categoryNames);
    }

    public ArrayList<Category> getCategories() {
        return this.categories;
    }

    public ArrayList<String> getCategoryFeatures(int i) {
      return categories.get(i).getFeatureNames();
    }

    // compile an ArrayMatrix(?) of all features across all categories for checksheet
    public ArrayList<ArrayList<String>> getAllFeatures() {
        ArrayList<ArrayList<String>> allFeatures = new ArrayList();
        for (Category category : categories) {
            allFeatures.add(category.getFeatureNames());
        }
        return (allFeatures);
    }
}
