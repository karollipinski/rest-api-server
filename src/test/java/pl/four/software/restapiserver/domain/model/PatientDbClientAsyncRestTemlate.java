package pl.four.software.restapiserver.domain.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import pl.four.software.restapiserver.domain.model.patient.entity.Patient;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
public class PatientDbClientAsyncRestTemlate {

    private static final String REST_SERVER_URI = "http://localhost:8090/api/db/patients";

    public static void main(String[] args) {
        listAllAsync();
        listAllAsyncCalback();
    }

    private static void listAllAsync() {
        log.info("List All Async Patient --------------");
        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
        ListenableFuture<ResponseEntity<List<Patient>>> exchange = asyncRestTemplate.exchange(REST_SERVER_URI, HttpMethod.GET, new HttpEntity<>(HeadersBasicAuthUtils.getHeaders()),
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
        ListenableFuture<ResponseEntity<List<Patient>>> exchange = asyncRestTemplate.exchange(REST_SERVER_URI, HttpMethod.GET, new HttpEntity<>(HeadersBasicAuthUtils.getHeaders()),
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









