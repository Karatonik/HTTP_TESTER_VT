package pl.kalksztejn.mateusz.httpmethodstester.service.Interface;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.kalksztejn.mateusz.httpmethodstester.model.Element;
import pl.kalksztejn.mateusz.httpmethodstester.model.SampleObject;

import java.util.List;

public interface SampleObjectService {

    Page<SampleObject> getSampleObjects(Pageable pageable);

    SampleObject getSampleObject(long id);

    boolean deleteSampleObject(long id);

    List<Element> getSampleObjectElements(long id);

    Page<Element> getSampleObjectElements(long id, Pageable pageable);

    SampleObject setSampleObject(SampleObject sampleObject);

    SampleObject updateSampleObject(SampleObject sampleObject);

}
