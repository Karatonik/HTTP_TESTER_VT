package pl.kalksztejn.mateusz.httpmethodstester.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kalksztejn.mateusz.httpmethodstester.model.Element;
import pl.kalksztejn.mateusz.httpmethodstester.model.SampleObject;
import pl.kalksztejn.mateusz.httpmethodstester.model.dto.ElementDto;
import pl.kalksztejn.mateusz.httpmethodstester.model.dto.SampleObjectDto;
import pl.kalksztejn.mateusz.httpmethodstester.service.Interface.ModelWrapper;
import pl.kalksztejn.mateusz.httpmethodstester.service.Interface.SampleObjectService;

import java.util.List;

@RestController
@RequestMapping("/sample_object")
@CrossOrigin(origins = "*", maxAge = 7200)
public class SampleObjectController {

    private final SampleObjectService sampleObjectService;

    private final ModelWrapper modelWrapper;

    @Autowired
    public SampleObjectController(SampleObjectService sampleObjectService, ModelWrapper modelWrapper) {
        this.sampleObjectService = sampleObjectService;
        this.modelWrapper = modelWrapper;
    }

    @GetMapping()
    public ResponseEntity<?> getSampleObjects(Pageable pageable) {
        return ResponseEntity.ok(sampleObjectService.getSampleObjects(pageable).stream().map(SampleObjectDto::new).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSampleObject(@PathVariable Long id) {
        SampleObject sampleObject = sampleObjectService.getSampleObject(id);
        if (sampleObject != null) {
            return ResponseEntity.ok(new SampleObjectDto(sampleObject));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSampleObject(@PathVariable Long id) {
        boolean deleted = sampleObjectService.deleteSampleObject(id);
        if (deleted) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/elements/{id}")
    public ResponseEntity<?> getSampleObjectElements(@PathVariable Long id) {
        List<Element> elements = sampleObjectService.getSampleObjectElements(id);
        if (!elements.isEmpty()) {
            return ResponseEntity.ok(elements.stream().map(ElementDto::new).toList());
        }
        return ResponseEntity.ok(null);
    }

    @GetMapping("/elements/page/{id}")
    public ResponseEntity<?> getSampleObjectElements(@PathVariable Long id, Pageable pageable) {
        Page<Element> page = sampleObjectService.getSampleObjectElements(id, pageable);
        if (page != null && page.hasContent()) {
            List<ElementDto> elementDTOs = page.getContent().stream()
                    .map(ElementDto::new)
                    .toList();
            return ResponseEntity.ok(elementDTOs);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<?> setSampleObject(@RequestBody SampleObjectDto sampleObjectDto) {
        SampleObject sampleObject = sampleObjectService.setSampleObject(modelWrapper.SampleObjectDtoToSampleObject(sampleObjectDto));
        if (sampleObject != null) {
            return ResponseEntity.ok(new SampleObjectDto(sampleObject));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping()
    public ResponseEntity<?> updateSampleObject(@RequestBody SampleObjectDto sampleObjectDto) {
        SampleObject sampleObject = sampleObjectService.updateSampleObject(modelWrapper.SampleObjectDtoToSampleObject(sampleObjectDto));
        if (sampleObject != null) {
            return ResponseEntity.ok(new SampleObjectDto(sampleObject));
        }
        return ResponseEntity.notFound().build();
    }

}
