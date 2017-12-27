package pivotal.io.controller;

import com.techolution.model.Money;
import com.techolution.service.MoneyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

@RestController
@RequestMapping("/json/money")
@CrossOrigin
public class MoneyRestController {

    final MoneyService moneyService;

    public MoneyRestController(MoneyService moneyService) {
        this.moneyService = moneyService;
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Money>> getMoney() {
        List<Money> moneys = moneyService.getMoneys();
        if (moneys != null) {
            return new ResponseEntity(moneys, HttpStatus.OK);
        } else {
            return new ResponseEntity(moneys, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Money> getMoney(@PathVariable final String id) {
Money money = moneyService.getMoney(id);
        if (money != null) {
            return new ResponseEntity(money, HttpStatus.OK);
        } else {
            return new ResponseEntity(money, HttpStatus.NOT_FOUND);
        }
    }

    //@PostMapping("/")
    //public ResponseEntity<Money> createMoney(@RequestBody Money money) {
//Money savedMoney = moneyService.createMoneye(money);
  //      if (savedMoney != null) {
    //        return new ResponseEntity(savedMoney, HttpStatus.CREATED);
      //  } else {
        //    return new ResponseEntity(savedMoney, HttpStatus.INTERNAL_SERVER_ERROR);
        //}
    //}

    @PutMapping("/{id}")
    public ResponseEntity<Money> updateMoney(@PathVariable final String id, @RequestBody Money money) {
        Money updatedMoney = moneyService.updateMoney(id, money);
        if(updatedMoney!= null) {
            return new ResponseEntity<Money>(updatedMoney, HttpStatus.OK);
        } else{
            return new ResponseEntity<Money>(updatedMoney, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
