package com.hatci.ccs;

public class App {
    public static void main(String[] args) {
        // configure program
        Configurator myConfig = new Configurator();
        // if configuration pre-check passed
        if(myConfig.checkAll()) {
            // create Checksheet objects using imported files
            Checksheet checksheet1 = new Checksheet(myConfig.getFileOne(), myConfig);
            Checksheet checksheet2 = new Checksheet(myConfig.getFileTwo(), myConfig);

            // compile common categories and features between lists
            CategorySet commonCategories = new CategorySet(checksheet1, checksheet2);

            // create chart objects using the common category list, configuration settings, and the checksheets themselves
            Chart chart1 = new Chart(checksheet1, commonCategories, myConfig);
            Chart chart2 = new Chart(checksheet2, commonCategories, myConfig);

            // create comparison chart object using two prior chart objects
            Chart comparisonChart = new Chart(chart1, chart2, commonCategories);
        }
        // configuration pre-check failed
        else {
            System.out.println("\nAdditional configuration needed." +
                    " Try re-running the program, checking your input files," +
                    " or deleting the config file." +
                    " Check console output for info.");
        }
    }
}
