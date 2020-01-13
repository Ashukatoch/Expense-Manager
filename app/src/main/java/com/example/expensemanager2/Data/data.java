package com.example.expensemanager2.Data;

public class data
{
    private int amount;
    private String id,date,type,note;

    data()
    {}
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public data(int amount, String id, String date, String type, String note) {
        this.amount = amount;
        this.id = id;
        this.date = date;
        this.type = type;
        this.note = note;
    }
}
