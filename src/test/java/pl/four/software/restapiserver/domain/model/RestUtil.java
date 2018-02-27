package pl.four.software.restapiserver.domain.model;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

class RestUtil {

    static boolean isError(HttpStatus status) {
        return CLIENT_ERROR.equals(status.series()) || SERVER_ERROR.equals(status.series());
    }
}