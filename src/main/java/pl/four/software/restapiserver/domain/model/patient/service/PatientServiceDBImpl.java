package pl.four.software.restapiserver.domain.model.patient.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pl.four.software.restapiserver.domain.model.patient.dto.PatientDto;
import pl.four.software.restapiserver.domain.model.patient.dto.PatientFiltr;
import pl.four.software.restapiserver.domain.model.patient.entity.Patient;
import pl.four.software.restapiserver.domain.model.patient.rapository.PatientQueryDslReporitory;
import pl.four.software.restapiserver.domain.model.patient.rapository.PatientRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service("PatientServiceDB")
public class PatientServiceDBImpl implements PatientService {

    private PatientRepository patientRepository;
    private PatientQueryDslReporitory patientQueryDslReporitory;

    @Override
    public Optional<Patient> findById(long id) {
        return patientRepository.findById(id);
    }

    @Override
    public Optional<Patient> findByPesel(String pesel) {
        return Optional.ofNullable(patientRepository.findByPesel(pesel));
    }

    @Override
    @Secured("ADMIN")
    public void save(Patient patient) {
        patientRepository.save(patient);
    }

    @Override
    @PreAuthorize(value = "hasRole('ADMIN')")
    public void update(Patient patient) {
        patientRepository.save(patient);
    }

    @Override
    public void delete(long id) {
        patientRepository.deleteById(id);
    }

    @Override
    @PreAuthorize(value = "hasRole('USER')")
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteAll() {
        patientRepository.deleteAll();
    }

    @Override
    public boolean isExist(Patient patient) {
        return patientRepository.isExist(patient.getPesel());
    }

    @Override
    public Page<PatientDto> findByFilter(PatientFiltr filtr, Pageable pageable) {
        return patientQueryDslReporitory.findByFilter(filtr, pageable);
    }
}
