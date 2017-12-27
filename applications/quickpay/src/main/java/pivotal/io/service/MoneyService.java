package pivotal.io.service;

import com.techolution.model.Money;
//import com.techolution.training.repository.MoneyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.Collections;

import java.util.List;


@Service
public class MoneyService {

    private final Logger log = LoggerFactory.getLogger(MoneyService.class);

//    private final MoneyRepository moneyRepository;

//    public MoneyService(MoneyRepository moneyRepository) {
//        this.moneyRepository = moneyRepository;
//    }

    public List<Money> getMoneys() {
//        return moneyRepository.findAll();
        return Collections.emptyList();
    }

    public Money getMoney(final String id) {
//        return moneyRepository.findOne(id);
  return new Money();
    }

//    public Money createMoney(final Money money) {
//        try{
//            return moneyRepository.save(money);
//        }catch (Exception ex) {
//            log.error("Exception occurs during saving/creating money in mongo db for ", money.getName());
//            return null;
//        }


    

    public Money updateMoney(final String id, Money money) {
      return money;
  }
//        Money moneyDb = moneyRepository.findOne(id);
//        if(StringUtils.isEmpty(moneyDb)) {
//            return null;
//        }

}
