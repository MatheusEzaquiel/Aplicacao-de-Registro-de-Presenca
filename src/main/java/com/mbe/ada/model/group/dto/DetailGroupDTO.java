package com.mbe.ada.model.group.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.mbe.ada.model.group.Group;
import com.mbe.ada.model.person.dto.BasicPersonDTO;

public record DetailGroupDTO(
		Long id,
	    String name,
	    String description,
	    LocalDate initialDate,
	    LocalDate endDate,
	    LocalTime initialTime,
	    LocalTime endTime,
	    Boolean monday,
	    Boolean tuesday,
	    Boolean wednesday,
	    Boolean thursday,
	    Boolean friday,
	    Boolean saturday,
	    Boolean sunday,
	    List<BasicPersonDTO> persons) {
	
	public DetailGroupDTO(Group group, List<BasicPersonDTO> persons) {
        this(
            group.getId(),
            group.getName(),
            group.getDescription(),
            group.getInitialDate(),
            group.getEndDate(),
            group.getInitialTime(),
            group.getEndTime(),
            group.isMonday(),
            group.isTuesday(),
            group.isWednesday(),
            group.isThursday(),
            group.isFriday(),
            group.isSaturday(),
            group.isSunday(),
            persons
        );
    }

}
