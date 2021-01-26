package com.hatci.ccs;

public class App {
    public static void main(String[] args) {
        Configurator myConfig = new Configurator();
        if(myConfig.checkAll()) {
            Checksheet checksheet1 = new Checksheet(myConfig.getFileOne(), myConfig);
            Checksheet checksheet2 = new Checksheet(myConfig.getFileTwo(), myConfig);
            CategorySet commonCategories = new CategorySet(checksheet1, checksheet2);
            //System.out.println(commonCategories);
            Chart chart1 = new Chart(checksheet1, commonCategories, myConfig);
            System.out.println(chart1);
        }
        else {
            System.out.println("\nAdditional configuration needed." +
                    " Try re-running the program, checking your input files," +
                    " or deleting the config file." +
                    " Check console output for info.");
        }
    }
}