package br.com.huppers.payservice.repositories;

import br.com.huppers.payservice.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
