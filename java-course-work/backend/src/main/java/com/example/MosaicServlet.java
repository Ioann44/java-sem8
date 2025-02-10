package com.example;

import com.example.mosaic.Mosaic;
import com.example.mosaic.MosaicDAO;
import com.example.utils.DatabaseConnector;
import com.example.utils.MosaicRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

@WebServlet("/api/mosaic/*")
public class MosaicServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MosaicDAO mosaicDAO = new MosaicDAO(DatabaseConnector.getConnection());

    public MosaicServlet() throws SQLException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            // Если запрос на получение всех мозаик
            List<Mosaic> mosaics = mosaicDAO.getAllMosaics();
            out.println(new ObjectMapper().writeValueAsString(mosaics));
        } else {
            // Если запрос на получение мозаики по ID
            String mosaicIdStr = pathInfo.substring(1); // Извлечь ID из URI
            try {
                int mosaicId = Integer.parseInt(mosaicIdStr);
                Mosaic mosaic = mosaicDAO.getMosaicById(mosaicId);
                if (mosaic != null) {
                    out.println(new ObjectMapper().writeValueAsString(mosaic));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.println("Mosaic not found");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("Invalid mosaic ID");
            }
        }
    }

    // @Override
    // protected void doPost(HttpServletRequest request, HttpServletResponse response)
    //         throws ServletException, IOException {
    //     try {
    //         // Получаем тело запроса (данные изображения) из потока ввода
    //         BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
    //         StringBuilder requestBody = new StringBuilder();
    //         String line;
    //         while ((line = reader.readLine()) != null) {
    //             requestBody.append(line);
    //         }
    //         reader.close();

    //         // Получаем параметры из JSON тела запроса
    //         MosaicRequest mosaicRequest = objectMapper.readValue(requestBody.toString(), MosaicRequest.class);

    //         // Декодируем изображение из base64 в байтовый массив
    //         byte[] imageBytes = Base64.getDecoder().decode(mosaicRequest.getImageBase64());

    //         // Создаем объект Mosaic для сохранения в базе данных
    //         Mosaic mosaic = new Mosaic();
    //         mosaic.setImageBase64(mosaicRequest.getImageBase64());
    //         mosaic.setRows(mosaicRequest.getRows());
    //         mosaic.setCols(mosaicRequest.getCols());

    //         // Вызываем метод для сохранения мозаики в базе данных
    //         mosaicDAO.insertMosaic(mosaic);

    //         // Отправляем успешный ответ клиенту
    //         response.setStatus(HttpServletResponse.SC_CREATED);
    //     } catch (Exception e) {
    //         // Отправляем ошибку клиенту в случае исключения
    //         response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    //         response.getWriter().write("Failed to process mosaic creation request");
    //         e.printStackTrace();
    //     }
    // }
}
