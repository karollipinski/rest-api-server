package pl.four.software.restapiserver.domain.model.patient.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class PatientDto {

    private long id;

    private String name;

    private String lastName;

    private String pesel;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @QueryProjection
    public PatientDto(long id, String name, String lastName, String pesel, Date date) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.pesel = pesel;
        this.date = date;
    }

    public PatientDto(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }
}
