package com.mbe.ada.model.attendance.dto;


import java.time.LocalDateTime;

import com.mbe.ada.model.attendance.Attendance;

public record ListAttendanceDTO(
		Long id,
	    Long personId,
	    String photoName,
	    LocalDateTime created
	    ) {
	
	 public ListAttendanceDTO(Attendance attendance) {
	        this(
	            attendance.getId(),
	            attendance.getPerson().getId(),
	            attendance.getPhotoName(),
	            attendance.getCreatedAt()
	        );
	  }
}
