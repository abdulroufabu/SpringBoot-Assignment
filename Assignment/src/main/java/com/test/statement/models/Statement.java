package com.test.statement.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "statement")

@SequenceGenerator(name = "statement_seq", sequenceName = "statement_sequence", initialValue = 1)
public class Statement {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statement_seq")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonIgnore
    private Account account;

    @Column(name = "datefield")
    private String datefield;
    
    @Column(name = "amount")
    private String amount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getDatefield() {
		return datefield;
	}

	public void setDatefield(String datefield) {
		this.datefield = datefield;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Statement [id=" + id + ", account=" + account + ", datefield=" + datefield + ", amount=" + amount + "]";
	}
    
    

}