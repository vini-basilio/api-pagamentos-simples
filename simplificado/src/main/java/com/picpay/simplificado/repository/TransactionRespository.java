package com.picpay.simplificado.repository;

import com.picpay.simplificado.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRespository extends JpaRepository<Transaction, Long> { }
