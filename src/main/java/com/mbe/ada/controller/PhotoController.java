package com.mbe.ada.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mbe.ada.model.person.Person;
import com.mbe.ada.model.photo.Photo;
import com.mbe.ada.model.photo.dto.PhotoDTO;
import com.mbe.ada.repository.IPersonRepository;
import com.mbe.ada.repository.IPhotoRepository;
import com.mbe.ada.service.ImageUtils;

@RestController
@RequestMapping(value = "/photos")
public class PhotoController {

    @Autowired
    IPhotoRepository photoRepository;
    
    @Autowired
	IPersonRepository personRepos;
    
    @Autowired
    ImageUtils imageUtils;


    @GetMapping
    public ResponseEntity<List<PhotoDTO>> index() {
    	
        List<Photo> data = photoRepository.findByActiveTrue();

        if (data.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        List<PhotoDTO> dataDTO = data.stream()
                .map(photo -> new PhotoDTO(photo))
                .toList();

        return new ResponseEntity<>(dataDTO, HttpStatus.OK);
    }
/*
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Photo> create(
    		@RequestParam("file") MultipartFile file,
    		@RequestParam("personId") Long personId,
    		@RequestParam("isDefault") boolean isDefault
    		) throws IOException {
    	
    	Optional<Person> person = personRepos.findById(personId);
        
        if (person.isEmpty()) 
        	return new ResponseEntity("Pessoa não encontrada", HttpStatus.NOT_FOUND);
    	

        String newFileName = imageUtils.saveImage(file.getBytes(), file.getOriginalFilename());
        
        //file -> byte[] -> base64
        String photoBase64 = Base64.getEncoder().encodeToString(file.getBytes());
        

        Photo photoToCreate = new Photo(newFileName, photoBase64, person.get(), isDefault);
        
        if(!isDefault)
        	photoToCreate.setImageData(null);
        
        Photo savedPhoto = photoRepository.save(photoToCreate);
        return new ResponseEntity<>(savedPhoto, HttpStatus.CREATED);
    }*/

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id) {
    	
        Optional<Photo> photoOpt = photoRepository.findById(id);
        
        System.out.println(photoOpt.get().getImageData());

        if (photoOpt.isEmpty())
            return new ResponseEntity("Foto não encontrada", HttpStatus.NOT_FOUND);
        
        
        Photo photo = photoOpt.get();
        
        if(!photoOpt.get().isDefault()) {
			
			String imageBase64 = ImageUtils.getImageBase64(photoOpt.get().getName(), null);
			
			photo = photoOpt.get();
			photo.setImageData(imageBase64);
			
		}

        PhotoDTO photoDTO = new PhotoDTO(photo);
        return new ResponseEntity<>(photoDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
    	
        Optional<Photo> photoOpt = photoRepository.findById(id);

        if (photoOpt.isEmpty())
            return new ResponseEntity("Foto não encontrada", HttpStatus.NOT_FOUND);

        Photo photoToDelete = photoOpt.get();
        photoToDelete.setActive(false);
        photoRepository.save(photoToDelete);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    
}
