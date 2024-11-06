package com.mbe.ada.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

public class AdaUtils {
	
	
	/**
	 *  Create a CSV File 
	 *  
	 *  @param pathFile
	 *  @param filename
	 *  @param headerColumns
	 *  @param rows
	 *  
	 *  
	 *  @matheus.bezerra
	 *  */
	public static void exportToCSV(String pathFile, String filename, String headerColumns, List<String> rows) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathFile + filename + ".csv"))) {

			// Header
			writer.write(headerColumns);
			writer.newLine();

			// Body
			for (String row : rows) {
				writer.write(row);
				writer.newLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
