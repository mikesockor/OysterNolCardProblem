package com.adfg.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transaction {

    @Id private String id;
    @Getter @Setter private Date checkInTime;
    @Getter private String type;
    @Getter private String cardId;
    @Getter private String stationName;
    @Getter private String stationType;
    @Getter private Integer stationZone;
    @Transient @Getter @Setter private double cost;
}