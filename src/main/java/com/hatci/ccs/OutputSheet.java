package com.hatci.ccs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class OutputSheet {

    private XSSFWorkbook wb = null;
    private OutputStream fileOut = null;
    private Chart comparisonChart = null;

    OutputSheet(Chart chartOne, Chart chartTwo) {

        wb = new XSSFWorkbook();

        try {
            fileOut = new FileOutputStream("Geeks.xlsx");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
