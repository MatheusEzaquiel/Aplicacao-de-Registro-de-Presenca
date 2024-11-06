package com.mbe.ada.model.group;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import com.mbe.ada.model.group.dto.CreateGroupDTO;
import com.mbe.ada.model.person.Person;
import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;


@Entity
@Table(name = "groups") 
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "initial_date")
    private LocalDate initialDate;

    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "initial_time")
    private LocalTime initialTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "monday", nullable = false, columnDefinition = "Boolean DEFAULT FALSE")
    private Boolean monday = false;

    @Column(name = "tuesday", nullable = false, columnDefinition = "Boolean DEFAULT FALSE")
    private Boolean tuesday = false;

    @Column(name = "wednesday", nullable = false, columnDefinition = "Boolean DEFAULT FALSE")
    private Boolean wednesday = false;

    @Column(name = "thursday", nullable = false, columnDefinition = "Boolean DEFAULT FALSE")
    private Boolean thursday = false;

    @Column(name = "friday", nullable = false, columnDefinition = "Boolean DEFAULT FALSE")
    private Boolean friday = false;

    @Column(name = "saturday", nullable = false, columnDefinition = "Boolean DEFAULT FALSE")
    private Boolean saturday = false;

    @Column(name = "sunday", nullable = false, columnDefinition = "Boolean DEFAULT FALSE")
    private Boolean sunday = false;

    @Column(name = "is_active", nullable = false, columnDefinition = "Boolean DEFAULT TRUE")
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    // Relacionamento Many-to-Many com a classe User
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "group_person",
              joinColumns = @JoinColumn(name = "group_id"),
              inverseJoinColumns = @JoinColumn(name = "person_id"))

    private Set<Person> persons;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public Group() {}

    
	public Group(Long id, String name, String description, LocalDate initialDate, LocalDate endDate,
			LocalTime initialTime, LocalTime endTime, Boolean monday, Boolean tuesday, Boolean wednesday,
			Boolean thursday, Boolean friday, Boolean saturday, Boolean sunday, Boolean isActive,
			LocalDateTime createdAt, LocalDateTime updatedAt, Set<Person> persons) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.initialDate = initialDate;
		this.endDate = endDate;
		this.initialTime = initialTime;
		this.endTime = endTime;
		this.monday = monday;
		this.tuesday = tuesday;
		this.wednesday = wednesday;
		this.thursday = thursday;
		this.friday = friday;
		this.saturday = saturday;
		this.sunday = sunday;
		this.isActive = isActive;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.persons = persons;
	}

	public Group(CreateGroupDTO data) {
		this.name = data.name();
		this.description = data.description();
		this.initialDate = data.initialDate();
		this.endDate = data.endDate();
		this.initialTime = data.initialTime();
		this.endTime = data.endTime();
		this.monday = data.monday();
		this.tuesday = data.tuesday();
		this.wednesday = data.wednesday();
		this.thursday = data.thursday();
		this.friday = data.friday();
		this.saturday = data.saturday();
		this.sunday = data.sunday();
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public LocalDate getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(LocalDate initialDate) {
		this.initialDate = initialDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LocalTime getInitialTime() {
		return initialTime;
	}

	public void setInitialTime(LocalTime initialTime) {
		this.initialTime = initialTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public Boolean isMonday() {
		return monday;
	}

	public void setMonday(Boolean monday) {
		this.monday = monday;
	}

	public Boolean isTuesday() {
		return tuesday;
	}

	public void setTuesday(Boolean tuesday) {
		this.tuesday = tuesday;
	}

	public Boolean isWednesday() {
		return wednesday;
	}

	public void setWednesday(Boolean wednesday) {
		this.wednesday = wednesday;
	}

	public Boolean isThursday() {
		return thursday;
	}

	public void setThursday(Boolean thursday) {
		this.thursday = thursday;
	}

	public Boolean isFriday() {
		return friday;
	}

	public void setFriday(Boolean friday) {
		this.friday = friday;
	}

	public Boolean isSaturday() {
		return saturday;
	}

	public void setSaturday(Boolean saturday) {
		this.saturday = saturday;
	}

	public Boolean isSunday() {
		return sunday;
	}

	public void setSunday(Boolean sunday) {
		this.sunday = sunday;
	}

	public Boolean isActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
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

	public Set<Person> getPersons() {
		return persons;
	}

	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}

	public Group updateValues(Group data) {

	    if (data.getName() != null && !data.getName().isEmpty()){
	        this.name = data.getName();
	    }

	    if (data.getDescription() != null && !data.getName().isEmpty()) {
	        this.description = data.getDescription();
	    }

	    if (data.getInitialDate() != null) {
	        this.initialDate = data.getInitialDate();
	    }

	    if (data.getEndDate() != null) {
	        this.endDate = data.getEndDate();
	    }

	    if (data.getInitialTime() != null) {
	        this.initialTime = data.getInitialTime();
	    }

	    if (data.getEndTime() != null) {
	        this.endTime = data.getEndTime();
	    }

	    if (data.isMonday()) {
	        this.monday = data.isMonday();
	    }

	    if (data.isTuesday() != null) {
	        this.tuesday = data.isTuesday();
	    }

	    if (data.isWednesday() != null) {
	        this.wednesday = data.isWednesday();
	    }

	    if (data.isThursday() != null) {
	        this.thursday = data.isThursday();
	    }

	    if (data.isFriday() != null) {
	        this.friday = data.isFriday();
	    }

	    if (data.isSaturday() != null) {
	        this.saturday = data.isSaturday();
	    }

	    if (data.isSunday() != null) {
	        this.sunday = data.isSunday();
	    }
	    
	    if(data.isActive != null) {
	    	this.isActive = data.isActive;
	    }
		
	    return this;
	}
    
}
	
