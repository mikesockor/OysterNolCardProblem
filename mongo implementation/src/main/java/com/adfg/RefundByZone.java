package com.adfg;

class RefundByZone {

    private String stationType;
    private int zoneIn;
    private int zoneOut;
    private Double cardMaxFare;

    RefundByZone(int zoneIn, int zoneOut, Double cardMaxFare, String stationType) {
        this.stationType = stationType;
        this.zoneIn = zoneIn;
        this.zoneOut = zoneOut;
        this.cardMaxFare = cardMaxFare;
    }

    Double getRefund(){

        // ALL BUS JOURNEY
        if (stationType.equals("bus"))
            return (cardMaxFare-1.80);

        // ANY IN 1 ZONE
        if (zoneIn==1 && zoneOut==1)
            return (cardMaxFare-2.50);

        // ANY ONE ZONE, OUTSIDE 1
        if ((zoneIn!=1 && zoneOut!=1) && zoneIn==zoneOut)
            return (cardMaxFare-2.00);

        // ANY TWO ZONES, EXCLUDING 1
        if ((zoneIn!=1 && zoneOut!=1) && zoneIn!=zoneOut)
            return (cardMaxFare-2.25);

        // ANY TWO ZONES, INCLUDING 1
        if (zoneIn!=zoneOut && (zoneIn==1 || zoneOut==1))
            return (cardMaxFare-3.00);

        // ANY THREE ZONES
        return 3.20;
    }
}