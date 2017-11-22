package com.adfg.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionResponse {
    @Getter @Setter private String message;
    @Getter @Setter private double cost;
}