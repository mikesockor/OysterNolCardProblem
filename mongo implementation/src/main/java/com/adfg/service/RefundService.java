package com.adfg.service;

import com.adfg.domain.Card;
import com.adfg.domain.Transaction;

@FunctionalInterface
public interface RefundService {
    Double computeRefund(Card card, Transaction transaction, Double cardMaxFare);
}
