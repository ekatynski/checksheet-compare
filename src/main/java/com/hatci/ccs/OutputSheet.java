package com.hatci.ccs;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class OutputSheet {

    private XSSFWorkbook wb = null;
    private OutputStream fileOut = null;
    private XSSFCellStyle defaultStyle = null;
    private XSSFCellStyle percentageStyle = null;
    private XSSFCellStyle ignoreStyle = null;
    private Chart comparisonChart = null;
    private int startingRow = 2;
    private int endingRow;
    private int rowCount;
    private int colCount;
    private Row[] rows = null;
    private Cell[][] cells = null;
    private String[] resultType = null;

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

        // set percentage cell style
        percentageStyle = wb.createCellStyle();
        percentageStyle.setDataFormat(wb.createDataFormat().getFormat("00.0%"));
        percentageStyle.setBorderTop(BorderStyle.THIN);
        percentageStyle.setBorderBottom(BorderStyle.THIN);
        percentageStyle.setBorderLeft(BorderStyle.THIN);
        percentageStyle.setBorderRight(BorderStyle.THIN);
        percentageStyle.setAlignment(HorizontalAlignment.CENTER);
        percentageStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        percentageStyle.setWrapText(true);

        // set invalid/other style
        ignoreStyle = wb.createCellStyle();
//        ignoreStyle.setBorderTop(BorderStyle.THIN);
//        ignoreStyle.setBorderBottom(BorderStyle.THIN);
//        ignoreStyle.setBorderLeft(BorderStyle.THIN);
//        ignoreStyle.setBorderRight(BorderStyle.THIN);
        ignoreStyle.setAlignment(HorizontalAlignment.CENTER);
        ignoreStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        ignoreStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(196, 195, 192)));
        ignoreStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        ignoreStyle.setWrapText(true);

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
            formatSheet(compare, commonCategories, chartOne.getWidth(), "Comparison", myConfig, comparisonChart.getIgnoredRows());
            populateResults(comparisonChart, compare);
            outlineTable(compare, "Comparison");
            // set up chart one sheet
            XSSFSheet sheetOne = wb.createSheet(chartOne.getProgramName());
            formatSheet(sheetOne, commonCategories, chartOne.getWidth(), chartOne.getProgramName(), myConfig, chartOne.getIgnoredRows());
            populateResults(chartOne, sheetOne);
            outlineTable(sheetOne, chartOne.getProgramName());
            // set up chart two sheet
            XSSFSheet sheetTwo = wb.createSheet(chartTwo.getProgramName());
            formatSheet(sheetTwo, commonCategories, chartOne.getWidth(), chartTwo.getProgramName(), myConfig, chartTwo.getIgnoredRows());
            populateResults(chartTwo, sheetTwo);
            outlineTable(sheetTwo, chartTwo.getProgramName());

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
    private void formatSheet(XSSFSheet currentSheet, CategorySet commonCategories, int outcomes, String programName, Configurator config, boolean[] ignoredRows) {
        // track inserted feature count to allow for proper cell merging
        int featureRows = startingRow;

        // one row per feature, plus a header
        rowCount = startingRow + commonCategories.getTotalFeatureCount() + 2;
        endingRow = rowCount - 2;
        // sidebar, plus one cellblock each for US and CAN containing all listed outcomes
        colCount = 2 + 2*outcomes;
        rows = new Row[rowCount];

        // create all rows and cells
        for(int i = 0; i < rowCount; i++) {
            rows[i] = currentSheet.createRow(i);
            for(int j = 0; j < colCount; j++) {
                rows[i].createCell(j);
                // outline cells, excluding a footer on the comparison tab
                if (programName != "Comparison" || i < endingRow) {
                    rows[i].getCell(j).setCellStyle(defaultStyle);
                }
                // blot out rows intructed for ignorance according to config settings
//                if (i >= startingRow && i < endingRow) {
//                    if (ignoredRows[i - 2]) {
//                        System.out.println("Detection!");
//                        ignoreLine(rows[i]);
//                    }
//                }
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

        //set up footer in program tables
        if (!programName.equals("Comparison")) {
            currentSheet.addMergedRegion(new CellRangeAddress(endingRow, endingRow, 0, 1));
            rows[endingRow].getCell(0).setCellValue("TOTALS");
            currentSheet.addMergedRegion(new CellRangeAddress(endingRow + 1, endingRow + 1, 0, 1));
            rows[endingRow + 1].getCell(0).setCellValue("PERCENTAGES");
        }

        // populate footer with formulas
        if (!programName.equals("Comparison")) {
            // column ID for first formula rows set to "C"
            char columnID = 67;
            String totalCountCell = "";
            // iterate through each row
            for (int i = 2; i < colCount; i++) {
                // set up summation formulas
                rows[endingRow].getCell(i).setCellFormula("SUM(" + Character.toString(columnID) + Integer.toString(startingRow)
                        + ":" + Character.toString(columnID) + Integer.toString(endingRow - 1) + ")");

                // set up percentage formulas
                // track "total" cell address
                if ((i - 2) % ((colCount -2) / 2) == 0) {
                    totalCountCell = Character.toString(columnID) + Integer.toString(endingRow + 1);
                }

                rows[endingRow + 1].getCell(i).setCellStyle(percentageStyle);
                rows[endingRow + 1].getCell(i).setCellFormula(Character.toString(columnID) + Integer.toString(endingRow + 1)
                        + "/" + totalCountCell);
                columnID++;
            }
        }

        // populate second row of table
        for(int i = 0; i < colCount; i++) {
            if (i == 0) {
                rows[1].getCell(i).setCellValue("Category");
            }
            else if (i == 1) {
                rows[1].getCell(i).setCellValue("Feature");
            }
            else {
                rows[1].getCell(i).setCellValue(resultType[(i - 2) % outcomes]);
            }
            // auto-size column for easy reading
            currentSheet.autoSizeColumn(i);
            // resize to allow for filter arrow buttons
            currentSheet.setColumnWidth(i, currentSheet.getColumnWidth(i) + 600);
        }

        // add filters to starting row
        currentSheet.setAutoFilter(new CellRangeAddress(startingRow - 1, startingRow - 1, 0, colCount - 1));
    }

    private void ignoreLine(Row ignoredRow) {
        // roll through each cell
        System.out.println("Line ignored!");
        for (int i = 2; i < colCount; i++) {
            // gray out the
            ignoredRow.getCell(i).setCellStyle(ignoreStyle);
        }
    }

    private void outlineTable(XSSFSheet currentSheet, String programName) {
        // outline entire table
        outlineAreasMedium(currentSheet, new CellRangeAddress(0, endingRow - 1, 0, colCount - 1));
        // outline header block
        outlineAreasMedium(currentSheet, new CellRangeAddress(0, startingRow - 1, 0, colCount - 1));
        // outline left margin key
        outlineAreasMedium(currentSheet, new CellRangeAddress(0, endingRow - 1, 0, 1));
        // outline US result block
        outlineAreasMedium(currentSheet, new CellRangeAddress(0, endingRow - 1, 2, colCount/2));
        // outline CAN result block
        outlineAreasMedium(currentSheet, new CellRangeAddress(0, endingRow - 1, 1 + colCount/2, colCount - 1));
        //outline footer outside of the comparison chart
        if (!programName.equals("Comparison")) {
            outlineAreasMedium(currentSheet, new CellRangeAddress(endingRow, rowCount - 1, 0, colCount - 1));
            outlineAreasMedium(currentSheet, new CellRangeAddress(endingRow, rowCount - 1, 0, 1));
            outlineAreasMedium(currentSheet, new CellRangeAddress(endingRow, rowCount - 1, 2, colCount/2));
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
        for(int i = startingRow; i < endingRow; i++) {
            // iterate through each cell in the row
            for(int j = 2; j < colCount; j++) {
                    // populate corresponding result
                    rows[i].getCell(j).setCellValue(currentChart.getTestResults()[i-2][j-2]);
            }
        }
    }
}
