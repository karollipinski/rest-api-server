package pl.four.software.restapiserver.domain.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import pl.four.software.restapiserver.domain.model.patient.dto.ClinicError;
import pl.four.software.restapiserver.domain.model.patient.entity.Patient;

import java.io.IOException;
import java.net.URI;
import java.util.*;

@Slf4j
public class ClinicDbClientRestTemplate {

    private static final String REST_SERVER_URI = "http://localhost:8090/RestApiServer/api/db/patients";

    public static void main(String[] args) {
        listAll();
        getPatient();
        getPatientEntity();
        getPatientErrorHandler();
        createPatient();
        updatePatient();
        delete();
    }

    private static HttpHeaders getHeaders() {
        String plainCredentials = "admin:abc123";
        String base64Credentials = Base64.getEncoder()
                                         .encodeToString(plainCredentials.getBytes());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Basic " + base64Credentials);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }

    private static void listAll() {
        log.info("Patient list All --------------");

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<List<Patient>> exchange = restTemplate.exchange(REST_SERVER_URI,
                                                                       HttpMethod.GET,
                                                                       new HttpEntity<>(getHeaders()),
                                                                       new ParameterizedTypeReference<List<Patient>>() {});
        List<Patient> result = exchange.getBody();
        if (Objects.nonNull(result) && !result.isEmpty()) {
            log.info("All patient result {}", result);
        }
        else {
            log.info("No patient exist ----------------");
        }
    }

    private static void getPatient() {
        log.info("Get Patient --------------");
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Patient> exchange = restTemplate.exchange(REST_SERVER_URI + "/1",
                                                                 HttpMethod.GET,
                                                                 new HttpEntity<>(getHeaders()),
                                                                 Patient.class);
        Patient patient = exchange.getBody();
        log.info("Get Patient result {} ", patient);
    }

    private static void getPatientEntity() {
        log.info("Get Patient Entity --------------");
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpEntity<Object> requestEntity = new HttpEntity<>(getHeaders());
            ResponseEntity<Patient> entity = restTemplate.exchange(REST_SERVER_URI + "/8", HttpMethod.GET,
                                                                   requestEntity,
                                                                   Patient.class);
            if (entity.getStatusCode()
                      .equals(HttpStatus.OK)) {
                log.info("Get Patient ResponseEntity result{} ", entity.getBody());
            }
            else {
                log.info("Get Patient ResponseEntity status {} body {}", entity.getStatusCode(), entity.getBody());
            }
        } catch (HttpClientErrorException e) {
            log.info("HttpClientErrorException {}", e.getMessage());
        } catch (HttpServerErrorException e) {
            log.info("HttpServerErrorException {}", e.getMessage());
        }
    }

    private static void getPatientErrorHandler() {
        log.info("Get Patient Error Handler --------------");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RestResponseErrorHandler());

        ResponseEntity<String> responseEntity = restTemplate.exchange(REST_SERVER_URI + "/8",
                                                                      HttpMethod.GET,
                                                                      new HttpEntity<>(getHeaders()), String.class);

        String responseEntityBody = responseEntity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (RestUtil.isError(responseEntity.getStatusCode())) {
                ClinicError clinicError = objectMapper.readValue(responseEntityBody, ClinicError.class);
                log.info("Get Patient Error Handler {}", clinicError);
            }
            else {
                Patient patient = objectMapper.readValue(responseEntityBody, Patient.class);
                log.info("Get Patient Error Handler result {}", patient);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createPatient() {
        log.info("Create Patient Entity --------------");
        RestTemplate restTemplate = new RestTemplate();
        Patient patient = Patient.builder()
                                 .id(0)
                                 .name("Name")
                                 .lastName("Last name1")
                                 .pesel("4734583445")
                                 .date(new Date())
                                 .build();

        HttpEntity request = new HttpEntity(patient, getHeaders());
        URI uri = restTemplate.postForLocation(REST_SERVER_URI, request, Patient.class);
        log.info("Location result {}", uri.toASCIIString());
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
        HttpEntity request = new HttpEntity(patient, getHeaders());
        ResponseEntity<String> exchange = restTemplate.exchange(REST_SERVER_URI + "/1", HttpMethod.PUT, new HttpEntity<>(getHeaders()), String.class);

        log.info("Updateg patient result {}", patient);
    }

    private static void delete() {
        log.info("Delete Patient --------------");
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> exchange = restTemplate.exchange(REST_SERVER_URI + "/1", HttpMethod.DELETE, new HttpEntity<>(getHeaders()), String.class);
        log.info("Delete result {}", exchange.getBody());

    }

}















