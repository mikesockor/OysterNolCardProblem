package com.adfg;

import com.adfg.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class ScheduledTask {

    @Value("${custom.rates.balance.fixation.hours}")
    private int ratesBalanceFixationHours;
    private final CardRepository cardRepository;

    @Autowired
    public ScheduledTask(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Scheduled(cron = "${custom.rates.balance.fixation.cron}")
    public void fixOutdatedCards() {

        cardRepository.findByCheckInTimeGreaterThan(Date.from(
                LocalDateTime.now()
                        .minusHours(ratesBalanceFixationHours)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()))
                .doOnNext(crd -> {
                    crd.setStationType(null);
                    crd.setStationZone(null);
                    cardRepository.save(crd);
                });
    }
}
