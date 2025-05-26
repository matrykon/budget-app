package org.example.pasir_matras_patryk.repository;

import org.example.pasir_matras_patryk.model.Transaction;
import org.example.pasir_matras_patryk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByUser(User user);

    List<Transaction> findByUser(User user);

    List<Transaction> findByUserAndTimestampGreaterThanEqual(User user, LocalDateTime startDate);
}
