package com.example.LoggerTypes;

import java.util.List;

public class ReqGroupData<T> {
    private List<T> list;
    private Integer number;

    // Constructor
    public ReqGroupData(List<T> list, Integer number) {
        this.list = list;
        this.number = number;
    }

    // Getters
    public List<T> getList() {
        return list;
    }

    public Integer getNumber() {
        return number;
    }

    // Setters
    public void setList(List<T> list) {
        this.list = list;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "ReqGroupData{" +
                "list=" + list +
                ", number=" + number +
                '}';
    }
}
