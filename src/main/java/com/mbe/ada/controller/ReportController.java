package com.mbe.ada.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbe.ada.model.auth.dto.ResponseDTO;
import com.mbe.ada.service.ReportService;

@RestController
@RequestMapping("/reports")
public class ReportController {
	
	@Autowired
	ReportService reportService;
	
	
	@GetMapping("/students/active")
	public ResponseDTO getActiveStudents() {
		
		reportService.getActiveStudents();
		return new ResponseDTO(HttpStatus.OK.value(), "Relatório Gerado com Sucesso", null);
		
	}

}
