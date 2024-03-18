package com.example;

import java.io.IOException;
import java.util.Random;
import java.awt.*;
import java.awt.image.BufferedImage;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/picture.png")
public class PictureServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int width = 640;
        int height = 120;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();

        Random random = new Random();
        graphics.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        graphics.fillRect(0, 0, width, height);

        Font font = new Font("Times New Roman", Font.BOLD, 40);
        graphics.setFont(font);
        graphics.setColor(Color.BLACK);
        graphics.drawString("Mark my work as Z instead of V!", 20, 70);

        response.setContentType("image/png");
        javax.imageio.ImageIO.write(image, "png", response.getOutputStream());
    }
}
