package com.ffi.financialservice.dto;

public class FinancialDTO {
	
	private String year;
	private String lineItem;
	private Double lineItemValue;
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getLineItem() {
		return lineItem;
	}
	public void setLineItem(String lineItem) {
		this.lineItem = lineItem;
	}
	public Double getLineItemValue() {
		return lineItemValue;
	}
	public void setLineItemValue(Double lineItemValue) {
		this.lineItemValue = lineItemValue;
	}

}
