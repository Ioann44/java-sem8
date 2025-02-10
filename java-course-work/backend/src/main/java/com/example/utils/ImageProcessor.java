package com.example.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageProcessor {

	public static PixelMatrix resizeImage(String imagePath, int targetWidth, int targetHeight) throws IOException {
		File file = new File(imagePath);
		BufferedImage originalImage = ImageIO.read(file);

		BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		resizedImage.createGraphics().drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);

		List<int[]> colorPalette = extractColorPalette(resizedImage);
		int[][] indexMatrix = createIndexMatrix(resizedImage, colorPalette);

		return new PixelMatrix(resizedImage.getWidth(), resizedImage.getHeight(), colorPalette, indexMatrix);
	}

	private static List<int[]> extractColorPalette(BufferedImage image) {
		List<int[]> colorPalette = new ArrayList<>();
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int color = image.getRGB(x, y);
				int red = (color >> 16) & 0xFF;
				int green = (color >> 8) & 0xFF;
				int blue = color & 0xFF;
				int[] rgb = { red, green, blue };
				if (!colorPalette.contains(rgb)) {
					colorPalette.add(rgb);
				}
			}
		}
		return colorPalette;
	}

	private static int[][] createIndexMatrix(BufferedImage image, List<int[]> colorPalette) {
		int[][] indexMatrix = new int[image.getHeight()][image.getWidth()];
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int color = image.getRGB(x, y);
				int red = (color >> 16) & 0xFF;
				int green = (color >> 8) & 0xFF;
				int blue = color & 0xFF;
				int[] rgb = { red, green, blue };
				int index = colorPalette.indexOf(rgb);
				indexMatrix[y][x] = index;
			}
		}
		return indexMatrix;
	}
}
