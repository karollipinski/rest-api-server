package pl.four.software.restapiserver.domain.model.patient.rapository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.four.software.restapiserver.domain.model.patient.dto.PatientDto;
import pl.four.software.restapiserver.domain.model.patient.dto.PatientFiltr;
import pl.four.software.restapiserver.domain.model.patient.dto.QPatientDto;
import pl.four.software.restapiserver.domain.model.patient.entity.Patient;
import pl.four.software.restapiserver.domain.model.patient.entity.QPatient;

import static java.util.Optional.ofNullable;

@Repository
public class PatientQueryDslReporitory extends QueryDslRepositorySupportPageable {

    public PatientQueryDslReporitory() {
        super(Patient.class);
    }

    public Page<PatientDto> findByFilter(PatientFiltr filtr, Pageable pageable) {
        QPatient qPatient = QPatient.patient;
        JPQLQuery<PatientDto> query = from(qPatient)
                .select(new QPatientDto(qPatient.id,
                                        qPatient.name,
                                        qPatient.lastName,
                                        qPatient.pesel,
                                        qPatient.date))
                .where(getFilter(filtr));

        return pagination(query, pageable);
    }

    private BooleanBuilder getFilter(PatientFiltr filtr) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QPatient qPatient = QPatient.patient;
        ofNullable(filtr.getName())
                .filter(name -> !name.isEmpty())
                .ifPresent(name -> booleanBuilder.and(qPatient.name.eq(name)));
        ofNullable(filtr.getLastName())
                .filter(lastName -> !lastName.isEmpty())
                .ifPresent(lastName -> booleanBuilder.and(qPatient.lastName.eq(lastName)));
        ofNullable(filtr.getPesel())
                .filter(pesel -> !pesel.isEmpty())
                .ifPresent(pesel -> booleanBuilder.and(qPatient.pesel.eq(pesel)));

        return booleanBuilder;
    }

}
