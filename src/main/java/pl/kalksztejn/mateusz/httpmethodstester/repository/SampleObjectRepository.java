package pl.kalksztejn.mateusz.httpmethodstester.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kalksztejn.mateusz.httpmethodstester.model.SampleObject;

public interface SampleObjectRepository extends JpaRepository<SampleObject, Long> {
}
