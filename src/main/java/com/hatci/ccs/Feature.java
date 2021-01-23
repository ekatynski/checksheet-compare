package com.hatci.ccs;

public class Feature {
    private String name;
    private int sheetIndex;  // refers to category/checksheet page of said feature
    private CaseCounter usTally = new CaseCounter();
    private CaseCounter canTally = new CaseCounter();

    public Feature(String name) {
        this.name = name;
        this.sheetIndex = sheetIndex;
    }

    public String getName() {
        return this.name;
    }

    public int getSheetIndex() {
        return this.sheetIndex;
    }

    public String toString() {
        return ("\nUS: " + this.name + " (sheet " + this.sheetIndex + ")\n"
                + "-----------------\n"
                + usTally.toString()
                + "\nCAN: " + this.name + " (sheet " + this.sheetIndex + ")\n"
                + "-----------------\n"
                + canTally.toString()
        );
    }

    public void processUsCase(String result) {
        this.usTally.processCase(result);
    }

    public void processCanCase(String result) {
        this.canTally.processCase(result);
    }
}
