package com.hatci.ccs;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Checksheet {

    private Configurator config;
    private String fileName;
    private File file;
    private FileInputStream fis;
    private XSSFWorkbook wb;
    private ArrayList<XSSFSheet> sheets = null;
    private ArrayList<Category> categories = null;

    Checksheet() {

    }

    Checksheet(String fileName, Configurator config) {
        // open checksheet file
        this.fileName = fileName;
        this.config = config;
        file = new File("../input/" + fileName);

        // confirm file is open
        if (file.isFile() && file.exists()) {
            System.out.println("\nOpened " + fileName);
        }
        else {
            System.out.println("\nCould not open file " + fileName);
        }

        try {
            // open input stream from new file
            fis = new FileInputStream(file);
            // open workbook from file input stream
            wb = new XSSFWorkbook(fis);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (wb.getNumberOfSheets() < (this.config.getLeadingSheets() + this.config.getSheetCount())) {
            System.out.print("Checksheet " + file.getName() + " has insufficient sheet count." +
                    " Verify that this checksheet is properly formatted.");
        }
        else {
            System.out.println("Importing pages from checksheet.");

            // open sheets from workbook
            sheets = new ArrayList<XSSFSheet>();

            // open category array
            categories = new ArrayList<Category>();

            // construct categories using loaded sheets from checksheet
            for (int i = 0; i < this.config.getSheetCount(); i++) {
                sheets.add(wb.getSheetAt(this.config.getLeadingSheets() + i));
                categories.add(new Category(sheets.get(i), config));
            }
        }
    }


}