package com.hatci.ccs;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class OutputSheet {

    private XSSFWorkbook wb = null;
    private OutputStream fileOut = null;
    private Chart comparisonChart = null;

    OutputSheet() {
        wb = new XSSFWorkbook();

        try {
            // create new output file
            fileOut = new FileOutputStream("../output/Output.xlsx");
            // set up output file sheets
            XSSFSheet compare = wb.createSheet("Comparison");
            XSSFSheet sheetOne = wb.createSheet("Checksheet One");
            XSSFSheet sheetTwo = wb.createSheet("Checksheet Two");

            // close up shop
            wb.write(fileOut);
            fileOut.close();
            wb.close();
            System.out.println("\nOutput file created.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    OutputSheet(Chart chartOne, Chart chartTwo, CategorySet commonCategories, Configurator myConfig) {
        wb = new XSSFWorkbook();

        try {
            String fileName = "";
            fileName += chartOne.getProgramName() + ", "
                    + chartTwo.getProgramName() + " Comparison" ;

            // create new output file
            fileOut = new FileOutputStream("../output/" + fileName + ".xlsx");
            // set up output file sheets
            XSSFSheet compare = wb.createSheet("Comparison");
            formatSheet(compare, commonCategories);
            XSSFSheet sheetOne = wb.createSheet(chartOne.getProgramName());
            formatSheet(sheetOne, commonCategories);
            XSSFSheet sheetTwo = wb.createSheet(chartTwo.getProgramName());
            formatSheet(sheetTwo, commonCategories);


            // close up shop
            wb.write(fileOut);
            fileOut.close();
            wb.close();
            System.out.println("\nOutput file created: " + fileName + ".xlsx");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // create grid, populate feature names and categories
    private void formatSheet(XSSFSheet currentSheet, CategorySet commonCategories) {
        int startingRow = 0;
        // track inserted feature count to allow for proper cell merging
        int featureRows = startingRow;

        // iterate through all categories
        for(int i = 0; i < commonCategories.getCategoryCount(); i++) {
            // iterate through all features per category
            for(int j = 0; j < commonCategories.getTotalFeatureList().get(i).size(); j++) {

            }
            // merge cells to fit category block
            currentSheet.addMergedRegion(new CellRangeAddress(featureRows, (featureRows + commonCategories.getTotalFeatureList().get(i).size()), 0, 0));
            featureRows += commonCategories.getTotalFeatureList().get(i).size() + 1;
        }
    }
}
