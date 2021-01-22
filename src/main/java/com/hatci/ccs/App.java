package com.hatci.ccs;

public class App {
    public static void main(String[] args) {
        Configurator myConfig = new Configurator();
        if(myConfig.checkAll()) {
            //Checksheet checksheet1 = new Checksheet(myConfig.getFileOne());
        }
        else {
            System.out.println("\nAdditional configuration needed." +
                    " Try re-running the program, checking your input files," +
                    " or deleting the config file." +
                    " Check console output for info.");
        }

        Checksheet checksheet1 = new Checksheet(myConfig.getFileOne());

    }
}
