package com.mbe.ada.model.person;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.mbe.ada.model.group.Group;
import com.mbe.ada.model.person.dto.CreatePersonDTO;
import com.mbe.ada.model.person.dto.PersonDTO;
import com.mbe.ada.model.photo.Photo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity(name="Person")
@Table(name="persons")
public class Person {
	
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@Column(name = "name", nullable = false, length = 75)
	@NotNull
	@NotEmpty
	@Size(min = 2, max = 75)
	private String name;

	@Column(name = "lastname", nullable = false, length = 200)
	@NotNull
	@NotEmpty
	@Size(min = 2, max = 200)
	private String lastname;

	@Column(name = "email", length = 100, unique = true)
	private String email;

	@Column(name = "cpf", length = 14, unique = true)
	@NotNull
	@NotEmpty
	private String cpf;

	@Column(name = "birth_date")
	private LocalDate birthDate;
	
	@Column(name = "is_teacher")
	private Boolean isTeacher;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Photo> photos;
    
    @ManyToMany(mappedBy = "persons", fetch = FetchType.LAZY)
    private Set<Group> groups; // Alterado para Set<Group>

	public Person(){}

	
	public Person(Long id, String name, String lastname, String email, String cpf, LocalDate birthDate, Boolean isTeacher, Long userId,
			Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.name = name;
		this.lastname = lastname;
		this.email = email;
		this.cpf = cpf;
		this.birthDate = birthDate;
		this.isTeacher = isTeacher;
		this.userId = userId;
		this.isActive = isActive;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
	public Person(CreatePersonDTO data) {
		this.name = data.name();
		this.lastname = data.lastname();
		this.email = data.email();
		this.cpf = data.cpf();
		this.birthDate = data.birthDate();
		this.userId = data.userId();
		this.isTeacher = data.isTeacher();
		this.isActive = true;
		this.createdAt = LocalDateTime.now();
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
	
	public Boolean getIsTeacher() {
		return isTeacher;
	}

	public void setIsTeacher(Boolean isTeacher) {
		this.isTeacher = isTeacher;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public List<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}
	
	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}


	public Person updateValues(PersonDTO data) {
		
		if (data.name() != null) {
            this.name = data.name();
        }

        if (data.lastname() != null) {
            this.lastname = data.lastname();
        }

        if (data.email() != null) {
            this.email = data.email();
        }

        if (data.cpf() != null) {
            this.cpf = data.cpf();
        }

        if (data.birthDate() != null) {
            this.birthDate = data.birthDate();
        }

        if (data.userId() != null) {
            this.userId = data.userId();
        }

        return this;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", lastname=" + lastname + ", email=" + email + ", cpf=" + cpf
				+ ", userId=" + userId + ", isActive=" + isActive + ", hashCode()=" + hashCode() + ", getClass()="
				+ getClass() + ", toString()=" + super.toString() + "]";
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Person person = (Person) o;
		return Objects.equals(email, person.email) && Objects.equals(cpf, person.cpf);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(cpf);
	}
	
}
