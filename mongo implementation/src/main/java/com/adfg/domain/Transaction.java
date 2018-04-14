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

    @Id
    private String id;
    private Date checkInTime;
    private String type;
    private String cardId;
    private String stationName;
    private String stationType;
    private Integer stationZone;
    @Transient
    private double cost;
}