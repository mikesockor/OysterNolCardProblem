package com.adfg.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
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
    private double cost;

    @CreatedDate
    private Instant createdDate;
    @LastModifiedDate
    private Instant lastModifiedDate;

}