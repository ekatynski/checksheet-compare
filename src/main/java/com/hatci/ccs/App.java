package com.hatci.ccs;

import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class App {
    public static void main(String[] args) {

        File f = null;
        boolean bool = false;

        try {
            // create new file
            f = new File("../testSheet.xlsx");

            // tries to create new file in the system
            bool = f.createNewFile();

            // prints
            System.out.println("File created: "+bool);
            System.out.println("File exists: "+f.exists());

        } catch(Exception e) {
            e.printStackTrace();
        }

        Configurator myConfig = new Configurator();


    }
}
