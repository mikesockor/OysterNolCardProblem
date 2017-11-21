package com.adfg.interfaces;

import com.adfg.domain.CardEntity;
import com.adfg.domain.StationEntity;

/**
 *  Interface for Card managing.
 *
 */
public interface CardService {

    /**
     * Getting existing card from database.
     *
     * @param cardId a card domain object id
     */
    CardEntity getCardEntity(Long cardId);

    /**
     * Create a new card with initial balance control.
     *
     * @param cardEntity a client defined card domain object
     * @throws com.adfg.exceptions.BusinessException if initial balance is below system defined maximum fare
     */
    CardEntity saveCardEntity(CardEntity cardEntity);

    /**
     * Keeping in existed card check-in-time and check-in-station domain object.
     *
     * @param cardId a card domain object id
     * @param checkInStation a station domain object id
     * @throws com.adfg.exceptions.BusinessException if checkout did not performed
     */
    CardEntity initialTransactionCheckIn(Long cardId, StationEntity checkInStation);

    /**
     * Processing check out action (clearing time and station as well), refund the difference between
     * initial maximum fare and fact fare to card balance.
     *
     * @param card a card domain object id
     * @param refund a station domain object id
     * @throws com.adfg.exceptions.BusinessException if checkin did not performed
     */
    CardEntity initialTransactionCheckOut(CardEntity card, Double refund);

    /**
     * Receiving payment to card balance
     *
     * @param cardId a card domain object id
     * @param payment a payment value
     */
    CardEntity paymentToCard(Long cardId, Double payment);

    /**
     * Processing by schedule outdated card, in case card was checked in and did not checked out
     *
     */
    void fixOutdatedCheckIns();

}
