package com.mbe.ada.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
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

        
		if (!file.exists())
			throw new RuntimeException("Arquivo de Imagem não encontrado " + filename);

		try {

			try (FileInputStream fis = new FileInputStream(file)) {
				fileData = fis.readAllBytes();
			}

			return Base64.getEncoder().encodeToString(fileData);

		} catch (Exception e) {
			throw new RuntimeException("Erro ao Resgatar Imagem do Diretório");
		}

	}
	
	public static  String uploadFile(String fileBase64, String filename) throws IOException {

		return null;

	}
	
	public String saveImage(byte[] fileData, String filename) throws IOException {
		
		String extension = getExtensionFile(filename);
		
		String fileNameUUID = UUID.randomUUID().toString(); 
		String newFileName = fileNameUUID + "." + extension;
		File file = new File(folderPath + File.separator + newFileName);
		
		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(fileData);
		}
		
		return newFileName;
		
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
	
	public byte[] convertBase64ToByte (String base64Image, String filename) throws IOException {
		System.out.println(base64Image);
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
	
	public boolean uploadImage(String filename, byte[] bytes) {
		
		// Cria o arquivo no caminho especificado
		File imageFile = new File(folderPath + File.separator + filename);
		
		
		try (FileOutputStream fileOutputStream = new FileOutputStream(imageFile)) {
			
			fileOutputStream.write(bytes);
			
			return true;
		} catch (Exception e) {
			throw new RuntimeException("Error to upload file");
		}

	}

}
