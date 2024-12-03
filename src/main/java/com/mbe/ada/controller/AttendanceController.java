package com.mbe.ada.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.mbe.ada.model.attendance.Attendance;
import com.mbe.ada.model.attendance.dto.CreateAttendanceDTO;
import com.mbe.ada.model.attendance.dto.ListAttendanceDTO;
import com.mbe.ada.model.attendance.dto.ResponseAttendanceDTO;
import com.mbe.ada.model.auth.dto.ResponseDTO;
import com.mbe.ada.model.group.Group;
import com.mbe.ada.model.person.Person;
import com.mbe.ada.recognitionApi.RecognitionAPIResponseDTO;
import com.mbe.ada.repository.IAttendanceRepository;
import com.mbe.ada.repository.IPersonRepository;
import com.mbe.ada.repository.IPhotoRepository;
import com.mbe.ada.service.APIService;
import com.mbe.ada.service.ImageUtils;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/attendances")
public class AttendanceController {
	

    @Autowired
    IAttendanceRepository attendanceRepos;

    @Autowired
    IPersonRepository personRepos;
    
    @Autowired
    IPhotoRepository photoRepos;
    
    @Autowired
    ImageUtils imageService;
    
    @Autowired
    APIService apiService;

    @GetMapping
    public ResponseEntity index() {
        List<Attendance> data = attendanceRepos.findByIsActiveTrue();

        if (data.isEmpty())
            return new ResponseEntity<>("Ainda não existem Registros de Presença",HttpStatus.NO_CONTENT);

        List<ListAttendanceDTO> dataDTO = data.stream()
            .map(attendance -> new ListAttendanceDTO(attendance))
            .toList();

        return new ResponseEntity<>(dataDTO, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id) {
        Optional<Attendance> attendance = attendanceRepos.findById(id);

        if (attendance.isEmpty())
            return new ResponseEntity<>("Presença não encontrada", HttpStatus.NOT_FOUND);

        ListAttendanceDTO attendanceDTO = new ListAttendanceDTO(attendance.get());
        return new ResponseEntity<>(attendanceDTO, HttpStatus.OK);
    }
    /*
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity create(
    		@RequestParam("file") MultipartFile file,
    		@RequestParam("personId") Long personId
    		) {
    	
        Optional<Person> person = personRepos.findById(personId);
        if (person.isEmpty())
            return new ResponseEntity<>("Pessoa relacionada não encontrada", HttpStatus.NOT_FOUND);
        
        // Save image and return the new file name
        String newFileName;
        
		try {
			newFileName = imageService.saveImage(file.getBytes(), file.getOriginalFilename());
		} catch(IOException ex) {
			throw new RuntimeException("Erro ao Salvar imagem");
		}
		
        
        Attendance attendanceToCreate = new Attendance(person.get(), newFileName);

        Attendance savedAttendance = attendanceRepos.save(attendanceToCreate);
        ListAttendanceDTO responseDTO = new ListAttendanceDTO(savedAttendance);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }*/
    
    @PostMapping
    public Mono<ResponseEntity<ResponseDTO>> create(@RequestBody CreateAttendanceDTO data) {
        
    	String photoBase64 = data.photoBase64();

    	
        Optional<Person> person = personRepos.findByCpf(data.cpf());
        if (person.isEmpty()) {
            return Mono.just(new ResponseEntity<>(new ResponseDTO(404, "Pessoa relacionada não encontrada", null), HttpStatus.NOT_FOUND));
        }
        
        if (photoBase64 == null || photoBase64.isEmpty()) {
            return Mono.just(new ResponseEntity<>(new ResponseDTO(400, "Imagem base64 não fornecida", null), HttpStatus.BAD_REQUEST));
        }
        
        // Building the JSON request for the Recognition API
        String url = "http://127.0.0.1:8000/compare";
        String requestJson = "{\"image_base64\": \"" + photoBase64 + "\"}";

        // Send the request asynchronously
        return apiService.makeRequest(url, requestJson)
            .map(response -> {
            	
                if (response != null) {
                	
                	 Gson gson = new Gson();
                	 ResponseAttendanceDTO responseAttendanceDTO;
                	 
                	 RecognitionAPIResponseDTO responseDTO = gson.fromJson(response, RecognitionAPIResponseDTO.class);
                	 
                	 
                	 if(!responseDTO.identified())
                		 return new ResponseEntity<>(new ResponseDTO(200, "Success", new ResponseAttendanceDTO(false, null, null, null)), HttpStatus.OK);
                		 
                		 
                	 // Get Photo name
                     String[] photoName = responseDTO.refereceImagePath().split("/");
                     String referenceImgBase64 = ImageUtils.getImageBase64(photoName[6], Person.class.getName());
                     
                     
                     // Register attendance
                     Attendance attendanceToCreate = new Attendance(person.get(), UUID.randomUUID().toString());
                     Set<Group> groups = attendanceToCreate.getPerson().getGroups();
                     
                     // Logic to determine if the person is late
                     LocalDateTime loginLDT = attendanceToCreate.getRegisterDate();
                     LocalTime loginLT = LocalTime.of(loginLDT.getHour(), loginLDT.getMinute(), loginLDT.getSecond());
                     
                   
                     for(Group currentGroup : groups) {
                    	 
                    	 LocalTime maxTime = currentGroup.getInitialTime().plusMinutes(20); //07:20:00 - tempo máximo de atraso
                    	 LocalTime minTime = currentGroup.getInitialTime().minusHours(2); 	// 05:20:20 - tempo mínimo para registro
                    	 
                    	 if(loginLT.isAfter(maxTime))
                    		 attendanceToCreate.setIsLate(true);
                    	 else 
                    		 attendanceToCreate.setIsLate(false);
                    	 
                     }
                     /*
                     0. Pegar a data do registro
                     1. Pegar os grupos da Pessoa
                     2. Comparar a data com a data dos grupos
                     3. Verificar se está atrasado ou não com base no horário que registro - horário do grupo  
                     */
                     
                     Attendance savedAttendance = attendanceRepos.save(attendanceToCreate);
                    
                     responseAttendanceDTO = new ResponseAttendanceDTO(true, photoBase64, referenceImgBase64, savedAttendance.getCreatedAt().toString());
                	 
                	 
                    return new ResponseEntity<>(new ResponseDTO(200, "Success", responseAttendanceDTO), HttpStatus.OK);
                } else {
                    // In case the response is null or empty
                    return new ResponseEntity<>(new ResponseDTO(500, "Error processing image", null), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            })
            .onErrorResume(error -> {
                // Handle any errors during the async operation
                return Mono.just(new ResponseEntity<>(new ResponseDTO(500, error.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR));
            });
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        Optional<Attendance> attendanceOpt = attendanceRepos.findById(id);
        if (attendanceOpt.isEmpty())
            return new ResponseEntity<>("Registro de Presença não encontrado", HttpStatus.NOT_FOUND);

        Attendance attendanceToDelete = attendanceOpt.get();
        attendanceToDelete.setIsActive(false);
        attendanceRepos.save(attendanceToDelete);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}