package com.adfg;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RefundTest {

    private Double cardMaxFare = 3.20;

    @Test
    public void allIn1Zone() {

        RefundByZone refundByZone = new RefundByZone(1,1,cardMaxFare, "bus");
        assertEquals((cardMaxFare-1.80), refundByZone.getRefund(), 0.0);
        refundByZone = new RefundByZone(1,1,cardMaxFare, "metro");
        assertEquals((cardMaxFare-2.50), refundByZone.getRefund(), 0.0);

    }

    @Test
    public void anyInOneZoneOutside1() {

        RefundByZone refundByZone = new RefundByZone(2,2,cardMaxFare, "bus");
        assertEquals((cardMaxFare-1.80), refundByZone.getRefund(), 0.0);
        refundByZone = new RefundByZone(2,2,cardMaxFare, "metro");
        assertEquals((cardMaxFare-2.00), refundByZone.getRefund(), 0.0);

    }

    @Test
    public void anyTwoZonesWith1() {

        RefundByZone refundByZone = new RefundByZone(1,2,cardMaxFare, "bus");
        assertEquals((cardMaxFare-1.80), refundByZone.getRefund(), 0.0);
        refundByZone = new RefundByZone(1,2,cardMaxFare, "metro");
        assertEquals((cardMaxFare-3.00), refundByZone.getRefund(), 0.0);

    }

    @Test
    public void anyTwoZonesWithout1() {

        RefundByZone refundByZone = new RefundByZone(2,3,cardMaxFare, "bus");
        assertEquals((cardMaxFare-1.80), refundByZone.getRefund(), 0.0);
        refundByZone = new RefundByZone(2,3,cardMaxFare, "metro");
        assertEquals((cardMaxFare-2.25), refundByZone.getRefund(), 0.0);

    }

}
