package br.com.huppers.payservice.services;

import br.com.huppers.payservice.domain.transaction.Transaction;
import br.com.huppers.payservice.domain.user.User;
import br.com.huppers.payservice.dto.TransactionDTO;
import br.com.huppers.payservice.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    public void createTransaction(TransactionDTO transaction) throws Exception {
        User sender = this.userService.findUser(transaction.senderId());
        User receiver = this.userService.findUser(transaction.receiverId());

        userService.validateTransaction(sender, transaction.value());

        Boolean isAuthorized = transactionIsAuthorized(sender, transaction.value());

        if(!isAuthorized){
            throw new Exception("Transação não autorizada");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.transactionRepository.save(newTransaction);

        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);
    }

    public Boolean transactionIsAuthorized(User sender, BigDecimal value) {
        ResponseEntity<Map> response = restTemplate.getForEntity("https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc", Map.class);



        if (response.getStatusCode() == HttpStatus.OK) {
            String message = (String) response.getBody().get("message");

            return "Autorizado".equalsIgnoreCase(message);
        } else return false;

    }
}
