package com.test.statement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.statement.models.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
