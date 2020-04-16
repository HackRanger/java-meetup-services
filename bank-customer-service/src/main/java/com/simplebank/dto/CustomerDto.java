package com.simplebank.dto;

public class CustomerDto {
	private int customernumber;
	private String firstName;
	private String lastName;
	private String accountNumber;
	private double balance;
	
	public int getCustomerNumber() {
		return this.customernumber;
	}
	
	public void setCustomerNumber(int  custNo) {
		this.customernumber = custNo;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public void setFirstName(String fn) {
		this.firstName = fn;
	}
	
	public String getLastName() {
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
	
	@Override
	public String toString(){
	    return new com.google.gson.Gson().toJson(this);
	}
	
}
