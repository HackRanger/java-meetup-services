package com.simplebank.dto;

public class CustomerDto {
	private String firstName;
	private String lastName;
	private String accountNumber;
	private double balance;
	
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public void setFirstName(String fn) {
		this.firstName = fn;
	}
	
	public String getLaneName() {
		return this.lastName;
	}
	
	public void setLastName(String ln) {
		this.lastName = ln;
	}
	
	public String getAccountNumber() {
		return this.accountNumber;
	}
	
	public void setAccountNumber(String accNumber) {
		this.accountNumber = accNumber;
	}
	
	public double getBalance() {
		return this.balance;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
}
