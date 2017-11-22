package com.adfg.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Card {
    @Id private String id;
    @Getter @Setter private Double balance;
    @Getter @Setter private Date checkInTime;
    @Getter private String stationType;
    @Getter private Integer stationZone;
}