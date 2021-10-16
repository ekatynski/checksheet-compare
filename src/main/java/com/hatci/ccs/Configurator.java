package com.hatci.ccs;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class Configurator {

    // file names for the checksheets being compared
    private String fileOne;
    private String fileTwo;

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

    public boolean checkAll() {
        boolean ready = true;
        System.out.println();
        // ensures all regular use directories are present
        // contains config.json file, checksheets for input, and chart output respectively
//        ready = (ready && this.directoryCheck("config"));
//        ready = (ready && this.directoryCheck("input"));
//        ready = (ready && this.directoryCheck("output"));
        this.directoryCheck("config");
        this.directoryCheck("input");
        this.directoryCheck("output");

        // searches for config file to set up app before checksheet compare begins
        if (!this.configFileCheck()) {
            // imports settings from config file if one existed before the check
            this.configFileRead();
        }

        // searches for clickable batch file
        this.exeCheck();

        // check to ensure exactly two checksheets are present
        ready = (ready && this.fileCheck());

        return (ready);
    }

    private void directoryCheck(String dirName) {
        // check if searched directory exists
        boolean ready = true;
        File checkDir = new File("../" + dirName);
        if (!checkDir.isDirectory()) {
            ready = false;
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
        //return (ready);
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
                    " \"colCategory\": 15,\n" +
                    " \"colFeature\": 2,\n" +
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
                    " \"rowStart\": 17,\n" +
                    " \"sheetCount\": 9\n" +
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
            this.setSheetCount((int) ((long) configFile.get("sheetCount")));

            System.out.println("Config settings updated from config.json file.");
        }
        catch  (Exception e) {
            e.printStackTrace();
        }
        /*// alert user if file format incorrect
        if ((this.getFormat() != (String) ".xlsx") && (this.getFormat() != (String) ".xlsm")) {
            System.out.println("\nWarning: input file format specified in config file" +
                    " is not default .xlsx or .xlsm formats," +
                    " program failure likely.");
            System.out.println(this.getFormat() == ".xlsx");
            System.out.println("Current format: " + this.getFormat());
        }*/
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
        // populate batch file
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

    private boolean fileCheck() {
        // ensure there are two checksheets of specified file type in input folder
        boolean correctFiles = false;
        int fileCount = 0;
        String[] pathnames;
        File f = new File("../input");
        pathnames = f.list();

        // count the number of files ending
        for (String pathname: pathnames) {
            // hidden files are also rejected
            if (pathname.endsWith(this.getFormat()) && !pathname.startsWith(".")) {
                fileCount++;
            }
        }

        // correct file count
        if (fileCount == 2) {
            System.out.println("Checksheets located.");
            correctFiles = true;
            this.setFileOne(pathnames[0]);
            this.setFileTwo(pathnames[1]);
        }
        // incorrect file count
        else if (fileCount >= 0 && fileCount != 2) {
            System.out.println("Incorrect number of files of requested type.");
        }
        // fileCheck fails entirely
        else {
            System.out.println("Location error.");
        }

        // list located files
        System.out.println("Located files: ");
        if (fileCount == 2) {
            System.out.println(this.getFileOne() + "\n" + this.getFileTwo());
        }
        else if (fileCount >= 0 && fileCount != 2) {
            for (String pathname : pathnames) {
                System.out.println(pathname);
            }
        }
        return correctFiles;
    }

    // standard sets and gets
    private void setFileOne(String fileOne) { this.fileOne = fileOne; }

    public String getFileOne() { return (this.fileOne); }

    private void setFileTwo(String fileTwo) { this.fileTwo = fileTwo; }

    public String getFileTwo() { return (this.fileTwo); }

    private void setColCan(int colCan) { this.colCan = colCan; }

    public int getColCan() { return this.colCan; }

    private void setColCategory(int colCategory) { this.colCategory = colCategory; }

    public int getColCategory() { return this.colCategory; }

    private void setColFeature(int colFeature) { this.colFeature = colFeature; }

    public int getColFeature() { return this.colFeature; }

    private void setColTestCase(int colTestCase) { this.colTestCase = colTestCase; }

    public int getColTestCase() { return this.colTestCase; }

    private void setColUs(int colUs) { this.colUs = colUs; }

    public int getColUs() { return this.colUs; }

    private void setFormat(String format) { this.format = format; }

    public String getFormat() { return this.format; }

    private void setIncludeInvalid(boolean includeInvalid) { this.includeInvalid = includeInvalid; }

    public boolean getIncludeInvalid() { return this.includeInvalid; }

    private void setIncludeOther(boolean includeOther) { this.includeOther = includeOther; }

    public boolean getIncludeOther() { return this.includeOther; }

    private void setLeadingSheets(int leadingSheets) { this.leadingSheets = leadingSheets; }

    public int getLeadingSheets() { return this.leadingSheets; }

    private void setPlatformCol(int platformCol) { this.platformCol = platformCol; }

    public int getPlatformCol() { return this.platformCol; }

    private void setPlatformRow(int platformRow) { this.platformRow = platformRow; }

    public int getPlatformRow() { return this.platformRow; }

    private void setPlatformSheet(int platformSheet) { this.platformSheet = platformSheet; }

    public int getPlatformSheet() { return this.platformSheet; }

    private void setRowStart(int rowStart) { this.rowStart = rowStart; }

    public int getRowStart() { return this.rowStart; }

    private void setSheetCount(int sheetCount) { this.sheetCount = sheetCount; }

    public int getSheetCount() { return this.sheetCount;}
}
