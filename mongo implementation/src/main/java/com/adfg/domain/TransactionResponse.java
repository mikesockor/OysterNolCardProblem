package com.adfg.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionResponse {
    private String message;
    private double cost;
}