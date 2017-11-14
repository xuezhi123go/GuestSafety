package com.gkzxhn.xjyyzs.entities;

/**
 * Created by Raleigh.Luo on 17/6/8.
 */

public class RecordEntity {
    private String name;
    private long time;
    private String cardNumber;
    private String relate;//与囚犯的关系
    private String prisionId;
    private boolean isClick;

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public String getPrisionId() {
        return prisionId;
    }

    public void setPrisionId(String prisionId) {
        this.prisionId = prisionId;
    }

    public RecordEntity(){}
    public RecordEntity(String name,String cardnumber,String relate,String prisionId){
        this.name=name;
        this.time=System.currentTimeMillis();
        this.cardNumber=cardnumber;
        this.relate=relate;
        this.prisionId=prisionId;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public String getCardNumber() {
        return cardNumber == null ? "" : cardNumber;
    }

    public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
    }

    public String getRelate() {
        return relate == null ? "" : relate;
    }

    public void setRelate(String relate) {
            this.relate = relate;
    }

    public void setName(String name) {
            this.name = name;
    }

    public long getTime() {
        return time ;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
