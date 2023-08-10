package pl.kalksztejn.mateusz.httpmethodstester.service.Implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kalksztejn.mateusz.httpmethodstester.model.Element;
import pl.kalksztejn.mateusz.httpmethodstester.model.SampleObject;
import pl.kalksztejn.mateusz.httpmethodstester.repository.ElementRepository;
import pl.kalksztejn.mateusz.httpmethodstester.repository.SampleObjectRepository;
import pl.kalksztejn.mateusz.httpmethodstester.service.Interface.SampleObjectService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SampleObjectServiceImp implements SampleObjectService {

    private final SampleObjectRepository sampleObjectRepository;
    private final ElementRepository elementRepository;

    public SampleObjectServiceImp(SampleObjectRepository sampleObjectRepository, ElementRepository elementRepository) {
        this.sampleObjectRepository = sampleObjectRepository;
        this.elementRepository = elementRepository;
    }

    @Override
    public Page<SampleObject> getSampleObjects(Pageable pageable) {
        return sampleObjectRepository.findAll(pageable);
    }

    @Override
    public SampleObject getSampleObject(long id) {
        Optional<SampleObject> optionalSampleObject = sampleObjectRepository.findById(id);
        return optionalSampleObject.orElse(null);
    }

    @Override
    @Transactional
    public boolean deleteSampleObject(long id) {
        Optional<SampleObject> optionalSampleObject = sampleObjectRepository.findById(id);
        if (optionalSampleObject.isPresent()) {
            SampleObject sampleObject = optionalSampleObject.get();
            elementRepository.deleteAll(sampleObject.getElements());
            sampleObject = sampleObjectRepository.getReferenceById(id);
            if (sampleObject.getElements().isEmpty()) {
                sampleObjectRepository.delete(optionalSampleObject.get());
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Element> getSampleObjectElements(long id) {
        Optional<SampleObject> optionalSampleObject = sampleObjectRepository.findById(id);
        if (optionalSampleObject.isPresent()) {
            return optionalSampleObject.get().getElements();
        }
        return new ArrayList<>();
    }

    @Override
    public Page<Element> getSampleObjectElements(long id, Pageable pageable) {
        Optional<SampleObject> optionalSampleObject = sampleObjectRepository.findById(id);
        return optionalSampleObject.map(sampleObject -> elementRepository.findElementsBySampleObject(sampleObject, pageable)).orElse(null);
    }

    @Override
    public SampleObject setSampleObject(SampleObject sampleObject) {
        sampleObject.setObjectId(null);
        List<Element> elements = sampleObject.getElements();
        sampleObject.setElements(new ArrayList<>());
        sampleObject = sampleObjectRepository.save(sampleObject);
        SampleObject finalSampleObject = sampleObject;
        if (sampleObject.getElements() != null && elements != null) {
            elements = elements.stream().peek(element -> element.setSampleObject(finalSampleObject)).toList();
            elementRepository.saveAll(elements);
        }

        return sampleObjectRepository.getReferenceById(sampleObject.getObjectId());
    }

    @Override
    @Transactional
    public SampleObject updateSampleObject(SampleObject sampleObject) {
        Optional<SampleObject> optionalSampleObject = sampleObjectRepository.findById(sampleObject.getObjectId());
        if (optionalSampleObject.isPresent()) {
            return sampleObjectRepository.save(sampleObject);
        }
        return null;
    }
}
