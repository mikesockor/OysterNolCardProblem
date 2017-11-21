package com.adfg.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Getter private Date checkTime;

    @ManyToOne(cascade= CascadeType.ALL )
    @Getter @Setter private CardEntity cardEntity;
    @ManyToOne(cascade= CascadeType.ALL )
    @Getter @Setter private StationEntity stationEntity;
    @Getter @Setter private TransactionType transactionType;

    @PrePersist
    protected void onCreate() {
        checkTime = new Date();
    }

    public Transaction(CardEntity cardEntity, StationEntity stationEntity, TransactionType transactionType) {
        this.cardEntity = cardEntity;
        this.stationEntity = stationEntity;
        this.transactionType = transactionType;
    }

}
