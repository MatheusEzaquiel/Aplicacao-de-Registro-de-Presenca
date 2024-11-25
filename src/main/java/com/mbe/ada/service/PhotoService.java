package com.mbe.ada.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.mbe.ada.model.person.Person;
import com.mbe.ada.model.photo.Photo;
import com.mbe.ada.repository.IPersonRepository;
import com.mbe.ada.repository.IPhotoRepository;

import jakarta.transaction.Transactional;

@Service
public class PhotoService {

	@Autowired
	IPhotoRepository photoRepos;
	
	@Autowired
	ImageUtils imageService;
	
	@Autowired
	IPersonRepository personRepos;
	
	@Transactional
	public String getImageDataByPersonId(@PathVariable Long personId) {
		
		Optional<Photo> photo = photoRepos.findByPersonId(personId);
		
		if(photo.isPresent()) {
			return photo.get().getImageData();
		}
		
		return null;
		
	}
	

	
	//1. resgatar nome e imagem base64
	//2. Gerar novo nome codificado
	//3. Converter base64 em File e salva na pasta
	//4. Salvar imagembase64 no Banco
	public Photo save(String fileBase64, String filename, Long personId, Boolean isDefault) {

    	Optional<Person> person = personRepos.findById(personId);
    	
        if (person.isEmpty()) 
        	System.out.println("Pessoa não encontrada");
    	
        
		try {
			
			byte[] imageData = imageService.convertBase64ToByte(fileBase64, filename);
			if(!imageService.uploadImage(filename, imageData))
				throw new RuntimeException("Erro ao fazer upload da imagem");
			  
	       
	        Photo photoToCreate = new Photo(filename, fileBase64, person.get(), isDefault);
	        
	        /*
	        // Caso não seja a foto de referencia, não salvar no BD
	        if(!isDefault)
	        	photoToCreate.setImageData(null);
	        */
	        
	        return photoRepos.save(photoToCreate);
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
      
        return null;
        
  
	}
	
	/*
	public Photo create(MultipartFile file, Long personId, Boolean isDefault) {

    	Optional<Person> person = personRepos.findById(personId);
        
        if (person.isEmpty()) 
        	System.out.println("Pessoa não encontrada");
    	

        String newFileName;
        
		try {
			
			newFileName = imageService.saveImage(file.getBytes(), file.getOriginalFilename());
			  
	        //file -> byte[] -> base64
	        String photoBase64 = Base64.getEncoder().encodeToString(file.getBytes());
	        

	        Photo photoToCreate = new Photo(newFileName, photoBase64, person.get(), isDefault);
	        
	        if(!isDefault)
	        	photoToCreate.setImageData(null);
	        
	        return photoRepos.save(photoToCreate);
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
      
        return null;
        
  
	}
	*/

}
