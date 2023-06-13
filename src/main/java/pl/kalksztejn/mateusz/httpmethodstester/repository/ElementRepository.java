package pl.kalksztejn.mateusz.httpmethodstester.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.kalksztejn.mateusz.httpmethodstester.model.Element;
import pl.kalksztejn.mateusz.httpmethodstester.model.SampleObject;

public interface ElementRepository extends JpaRepository<Element, Long> {

    Page<Element> findElementsBySampleObject(SampleObject sampleObject, Pageable pageable);
}
