package com.mbe.ada.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mbe.ada.model.auth.dto.ResponseDTO;
import com.mbe.ada.model.person.Person;
import com.mbe.ada.repository.IPersonRepository;
import com.mbe.ada.utils.AdaUtils;

@Service
public class ReportService {

    @Autowired
    IPersonRepository personRepos;
    

    public ResponseDTO getActiveStudents() {
    	
    	// Data
        List<Person> students = personRepos.findByIsTeacherFalseAndIsActiveTrue();
        
        // Get Each row formatted and set in a position on array
        List<String> rows = students.stream()
        .map(student -> student.getName() + " " + student.getLastname() + "," + student.getCpf()+ "," + 
	        student.getGroups()
	        .stream()
	        .map(group -> group.getName())
        	.collect(Collectors.joining(";"))
        )
        .collect(Collectors.toList());
        
        // Call method to create CSV file
        AdaUtils.exportToCSV("/opt/", "31102024_225604", "Nome,CPF, Grupo", rows);

        return new ResponseDTO(HttpStatus.OK.value(), "Relatório Gerado com Sucesso", null);
    }

   
}
