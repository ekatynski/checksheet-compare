package com.hatci.ccs;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.poi.ss.extractor.ExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Checksheet {

    private int rows;
    private int cols;

    private String fileName;
    private File file;
    private FileInputStream fis;
    private XSSFWorkbook wb;
    private ArrayList<XSSFSheet> sheets = null;
    private ArrayList<Category> categories = null;

    Checksheet() {

    }

    Checksheet(String fileName) {
        // open checksheet file
        this.fileName = fileName;
        file = new File("../input/" + fileName);

        // confirm file is open
        if (file.isFile() && file.exists()) {
            System.out.println("Opened " + fileName);
        }
        else {
            System.out.println("Could not open file " + fileName);
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

        // open sheets from workbook
        sheets = new ArrayList<XSSFSheet>();
        sheets.add(wb.getSheetAt(0));

        // open category array
        categories = new ArrayList<Category>();

        // construct categories using loaded sheets from checksheet
        for (int i = 0; i < sheets.size(); i++) {
            categories.add(new Category(sheets.get(i)));
        }
    }


}
