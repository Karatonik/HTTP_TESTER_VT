package pl.kalksztejn.mateusz.httpmethodstester.service.Interface;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.kalksztejn.mateusz.httpmethodstester.model.Element;
import pl.kalksztejn.mateusz.httpmethodstester.model.SampleObject;

public interface ElementService {

    Page<Element> getElements(Pageable pageable);
    Element getElement(long id);

    boolean deleteElement(long id);

    SampleObject getSampleObjectByElement(long elementId);

    Element updateElement(Element element);

    Element setElement(Element element);
}
