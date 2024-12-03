package com.mbe.ada.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageUtils {
	
	private static Path folderPath = Path.of("/opt/images");
	private static Path referenceImgPath = Path.of("/persons/reference-img");
	
	public static String getImageBase64(String filename, String entity) {

	    byte[] fileData = null;
        File file;

        // Verifique se a entidade é nula e crie o caminho do arquivo
        if (entity != null) {
            file = new File(folderPath + File.separator + referenceImgPath + File.separator + filename);
        } else {
            file = new File(folderPath + File.separator + filename);
        }

        
		if (file.exists()) {
		
			try (FileInputStream fis = new FileInputStream(file)) {
				fileData = fis.readAllBytes();
			} catch (Exception e) {
				throw new RuntimeException("Erro ao Resgatar Imagem do Diretório");
			}

			return Base64.getEncoder().encodeToString(fileData);
		} else 
			System.out.println("Arquivo de Imagem não encontrado " + filename);
		
		return null;

	}
	
	public static  String uploadFile(String fileBase64, String filename) throws IOException {

		return null;

	}
	

	public boolean uploadImage(String filename, String fileBase64) {
		
		byte[] bytes = null;
		
		try {
			bytes = convertBase64ToByte(fileBase64);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Cria o arquivo no caminho especificado
		File imageFile = new File(folderPath + File.separator + referenceImgPath + File.separator + filename);
		
		
		try (FileOutputStream fileOutputStream = new FileOutputStream(imageFile)) {
			
			fileOutputStream.write(bytes);
			
			return true;
		} catch (Exception e) {
			throw new RuntimeException("Error to upload file");
		}

	}
	
	 public static void deleteFile(String filename) {
		 
		 	String filePath = folderPath + File.separator + referenceImgPath + File.separator + filename;
	        Path path = Paths.get(filePath);
	        
	        try {
	            // Verifica se o arquivo existe antes de tentar apagá-lo
	            if (Files.exists(path)) {
	                Files.delete(path);
	                System.out.println("Arquivo apagado com sucesso: " + filePath);
	            } else {
	                System.out.println("Arquivo não encontrado: " + filePath);
	            }
	        } catch (IOException e) {
	            System.err.println("Erro ao apagar o arquivo: " + e.getMessage());
	        }
	    }

	
	public byte[] converFileToBytes(File file) throws IOException {
		
		byte[] fileData = new byte[(int) file.length()];
		
		try(FileInputStream fis = new FileInputStream(file)) {
			fis.read(fileData);
		}
		return fileData;
		
	}
	
	public String generateFileName(String filename) {
		// Create new FileName
		String extension = getExtensionFile(filename);
		String fileNameUUID = UUID.randomUUID().toString();
		return fileNameUUID + "." + extension;
	}
	
	public String getExtensionFile(String filename) {
		String[] part = filename.split("\\.");
		int lastItem = part.length - 1;
		return part[lastItem];
	}
	
	public String getNameFile(String filename) {
		String[] part = filename.split("\\.");
		return part[0];
	}
	
	public byte[] convertBase64ToByte (String base64Image) throws IOException {

		// Remove o prefixo "data:image/png;base64," se estiver presente
		if (base64Image.contains(",")) {
			base64Image = base64Image.split(",")[1];
		}

		// Decodifica a string Base64 em bytes
		byte[] imageBytes = Base64.getDecoder().decode(base64Image);

		return imageBytes;
	}
	
	public String convertFileToBase64(MultipartFile file) {
		
		try {
			return Base64.getEncoder().encodeToString(file.getBytes());
		} catch (IOException e) {
			throw new RuntimeException("Erro ao converte imagem para String base64");
		}
	}
	

}
