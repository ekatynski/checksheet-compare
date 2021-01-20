package com.hatci.ccs;

import java.io.File;

public class Configurator {

    // default configuration file values
    private int col_Can = 8;
    private int col_category = 15;
    private int col_feature = 3;
    private int col_test_case = 14;
    private int col_US = 7;
    private String format = ".xslx";
    private boolean include_invalid = false;
    private boolean include_other = false;
    private int leading_sheets = 2;
    private int row_start = 17;

    public Configurator() {
        // ensures all regular use directories are present
        this.directoryCheck("config");  // contains config.json file
        this.directoryCheck("input");   // where checksheet files are placed
        this.directoryCheck("output");  // where the comparison chart is saved

        this.configFileCheck();
    }

    public void directoryCheck(String dirName) {
        // check if searched directory exists
        File checkDir = new File("../" + dirName);
        if (!checkDir.isDirectory()) {
            System.out.println("No " + dirName + " directory exists.");
            // create directory in question if none exists
            checkDir.mkdirs();
            if (checkDir.isDirectory()) {
                System.out.println(dirName.substring(0, 1).toUpperCase()
                        + dirName.substring(1) + " directory created.");
            }
        }
    }

    public void configFileCheck() {
        // check if config file exists
        File configFile = new File("../config/config.json");
        // create config file if none exists
        if (!configFile.isFile()) {
            try {
                System.out.println("No config file detected.");
                configFile.createNewFile();
                if (configFile.isFile()) {
                    System.out.println("Config file created.");
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void configFileSet() {

    }

    public void configFileRead() {

    }
}
