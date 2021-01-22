package com.hatci.ccs;

public class CaseCounter {

    // tally for each test case type per relevant feature
    private int total;      // total includes tested and not tested
    private int tested;     // tested includes passes, fails, and singles
    private int pass;
    private int fail;
    private int na;
    private int notTested;  // includes blocked and not-tested
    private int blocked;
    private int single;
    private int invalid;    // for cases marked "invalid" in Test Case Type/Category
    private int other;      // for cases mismarked in the result column

    public CaseCounter() {
        this.total = 0;
        this.tested = 0;
        this.pass = 0;
        this.fail = 0;
        this.na = 0;
        this.notTested = 0;
        this.blocked = 0;
        this.single = 0;
        this.invalid = 0;
        this.other = 0;
    }

    public void processCase(String result) {
        switch (result.toUpperCase()) {

        }
    }

    public void incTotal() {
        this.total++;
    }

    public void incTested() {
        this.tested++;
        this.incTotal();
    }

    public void incPass() {
        this.pass++;
        this.incTested();
    }

    public void incFail() {
        this.fail++;
        this.incTested();
    }

    public void incNA() {
        this.na++;
    }

    public void incNotTested() {
        this.notTested++;
        this.incTotal();
    }

    public void incBlocked() {
        this.blocked++;
        this.incNotTested();  // "blocked" test cases are considered "not tested"
    }

    public void incSingle() {
        this.single++;
        this.incTested();
    }

    public void incInvalid() {
        this.invalid++;
    }

    public void incOther() {
        this.other++;
    }

    public int getTotal() {
        return this.total;
    }

    public int getTested() {
        return this.tested;
    }

    public int getPass() {
        return this.pass;
    }

    public int getFail() {
        return this.fail;
    }

    public int getNA() {
        return this.na;
    }

    public int getNotTested() {
        return this.notTested;
    }

    public int getBlocked() {
        return this.blocked;
    }

    public int getSingle() {
        return this.single;
    }

    public int getInvalid() {
        return this.invalid;
    }

    public int getOther() {
        return this.other;
    }

    public String toString() {
        return ("Total: \t\t" + this.getTotal()
                + "\nTested: \t" + this.getTested()
                + "\nPass: \t\t" + this.getPass()
                + "\nFail: \t\t" + this.getFail()
                + "\nN/A: \t\t" + this.getNA()
                + "\nNot Tested: \t" + this.getNotTested()
                + "\nBlocked: \t" + this.getBlocked()
                + "\nSingle: \t" + this.getSingle()
                + "\nInvalid: \t" + this.getInvalid()
                + "\nOther: \t\t" + this.getOther()
                + "\n");
    }
}