package com.hatci.ccs;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class Configurator {

    // default configuration file values
    private int colCan = 8;
    private int colCategory = 15;
    private int colFeature = 3;
    private int colTestCase = 14;
    private int colUs = 7;
    private String format = ".xslx";
    private boolean includeInvalid = false;
    private boolean includeOther = false;
    private int leadingSheets = 2;
    private int platformCol = 3;
    private int platformRow = 3;
    private int platformSheet = leadingSheets + 1;
    private int rowStart = 17;

    public Configurator() {
        // ensures all regular use directories are present
        this.directoryCheck("config");  // contains config.json file
        this.directoryCheck("input");   // where checksheet files are placed
        this.directoryCheck("output");  // where the comparison chart is saved

        // searches for config file to set up app before checksheet compare begins
        if (!this.configFileCheck()) {
            // imports settings from config file if one existed before the check
            this.configFileRead();
        }

        // searches for clickable batch file
        this.exeCheck();
    }

    private void directoryCheck(String dirName) {
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
        else {
            System.out.println(dirName.substring(0, 1).toUpperCase()
                    + dirName.substring(1) + " directory found.");
        }
    }

    private boolean configFileCheck() {
        boolean configCreated = false;
        // check if config file exists
        String fileName = "../config/config.json";
        File configFile = new File(fileName);
        // create config file if none exists
        if (!configFile.isFile()) {
            try {
                System.out.println("No config file detected.");
                configFile.createNewFile();
                // if the config file is created using defaults, no point in importing settings
                configCreated = true;
                if (configFile.isFile()) {
                    System.out.println("Config file created.");
                    configFileDefault(fileName);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Config file found.");
        }
        return configCreated;
    }

    private void configFileDefault(String fileName) {
        try {
            FileWriter configFile = new FileWriter(fileName);
            // export default config settings to config file
            configFile.write("{\n" +
                    " \"colCan\": 8,\n" +
                    " \"colCategory\": 8,\n" +
                    " \"colFeature\": 15,\n" +
                    " \"colTestCase\": 14,\n" +
                    " \"colUs\": 7,\n" +
                    " \"format\": \".xlsx\",\n" +
                    " \"includeInvalid\": false,\n" +
                    " \"includeOther\": false,\n" +
                    " \"leadingSheets\": 2,\n" +
                    " \"platform\": {\n" +
                    "\t\"col\": 3,\n" +
                    "\t\"row\": 3,\n" +
                    "\t\"sheet\": 3\n" +
                    " },\n" +
                    " \"rowStart\": 17\n" +
                    "}");
            configFile.close();
            System.out.println("No config settings present; defaults applied to config.json file.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void configFileRead() {
        String fileName = "../config/config.json";
        JSONParser parser = new JSONParser();
        // read config.json to import settings
        try {
            Object obj = parser.parse(new FileReader(fileName));
            JSONObject configFile = (JSONObject)obj;
            JSONObject platformSettings = (JSONObject)configFile.get("platform");
            this.setColCan((int) ((long) configFile.get("colCan")));
            this.setColCategory((int) ((long) configFile.get("colCategory")));
            this.setColFeature((int) ((long) configFile.get("colFeature")));
            this.setColTestCase((int) ((long) configFile.get("colTestCase")));
            this.setColUs((int) ((long) configFile.get("colUs")));
            this.setFormat((String) configFile.get("format"));
            this.setIncludeInvalid((boolean) configFile.get("includeInvalid"));
            this.setIncludeOther((boolean) configFile.get("includeOther"));
            this.setLeadingSheets((int) ((long) configFile.get("leadingSheets")));
            this.setPlatformCol((int) ((long) platformSettings.get("col")));
            this.setPlatformRow((int) ((long) platformSettings.get("row")));
            this.setPlatformSheet((int) ((long) platformSettings.get("sheet")));
            this.setRowStart((int) ((long) configFile.get("rowStart")));

            System.out.println("Config settings updated from config.json file.");
        }
        catch  (Exception e) {
            e.printStackTrace();
        }
    }

    private void exeCheck() {
        // checks to make sure a convenient clickable executable is present
        String fileName = "checksheet-compare.bat";
        File exeFile = new File(fileName);
        // create clickable batch file if none is present
        if (!exeFile.isFile()) {
            try {
                System.out.println("No clickable batch file present.");
                exeFile.createNewFile();
                if(exeFile.isFile()) {
                    // populate batch file
                    this.exeFileSetup(fileName);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Batch file present.");
        }
    }

    private void exeFileSetup(String fileName) {
        try {
            FileWriter exeFile = new FileWriter(fileName);
            // need to investigate changing jar file name from absolute value
            exeFile.write("java -jar %~dp0/checksheet-compare-all.jar \npause");
            System.out.println("Batch file created for easy use.");
            exeFile.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fileCheck() {

    }


    // standard sets and gets
    public void setColCan(int colCan) { this.colCan = colCan; }

    public int getColCan() { return this.colCan; }

    public void setColCategory(int colCategory) { this.colCategory = colCategory; }

    public int getColCategory() { return this.colCategory; }

    public void setColFeature(int colFeature) { this.colFeature = colFeature; }

    public int getColFeature() { return this.colFeature; }

    public void setColTestCase(int colTestCase) { this.colTestCase = colTestCase; }

    public int getColTestCase() { return this.colTestCase; }

    public void setColUs(int colUs) { this.colUs = colUs; }

    public int getColUs() { return this.colUs; }

    public void setFormat(String format) { this.format = format; }

    public String getFormat() { return this.format; }

    public void setIncludeInvalid(boolean includeInvalid) { this.includeInvalid = includeInvalid; }

    public boolean getIncludeInvalid() { return this.includeInvalid; }

    public void setIncludeOther(boolean includeOther) { this.includeOther = includeOther; }

    public boolean getIncludeOther() { return this.includeOther; }

    public void setLeadingSheets(int leadingSheets) { this.leadingSheets = leadingSheets; }

    public int getLeadingSheets() { return this.leadingSheets; }

    public void setPlatformCol(int platformCol) { this.platformCol = platformCol; }

    public int getPlatformCol() { return this.platformCol; }

    public void setPlatformRow(int platformRow) { this.platformRow = platformRow; }

    public int getPlatformRow() { return this.platformRow; }

    public void setPlatformSheet(int platformSheet) { this.platformSheet = platformSheet; }

    public int getPlatformSheet() { return this.platformSheet; }

    public void setRowStart(int rowStart) { this.rowStart = rowStart; }

    public int getRowStart() { return this.rowStart; }

}
