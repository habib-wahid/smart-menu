package org.example.menuapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "addon")
@Getter @Setter
public class Addon {

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

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "rating")
    private Double rating;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "addon_item",
            joinColumns = @JoinColumn(name = "addon_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private Set<Item> item = new HashSet<>();
}
