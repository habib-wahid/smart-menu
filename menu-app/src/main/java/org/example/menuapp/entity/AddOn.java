package org.example.menuapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class AddOn {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String filePath;
    private String fullPathUrl;
    private Double rating;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "addon_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private Set<Item> item = new HashSet<>();
}
