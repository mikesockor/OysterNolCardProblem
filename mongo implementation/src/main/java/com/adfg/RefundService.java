package com.adfg;

import com.adfg.domain.Card;
import com.adfg.domain.Transaction;

public interface RefundService {

    default Double computeRefund(Card card, Transaction transaction, Double cardMaxFare){

        // ALL BUS JOURNEY
        if (card.getStationType().equals("bus"))
            return (card.getBalance() + cardMaxFare - 1.80);

        // ANY IN 1 ZONE
        if (card.getStationZone()==1 && transaction.getStationZone()==1)
            return (card.getBalance() + cardMaxFare - 2.50);

        // ANY ONE ZONE, OUTSIDE 1
        if ((card.getStationZone()!=1 && transaction.getStationZone()!=1) && card.getStationZone()==transaction.getStationZone())
            return (card.getBalance() + cardMaxFare - 2.00);

        // ANY TWO ZONES, EXCLUDING 1
        if ((card.getStationZone()!=1 && transaction.getStationZone()!=1) && card.getStationZone()!=transaction.getStationZone())
            return (card.getBalance() + cardMaxFare - 2.25);

        // ANY TWO ZONES, INCLUDING 1
        if (card.getStationZone()!=transaction.getStationZone() && (card.getStationZone()==1 || transaction.getStationZone()==1))
            return (card.getBalance() + cardMaxFare - 3.00);

        // ANY THREE ZONES
        return 3.20;
    }
}
