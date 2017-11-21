package com.adfg;

import com.adfg.domain.CardEntity;
import com.adfg.repository.CardRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class CardRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CardRepository repository;

    @Test
    public void whenFindById_thenReturnCard() {

        // given
        CardEntity card = new CardEntity();
        card.setId(Long.valueOf(984596874));
        card.setOwner("Frank Sinatra");

        entityManager.persist(card);
        entityManager.flush();

        // when
        CardEntity found = repository.findCardById(card.getId());

        // then
        assertEquals(found.getOwner(), card.getOwner());
    }
}
