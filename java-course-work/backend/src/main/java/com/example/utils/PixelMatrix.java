package com.example.utils;

import java.util.List;

public class PixelMatrix {

    private int rows;
    private int cols;
    private int[] pixels;
    private List<int[]> colorPalette;

    public PixelMatrix(int width, int height, List<int[]> colorPalette, int[][] indexMatrix) {
        this.cols = width;
        this.rows = height;
        this.colorPalette = colorPalette;
        this.pixels = new int[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                pixels[i * rows + cols] = indexMatrix[i][j];
            }
        }
    }

    public int[] getPixels() {
        return pixels;
    }

    public List<int[]> getColorPalette() {
        return colorPalette;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
