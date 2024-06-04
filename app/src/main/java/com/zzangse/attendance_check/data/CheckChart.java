package com.zzangse.attendance_check.data;

public class CheckChart {
    String infoCheck, checkCount;

    public CheckChart(String infoCheck, String checkCount) {
        this.infoCheck = infoCheck;
        this.checkCount = checkCount;
    }

    public String getInfoCheck() {
        return infoCheck;
    }

    public void setInfoCheck(String infoCheck) {
        this.infoCheck = infoCheck;
    }

    public String getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(String checkCount) {
        this.checkCount = checkCount;
    }
}
