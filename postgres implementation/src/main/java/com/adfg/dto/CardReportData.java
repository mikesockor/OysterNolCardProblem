package com.adfg.dto;

import com.adfg.domain.StationType;
import com.adfg.domain.TransactionType;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
public class CardReportData {

    @Setter public Date checkTime;
    @Setter public String name;
    @Setter public StationType stationType;
    @Setter public Integer zone;
    @Setter public String agglomerationName;
    @Setter public TransactionType transactionType;
}
