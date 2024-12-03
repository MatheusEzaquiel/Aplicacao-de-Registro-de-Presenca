package com.mbe.ada.recognitionApi;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;

@Service
public class RecognitionAPI {
	
	private String apiURL = "http://127.0.0.1:8000";
	

	/**
	 * 
	 * Get Encodings from Image in Recognition API
	 * 
	 * @author matheus.bezerra
	 * */
	public String getEncodingFromImage(String imgBase64) {
		
		HttpResponse<String> response;
		
		
		HttpClient client = HttpClient.newHttpClient();
		
		JsonObject json = new JsonObject();
		json.addProperty("image_base64", imgBase64);
		
		try {
		
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiURL + "/extract-encoding"))
					.header("Content-Type", "application/json") // Define o cabeçalho
					.POST(BodyPublishers.ofString(json.toString())) // Método POST com o corpo da requisição
					.build();
			response = client.send(request, HttpResponse.BodyHandlers.ofString());

			return response.body();
				
		} catch (IOException e) {
	        System.out.println("Erro de I/O: " + e.getMessage());
	    } catch (InterruptedException e) {
	        System.out.println("Requisição interrompida: " + e.getMessage());
	    } catch (Exception e) {
	        System.out.println("Erro inesperado: " + e.getMessage());
	    }
		
		return null;
	}

}
