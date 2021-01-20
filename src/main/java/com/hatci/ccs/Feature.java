package com.hatci.ccs;

public class Feature {
    private String name;
    private int sheet;  // refers to category/checksheet page of said feature
    CaseCount usTally = new CaseCount();
    CaseCount canTally = new CaseCount();

    public Feature(String name, int sheet) {
        this.name = name;
        this.sheet = sheet;
    }

    public String getName() {
        return this.name;
    }

    public int getSheet() {
        return this.sheet;
    }

    public String toString() {
        return ("\nUS: " + this.name + " (sheet " + this.sheet + ")\n"
                + "-----------------\n"
                + usTally.toString()
                + "\nCAN: " + this.name + " (sheet " + this.sheet + ")\n"
                + "-----------------\n"
                + canTally.toString()
        );
    }
}
