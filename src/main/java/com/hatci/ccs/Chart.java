package com.hatci.ccs;

public class Chart {

    private String[] resultType = {"TOTAL", "TESTED", "PASS", "FAIL", "N/A",
    "NOT TESTED", "BLOCKED", "SINGLE", "INVALID", "OTHER"};

    private CategorySet currentSet = null;
    private int[][] testCaseResults = null;
    private int width;
    private int categoryCount;
    private int[] featureCounts = null;
    private String programName = null;
    private int totalFeatureCount;

    Chart(Checksheet sheet, CategorySet commonCatsAndFeatures, Configurator config) {
        currentSet = commonCatsAndFeatures;
        categoryCount = commonCatsAndFeatures.getCategoryCount();
        totalFeatureCount = 0;
        programName = sheet.getCategories().get(0).getProgramName();

        System.out.println("Feature count breakdown: ");
        // count features per category
        featureCounts = new int[commonCatsAndFeatures.getCategoryCount()];
        for(int i = 0; i < categoryCount; i++) {
            featureCounts[i] = commonCatsAndFeatures.getFeatureCount(i);
            totalFeatureCount += featureCounts[i];
            System.out.println(i + ":\t" + featureCounts[i]);
        }

        System.out.println("Total number of features: " + totalFeatureCount);

        // determine chart width depending on config settings include invalid/other cases or not
        width = 8;
        if ((config.getIncludeInvalid() == true) && (config.getIncludeOther() == true)) {
            width = 10;
        }
        else if (config.getIncludeInvalid() != config.getIncludeOther()) {
            width = 9;
        }

        // matrix is double-wide to contain both American and Canadian results
        testCaseResults = new int[commonCatsAndFeatures.getTotalFeatureCount()][2*width];
        System.out.println("Test Case Result Rows: " + testCaseResults.length);
        System.out.println("Test Case Result Cols: " + testCaseResults[0].length);



        commonCatsAndFeatures.getAllCategories();

        int listedFeatures = 0;
        // iterate through all categories in common category list
        for(int i = 0; i < config.getSheetCount(); i++) {
            // check that this checksheet contains said category
            if (sheet.getCategoryNames().contains(commonCatsAndFeatures.getAllCategories().get(i))) {
                // calculate the checksheet category list index corresponding to current master list category
                int sheetCategoryIndex = sheet.getCategoryNames().indexOf(commonCatsAndFeatures.getAllCategories().get(i));
                // iterate through all features in common category list
                for(int j = 0; j < commonCatsAndFeatures.getFeatureCount(i); j++) {
                    // if feature set for current checksheet category contains a common-list feature
                    if(sheet.getAllFeatures().get(sheetCategoryIndex).contains(commonCatsAndFeatures.getTotalFeatureList().get(i).get(j))) {
                        // valid category contains a valid feature - populate results
                        // calculate the category feature list index corresponding to current master list feature
                        int categoryFeatureIndex = sheet.getCategories().get(sheetCategoryIndex).getFeatureNames().indexOf(commonCatsAndFeatures.getTotalFeatureList().get(i).get(j));
                        // US
                        System.out.println("WIDTH: " + 2*width);
                        System.out.println("Inserting feature: " + currentSet.getTotalFeatureList().get(i).get(j));
                        for(int k = 0; k < width; k++) {
                            // copy test case results from US CaseCounter within current feature within current category
                            // ERROR HERE
                            System.out.println("Index before: " + (listedFeatures + j));
                            System.out.println("ROW: " + (listedFeatures + j) + "\tCOL: " + k);
                            System.out.println("Sheet Category Index: " + sheetCategoryIndex);
                            System.out.println("Sheet Category Size: " + sheet.getCategories().size());
                            System.out.println("Category Feature Index: " + categoryFeatureIndex);
                            System.out.println("Category Feature Size: " + sheet.getCategories().get(sheetCategoryIndex).getFeatures().size());
                            // PROBLEM IN COUNTING ON SHEET TO HARBOR ALL RESULTS - categoryFeatureIndex is causing the issue!
                            testCaseResults[listedFeatures + j][k] = ((sheet.getCategories().get(sheetCategoryIndex)).getFeatures().get(categoryFeatureIndex)).getUsResults()[k];

                            System.out.println("Index after: " + (listedFeatures + j));
                        }
                        // CAN -- offset to other half of Matrix (indices 10 and above)
                        for(int k = 0; k < width; k++) {
                            // LIKELY SUBSEQUENT ERROR HERE
                            //testCaseResults[listedFeatures + j][k + width] = ((sheet.getCategories().get(sheetCategoryIndex)).getFeatures().get(categoryFeatureIndex)).getCanResults()[k];
                        }
                    }
                    else {
                        // no valid test cases exist for this feature - mark all as zero
                        for(int k = 0; k < 2*width; k++) {
                            testCaseResults[listedFeatures + j][k] = 0;
                        }
                    }
                }
            }
            // if category is not contained in said checksheet
            else {
                // no test cases for this category will exist; mark everything zero
                // iterate through category features
                for(int j = 0; j < commonCatsAndFeatures.getFeatureCount(i); j++) {


                    // iterate through all columns visible on finished chart
                    for(int k = 0; k < width; k++) {
                        testCaseResults[listedFeatures + j][k] = 0;
                    }
                }
            }
            // track rows that are already populated
            listedFeatures += commonCatsAndFeatures.getFeatureCount(i);
        }
    }

