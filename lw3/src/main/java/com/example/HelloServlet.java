package com.example;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HelloServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Установка типа содержимого ответа
        response.setContentType("text/html");

        // Получение потока вывода для записи HTML
        PrintWriter out = response.getWriter();

        // Формирование HTML-контента
        out.println("<html><head><title>Hello Servlet</title></head>");
        out.println("<body>");
        out.println("<h1>Hello, Worlds!</h1>");
        out.println("</body></html>");
    }
}
