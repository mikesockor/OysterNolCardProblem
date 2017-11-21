package com.adfg;

import com.adfg.domain.CardEntity;
import com.adfg.domain.StationEntity;
import com.adfg.exceptions.CheckInException;
import com.adfg.exceptions.UnbalancedException;
import com.adfg.interfaces.CardService;
import com.adfg.repository.AgglomerationRepository;
import com.adfg.repository.CardRepository;
import com.adfg.repository.StationRepository;
import com.adfg.service.CardServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@TestPropertySource(properties = {
        "card.maxFare=3.20",
        "rates.balance.fixation.hours=3",
})
public class CardServiceTest {

    @TestConfiguration
    static class serviceImplTestContextConfiguration {

        @Bean
        public AgglomerationRepository agglomerationRepository() {
            return Mockito.mock(AgglomerationRepository.class);
        }

        @Bean
        public CardRepository cardRepository() {
            return Mockito.mock(CardRepository.class);
        }

        @Bean
        public StationRepository stationRepository() {
            return Mockito.mock(StationRepository.class);
        }

        @Bean
        public CardService cardService() {
            return new CardServiceImpl(cardRepository(),stationRepository(), agglomerationRepository());
        }
    }

    @MockBean
    private CardRepository cardRepository;
    @MockBean
    private StationRepository stationRepository;
    @MockBean
    private AgglomerationRepository agglomerationRepository;
    @Autowired
    CardServiceImpl cardService;

    @Before
    public void setUp() {
        CardEntity card = new CardEntity();
        card.setId(Long.valueOf(654987321));
        card.setOwner("Frank Sinatra");
        Mockito.when(cardRepository.findCardById(Long.valueOf(654987321))).thenReturn(card);
    }

    @Test
    public void getCardEntity_thenReturnCardOwner() {

        CardEntity found = cardService.getCardEntity(Long.valueOf(654987321));
        assertEquals("Frank Sinatra", found.getOwner());
    }

    @Test(expected = UnbalancedException.class)
    public void saveCardEntity_thenReturnCardOwner() {

        CardEntity card = new CardEntity();
        card.setId(Long.valueOf(654987321));
        card.setOwner("Frank Sinatra");
        card.setBalance(2.00);

        CardEntity saved = cardService.saveCardEntity(card);
    }

    @Test(expected = CheckInException.class)
    public void initialTransactionCheckIn_thenReturnCard() {

        CardEntity card = new CardEntity();
        card.setCheckInTime(new Date(Calendar.getInstance().getTimeInMillis()));
        Mockito.when(cardRepository.findCardById(Long.valueOf(654987321))).thenReturn(card);

        CardEntity saved = cardService.initialTransactionCheckIn(Long.valueOf(654987321), new StationEntity());
    }

}
