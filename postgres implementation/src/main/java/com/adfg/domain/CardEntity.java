package com.adfg.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
public class CardEntity {

    @Id
    @Getter @Setter private Long id;
    @Getter @Setter private String owner;
    @Getter @Setter private Double balance;

    @Getter @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkInTime;
    @ManyToOne(cascade= CascadeType.ALL )
    @Getter @Setter private StationEntity checkinStationEntity;

}
