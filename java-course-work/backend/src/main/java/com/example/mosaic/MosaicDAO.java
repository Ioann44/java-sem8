package com.example.mosaic;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MosaicDAO {

	private final Connection connection;

	public MosaicDAO(Connection connection) {
		this.connection = connection;
	}

	public List<Mosaic> getAllMosaics() {
		List<Mosaic> mosaics = new ArrayList<>();
		String sql = "SELECT * FROM mosaics";

		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Mosaic mosaic = extractMosaicFromResultSet(rs);
				mosaics.add(mosaic);
			}
		} catch (SQLException e) {
			System.err.println("Error fetching all mosaics: " + e.getMessage());
		}

		return mosaics;
	}

	public Mosaic getMosaicById(int mosaicId) {
		Mosaic mosaic = null;
		String sql = "SELECT * FROM mosaics WHERE id = ?";

		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);

			pstmt.setInt(1, mosaicId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				mosaic = extractMosaicFromResultSet(rs);
			}
		} catch (SQLException e) {
			System.err.println("Error fetching mosaic by ID: " + e.getMessage());
		}

		return mosaic;
	}

	private Mosaic extractMosaicFromResultSet(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String imageBase64 = rs.getString("image_base64");

		Array colorsUsedArray = rs.getArray("colors_used");
		int[] colorsUsed;
		Object[] objectIndices = (Object[]) colorsUsedArray.getArray();
		colorsUsed = new int[objectIndices.length];
		for (int i = 0; i < objectIndices.length; i++) {
			colorsUsed[i] = (Integer) objectIndices[i];
		}

		Array compressedIndicesArray = rs.getArray("compressed_indices");
		int[] compressedIndices;
		objectIndices = (Object[]) compressedIndicesArray.getArray();
		compressedIndices = new int[objectIndices.length];
		for (int i = 0; i < objectIndices.length; i++) {
			compressedIndices[i] = (Integer) objectIndices[i];
		}

		int numRows = rs.getInt("num_rows");
		int numCols = rs.getInt("num_cols");
		long hash = rs.getLong("hash");

		return new Mosaic(id, imageBase64, colorsUsed, compressedIndices, numRows, numCols, hash);
	}

	// public void insertMosaic(Mosaic mosaic) throws SQLException {
	// String sql = "INSERT INTO mosaics (image_base64, color_palette, index_matrix,
	// rows, cols, hash) VALUES (?, ?, ?, ?, ?, ?)";
	// try (PreparedStatement statement = connection.prepareStatement(sql)) {
	// List<Integer> flattenedList = new ArrayList<>();
	// for (int[] array : mosaic.getColorPalette()) {
	// for (int num : array) {
	// flattenedList.add(num);
	// }
	// }

	// statement.setString(1, mosaic.getImageBase64());
	// statement.setArray(2, connection.createArrayOf("integer",
	// flattenedList.toArray()));
	// statement.setArray(3, connection.createArrayOf("integer",
	// Arrays.stream(mosaic.getIndexMatrix()).boxed().toArray(Integer[]::new)));
	// statement.setInt(4, mosaic.getRows());
	// statement.setInt(5, mosaic.getCols());
	// statement.setLong(6, mosaic.getHash());
	// statement.executeUpdate();
	// }
	// }
}
