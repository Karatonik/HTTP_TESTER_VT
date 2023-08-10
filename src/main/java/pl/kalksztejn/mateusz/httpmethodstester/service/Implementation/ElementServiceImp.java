package pl.kalksztejn.mateusz.httpmethodstester.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kalksztejn.mateusz.httpmethodstester.model.Element;
import pl.kalksztejn.mateusz.httpmethodstester.model.SampleObject;
import pl.kalksztejn.mateusz.httpmethodstester.repository.ElementRepository;
import pl.kalksztejn.mateusz.httpmethodstester.service.Interface.ElementService;

import java.util.Optional;

@Service
public class ElementServiceImp implements ElementService {

    private final ElementRepository elementRepository;

    @Autowired
    public ElementServiceImp(ElementRepository elementRepository) {
        this.elementRepository = elementRepository;
    }

    @Override
    public Page<Element> getElements(Pageable pageable) {
        return elementRepository.findAll(pageable);
    }

    @Override
    public Element getElement(long id) {
       Optional<Element> optionalElement = elementRepository.findById(id);
        return optionalElement.orElse(null);
    }

    @Override
    @Transactional
    public boolean deleteElement(long id) {
        Optional<Element> optionalElement = elementRepository.findById(id);
        if (optionalElement.isPresent()) {
            elementRepository.delete(optionalElement.get());
            return true;
        }
        return false;
    }

    @Override
    public SampleObject getSampleObjectByElement(long elementId) {
        Optional<Element> optionalElement = elementRepository.findById(elementId);
        return optionalElement.map(Element::getSampleObject).orElse(null);
    }

    @Override
    @Transactional
    public Element updateElement(Element element) {
        Optional<Element> optionalElement = elementRepository.findById(element.getElementId());
        if (optionalElement.isPresent()) {
            return elementRepository.save(element);
        }
        return null;
    }

    @Override
    public Element setElement(Element element) {
        element.setElementId(null);
        return elementRepository.save(element);
    }
}
