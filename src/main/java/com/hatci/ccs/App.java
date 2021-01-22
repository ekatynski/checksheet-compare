package com.hatci.ccs;

import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class App {
    public static void main(String[] args) {
        XSSFWorkbook wb = new XSSFWorkbook();

        Configurator myConfig = new Configurator();
        if(myConfig.checkAll()) {

        }
        else {
            System.out.println("Additional configuration needed." +
                    " Try re-running the program or deleting the config file." +
                    " Check console output for info.");
        }
    }
}
