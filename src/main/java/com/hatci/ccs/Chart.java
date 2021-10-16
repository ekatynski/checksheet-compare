package com.hatci.ccs;

import java.util.Arrays;

public class Chart {

    private CategorySet currentSet = null;
    private int[][] testCaseResults = null;
    private int width;
    private final int categoryCount;
    private int[] featureCounts = null;
    private String programName = null;
    private boolean[] featureIgnored = null;

    Chart(Checksheet sheet, CategorySet commonCatsAndFeatures, Configurator config) {
        System.out.println("Sheet category size:" + sheet.getCategories().get(8).getFeatureNames().size());

        currentSet = commonCatsAndFeatures;
        categoryCount = commonCatsAndFeatures.getCategoryCount();
        int totalFeatureCount = 0;
        programName = sheet.getCategories().get(0).getProgramName();

        System.out.println("Feature count breakdown: ");
        // count features per category
        featureCounts = new int[commonCatsAndFeatures.getCategoryCount()];
        for(int i = 0; i < categoryCount; i++) {
            featureCounts[i] = commonCatsAndFeatures.getFeatureCount(i);
            totalFeatureCount += featureCounts[i];
            System.out.println(i + ":\t" + featureCounts[i]);
        }

        // track which features are to be ignored in output sheet
        featureIgnored = new boolean[totalFeatureCount];
        Arrays.fill(featureIgnored, false);

        System.out.println("Total number of features: " + totalFeatureCount);

        // determine chart width depending on config settings include invalid/other cases or not
        width = 8;
        if ((config.getIncludeInvalid()) && (config.getIncludeOther())) {
            width = 10;
        }
        else if (config.getIncludeInvalid() != config.getIncludeOther()) {
            width = 9;
        }

        // matrix is double-wide to contain both American and Canadian results
        testCaseResults = new int[commonCatsAndFeatures.getTotalFeatureCount()][2*width];
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
                    System.out.println(sheet.getAllFeatures().get(sheetCategoryIndex) + " contains " + commonCatsAndFeatures.getTotalFeatureList().get(i).get(j) + ": "
                            + sheet.getAllFeatures().get(sheetCategoryIndex).contains(commonCatsAndFeatures.getTotalFeatureList().get(i).get(j)));
                    if(sheet.getAllFeatures().get(sheetCategoryIndex).contains(commonCatsAndFeatures.getTotalFeatureList().get(i).get(j))) {
                        // valid category contains a valid feature - populate results
                        // calculate the category feature list index corresponding to current master list feature
                        int categoryFeatureIndex = sheet.getCategories().get(sheetCategoryIndex).getFeatureNames().indexOf(commonCatsAndFeatures.getTotalFeatureList().get(i).get(j));
                        // US
                        for(int k = 0; k < width; k++) {
                            // copy test case results from US CaseCounter within current feature within current category
                            testCaseResults[listedFeatures + j][k] = ((sheet.getCategories().get(sheetCategoryIndex)).getFeatures().get(categoryFeatureIndex)).getUsResults()[k];
                            setIgnorance(listedFeatures + j, sheet.getCategories().get(sheetCategoryIndex).getFeatures().get(categoryFeatureIndex).getUsResults(), config);
                        }
                        // CAN -- offset to other half of Matrix (indices 10 and above)
                        for(int k = 0; k < width; k++) {
                            testCaseResults[listedFeatures + j][k + width] = ((sheet.getCategories().get(sheetCategoryIndex)).getFeatures().get(categoryFeatureIndex)).getCanResults()[k];
                            setIgnorance(listedFeatures + j, sheet.getCategories().get(sheetCategoryIndex).getFeatures().get(categoryFeatureIndex).getCanResults(), config);
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

        this.featureIgnored = new boolean[chartOne.featureIgnored.length];
        for(int i = 0; i < chartOne.featureIgnored.length; i++) {
            // ignore a feature if it's ignored on both sheets
            this.featureIgnored[i] = chartOne.featureIgnored[i] && chartTwo.featureIgnored[i];
        }

        // import data from other charts
        int[][] resultsOne = chartOne.getTestResults();
        int[][] resultsTwo = chartTwo.getTestResults();

        // iterate through the rows of the matrix
        for(int i = 0; i < resultsOne.length; i++) {
            for(int j = 0; j < resultsOne[0].length; j++) {
                // comparison chart results are determined subtractively
                this.testCaseResults[i][j] = resultsOne[i][j] - resultsTwo[i][j];
            }
        }
    }

    private void setIgnorance(int row, int[] testOutcomes, Configurator config) {
        // cycle through all of the outcomes
        boolean potentialIgnoreCase = true;
        for (int i = 0; i < testOutcomes.length; i++) {
            // if there are any non-invalid test results
            if (i < 8 && testOutcomes[i] > 0) {
                // skip the feature
                potentialIgnoreCase = false;
                break;
            }
        }
        if (potentialIgnoreCase) {
            if (!config.getIncludeInvalid() && !config.getIncludeOther()) {
                if (testOutcomes[8] > 0 || testOutcomes[9] > 0) {
                    featureIgnored[row] = true;
                }
            }
            // if invalid cases are excluded but not "other"
            else if (!config.getIncludeInvalid()) {
                // if there are invalid cases but no "other" cases
                if (testOutcomes[8] > 0 && testOutcomes[9] == 0) {
                    featureIgnored[row] = true;
                }
            }
            // if "other" cases are excluded but not invalid
            else if (!config.getIncludeOther()) {
                // if there are "other" cases but no invalid cases
                if (testOutcomes[9] > 0 && testOutcomes[8] == 0) {
                    featureIgnored[row] = true;
                }
            }
        }
    }

    public boolean[] getIgnoredRows() {
        return featureIgnored;
    }

    public CategorySet getCategories() {
        return currentSet;
    }

    public int[] getFeatureCounts() {
        return featureCounts;
    }

    public int[][] getTestResults() {
        return testCaseResults;
    }

    public int getWidth() {
        return width;
    }

    public int getCategoryCount() {
        return categoryCount;
    }

    public String getProgramName() {
        return programName;
    }

    public String toString() {
        StringBuilder output = new StringBuilder("\nUS:\t | \tCAN:\n----------------\n");
        int offset = 0;

        for(int i = 0; i < categoryCount; i++) {
            // place category name
            output.append("\n").append(currentSet.getAllCategories().get(i)).append("\n");
            // place feature name
            for(int j = 0; j < featureCounts[i]; j++) {
                output.append("\t").append(currentSet.getTotalFeatureList().get(i).get(j)).append("(").append(offset + j).append(")").append("\n\t\t");
                // place test results
                for(int k = 0; k < width * 2; k++) {
                    if (k == width ) {
                      output.append(" | \t");
                    }
                    output.append(testCaseResults[offset + j][k]).append("\t");
                }
                output.append("\n");
            }
        }
        return output.toString();
    }
}
