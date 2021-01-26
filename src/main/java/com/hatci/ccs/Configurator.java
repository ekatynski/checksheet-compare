package com.hatci.ccs;

public class Configurator {

    // configuration file settings
    private int colCan;
    private int colCategory;
    private int colFeature;
    private int colTestCase;
    private int colUs;
    private String format;
    private boolean includeInvalid;
    private boolean includeOther;
    private int leadingSheets;
    private int platformCol;
    private int platformRow;
    private int platformSheet;
    private int rowStart;
    private int sheetCount;

    public Configurator() {
        // default configuration file values
        this.colCan = 8;
        this.colCategory = 15;
        this.colFeature = 2;
        this.colTestCase = 14;
        this.colUs = 7;
        this.format = ".xlsx";
        this.includeInvalid = false;
        this.includeOther = false;
        this.leadingSheets = 2;
        this.platformCol = 3;
        this.platformRow = 3;
        this.platformSheet = leadingSheets + 1;
        this.rowStart = 17;
        this.sheetCount = 9;
    }

}
