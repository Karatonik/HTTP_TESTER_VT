package pl.kalksztejn.mateusz.httpmethodstester.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "element")

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Element {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long elementId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 100)
    private String description;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataAndTimeOfCreation;
    @ManyToOne
    @JoinColumn(name = "sample_object")
    private SampleObject sampleObject;

    public Element(String name, String description, LocalDateTime dataAndTimeOfCreation, SampleObject sampleObject) {
        this.name = name;
        this.description = description;
        this.dataAndTimeOfCreation = dataAndTimeOfCreation;
        this.sampleObject = sampleObject;
    }
}
