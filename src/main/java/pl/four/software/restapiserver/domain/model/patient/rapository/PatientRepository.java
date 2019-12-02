package pl.four.software.restapiserver.domain.model.patient.rapository;

import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.four.software.restapiserver.domain.model.patient.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Patient findByPesel(String pesel);

    @Query("SELECT (count(p)>0) as result FROM Patient p WHERE p.pesel=:pesel")
    boolean isExist(@Param(value = "pesel") String pesel);


    @Query("Select new pl.four.software.restapiserver.domain.model.patient.dto.PatientDto(p.name, p.lastName) from Patient p where p.pesel = :pesel1")
    Patient findPatientDtoByPesel(@Param(value = "pesel1") String pesel);

}
