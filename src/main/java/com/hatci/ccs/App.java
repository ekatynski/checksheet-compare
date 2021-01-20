package com.hatci.ccs;

public class App {
    public static void main(String[] args) {


        Feature testFeat = new Feature("User Profile", 3);
        testFeat.usTally.incPass();
        testFeat.canTally.incBlocked();
        System.out.println(testFeat);
    }
}
