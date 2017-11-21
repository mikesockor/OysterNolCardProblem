package com.adfg.service;

import com.adfg.domain.CardEntity;
import com.adfg.domain.StationEntity;
import com.adfg.domain.Transaction;
import com.adfg.domain.TransactionType;
import com.adfg.dto.CardReport;
import com.adfg.dto.CardReportData;
import com.adfg.exceptions.CheckInException;
import com.adfg.interfaces.TransactionService;
import com.adfg.repository.StationRepository;
import com.adfg.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Value("${card.maxFare}")
    private Double cardMaxFare;

    private final StationRepository stationRepository;
    private final TransactionRepository transactionRepository;
    private final CardServiceImpl cardServiceImpl;
    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, CardServiceImpl cardServiceImpl, StationRepository stationRepository) {
        this.transactionRepository = transactionRepository;
        this.cardServiceImpl = cardServiceImpl;
        this.stationRepository = stationRepository;
    }

    @Override
    public Transaction transactionProceed(Transaction transaction){

        CardEntity cardEntity;
        if (transaction.getTransactionType()==TransactionType.IN){
            cardEntity = cardServiceImpl.initialTransactionCheckIn(transaction.getCardEntity().getId(), transaction.getStationEntity());
        }else{

            cardEntity = cardServiceImpl.getCardEntity(transaction.getCardEntity().getId());
            if (cardEntity.getCheckInTime()==null)
                throw new CheckInException("card did not checked in");

            RefundByZone refundByZone = new RefundByZone(cardEntity.getCheckinStationEntity(), transaction.getStationEntity(),
                    cardMaxFare, transaction.getStationEntity().getStationType());
            cardEntity = cardServiceImpl.initialTransactionCheckOut(cardEntity, refundByZone.getRefund());
        }

        StationEntity stationEntity = stationRepository.findByNameAndZone(transaction.getStationEntity().getName(), transaction.getStationEntity().getZone());

        return transactionRepository.save(new Transaction(cardEntity,stationEntity,transaction.getTransactionType()));
    }

    @Override
    public CardReport getCardReport(Long cardId, int hours) {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -hours);
        Date hoursBack = cal.getTime();

        CardReport cardReport = new CardReport();
        cardReport.setCardEntity(cardServiceImpl.getCardEntity(cardId));

        List<CardReportData> cardReportDatas = transactionRepository.findByCheckTimeGreaterThanAndCardEntityId(hoursBack, cardId).stream()
                .map(e-> {
                    CardReportData cardReportData = new CardReportData();
                    cardReportData.setCheckTime(e.getCheckTime());
                    cardReportData.setName(e.getStationEntity().getName());
                    cardReportData.setStationType(e.getStationEntity().getStationType());
                    cardReportData.setZone(e.getStationEntity().getZone());
                    cardReportData.setAgglomerationName(e.getStationEntity().getAgglomerationEntity().getName());
                    cardReportData.setTransactionType(e.getTransactionType());
                    return cardReportData;
                })
                .collect(Collectors.toList());

        cardReport.setCardReportData(cardReportDatas);
        return cardReport;
    }
}