    public Chart(Chart chartOne, Chart chartTwo, CategorySet commonCatsAndFeatures) {
        // gather common test data
        this.currentSet = chartOne.getCategories();
        this.width = chartOne.width;
        this.featureCounts = chartOne.featureCounts;
        this.categoryCount = chartOne.categoryCount;
        this.programName = chartOne.getProgramName() + " / " + chartTwo.getProgramName() + " Comparison";
        this.testCaseResults = new int[commonCatsAndFeatures.getTotalFeatureCount()][2*width];
        System.out.println("commonCatsAndFeatures feature count: " + commonCatsAndFeatures.getTotalFeatureCount());

        // import data from other charts
        int resultsOne[][] = chartOne.getTestResults();
        int resultsTwo[][] = chartTwo.getTestResults();

        // iterate through the rows of the matrix

        System.out.println("CHART ROWS: ");
        System.out.println("CHART COLUMNS: ");
        System.out.println("FEATURE:\tRESULT, CHARTONE, CHARTTWO");
        for(int i = 0; i < resultsOne.length; i++) {

            for(int j = 0; j < resultsOne[0].length; j++) {
                // comparison chart results are determined subtractively
                this.testCaseResults[i][j] = resultsOne[i][j] - resultsTwo[i][j];
                System.out.print(testCaseResults[i][j] + "," + resultsOne[i][j] + "," + resultsTwo[i][j] + "\t");
            }
            System.out.println("");
        }
    }


    private CategorySet getCategories() {
        return currentSet;
    }

    private int[] getFeatureCounts() {
        return featureCounts;
    }

    private int[][] getTestResults() {
        return testCaseResults;
    }

    private int getWidth() {
        return width;
    }

    private int getCategoryCount() {
        return categoryCount;
    }

    private String getProgramName() {
        return programName;
    }

    public String toString() {
        String output = "\nUS:\t\t\tCAN:\n----------------\n";
        int offset = 0;

        for(int i = 0; i < categoryCount; i++) {
            // place category name
            output += "\n" + currentSet.getAllCategories().get(i) + "\n";
            // place feature name
            for(int j = 0; j < featureCounts[i]; j++) {
                output += "\t" + currentSet.getTotalFeatureList().get(i).get(j) + "(" + (offset + j) + ")" + "\n\t\t";
                // place test results
                for(int k = 0; k < width * 2; k++) {
                    if (k == width ) {
                      output += " | \t";
                    }
                    output += testCaseResults[offset + j][k] + "\t";
                }
                output += "\n";
            }
        }
        return output;
    }
}
