package pivotal.io.service;

import com.techolution.model.Money;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by tdelesio on 11/21/17.
 */
@RunWith(SpringRunner.class)
public class MoneyServiceTest {

    @TestConfiguration
    static class MoneyTestContextConfiguration {

        @Bean
        public MoneyService moneyService() {
            return new MoneyService();
        }
    }

    @Autowired
    private MoneyService moneyService;

//    @MockBean
//    private MoneyRepository moneyRepository;

    @Before
    public void setUp() {
        
    }

    
}
