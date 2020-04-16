package com.simplebank.dto;

public class FundTransferDto {
	private String srcAccountNumber;
	private String destAccountNumber;
	
	public void setSourceAccountNumber(String srcAccNo) {
		this.srcAccountNumber = srcAccNo;
	}
	public void setDestinationAccountNumber(String destAccNo) {
		this.destAccountNumber = destAccNo;
	}
	
	@Override
	public String toString(){
	    return new com.google.gson.Gson().toJson(this);
	}

}
