package com.hatci.ccs;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Configurator {

    // ensures all regular use directories are present
    public Configurator() {
        this.directoryCheck("config");  // contains config.json file
        this.directoryCheck("input");   // where checksheet files are placed
        this.directoryCheck("output");  // where the comparison chart is saved
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
}
