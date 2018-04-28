package com.adfg;

import com.adfg.domain.Card;
import com.adfg.domain.Transaction;
import com.adfg.service.RefundServiceImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RefundServiceTest {

    private Double cardMaxFare = 3.20;
    private Card card = new Card();
    private Transaction transaction = new Transaction();

    @Before
    public void testInit() {
        card.setBalance(0.0);
    }

    @Test
    public void allIn1Zone() {

        card.setStationZone(1);
        transaction.setStationZone(1);

        card.setStationType("bus");
        assertEquals((cardMaxFare - 1.80), RefundServiceImpl.computeImpl.computeRefund(card, transaction, cardMaxFare), 0.0);
        card.setStationType("metro");
        assertEquals((cardMaxFare - 2.50), RefundServiceImpl.computeImpl.computeRefund(card, transaction, cardMaxFare), 0.0);

    }

    @Test
    public void anyInOneZoneOutside1() {

        card.setStationZone(2);
        transaction.setStationZone(2);

        card.setStationType("bus");
        assertEquals((cardMaxFare - 1.80), RefundServiceImpl.computeImpl.computeRefund(card, transaction, cardMaxFare), 0.0);
        card.setStationType("metro");
        assertEquals((cardMaxFare - 2.00), RefundServiceImpl.computeImpl.computeRefund(card, transaction, cardMaxFare), 0.0);

    }

    @Test
    public void anyTwoZonesWith1() {

        card.setStationZone(1);
        transaction.setStationZone(2);

        card.setStationType("bus");
        assertEquals((cardMaxFare - 1.80), RefundServiceImpl.computeImpl.computeRefund(card, transaction, cardMaxFare), 0.0);
        card.setStationType("metro");
        assertEquals((cardMaxFare - 3.00), RefundServiceImpl.computeImpl.computeRefund(card, transaction, cardMaxFare), 0.0);

    }

    @Test
    public void anyTwoZonesWithout1() {

        card.setStationZone(2);
        transaction.setStationZone(4);

        card.setStationType("bus");
        assertEquals((cardMaxFare - 1.80), RefundServiceImpl.computeImpl.computeRefund(card, transaction, cardMaxFare), 0.0);
        card.setStationType("metro");
        assertEquals((cardMaxFare - 2.25), RefundServiceImpl.computeImpl.computeRefund(card, transaction, cardMaxFare), 0.0);

    }

    @Test
    public void anyThreeZones() {

        card.setStationZone(3);
        transaction.setStationZone(3);

        card.setStationType("bus");
        assertEquals((cardMaxFare - 1.80), RefundServiceImpl.computeImpl.computeRefund(card, transaction, cardMaxFare), 0.0);
        card.setStationType("metro");
        assertEquals((cardMaxFare - 3.20), RefundServiceImpl.computeImpl.computeRefund(card, transaction, cardMaxFare), 0.0);

    }

}