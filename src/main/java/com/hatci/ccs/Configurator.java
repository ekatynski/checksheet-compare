package com.hatci.ccs;

import java.io.File;
import java.io.FileWriter;

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
    private int platform_col = 3;
    private int platform_row = 3;
    private int platform_sheet = leading_sheets + 1;
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
        String fileName = "../config/config.json";
        File configFile = new File(fileName);
        // create config file if none exists
        if (!configFile.isFile()) {
            try {
                System.out.println("No config file detected.");
                configFile.createNewFile();
                if (configFile.isFile()) {
                    System.out.println("Config file created.");
                    this.configFileSet(fileName);
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void configFileSet(String fileName) {
        try {
            FileWriter configFile = new FileWriter(fileName);
            // export default config settings to config file
            configFile.write("{\n" +
                    " \"col_Can\": 8,\n" +
                    " \"col_category\": 8,\n" +
                    " \"col_feature\": 15,\n" +
                    " \"col_test_case\": 14,\n" +
                    " \"col_US\": 7,\n" +
                    " \"format\": \".xlsx\",\n" +
                    " \"include_invalid\": false,\n" +
                    " \"include_other\": false,\n" +
                    " \"leading_sheets\": 2,\n" +
                    " \"platform\": {\n" +
                    "\t\"col\": 3,\n" +
                    "\t\"row\": 3,\n" +
                    "\t\"sheet\": 3\n" +
                    " },\n" +
                    " \"row_start\": 17\n" +
                    "}");
            configFile.close();
            System.out.println("Config settings successfully updated.");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void configFileRead() {
        
    }
}
