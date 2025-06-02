package org.example.menuapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "addon")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class AddOn {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addon_seq")
    @SequenceGenerator(name = "addon_seq", sequenceName = "addon_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "full_path")
    private String fullPathUrl;

    @Column(name = "rating")
    private Double rating;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "addon_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    @Builder.Default
    private Set<Item> item = new HashSet<>();
}
