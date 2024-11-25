package com.mbe.ada.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbe.ada.model.person.Person;

@Repository
public interface IPersonRepository extends JpaRepository<Person, Long>{
	
	List<Person> findByIsActiveTrue();
	
	List<Person> findByIsTeacherFalse();
	
	List<Person> findByIsTeacherTrue();
	
	List<Person> findByIsTeacherFalseAndIsActiveTrue();
	
	List<Person> findByIsTeacherFalseAndIsActiveFalse();
	
	Optional<Person> findByCpf(String cpf);
	
}