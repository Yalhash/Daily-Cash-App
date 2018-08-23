package com.example.youse.financemanager;

import java.io.Serializable;

public class financeModule implements Serializable{
    private String financeName;
    private String financeNotes;
    private Boolean posNeg;
    private Integer value;


    public String getFinanceName() {
        return financeName;
    }

    public Boolean getPosNeg() {
        return posNeg;
    }

    public Integer getValue() { return value; }

    public String getValueText(){
        return "$" + value.toString();

    }

    public String getNotes() { return financeNotes; }


    public Integer getPic(){
        if (posNeg){
            return R.drawable.ic_up;
        }else{
            return R.drawable.ic_down;
        }
    }



    public void setFinanceName(String name) {
        this.financeName = name;
    }

    public void setNotes(String notes) {
        this.financeNotes = notes;
    }

    public void setPosNeg(Boolean isPositive) {
        this.posNeg = isPositive;
    }

    public void setValue(Integer value) {
        this.value = value;
    }


    financeModule(String name, Boolean isPositive, Integer cashValue){
        this.financeName = name;
        this.posNeg = isPositive;
        this.value = cashValue;
        this.financeNotes = "";
    }


    financeModule(String name, Boolean isPositive, Integer cashValue, String notes){
        this.financeName = name;
        this.posNeg = isPositive;
        this.value = cashValue;
        this.financeNotes = notes;
    }

}
