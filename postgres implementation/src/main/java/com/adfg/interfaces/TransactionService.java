package com.adfg.interfaces;

import com.adfg.domain.Transaction;
import com.adfg.dto.CardReport;

/**
 *  Interface for creating transactions.
 *
 */
public interface TransactionService {

    /**
     * Create a new transaction with card balance control.
     *
     * @param transaction a client defined transaction domain object
     * @throws com.adfg.exceptions.BusinessException if there are any problems creating the transaction
     */
    Transaction transactionProceed(Transaction transaction);

    /**
     * Getting last N hours card transaction from database.
     *
     * @param cardId a card domain object id
     */
    CardReport getCardReport(Long cardId, int hours);

}
