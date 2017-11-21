package com.adfg.service;

import com.adfg.domain.CardEntity;
import com.adfg.domain.StationEntity;
import com.adfg.exceptions.CheckInException;
import com.adfg.exceptions.UnbalancedException;
import com.adfg.interfaces.CardService;
import com.adfg.repository.AgglomerationRepository;
import com.adfg.repository.CardRepository;
import com.adfg.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class CardServiceImpl implements CardService{

    private final CardRepository cardRepository;
    private final StationRepository stationRepository;
    private final AgglomerationRepository agglomerationRepository;

    @Value("${card.maxFare}")
    private Double cardMaxFare;
    @Value("${rates.balance.fixation.hours}")
    private int ratesBalanceFixationHours;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository, StationRepository stationRepository, AgglomerationRepository agglomerationRepository) {
        this.cardRepository = cardRepository;
        this.stationRepository = stationRepository;
        this.agglomerationRepository = agglomerationRepository;
    }

    @Override
    public CardEntity getCardEntity(Long cardId){
        return cardRepository.findCardById(cardId);
    }

    @Override
    public CardEntity saveCardEntity(CardEntity cardEntity){

        if (cardEntity.getBalance()< cardMaxFare)
            throw new UnbalancedException("minimum balance is "+ cardMaxFare +", yours: "+cardEntity.getBalance());

        return cardRepository.save(cardEntity);
    }

    @Override
    public CardEntity initialTransactionCheckIn(Long cardId, StationEntity checkInStation){

        CardEntity card = cardRepository.findCardById(cardId);

        if (card.getCheckInTime()!=null)
            throw new CheckInException("card already checked in at: "+card.getCheckInTime());

        card.setCheckInTime(new Date(Calendar.getInstance().getTimeInMillis()));
        card.setCheckinStationEntity(checkInStation);
        card.setBalance(card.getBalance()-cardMaxFare);

        return cardRepository.save(card);
    }

    @Override
    public CardEntity initialTransactionCheckOut(CardEntity card, Double refund){

        card.setCheckInTime(null);
        card.setCheckinStationEntity(null);
        card.setBalance(card.getBalance()+refund);

        return cardRepository.save(card);
    }

    @Override
    public CardEntity paymentToCard(Long cardId, Double payment){

        CardEntity card = cardRepository.findCardById(cardId);
        card.setBalance(card.getBalance()+payment);
        return cardRepository.save(card);
    }

    @Override
    public void fixOutdatedCheckIns(){

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -ratesBalanceFixationHours);
        Date hoursBack = cal.getTime();

        cardRepository.findByCheckInTimeGreaterThan(hoursBack)
                .forEach(e-> {
                    e.setCheckInTime(null);
                    e.setCheckinStationEntity(null);
                    cardRepository.save(e);
                });

    }
}
