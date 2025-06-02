package com.mbe.ada.model.photo;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;

import org.springframework.web.multipart.MultipartFile;

import com.mbe.ada.model.person.Person;
import com.mbe.ada.model.photo.dto.PhotoDTO;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "photos")
public class Photo {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(name = "name", nullable = false, unique = true)
	    private String name;
	    
	    @ManyToOne
	    @JoinColumn(name = "person_id")
	    private Person person;

	    @Lob
	    @Column(name = "image_data", columnDefinition = "TEXT")
	    private String imageData;
	    
	    @Column(name = "is_default")
	    private boolean isDefault = false;

	    @Column(name = "is_active")
	    private boolean active = true;

	    @Column(name = "created_at")
	    private Timestamp createdAt;

	    @Column(name = "updated_at")
	    private Timestamp updatedAt;
	    
	    
	    public Photo() {}
	    
	    public Photo(Long id, String imageData) {
			this.id = id;
			this.imageData = imageData;
		}
		
		public Photo(Long id, String name, Person person, String imageData, boolean isDefault, boolean active,
				Timestamp createdAt, Timestamp updatedAt) {
			super();
			this.id = id;
			this.name = name;
			this.person = person;
			this.imageData = imageData;
			this.isDefault = isDefault;
			this.active = active;
			this.createdAt = createdAt;
			this.updatedAt = updatedAt;
		}

		public Photo(PhotoDTO data) {
			this.id = data.id();
			this.name = data.name();
			this.imageData = data.imageData();
			this.isDefault = data.isDefault();
			this.person = data.person();
		}
		
		public Photo(String newFileName, String imageData, Person person, boolean isDefault) throws IOException {
			this.name = newFileName;
			this.imageData = imageData;
			this.person = person;
			this.isDefault = isDefault;
			this.active = true;
			this.createdAt = Timestamp.from(Instant.now());
			this.updatedAt = null;
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

		public Person getPerson() {
			return person;
		}

		public void setPerson(Person person) {
			this.person = person;
		}

		public String getImageData() {
			return imageData;
		}

		public void setImageData(String imageData) {
			this.imageData = imageData;
		}

		public boolean isDefault() {
			return isDefault;
		}

		public void setDefault(boolean isDefault) {
			this.isDefault = isDefault;
		}

		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

		public Timestamp getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(Timestamp createdAt) {
			this.createdAt = createdAt;
		}

		public Timestamp getUpdatedAt() {
			return updatedAt;
		}

		public void setUpdatedAt(Timestamp updatedAt) {
			this.updatedAt = updatedAt;
		}

		@Override
		public String toString() {
			return "Photo [id=" + id + ", name=" + name + ", imageData="
					+ imageData + ", active=" + active + ", createdAt=" + createdAt + ", updatedAt="
					+ updatedAt + "]";
		}
		

}

