package com.adfg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SheduledTask {

    private final CardServiceImpl cardServiceImpl;
    @Autowired
    public SheduledTask(CardServiceImpl cardServiceImpl) {
        this.cardServiceImpl = cardServiceImpl;
    }

    @Scheduled(cron = "${rates.balance.fixation.cron}")
    public void reportCurrentTime() throws IOException {
        cardServiceImpl.fixOutdatedCheckIns();
    }
}
