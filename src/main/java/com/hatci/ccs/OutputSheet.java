package com.hatci.ccs;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
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

        // create comparison chart
        comparisonChart = new Chart(chartOne, chartTwo, commonCategories);

        try {
            String fileName = "";
            fileName += chartOne.getProgramName() + ", "
                    + chartTwo.getProgramName() + " Comparison" ;

            // create new output file
            fileOut = new FileOutputStream("../output/" + fileName + ".xlsx");

            // set up comparison file sheet
            XSSFSheet compare = wb.createSheet("Comparison");
            formatSheet(compare, commonCategories, chartOne.getWidth(), "Comparison");
            populateResults(comparisonChart, compare);
            // set up chart one sheet
            XSSFSheet sheetOne = wb.createSheet(chartOne.getProgramName());
            formatSheet(sheetOne, commonCategories, chartOne.getWidth(), chartOne.getProgramName());
            populateResults(chartOne, sheetOne);
            // set up chart two sheet
            XSSFSheet sheetTwo = wb.createSheet(chartTwo.getProgramName());
            formatSheet(sheetTwo, commonCategories, chartOne.getWidth(), chartTwo.getProgramName());
            populateResults(chartTwo, sheetTwo);

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
                System.out.println("\t - " + commonCategories.getTotalFeatureList().get(i).get(j));
            }
            // merge category columns to match subsequent features
            currentSheet.addMergedRegion(new CellRangeAddress(featureRows, (featureRows + commonCategories.getTotalFeatureList().get(i).size() - 1), 0, 0));
            featureRows += commonCategories.getTotalFeatureList().get(i).size();
        }

        // merge top row, form US/CAN columns
        currentSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
        rows[0].getCell(0).setCellValue(programName);
        currentSheet.addMergedRegion(new CellRangeAddress(0, 0, 2,  colCount/2));
        rows[0].getCell(2).setCellValue("US");
        currentSheet.addMergedRegion(new CellRangeAddress(0, 0, 1 + colCount/2, colCount - 1));
        rows[0].getCell(1 + colCount/2).setCellValue("CANADA");

        // outline entire table
        outlineAreasMedium(currentSheet, new CellRangeAddress(0, rowCount - 1, 0, colCount - 1));
        // outline header block
        outlineAreasMedium(currentSheet, new CellRangeAddress(0, startingRow - 1, 0, colCount - 1));
        // outline left margin key
        outlineAreasMedium(currentSheet, new CellRangeAddress(0, rowCount - 1, 0, 1));
        // outline US result block
        outlineAreasMedium(currentSheet, new CellRangeAddress(0, rowCount - 1, 2, colCount/2));
        // outline CAN result block
        outlineAreasMedium(currentSheet, new CellRangeAddress(0, rowCount - 1, 1 + colCount/2, colCount - 1));

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
            // auto-size column for easy reading
            currentSheet.autoSizeColumn(i);
        }
    }

    private void outlineAreasMedium(XSSFSheet currentSheet, CellRangeAddress range) {
        // outline table with medium border
        CellRangeAddress fullSheetRegion = new CellRangeAddress(0, rowCount - 1, 0, colCount - 1);
        RegionUtil.setBorderTop(BorderStyle.MEDIUM, range, currentSheet);
        RegionUtil.setBorderLeft(BorderStyle.MEDIUM, range, currentSheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, range, currentSheet);
        RegionUtil.setBorderBottom(BorderStyle.MEDIUM, range, currentSheet);
    }

    private void populateResults(Chart currentChart, XSSFSheet currentSheet) {
        // iterate through rows
        for(int i = 2; i < rowCount; i++) {
            // iterate through each cell in the row
            for(int j = 2; j < colCount; j++) {
                    // populate corresponding result
                    rows[i].getCell(j).setCellValue(currentChart.getTestResults()[i-2][j-2]);
            }
        }
    }
}
