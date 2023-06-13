package pl.kalksztejn.mateusz.httpmethodstester.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kalksztejn.mateusz.httpmethodstester.model.Element;
import pl.kalksztejn.mateusz.httpmethodstester.model.SampleObject;
import pl.kalksztejn.mateusz.httpmethodstester.model.dto.ElementDto;
import pl.kalksztejn.mateusz.httpmethodstester.model.dto.SampleObjectDto;
import pl.kalksztejn.mateusz.httpmethodstester.service.Interface.ElementService;
import pl.kalksztejn.mateusz.httpmethodstester.service.Interface.ModelWrapper;

@RestController
@RequestMapping("/element")
@CrossOrigin(origins = "*", maxAge = 7200)
public class ElementController {

    private final ElementService elementService;

    private final ModelWrapper modelWrapper;

    @Autowired
    public ElementController(ElementService elementService, ModelWrapper modelWrapper) {
        this.elementService = elementService;
        this.modelWrapper = modelWrapper;
    }
    @GetMapping()
    public ResponseEntity<?> getElements(Pageable pageable) {
            return ResponseEntity.ok(elementService.getElements(pageable).stream().map(ElementDto::new).toList());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getElement(@PathVariable Long id) {
       Element element = elementService.getElement(id);
        if(element != null){
            return ResponseEntity.ok(new ElementDto(element));
        }
        return  ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteElement(@PathVariable Long id) {
        boolean deleted = elementService.deleteElement(id);
        if(deleted){
            return ResponseEntity.ok(true);
        }
        return  ResponseEntity.notFound().build();
    }
    @GetMapping("/object/{id}")
    public ResponseEntity<?> getSampleObjectByElement(@PathVariable Long id) {
        SampleObject sampleObject = elementService.getSampleObjectByElement(id);
        if(sampleObject != null){
            return ResponseEntity.ok(new SampleObjectDto(sampleObject));
        }
        return  ResponseEntity.notFound().build();
    }
    @PutMapping()
    public ResponseEntity<?> updateElement(@RequestBody ElementDto elementDto) {
        Element element1 = elementService.updateElement(modelWrapper.ElementDtoToElement(elementDto));
        if(element1 != null){
            return ResponseEntity.ok(new ElementDto(element1));
        }
        return  ResponseEntity.notFound().build();
    }
    @PostMapping()
    public ResponseEntity<?> setElement(@RequestBody ElementDto elementDto) {
        Element element1 = elementService.setElement(modelWrapper.ElementDtoToElement(elementDto));
        if(element1 != null){
            return ResponseEntity.ok(new ElementDto(element1));
        }
        return  ResponseEntity.notFound().build();
    }
}
