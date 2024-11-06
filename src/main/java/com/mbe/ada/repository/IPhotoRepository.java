package com.mbe.ada.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mbe.ada.model.photo.Photo;


public interface IPhotoRepository extends JpaRepository<Photo, Long> {
	List<Photo> findByActiveTrue();

	@Query("SELECT new com.mbe.ada.model.photo.Photo(p.id, p.imageData) FROM Photo p WHERE p.person.id = :personId")
	Optional<Photo> findByPersonId(@Param("personId") Long personId);
}
