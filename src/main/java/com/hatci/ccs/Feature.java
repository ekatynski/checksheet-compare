package com.hatci.ccs;

public class Feature {
    private String name;
    private int sheetIndex;  // refers to category/checksheet page of said feature
    private CaseCount usTally;
    private CaseCount canTally;

    public Feature(String name, int sheetIndex) {
        this.name = name;
        this.sheetIndex = sheetIndex;
        CaseCount usTally = new CaseCount();
        CaseCount canTally = new CaseCount();
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
}