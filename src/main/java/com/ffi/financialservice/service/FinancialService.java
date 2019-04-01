package com.ffi.financialservice.service;

import java.util.Map;
import java.util.Set;

import com.ffi.financialservice.exception.ApplicationBusinessException;

public interface FinancialService {
	
	public Map<String, Map<String,Set<String>>> checkDataAvailability(String customerId) throws ApplicationBusinessException;

}
