package com.hatci.ccs;

import org.apache.poi.xssf.usermodel.XSSFSheet;

public class Category {

    private XSSFSheet sheet = null;

    Category() {

    }

    Category(XSSFSheet sheet) {
        this.sheet = sheet;
    }

}
