package br.com.huppers.payservice.services;

import br.com.huppers.payservice.domain.transaction.Transaction;
import br.com.huppers.payservice.domain.user.User;
import br.com.huppers.payservice.domain.user.UserType;
import br.com.huppers.payservice.dto.UserDTO;
import br.com.huppers.payservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception {
        Transaction transaction;


        if(sender.getUserType() == UserType.MERCHANT){
            throw new Exception("Usuário do tipo lojista não está autorizado a realizar transação!");
        }

        if(sender.getBalance().compareTo(amount)< 0){
            throw new Exception("Saldo insuficiente!");
        }

    }

    public User findUser(Long id) throws Exception {
        return userRepository.findUserById(id).orElseThrow(() -> new Exception("Usuário não encontrado"));
    }


    public void saveUser(User user){
        this.userRepository.save(user);
    }

    public User createUser(UserDTO user){
        User newUser = new User(user);
        this.saveUser(newUser);
        return newUser;
    }

    public List<User> getAllUsers(){
        return this.userRepository.findAll();
    }
}
