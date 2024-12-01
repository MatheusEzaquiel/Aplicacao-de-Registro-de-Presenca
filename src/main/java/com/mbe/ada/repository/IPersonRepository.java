package com.mbe.ada.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbe.ada.model.person.Person;

@Repository
public interface IPersonRepository extends JpaRepository<Person, Long>{
	
	Optional<Person> findByCpf(String cpf);
	Optional<Person> findByEmail(String email);
	
	List<Person> findByIsActiveTrue();
	
	// Person(Student)
	List<Person> findByIsTeacherFalse();
	List<Person> findByIsTeacherFalseAndIsActiveTrue();
	List<Person> findByIsTeacherFalseAndIsActiveFalse();
	
	// Person(Teacher)
	List<Person> findByIsTeacherTrue();
	List<Person> findByIsTeacherTrueAndIsActiveTrue();
	
	
	
}