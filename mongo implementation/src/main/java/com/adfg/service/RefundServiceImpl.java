package com.adfg.service;

import com.adfg.domain.Card;
import com.adfg.domain.Transaction;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiPredicate;

/**
 * ALL BUS JOURNEY
 * ANY THREE ZONES
 * ANY IN 1 ZONE
 * ANY ONE ZONE, OUTSIDE 1
 * ANY TWO ZONES, EXCLUDING 1
 * ANY TWO ZONES, INCLUDING 1
 **/
public interface RefundServiceImpl {

    LinkedHashMap<BiPredicate<Card, Transaction>, Double> predicates = new LinkedHashMap<BiPredicate<Card, Transaction>, Double>() {{
        put((crd, trx) -> crd.getStationType().equals("bus"), 1.80);
        put((crd, trx) -> crd.getStationZone() == 3 || trx.getStationZone() == 3, 3.20);
        put((crd, trx) -> crd.getStationZone() == 1 && trx.getStationZone() == 1, 2.50);
        put((crd, trx) -> (crd.getStationZone() != 1 && trx.getStationZone() != 1) && crd.getStationZone().equals(trx.getStationZone()), 2.00);
        put((crd, trx) -> (crd.getStationZone() != 1 && trx.getStationZone() != 1) && !crd.getStationZone().equals(trx.getStationZone()), 2.25);
        put((crd, trx) -> ((crd.getStationZone() == 1 || trx.getStationZone() == 1) && !crd.getStationZone().equals(trx.getStationZone())), 3.00);
    }};

    RefundService computeImpl = (card, transaction, cardMaxFare) ->
            card.getBalance() + cardMaxFare - predicates.entrySet().stream()
            .filter(pr -> pr.getKey().test(card, transaction))
            .findFirst()
            .map(Map.Entry::getValue)
            .orElse(cardMaxFare);
}
