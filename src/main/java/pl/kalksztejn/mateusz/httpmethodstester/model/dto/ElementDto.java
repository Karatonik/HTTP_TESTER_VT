package pl.kalksztejn.mateusz.httpmethodstester.model.dto;

import lombok.*;
import pl.kalksztejn.mateusz.httpmethodstester.model.Element;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ElementDto {

    private Long elementId;
    private String name;
    private String description;
    private LocalDateTime dataAndTimeOfCreation;
    private Long sampleObjectId;

    public ElementDto(String name, String description, LocalDateTime dataAndTimeOfCreation, Long sampleObjectId) {
        this.name = name;
        this.description = description;
        this.dataAndTimeOfCreation = dataAndTimeOfCreation;
        this.sampleObjectId = sampleObjectId;
    }
    public ElementDto(Element element){
        this.elementId = element.getElementId();
        this.description = element.getDescription();
        this.name = element.getName();
        this.dataAndTimeOfCreation = element.getDataAndTimeOfCreation();
        this.sampleObjectId = element.getSampleObject().getObjectId();
    }
}
