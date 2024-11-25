package com.mbe.ada.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbe.ada.model.auth.dto.ResponseDTO;
import com.mbe.ada.service.ReportService;

import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/reports")
public class ReportController {

	@Autowired
	private ReportService reportService;

	// Endpoint para gerar e baixar o relatório de professores ativos
	@GetMapping("/teachers/active/csv")
	public ResponseEntity<byte[]> getActiveTeachersCsv() {
		try {
			// Chama o serviço para gerar o CSV
			String csvFilePath = reportService.getActiveTeachersCsv();

			// Lê o arquivo gerado como bytes
			byte[] csvData = Files.readAllBytes(Paths.get(csvFilePath));

			// Configura a resposta com o arquivo CSV para download
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"professores_ativos.csv\"")
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					.body(csvData);

		} catch (Exception e) {
			// Retorna um erro caso algo dê errado
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(("Erro ao gerar o relatório: " + e.getMessage()).getBytes());
		}
	}

	// Endpoint para gerar e baixar o relatório de ALUNOS ativos
	@GetMapping("/students/active/csv")
	public ResponseEntity<byte[]> getActiveStudentsCsv() {
		try {
			// Chama o serviço para gerar o CSV
			String csvFilePath = reportService.getActiveTeacherFalseCsv();

			// Lê o arquivo gerado como bytes
			byte[] csvData = Files.readAllBytes(Paths.get(csvFilePath));

			// Configura a resposta com o arquivo CSV para download
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"alunos_ativos.csv\"")
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					.body(csvData);

		} catch (Exception e) {
			// Retorna um erro caso algo dê errado
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(("Erro ao gerar o relatório: " + e.getMessage()).getBytes());
		}
	}

	@GetMapping("/groups/active/csv")
	public ResponseEntity<byte[]> getActiveGroupsCsv() {
		try {
			// Chama o serviço para gerar o CSV
			String csvFilePath = reportService.getActiveGroupsCsv();

			// Lê o arquivo gerado como bytes
			byte[] csvData = Files.readAllBytes(Paths.get(csvFilePath));

			// Configura a resposta com o arquivo CSV para download
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"grupos_ativos.csv\"")
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					.body(csvData);

		} catch (Exception e) {
			// Retorna um erro caso algo dê errado
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(("Erro ao gerar o relatório: " + e.getMessage()).getBytes());
		}
	}
}

