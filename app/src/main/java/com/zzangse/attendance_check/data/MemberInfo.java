package com.zzangse.attendance_check.data;

public class MemberInfo {
    private int priNum;
    private String infoGroupName, infoName, infoNumber, infoNumber2, infoAddress, infoMemo;

    public MemberInfo() {

    }

    public MemberInfo(int priNum,String infoName) {
        this.priNum = priNum;
        this.infoName = infoName;
    }

    public MemberInfo(String infoGroupName, String infoName, String infoNumber, String infoNumber2, String infoAddress, String infoMemo) {
        this.infoGroupName = infoGroupName;
        this.infoName = infoName;
        this.infoNumber = infoNumber;
        this.infoNumber2 = infoNumber2;
        this.infoAddress = infoAddress;
        this.infoMemo = infoMemo;
    }

    public int getPriNum() {
        return priNum;
    }

    public void setPriNum(int priNum) {
        this.priNum = priNum;
    }

    public String getInfoGroupName() {
        return infoGroupName;
    }

    public void setInfoGroupName(String infoGroupName) {
        this.infoGroupName = infoGroupName;
    }

    public String getInfoName() {
        return infoName;
    }

    public void setInfoName(String infoName) {
        this.infoName = infoName;
    }

    public String getInfoNumber() {
        return infoNumber;
    }

    public void setInfoNumber(String infoNumber) {
        this.infoNumber = infoNumber;
    }

    public String getInfoNumber2() {
        return infoNumber2;
    }

    public void setInfoNumber2(String infoNumber2) {
        this.infoNumber2 = infoNumber2;
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

    @Override
    public String toString() {
        return "MemberInfo{" +
                "priNum=" + priNum +
                ", infoGroupName='" + infoGroupName + '\'' +
                ", infoName='" + infoName + '\'' +
                ", infoNumber='" + infoNumber + '\'' +
                ", infoNumber2='" + infoNumber2 + '\'' +
                ", infoAddress='" + infoAddress + '\'' +
                ", infoMemo='" + infoMemo + '\'' +
                '}';
    }
}

