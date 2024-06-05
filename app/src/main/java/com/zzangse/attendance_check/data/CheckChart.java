package com.zzangse.attendance_check.data;

public class CheckChart {
    String infoCheck, checkCount, infoName, attendanceCount, tardyCount, absenceCount;

    // 차트 생성자
    public CheckChart(String infoCheck, String checkCount) {
        this.infoCheck = infoCheck;
        this.checkCount = checkCount;
    }

    // 차트 리싸이클러뷰 생성자
    public CheckChart(String infoName, String attendanceCount, String tardyCount, String absenceCount) {
        this.infoName = infoName;
        this.attendanceCount = attendanceCount;
        this.tardyCount = tardyCount;
        this.absenceCount = absenceCount;
    }

    public String getInfoName() {
        return infoName;
    }

    public void setInfoName(String infoName) {
        this.infoName = infoName;
    }

    public String getAttendanceCount() {
        return attendanceCount;
    }

    public void setAttendanceCount(String attendanceCount) {
        this.attendanceCount = attendanceCount;
    }

    public String getTardyCount() {
        return tardyCount;
    }

    public void setTardyCount(String tardyCount) {
        this.tardyCount = tardyCount;
    }

    public String getAbsenceCount() {
        return absenceCount;
    }

    public void setAbsenceCount(String absenceCount) {
        this.absenceCount = absenceCount;
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
