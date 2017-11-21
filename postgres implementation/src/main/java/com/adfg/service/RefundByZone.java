package com.adfg.service;

import com.adfg.domain.StationEntity;
import com.adfg.domain.StationType;

import java.util.Objects;

public class RefundByZone {

    private StationType stationType;
    private StationEntity stationIn;
    private StationEntity stationOut;
    private Double cardMaxFare;

    public RefundByZone(StationEntity stationIn, StationEntity stationOut, Double cardMaxFare, StationType stationType) {
        this.stationType = stationType;
        this.stationIn = stationIn;
        this.stationOut = stationOut;
        this.cardMaxFare = cardMaxFare;
    }

    public Double getRefund(){

        // ALL BUS JOURNEY
        if (stationType== StationType.bus)
            return (cardMaxFare-1.80);

        // ANY IN 1 ZONE
        if (stationIn.isFirst() && stationOut.isFirst())
            return (cardMaxFare-2.50);

        // ANY ONE ZONE, OUTSIDE 1
        if ((!stationIn.isFirst() && !stationOut.isFirst())
                && Objects.equals(stationIn.getZone(), stationOut.getZone()))
            return (cardMaxFare-2.00);

        // ANY TWO ZONES, INCLUDING 1
        if ((stationIn.isFirst() || stationOut.isFirst())
                && !Objects.equals(stationIn.getZone(), stationOut.getZone()))
            return (cardMaxFare-3.00);

        // ANY TWO ZONES, EXCLUDING 1
        if ((!stationIn.isFirst() && !stationOut.isFirst())
                && !Objects.equals(stationIn.getZone(), stationOut.getZone()))
            return (cardMaxFare-2.25);

        // ANY THREE ZONES
        return 3.20;
    }
}
