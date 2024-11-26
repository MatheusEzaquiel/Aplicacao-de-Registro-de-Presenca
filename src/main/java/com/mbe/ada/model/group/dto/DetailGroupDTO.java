package com.mbe.ada.model.group.dto;

import java.util.List;

import com.mbe.ada.model.group.Group;
import com.mbe.ada.model.person.dto.BasicPersonDTO;

public record DetailGroupDTO(
		Long id,
	    String name,
	    String description,
	    String initialDate,
	    String endDate,
	    String initialTime,
	    String endTime,
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
            group.getInitialDate().toString(),
            group.getEndDate().toString(),
            group.getInitialTime().toString(),
            group.getEndTime().toString(),
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
