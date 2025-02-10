package com.example.utils;

public class MosaicRequest {

    private String imageBase64;
    private int rows;
    private int cols;

    public MosaicRequest() {
        // Пустой конструктор для десериализации JSON
    }

    // Геттеры и сеттеры для полей запроса

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }
}
