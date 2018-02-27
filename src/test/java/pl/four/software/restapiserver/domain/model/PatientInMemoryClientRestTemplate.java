package pl.four.software.restapiserver.domain.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import pl.four.software.restapiserver.domain.model.patient.dto.ClinicError;
import pl.four.software.restapiserver.domain.model.patient.entity.Patient;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.List;

@Slf4j
public class PatientInMemoryClientRestTemplate {

    private static final String REST_SERVER_URI = "http://localhost:8090/RestApiServer/api/memory/patients";

    public static void main(String[] args) {

        listAll();
        getPatient();
        getPatientEntity();
        createPatient();
        updatePatient();
        delete();
    }

    private static void listAll() {
        log.info("Patient list All --------------");

        RestTemplate restTemplate = new RestTemplate();
        List<Patient> result = restTemplate.getForObject(REST_SERVER_URI, List.class);
        if (!result.isEmpty()) {
            log.info("All patient: {}", result);
        }
        else {
            log.info("No patient exist ----------------");
        }
    }

    private static void getPatient() {
        log.info("Get Patient --------------");
        RestTemplate restTemplate = new RestTemplate();
        Patient patient = restTemplate.getForObject(REST_SERVER_URI + "/4", Patient.class);
        log.info("Get Patient result {} ", patient);
    }

    private static void getPatientError() {
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RestResponseErrorHandler());
        ResponseEntity<String> response = restTemplate.getForEntity(REST_SERVER_URI + "/7", String.class);

        String responseBody = response.getBody();
        try {
            if (RestUtil.isError(response.getStatusCode())) {
                ClinicError error = objectMapper.readValue(responseBody, ClinicError.class);
                log.info("Get ClinicError Entity {} ", error);
            }
            else {
                Patient success = objectMapper.readValue(responseBody, Patient.class);
                log.info("Get Patient Entity result {} ", success);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getPatientEntity() {
        log.info("Get Patient Entity --------------");
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Patient> entity = restTemplate.getForEntity(REST_SERVER_URI + "/8", Patient.class);
            if (entity.getStatusCode()
                      .equals(HttpStatus.OK)) {
                log.info("Get Patient Entity result {} ", entity.getBody());
            }
            else {
                log.info("Get Patient Entity status {} body {}", entity.getStatusCode(), entity.getBody());
            }
        } catch (HttpClientErrorException e) {
            log.info("HttpClientErrorException {}", e.getMessage());
        } catch (HttpServerErrorException e) {
            log.info("HttpServerErrorException {}", e.getMessage());
        }
    }

    private static void createPatient() {
        log.info("Create Patient Entity --------------");
        RestTemplate restTemplate = new RestTemplate();
        Patient patient = Patient.builder()
                                 .name("Name")
                                 .lastName("Last name")
                                 .pesel("473458345")
                                 .date(new Date())
                                 .build();

        URI uri = restTemplate.postForLocation(REST_SERVER_URI, patient, Patient.class);
        log.info("Location {}", uri.toASCIIString());
    }

    private static void updatePatient() {
        log.info("Update Patient Entity --------------");
        RestTemplate restTemplate = new RestTemplate();
        Patient patient = Patient.builder()
                                 .name("Name")
                                 .lastName("Kowalski")
                                 .pesel("473458345")
                                 .date(new Date())
                                 .build();
        restTemplate.put(REST_SERVER_URI + "/5", patient);
        log.info("Updateg patient result {}", patient);
    }

    private static void delete() {
        log.info("Delete Patient --------------");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(REST_SERVER_URI + "/6");
    }
}















