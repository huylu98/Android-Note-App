package com.example.maintenance;

public class Note {
    private int id;
    private String date;
    private String task;
    private String amount;
    private String distance;


    public Note(int id, String date, String task, String amount, String distance) {
        this.id = id;
        this.date = date;
        this.task = task;
        this.amount = amount;
        this.distance = distance;
    }

    public Note(String date, String task, String amount, String distance) {
        this.date = date;
        this.task = task;
        this.amount = amount;
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
