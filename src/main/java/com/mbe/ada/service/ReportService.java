package com.mbe.ada.service;

import java.util.List;
import java.util.stream.Collectors;

import com.mbe.ada.model.group.Group;
import com.mbe.ada.repository.IGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbe.ada.model.auth.dto.ResponseDTO;
import com.mbe.ada.model.person.Person;
import com.mbe.ada.repository.IPersonRepository;
import com.mbe.ada.utils.AdaUtils;

@Service
public class ReportService {

    @Autowired
    private IPersonRepository personRepos;

    // Método para gerar e salvar o CSV no diretório especificado
    public String getActiveTeachersCsv() {
        // Busca os professores ativos
        List<Person> teachers = personRepos.findByIsTeacherTrueAndIsActiveTrue();

        // Formata os dados como linhas CSV
        List<String> rows = teachers.stream()
                .map(teacher -> teacher.getName() + " " + teacher.getLastname() + "," + teacher.getCpf() + "," +
                        teacher.getGroups()
                                .stream()
                                .map(group -> group.getName())
                                .collect(Collectors.joining(";"))
                )
                .collect(Collectors.toList());

        // Caminho do arquivo
        //String pathFile = "C:\\Users\\Leonardo\\Downloads\\";
        String pathFile = "/opt/";
        String filename = "professores_ativos";
        String headerColumns = "Nome,CPF,Grupo";

        // Gera o arquivo CSV
        AdaUtils.exportToCSV(pathFile, filename, headerColumns, rows);

        // Retorna o caminho completo do arquivo gerado
        return pathFile + filename + ".csv";
    }

    // BAIXAR ARQUIVO CSV P/ ALUNO
    public String getActiveTeacherFalseCsv() {
        List<Person> students = personRepos.findByIsTeacherFalseAndIsActiveTrue();

        // Formata os dados como linhas CSV
        List<String> rows = students.stream()
                .map(student -> String.join(",",
                        student.getName() + " " + student.getLastname(),
                        student.getEmail(),
                        student.getCpf() ,
                        student.getGroups().stream()
                                .map(group -> group.getName())
                                .collect(Collectors.joining(";"))
                ))
                .collect(Collectors.toList());

        // Caminho do arquivo
        String pathFile = "/opt/";
        String filename = "aluno_ativos";
        String headerColumns = "Nome,Email,CPF,Grupo";

        // Gera o arquivo CSV
        AdaUtils.exportToCSV(pathFile, filename, headerColumns, rows);

        // Retorna o caminho completo do arquivo gerado
        return pathFile + filename + ".csv";
    }

    @Autowired
    private IGroupRepository groupRepository;

    public String getActiveGroupsCsv() {
        // Busca todos os grupos ativos
        List<Group> activeGroups = groupRepository.findByIsActiveTrue();

        // Formata os dados como linhas CSV
        List<String> rows = activeGroups.stream()
                .map(group -> String.join(",",
                        group.getId().toString(), // ID do grupo
                        group.getName(), // Nome do grupo
                        group.getDescription() != null ? group.getDescription() : "" // Descrição do grupo
                ))
                .collect(Collectors.toList());

        // Caminho do arquivo
        String pathFile = "/opt/";
        String filename = "grupos_ativos";
        String headerColumns = "ID,Nome,Descrição";

        // Gera o arquivo CSV
        AdaUtils.exportToCSV(pathFile, filename, headerColumns, rows);

        // Retorna o caminho completo do arquivo gerado
        return pathFile + filename + ".csv";
    }
}

