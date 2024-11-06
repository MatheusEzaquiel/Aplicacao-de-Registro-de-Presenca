package com.mbe.ada.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mbe.ada.model.attendance.Attendance;
import com.mbe.ada.model.attendance.dto.CreateAttendanceDTO;
import com.mbe.ada.model.attendance.dto.ListAttendanceDTO;
import com.mbe.ada.model.auth.dto.ResponseDTO;
import com.mbe.ada.model.person.Person;
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
    	
    	Long personId = 1L;

        // Simulate fetching a person, assuming you're using the repository or service
        Optional<Person> person = personRepos.findById(personId);
        if (person.isEmpty()) {
            return Mono.just(new ResponseEntity<>(new ResponseDTO(404, "Pessoa relacionada não encontrada", null), HttpStatus.NOT_FOUND));
        }

        
        // Validação da imagem base64
        if (photoBase64 == null || photoBase64.isEmpty()) {
            return Mono.just(new ResponseEntity<>(new ResponseDTO(400, "Imagem base64 não fornecida", null), HttpStatus.BAD_REQUEST));
        }
        
        // Build the JSON request for the external service
        String url = "http://127.0.0.1:8000/compare";
        String requestJson = "{\"image_base64\": \"" + photoBase64 + "\"}";

        // Send the request asynchronously
        return apiService.makeRequest(url, requestJson)
            .map(response -> {
                // Process the response
                if (response != null) {
                    // Assuming the service returns a valid response that you can parse
                    return new ResponseEntity<>(new ResponseDTO(200, "Success", response), HttpStatus.OK);
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
