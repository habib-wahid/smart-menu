package org.example.menuapp.entity.audit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Entity
@RevisionEntity
@Table(name = "revinfo")
@Getter
@Setter
public class BaseRevisionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "revseq")
    @SequenceGenerator(name = "revseq", sequenceName = "revinfo_seq", allocationSize = 1)
    @RevisionNumber
    @Column(name = "rev")
    private Long rev;

    @Column(name = "revstmp")
    @RevisionTimestamp
    private Long revstmp;

    public BaseRevisionEntity() {}
}
