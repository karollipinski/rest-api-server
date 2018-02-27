package pl.four.software.restapiserver.domain.model.patient.service;

import pl.four.software.restapiserver.domain.model.patient.dto.PatientFiltr;
import pl.four.software.restapiserver.domain.model.patient.dto.PatientDto;
import pl.four.software.restapiserver.domain.model.patient.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service("PatienServiceInMemory")
public class PatientServiceInMemoryImpl implements PatientService {

    private static final AtomicLong counter = new AtomicLong();

    private static List<Patient> patients;

    static {
        patients = initPatients();
    }

    @Override
    public Optional<Patient> findById(long id) {
        return patients.stream()
                       .filter(p -> p.getId() == id)
                       .findFirst();
    }

    @Override
    public Optional<Patient> findByPesel(String pesel) {
        return patients.stream()
                       .filter(p -> Objects.equals(p.getPesel(), pesel))
                       .findFirst();
    }

    @Override
    public void save(Patient patient) {
        patient.setId(counter.incrementAndGet());
        patients.add(patient);
    }

    @Override
    public void update(Patient patient) {
        int index = patients.indexOf(patient);
        patients.set(index, patient);
    }

    @Override
    public void delete(long id) {
        for (Iterator<Patient> iterator = patients.iterator(); iterator.hasNext(); ) {
            Patient patient = iterator.next();
            if (patient.getId() == id) {
                iterator.remove();
            }
        }
    }

    @Override
    public List<Patient> findAll() {
        return patients;
    }

    @Override
    public void deleteAll() {
        patients.clear();
    }

    @Override
    public boolean isExist(Patient patient) {
        return patients.contains(patient);
    }

    @Override
    public Page<PatientDto> findByFilter(PatientFiltr filtr, Pageable pageable) {
        return null;
    }

    private static List<Patient> initPatients() {
        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient(counter.incrementAndGet(), "Adam", "Małysz", "8573635353535", new Date()));
        patients.add(new Patient(counter.incrementAndGet(), "Jan", "Kowalski", "9973635353535", new Date()));
        patients.add(new Patient(counter.incrementAndGet(), "Anna", "Nowak", "5573635353535", new Date()));
        patients.add(new Patient(counter.incrementAndGet(), "Krzysztof", "Polak", "7773635353535", new Date()));
        return patients;
    }

}
