package com.adfg.domain;

import com.adfg.RefundService;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Card implements RefundService {
    @Id
    private String id;
    private Double balance;
    private Date checkInTime;
    private String stationType;
    private Integer stationZone;
}