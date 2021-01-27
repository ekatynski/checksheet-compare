package com.hatci.ccs;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class OutputSheet {

    private XSSFWorkbook wb = null;
    private OutputStream fileOut = null;
    private XSSFCellStyle defaultStyle = null;
    private Chart comparisonChart = null;
    private int startingRow = 2;
    private int rowCount;
    private int colCount;
    private Row[] rows = null;
    private Cell[][] cells = null;

    private String[] resultType = null;

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

        resultType = new String[] {"TOTAL", "TESTED", "PASS", "FAIL", "N/A",
                "NOT TESTED", "BLOCKED", "SINGLE", "INVALID", "OTHER"};

        // set default cell style
        defaultStyle = wb.createCellStyle();
        defaultStyle.setBorderTop(BorderStyle.THIN);
        defaultStyle.setBorderBottom(BorderStyle.THIN);
        defaultStyle.setBorderLeft(BorderStyle.THIN);
        defaultStyle.setBorderRight(BorderStyle.THIN);
        defaultStyle.setAlignment(HorizontalAlignment.CENTER);
        defaultStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        defaultStyle.setWrapText(true);

        try {
            String fileName = "";
            fileName += chartOne.getProgramName() + ", "
                    + chartTwo.getProgramName() + " Comparison" ;

            // create new output file
            fileOut = new FileOutputStream("../output/" + fileName + ".xlsx");
            // set up output file sheets
            XSSFSheet compare = wb.createSheet("Comparison");
            formatSheet(compare, commonCategories, chartOne.getWidth(), "Comparison");
            XSSFSheet sheetOne = wb.createSheet(chartOne.getProgramName());
            formatSheet(sheetOne, commonCategories, chartOne.getWidth(), chartOne.getProgramName());
            XSSFSheet sheetTwo = wb.createSheet(chartTwo.getProgramName());
            formatSheet(sheetTwo, commonCategories, chartOne.getWidth(), chartTwo.getProgramName());

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
    private void formatSheet(XSSFSheet currentSheet, CategorySet commonCategories, int outcomes, String programName) {
        // track inserted feature count to allow for proper cell merging
        int featureRows = startingRow;
        // one row per feature, plus a header
        rowCount = startingRow + commonCategories.getTotalFeatureCount();
        // sidebar, plus one cellblock each for US and CAN containing all listed outcomes
        colCount = 2 + 2*outcomes;
        rows = new Row[rowCount];

        // create all rows and cells
        for(int i = 0; i < rowCount; i++) {
            rows[i] = currentSheet.createRow(i);
            for(int j = 0; j < colCount; j++) {
                rows[i].createCell(j);
                // outline cells
                rows[i].getCell(j).setCellStyle(defaultStyle);

            }
        }

        // iterate through all categories
        for(int i = 0; i < commonCategories.getCategoryCount(); i++) {
            // populate category names
            rows[featureRows].getCell(0).setCellValue(commonCategories.getAllCategories().get(i));
            // output featurelist, just for testing
            System.out.println("Feature count for category " + commonCategories.getAllCategories().get(i) + ": " +
                    commonCategories.getTotalFeatureList().get(i).size());
            for(int j = 0; j < commonCategories.getTotalFeatureList().get(i).size(); j++) {
                // populate feature names
                rows[featureRows + j].getCell(1).setCellValue(commonCategories.getTotalFeatureList().get(i).get(j));
                System.out.println("\t - " + commonCategories.getTotalFeatureList().get(j));
            }
            // merge category columns to match subsequent features
            currentSheet.addMergedRegion(new CellRangeAddress(featureRows, (featureRows + commonCategories.getTotalFeatureList().get(i).size() - 1), 0, 0));
            featureRows += commonCategories.getTotalFeatureList().get(i).size();
        }

        // merge top row, form US/CAN columns
        currentSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
        rows[0].getCell(0).setCellValue(programName);
        currentSheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 1 + (colCount/2)));
        rows[0].getCell(2).setCellValue("US");
        currentSheet.addMergedRegion(new CellRangeAddress(0, 0, 2 + colCount/2, colCount - 1));
        rows[0].getCell(2 + colCount/2).setCellValue("CANADA");

        // populate second row of table
        for(int i = 0; i < colCount; i++) {
            if (i == 0) {
                rows[1].getCell(i).setCellValue("Category");
            }
            else if (i == 1) {
                rows[1].getCell(i).setCellValue("Feature");
            }
            else {
                rows[1].getCell(i).setCellValue(resultType[(i - 2 ) % outcomes]);
            }
        }
    }


}
