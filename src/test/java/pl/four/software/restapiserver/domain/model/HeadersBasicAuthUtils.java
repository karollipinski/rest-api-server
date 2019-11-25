package pl.four.software.restapiserver.domain.model;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Base64;

class HeadersBasicAuthUtils {

    static HttpHeaders getHeaders() {
        String plainCredentials = "admin:abc123";
        String base64Credentials = Base64.getEncoder()
                                         .encodeToString(plainCredentials.getBytes());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Basic " + base64Credentials);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }

}
