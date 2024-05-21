package com.zzangse.attendance_check.data;

public class UserMember {
    private int priNum;
    private String userID, groupName, infoName, infoPhoneNumber, infoPhoneNumber2, infoAddress, infoMemo, infoCheck, infoDate;

    public UserMember() {
    }

    public UserMember(int priNum, String userID, String groupName, String infoName, String infoPhoneNumber, String infoPhoneNumber2, String infoAddress, String infoMemo) {
        this.priNum = priNum;
        this.userID = userID;
        this.groupName = groupName;
        this.infoName = infoName;
        this.infoPhoneNumber = infoPhoneNumber;
        this.infoPhoneNumber2 = infoPhoneNumber2;
        this.infoAddress = infoAddress;
        this.infoMemo = infoMemo;
    }

    public int getPriNum() {
        return priNum;
    }

    public void setPriNum(int priNum) {
        this.priNum = priNum;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getInfoName() {
        return infoName;
    }

    public void setInfoName(String infoName) {
        this.infoName = infoName;
    }

    public String getInfoPhoneNumber() {
        return infoPhoneNumber;
    }

    public void setInfoPhoneNumber(String infoPhoneNumber) {
        this.infoPhoneNumber = infoPhoneNumber;
    }

    public String getInfoPhoneNumber2() {
        return infoPhoneNumber2;
    }

    public void setInfoPhoneNumber2(String infoPhoneNumber2) {
        this.infoPhoneNumber2 = infoPhoneNumber2;
    }

    public String getInfoAddress() {
        return infoAddress;
    }

    public void setInfoAddress(String infoAddress) {
        this.infoAddress = infoAddress;
    }

    public String getInfoMemo() {
        return infoMemo;
    }

    public void setInfoMemo(String infoMemo) {
        this.infoMemo = infoMemo;
    }

    public String getInfoCheck() {
        return infoCheck;
    }

    public void setInfoCheck(String infoCheck) {
        this.infoCheck = infoCheck;
    }

    public String getInfoDate() {
        return infoDate;
    }

    public void setInfoDate(String infoDate) {
        this.infoDate = infoDate;
    }
}
