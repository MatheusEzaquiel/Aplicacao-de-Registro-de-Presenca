package com.mbe.ada.model.user;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.mbe.ada.model.user.dto.CreateUserDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity(name = "User")
@Table(name = "users")
public class User {
	
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@Column(name = "name", length = 75)
	@NotNull(message="Nome não pode ser Nulo")
	@NotEmpty
	@Size(min = 2, max = 75)
	private String name;	

	@Column(name = "lastname", length = 200)
	@Size(min = 2, max = 200)
	private String lastname;

	@Column(name = "email", length = 100, unique = true)
	private String email;

	@Column(name = "cpf", length = 11, unique = true)
	@NotEmpty
	@Size(min = 11)
	private String cpf;

	@Column(name = "birth_date")
	private LocalDate birthDate;
	
	@Column(name = "password")
	@NotBlank
	@Size(min = 8, max = 150)
	private String password;

	@Column(name = "is_active" )
	private Boolean isActive;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	
	public User(){}

	public User(Long id, String name, String lastname, String email, String cpf, LocalDate birthDate,
			String password, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.name = name;
		this.lastname = lastname;
		this.email = email;
		this.cpf = cpf;
		this.birthDate = birthDate;
		this.password = password;
		this.isActive = true;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
	public User(CreateUserDTO data) {
		this.name = data.name();
		this.lastname = data.lastname();
		this.email = data.email();
		this.cpf = data.cpf();
		this.password = data.password();
		this.birthDate = data.birthDate();
		this.isActive = true;
		this.createdAt = LocalDateTime.now();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", lastname=" + lastname + ", email=" + email + ", cpf=" + cpf
				+ ", birthDate=" + birthDate + ", password=" + password + ", isActive=" + isActive + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

}
