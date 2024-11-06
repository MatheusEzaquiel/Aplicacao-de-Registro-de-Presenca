package com.mbe.ada.model.photo.dto;


import com.mbe.ada.model.person.Person;
import com.mbe.ada.model.photo.Photo;

public record PhotoDTO(
		Long id,
	    String name,
	    String imageData,
	    boolean isDefault,
	    Person person) {
	

	    // Construtor que aceita uma entidade Photo
	    public PhotoDTO(Photo photo) {
	        this(
	            photo.getId(),
	            photo.getName(),
	            photo.getImageData(),
	            photo.isDefault(),
	            photo.getPerson()
	        );
	    }

}
