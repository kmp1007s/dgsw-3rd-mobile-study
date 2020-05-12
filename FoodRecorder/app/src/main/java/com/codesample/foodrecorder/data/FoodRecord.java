package com.codesample.foodrecorder.data;

public class FoodRecord {
    private int id;
    private String food;
    private String time;

    public FoodRecord() {}
    public FoodRecord(String food, String time) {
        this.food = food;
        this.time = time;
    }
    public FoodRecord(int id, String food, String time) {
        this.id=id;
        this.food=food;
        this.time=time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
