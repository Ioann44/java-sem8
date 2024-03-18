package com.example;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/calculator")
public class CalculatorServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getRequestDispatcher("calculator.jsp").forward(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			double num1 = Double.parseDouble(request.getParameter("num1"));
			double num2 = Double.parseDouble(request.getParameter("num2"));
			String operation = request.getParameter("operation");
			double result = calculate(num1, num2, operation);

			request.setAttribute("result", num1 + " " + operation + " " + num2 + " = " + result);
			request.getRequestDispatcher("calculator.jsp").forward(request, response);
		} catch (Exception e) {
			try {
				response.sendRedirect("error.jsp");
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	private double calculate(double num1, double num2, String operation) {
		switch (operation) {
			case "add":
				return num1 + num2;
			case "subtract":
				return num1 - num2;
			case "multiply":
				return num1 * num2;
			case "divide":
				return num1 / num2;
			default:
				throw new IllegalArgumentException("Invalid operation");
		}
	}
}
