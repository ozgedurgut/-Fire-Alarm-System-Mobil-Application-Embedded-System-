package com.ozge.alarmsistemapp;

public class Canli {

    String canliid;
    String canliAd;
    String canliNo;
    String canliWho;
    String info;

    public Canli(){}

    public Canli(String canliid, String canliAd, String canliNo, String canliWho) {
        this.canliid = canliid;
        this.canliAd = canliAd;
        this.canliNo = canliNo;
        this.canliWho = canliWho;
    }

    public String getCanliid() {
        return canliid;
    }

    public void setCanliid(String canliid) {
        this.canliid = canliid;
    }

    public String getCanliAd() {
        return canliAd;
    }

    public void setCanliAd(String canliAd) {
        this.canliAd = canliAd;
    }

    public String getCanliNo() {
        return canliNo;
    }

    public void setCanliNo(String canliNo) {
        this.canliNo = canliNo;
    }

    public String getCanliWho() {
        return canliWho;
    }

    public void setCanliWho(String canliWho) {
        this.canliWho = canliWho;
    }
}
