package com.adfg;

import com.adfg.domain.StationEntity;
import com.adfg.domain.StationType;
import org.junit.Test;
import com.adfg.service.*;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class RefundTest {

    private StationEntity stationIn  = Mockito.mock(StationEntity.class);
    private StationEntity stationOut = Mockito.mock(StationEntity.class);
    private Double cardMaxFare = 3.20;

    @Test
    public void allIn1Zone() {

        Mockito.when(stationIn.isFirst()).thenReturn(true);
        Mockito.when(stationOut.isFirst()).thenReturn(true);

        RefundByZone refundByZone = new RefundByZone(stationIn,stationOut,cardMaxFare, StationType.bus);
        assertEquals((cardMaxFare-1.80), refundByZone.getRefund(), 0.0);
        refundByZone = new RefundByZone(stationIn,stationOut,cardMaxFare, StationType.metro);
        assertEquals((cardMaxFare-2.50), refundByZone.getRefund(), 0.0);

    }

    @Test
    public void anyInOneZoneOutside1() {

        Mockito.when(stationIn.isFirst()).thenReturn(false);
        Mockito.when(stationOut.isFirst()).thenReturn(false);
        Mockito.when(stationIn.getZone()).thenReturn(2);
        Mockito.when(stationOut.getZone()).thenReturn(2);

        RefundByZone refundByZone = new RefundByZone(stationIn,stationOut,cardMaxFare, StationType.bus);
        assertEquals((cardMaxFare-1.80), refundByZone.getRefund(), 0.0);
        refundByZone = new RefundByZone(stationIn,stationOut,cardMaxFare, StationType.metro);
        assertEquals((cardMaxFare-2.00), refundByZone.getRefund(), 0.0);

    }

    @Test
    public void anyTwoZonesWith1() {

        Mockito.when(stationIn.isFirst()).thenReturn(true);
        Mockito.when(stationOut.isFirst()).thenReturn(false);
        Mockito.when(stationIn.getZone()).thenReturn(1);
        Mockito.when(stationOut.getZone()).thenReturn(2);

        RefundByZone refundByZone = new RefundByZone(stationIn,stationOut,cardMaxFare, StationType.bus);
        assertEquals((cardMaxFare-1.80), refundByZone.getRefund(), 0.0);
        refundByZone = new RefundByZone(stationIn,stationOut,cardMaxFare, StationType.metro);
        assertEquals((cardMaxFare-3.00), refundByZone.getRefund(), 0.0);

    }

    @Test
    public void anyTwoZonesWithout1() {

        Mockito.when(stationIn.isFirst()).thenReturn(false);
        Mockito.when(stationOut.isFirst()).thenReturn(false);
        Mockito.when(stationIn.getZone()).thenReturn(2);
        Mockito.when(stationOut.getZone()).thenReturn(3);

        RefundByZone refundByZone = new RefundByZone(stationIn,stationOut,cardMaxFare, StationType.bus);
        assertEquals((cardMaxFare-1.80), refundByZone.getRefund(), 0.0);
        refundByZone = new RefundByZone(stationIn,stationOut,cardMaxFare, StationType.metro);
        assertEquals((cardMaxFare-2.25), refundByZone.getRefund(), 0.0);

    }

}
