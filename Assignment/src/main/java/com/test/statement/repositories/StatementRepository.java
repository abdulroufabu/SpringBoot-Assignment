package com.test.statement.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.statement.models.Statement;

@Repository
public interface StatementRepository extends JpaRepository<Statement, Long> {
	
	
	List<Statement> findByAccountId(Long accountId);
	

}