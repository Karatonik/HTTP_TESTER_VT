package pl.kalksztejn.mateusz.httpmethodstester.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

@Entity(name = "sample_object")
public class SampleObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long objectId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 100)
    private String description;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataAndTimeOfCreation;

    @OneToMany(mappedBy = "sampleObject")
    @ToString.Exclude
    private List<Element> elements;

    public SampleObject(String name, String description, LocalDateTime dataAndTimeOfCreation, List<Element> elements) {
        this.name = name;
        this.description = description;
        this.dataAndTimeOfCreation = dataAndTimeOfCreation;
        this.elements = elements;
    }

    public SampleObject(String name, String description, LocalDateTime dataAndTimeOfCreation) {
        this.name = name;
        this.description = description;
        this.dataAndTimeOfCreation = dataAndTimeOfCreation;
        this.elements = new ArrayList<>();
    }
}
