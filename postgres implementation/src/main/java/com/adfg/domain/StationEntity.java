package com.adfg.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class StationEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Getter @Setter private String name;
    @Getter @Setter private StationType stationType;
    @Getter @Setter private Integer zone;
    @ManyToOne(cascade= CascadeType.ALL )
    @Getter @Setter private AgglomerationEntity agglomerationEntity;

    public boolean isFirst(){
        return zone.equals(1);
    }
}
