package com.mbe.ada.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbe.ada.model.user.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long>{

	Optional<User> findByCpf(String cpf);
	Optional<User> findByEmail(String email);
	
}