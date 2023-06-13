package pl.kalksztejn.mateusz.httpmethodstester.service.Interface;

import pl.kalksztejn.mateusz.httpmethodstester.model.Element;
import pl.kalksztejn.mateusz.httpmethodstester.model.SampleObject;
import pl.kalksztejn.mateusz.httpmethodstester.model.dto.ElementDto;
import pl.kalksztejn.mateusz.httpmethodstester.model.dto.SampleObjectDto;

public interface ModelWrapper {

    Element ElementDtoToElement(ElementDto elementDto);

    SampleObject SampleObjectDtoToSampleObject(SampleObjectDto sampleObjectDto);
}
