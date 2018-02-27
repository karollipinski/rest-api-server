package pl.four.software.restapiserver.domain.model.patient.service;

import pl.four.software.restapiserver.domain.model.patient.dto.PatientFiltr;
import pl.four.software.restapiserver.domain.model.patient.dto.PatientDto;
import pl.four.software.restapiserver.domain.model.patient.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PatientService {

    Optional<Patient> findById(long id);

    Optional<Patient> findByPesel(String pesel);

    void save(Patient patient);

    void update(Patient patient);

    void delete(long id);

    List<Patient> findAll();

    void deleteAll();

    boolean isExist(Patient patient);

    Page<PatientDto> findByFilter(PatientFiltr filtr, Pageable pageable);
}
