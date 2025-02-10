package com.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.utils.DatabaseConnector;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/record")
public class RecordServlet extends HttpServlet {

	private final Connection connection = DatabaseConnector.getConnection();

	public RecordServlet() throws SQLException {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Records");

			StringBuilder recordsJson = new StringBuilder();
			recordsJson.append("[");

			while (resultSet.next()) {
				recordsJson.append("{")
						.append("\"id\":").append(resultSet.getInt("id")).append(",")
						.append("\"name\":\"").append(resultSet.getString("name")).append("\",")
						.append("\"time\":").append(resultSet.getInt("time")).append(",")
						.append("\"mosaicId\":").append(resultSet.getInt("mosaicId"))
						.append("},");
			}

			if (recordsJson.length() > 1) {
				recordsJson.setLength(recordsJson.length() - 1); // Удаление последней запятой
			}

			recordsJson.append("]");

			response.getWriter().write(recordsJson.toString());
		} catch (SQLException e) {
			throw new ServletException("Ошибка при получении рекордов", e);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("name");
		int time = Integer.parseInt(request.getParameter("time"));
		int mosaicId = Integer.parseInt(request.getParameter("mosaicId"));

		try {
			PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO records (name, time, mosaicId) VALUES (?, ?, ?)");
			statement.setString(1, name);
			statement.setInt(2, time);
			statement.setInt(3, mosaicId);
			statement.executeUpdate();

			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println("Рекорд успешно сохранен.");
		} catch (SQLException e) {
			throw new ServletException("Ошибка при сохранении рекорда", e);
		}
	}
}
