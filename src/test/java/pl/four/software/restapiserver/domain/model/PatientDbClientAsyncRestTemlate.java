package pl.four.software.restapiserver.domain.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import pl.four.software.restapiserver.domain.model.patient.entity.Patient;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
public class PatientDbClientAsyncRestTemlate {

    private static final String REST_SERVER_URI = "http://localhost:8090/RestApiServer/api/db/patients";

    public static void main(String[] args) {
        listAllAsync();
        listAllAsyncCalback();
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

    private static void listAllAsync() {
        log.info("List All Async Patient --------------");
        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
        ListenableFuture<ResponseEntity<List<Patient>>> exchange = asyncRestTemplate.exchange(REST_SERVER_URI, HttpMethod.GET, new HttpEntity<>(getHeaders()),
                                                                                              new ParameterizedTypeReference<List<Patient>>() {});

        try {
            ResponseEntity<List<Patient>> responseEntity = exchange.get();
            if (OK.equals(responseEntity.getStatusCode())) {
                log.info("List Async result {}", responseEntity.getBody());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void listAllAsyncCalback() {
        log.info("List All Async calback Patient --------------");
        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
        ListenableFuture<ResponseEntity<List<Patient>>> exchange = asyncRestTemplate.exchange(REST_SERVER_URI, HttpMethod.GET, new HttpEntity<>(getHeaders()),
                                                                                              new ParameterizedTypeReference<List<Patient>>() {});

        exchange.addCallback(new ListenableFutureCallback<ResponseEntity<List<Patient>>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("Error :", ex.getMessage());
            }

            @Override
            public void onSuccess(ResponseEntity<List<Patient>> result) {
                if (OK.equals(result.getStatusCode())) {
                    log.info("List Async calback result {}", result.getBody());
                }
            }
        });
    }

}









