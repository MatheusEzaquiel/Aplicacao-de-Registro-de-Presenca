package com.mbe.ada.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import com.google.gson.reflect.TypeToken;

import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.CPFValidator;

import com.google.gson.Gson;
import java.lang.reflect.Type;


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

	/**
	 *  Convert a json String to Object MAP <String, Object>
	 *  
	 *  @param pathFile
	 *  @param filename
	 *  @param headerColumns
	 *  @param rows
	 *  
	 *  
	 *  @author matheus.bezerra
	 *  */
    public static Map<String, Object> jsonToMap(String json) {
        // Criação do objeto Gson
        Gson gson = new Gson();
        
        // Definindo o tipo de Map (String, Object)
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        
        // Convertendo o JSON para um Map
        return gson.fromJson(json, type);
    }
    
    /**
     * 
     * CPF Validator
     * 
     * @author matheus.bezerra
     * */
    
	public static boolean isValidCPF(String cpf) {
		CPFValidator cpfValidator = new CPFValidator();
		List<ValidationMessage> erros = cpfValidator.invalidMessagesFor(cpf);
		
		if(erros.size() > 0) {
			System.out.println(erros);
			return false;
		} else {
			return true; 
		}
	}
	
}
