package web;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

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

        Font font = new Font("Times New Roman", Font.BOLD, 72);
        graphics.setFont(font);
        graphics.setColor(Color.BLACK);
        graphics.drawString("Hello World!", 100, 100);

        response.setContentType("image/png");
        javax.imageio.ImageIO.write(image, "png", response.getOutputStream());
    }
}