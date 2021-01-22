package com.hatci.ccs;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Checksheet {

    private String fileName;
    private File file = null;
    private FileInputStream fis = null;
    private XSSFWorkbook wb = null;

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
            fis = new FileInputStream(file);
            wb = new XSSFWorkbook(fis);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }



}
