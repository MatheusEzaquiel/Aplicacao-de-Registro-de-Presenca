package com.mbe.ada.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbe.ada.model.attendance.Attendance;

public interface IAttendanceRepository extends JpaRepository<Attendance, Long>{
	List<Attendance> findByIsActiveTrue();
}
