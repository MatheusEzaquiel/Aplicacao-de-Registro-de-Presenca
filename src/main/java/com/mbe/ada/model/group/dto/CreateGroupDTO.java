package com.mbe.ada.model.group.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.mbe.ada.model.group.Group;

public record CreateGroupDTO(
		String name,
		String description,
		LocalDate initialDate,
		LocalDate endDate,
		LocalTime initialTime,
		LocalTime endTime,
		boolean monday,
		boolean tuesday,
		boolean wednesday,
		boolean thursday,
		boolean friday,
		boolean saturday,
		boolean sunday) {
	
	public CreateGroupDTO(Group group) {
        this(
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
        		group.isSunday()
        );
    }
}
