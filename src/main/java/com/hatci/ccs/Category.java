package com.hatci.ccs;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.ArrayList;

public class Category {

    private XSSFSheet sheet = null;
    private ArrayList<Feature> features = null;

    Category() {

    }

    Category(XSSFSheet sheet) {
        this.sheet = sheet;
    }

    public String getSheetName() {
        return ("Sheet");
    }

    public String getProgramName() {
        return ("Program");
    }

}