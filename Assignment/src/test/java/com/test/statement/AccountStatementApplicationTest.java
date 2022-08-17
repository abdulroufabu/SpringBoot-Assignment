package com.test.statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.test.statement.controllers.AccountStatementController;
import com.test.statement.services.AccountStatementService;
import com.test.statement.utils.SearchInput;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, 
	classes = AccountStatementController.class)
@AutoConfigureMockMvc
public class AccountStatementApplicationTest {

	private ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AccountStatementService stmntService;

	@Test
	@DisplayName("Test searchStatement with valid request")
	public void searchStatement_WhenInputRequestIsIsValid() throws Exception {
		// Given
		SearchInput searchInput = new SearchInput();
		searchInput.setAccountId(1l);
		searchInput.setFromDate(convertStringToDate("2010-08-16"));
		searchInput.setToDate(convertStringToDate("2022-08-16"));
		searchInput.setFromAmount(200.0);
		searchInput.setFromAmount(600.0);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/api/v1/accounts")
				.with(httpBasic("admin", "admin"))
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(searchInput))
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	@DisplayName("Test searchStatement with Invalid request")
	public void searchStatement_WhenInputRequestIsInValid() throws Exception {
		// Given
		SearchInput searchInput = new SearchInput();
		searchInput.setAccountId(1l);
		searchInput.setFromDate(convertStringToDate("2012-08-16"));
		searchInput.setToDate(convertStringToDate("2020-08-16"));
		searchInput.setFromAmount(200.0);
		searchInput.setFromAmount(600.0);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
				.with(httpBasic("admin", "admin"))
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(searchInput)).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isBadRequest());
	}

	private static LocalDate convertStringToDate(String date) throws Exception {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate convertedDate = LocalDate.parse(date, formatter);
		return convertedDate;
	}

}