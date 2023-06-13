package pl.kalksztejn.mateusz.httpmethodstester.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kalksztejn.mateusz.httpmethodstester.model.Element;
import pl.kalksztejn.mateusz.httpmethodstester.model.SampleObject;
import pl.kalksztejn.mateusz.httpmethodstester.model.dto.ElementDto;
import pl.kalksztejn.mateusz.httpmethodstester.model.dto.SampleObjectDto;
import pl.kalksztejn.mateusz.httpmethodstester.repository.ElementRepository;
import pl.kalksztejn.mateusz.httpmethodstester.repository.SampleObjectRepository;
import pl.kalksztejn.mateusz.httpmethodstester.service.Interface.ModelWrapper;

import java.util.Optional;

@Service
public class ModelWrapperImp implements ModelWrapper {

    private final SampleObjectRepository sampleObjectRepository;

    @Autowired
    public ModelWrapperImp(SampleObjectRepository sampleObjectRepository, ElementRepository elementRepository) {
        this.sampleObjectRepository = sampleObjectRepository;
    }

    @Override
    public Element ElementDtoToElement(ElementDto elementDto) {
        Element element = new Element();
        if (elementDto.getElementId() != null) {
            element.setElementId(elementDto.getElementId());
        }
        element.setName(elementDto.getName());
        element.setDescription(elementDto.getDescription());
        if (elementDto.getSampleObjectId() != null) {
            Optional<SampleObject> sampleObject = sampleObjectRepository.findById(elementDto.getSampleObjectId());
            sampleObject.ifPresent(element::setSampleObject);
        }
        element.setDataAndTimeOfCreation(elementDto.getDataAndTimeOfCreation());
        return element;
    }

    @Override
    public SampleObject SampleObjectDtoToSampleObject(SampleObjectDto sampleObjectDto) {
        SampleObject sampleObject = new SampleObject();
        if (sampleObjectDto.getObjectId() != null) {
            sampleObject.setObjectId(sampleObjectDto.getObjectId());
            Optional<SampleObject> so = sampleObjectRepository.findById(sampleObjectDto.getObjectId());
            so.ifPresent(object -> sampleObject.setElements(object.getElements()));
        }
        sampleObject.setName(sampleObjectDto.getName());
        sampleObject.setDescription(sampleObjectDto.getDescription());
        sampleObject.setDataAndTimeOfCreation(sampleObjectDto.getDataAndTimeOfCreation());
        return sampleObject;
    }
}
