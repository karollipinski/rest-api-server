package pl.four.software.restapiserver.domain.model.patient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pl.four.software.restapiserver.domain.model.patient.dto.ClinicError;
import pl.four.software.restapiserver.domain.model.patient.dto.PatientDto;
import pl.four.software.restapiserver.domain.model.patient.dto.PatientFiltr;
import pl.four.software.restapiserver.domain.model.patient.entity.Patient;
import pl.four.software.restapiserver.domain.model.patient.service.PatientService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/db/patients")
public class PatientDbController {

    @Autowired
    @Qualifier("PatientServiceDB")
    private PatientService patientService;

    @GetMapping()
    public ResponseEntity<List<Patient>> listAll() {
        log.info("List all patients ");
        List<Patient> patients = patientService.findAll();
        return Optional.of(patients)
                       .filter(list -> !list.isEmpty())
                       .map(ResponseEntity::ok)
                       .orElseGet(ResponseEntity.noContent()::build);
    }

    @PostMapping("/search")
    public Page<PatientDto> getPageable(@PageableDefault(sort = "lastName", direction = Sort.Direction.DESC) Pageable pageable,
                                        @RequestBody PatientFiltr filtr) {
        return patientService.findByFilter(filtr, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity getPatient(@PathVariable("id") long id) {
        log.info("Get patient {}", id);
        return patientService.findById(id)
                             .map(ResponseEntity::ok)
                             .orElseGet(() -> new ResponseEntity(new ClinicError("PatientDto with id " + id + " not found"), HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    public ResponseEntity create(@RequestBody Patient patient, UriComponentsBuilder uriComponentsBuilder) {
        log.info("Create patient {}", patient);
        if (patientService.isExist(patient)) {
            log.error("Unable to create patient. PatientDto with pesel {} alredy exist", patient.getPesel());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body(new ClinicError("Unable to update patient. PatientDto with pesel  " + patient.getPesel() + " alredy exist"));

        }
        patientService.save(patient);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("/api/patients/{id}")
                                                    .buildAndExpand(patient.getId())
                                                    .toUri());
        return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable("id") long id, @RequestBody Patient patient) {
        log.info("Update patient with id {}", id);

        return patientService.findById(id)
                             .map(p -> getUpdateAndRetirnResponseEntity(patient, p))
                             .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT)
                                                            .body(new ClinicError("Unable to update patient. PatientDto with id  " + id + " alredy exist")));
    }

    private ResponseEntity getUpdateAndRetirnResponseEntity(@RequestBody Patient patient, Patient entity) {
        mapPatient(patient, entity);
        patientService.update(entity);
        return ResponseEntity.ok(entity);
    }

    private void mapPatient(@RequestBody Patient patient, Patient entity) {
        entity.setName(patient.getName());
        entity.setLastName(patient.getLastName());
        entity.setPesel(patient.getPesel());
        entity.setDate(entity.getDate());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") long id) {
        log.info("Delete patient with id {}", id);

        Optional<Patient> patientOptional = patientService.findById(id);
        if (patientOptional.isPresent()) {
            patientService.delete(id);
            return ResponseEntity.noContent()
                                 .build();
        }

        log.info("Unable to delete. PatientDto with id {} not found", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ClinicError("Unable to delete. PatientDto with id  " + id + " not found"));
    }

    @DeleteMapping
    public ResponseEntity deleteAll() {
        log.info("Delete all patient ");

        patientService.deleteAll();
        return ResponseEntity.noContent()
                             .build();
    }
}





















