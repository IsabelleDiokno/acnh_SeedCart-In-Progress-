package com.example.testcoffee;

public class transactionModel {
    int IDNum;
    String transDateTime, acctNm, cardType,transType, apprvdamt, ecrRef, authCode,
            hostCode, hostResponse, hostMessage, refNum, nameCard;

    public transactionModel() {
    }

    public transactionModel(int IDNum, String transDateTime, String acctNm, String cardType, String transType,
                            String apprvdamt, String ecrRef, String nameCard, String authCode, String hostCode,
                            String hostResponse, String hostMessage,
                            String refNum) {

        System.out.print("Inserting AcctNm: "+acctNm+" Host: "+hostResponse);

        this.IDNum = IDNum;
        this.transDateTime = transDateTime;
        this.acctNm = acctNm;
        this.cardType = cardType;
        this.transType = transType;
        this.apprvdamt = apprvdamt;
        this.ecrRef = ecrRef;
        this.nameCard = nameCard;
        this.authCode = authCode;
        this.hostCode = hostCode;
        this.hostResponse = hostResponse;
        this.hostMessage = hostMessage;
        this.refNum = refNum;

    }

    @Override
    public String toString() {

        String test =  "transactionModel{" +
                "intID='" + IDNum +'\'' +
                ", transDateTime='" + transDateTime + '\'' +
                ", acctNm='" + acctNm + '\'' +
                ", cardType='" + cardType + '\'' +
                ", transType='" + transType + '\'' +
                ", apprvdamt='" + apprvdamt + '\'' +
                ", ecrRef='" + ecrRef + '\'' +
                ", nameCard='" + nameCard + '\'' +
                ", authCode='" + authCode + '\'' +
                ", hostCode='" + hostCode + '\'' +
                ", hostResponse='" + hostResponse + '\'' +
                ", hostMessage='" + hostMessage + '\'' +
                ", refNum='" + refNum + '\'' +
                '}';
//        return "transactionModel{" +
//                "transDateTime='" + transDateTime + '\'' +
//                ", acctNm='" + acctNm + '\'' +
//                ", cardType='" + cardType + '\'' +
//                ", transType='" + transType + '\'' +
//                ", apprvdamt='" + apprvdamt + '\'' +
//                ", ecrRef='" + ecrRef + '\'' +
//                ", nameCard='" + nameCard + '\'' +
//                ", authCode='" + authCode + '\'' +
//                ", hostCode='" + hostCode + '\'' +
//                ", hostResponse='" + hostResponse + '\'' +
//                ", hostMessage='" + hostMessage + '\'' +
//                ", refNum='" + refNum + '\'' +
//                '}';
        System.out.println(test);
        return test;
    }

    public String getTransDateTime() {
        return transDateTime;
    }

    public void setTransDateTime(String transDateTime) {
        this.transDateTime = transDateTime;
    }

    public String getAcctNm() {

        System.out.print("Inserting: "+acctNm);
        return acctNm;
    }

    public void setAcctNm(String acctNm) {
        this.acctNm = acctNm;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getApprvdamt() {
        return apprvdamt;
    }

    public void setApprvdamt(String apprvdamt) {
        this.apprvdamt = apprvdamt;
    }

    public String getEcrRef() {
        return ecrRef;
    }

    public void setEcrRef(String ecrRef) {
        this.ecrRef = ecrRef;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getHostCode() {
        return hostCode;
    }

    public void setHostCode(String hostCode) {
        this.hostCode = hostCode;
    }

    public String getHostResponse() {
        return hostResponse;
    }

    public void setHostResponse(String hostResponse) {
        this.hostResponse = hostResponse;
    }

    public String getHostMessage() {
        return hostMessage;
    }

    public void setHostMessage(String hostMessage) {
        this.hostMessage = hostMessage;
    }

    public String getRefNum() {
        return refNum;
    }

    public void setRefNum(String refNum) {
        this.refNum = refNum;
    }

    public String getNameCard() {
        return nameCard;
    }

    public void setNameCard(String nameCard) {
        this.nameCard = nameCard;
    }
}
