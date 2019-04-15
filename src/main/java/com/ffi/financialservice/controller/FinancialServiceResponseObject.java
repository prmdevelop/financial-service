package com.ffi.financialservice.controller;

import java.io.Serializable;
import java.util.Map;

public class FinancialServiceResponseObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String, Object> financialServiceResponse;

	public Map<String, Object> getFinancialServiceResponse() {
		return financialServiceResponse;
	}

	public void setFinancialServiceResponse(Map<String, Object> financialServiceResponse) {
		this.financialServiceResponse = financialServiceResponse;
	}
}
