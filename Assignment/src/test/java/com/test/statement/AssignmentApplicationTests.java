package com.test.statement;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.test.statement.controllers.AccountStatementController;

@SpringBootTest
class AssignmentApplicationTests {

	@Autowired
    private AccountStatementController controller;
    
	@Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

}
