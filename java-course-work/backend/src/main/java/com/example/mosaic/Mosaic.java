package com.example.mosaic;

import java.util.List;

public class Mosaic {
    public int id;
    private String imageBase64;
    private int[] colorPalette;
    private int[] indexMatrix;
    private int rows;
    private int cols;
    private long hash;

    public Mosaic() {
        this.id = 0;
    }

    // Геттеры и сеттеры для полей мозаики

    public Mosaic(int id, String imageBase642, int[] colorPalette2, int[] compressedIndices, int numRows, int numCols,
            long hash2) {
        this.id = id;
        this.imageBase64 = imageBase642;
        this.colorPalette = colorPalette2;
        this.indexMatrix = compressedIndices;
        this.rows = numRows;
        this.cols = numCols;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public int[] getColorPalette() {
        return colorPalette;
    }

    public void setColorPalette(int[] colorPalette) {
        this.colorPalette = colorPalette;
    }

    public int[] getIndexMatrix() {
        return indexMatrix;
    }

    public void setIndexMatrix(int[] indexMatrix) {
        this.indexMatrix = indexMatrix;
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

    public long getHash() {
        return hash;
    }

    public void setHash(long hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "Mosaic{" +
                "imageBase64='" + imageBase64 + '\'' +
                ", colorPalette=" + colorPalette +
                ", rows=" + rows +
                ", cols=" + cols +
                ", hash=" + hash +
                '}';
    }
}
