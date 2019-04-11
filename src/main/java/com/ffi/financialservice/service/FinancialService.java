package com.ffi.financialservice.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ffi.financialservice.dto.PeriodRequest;
import com.ffi.financialservice.exception.ApplicationBusinessException;

public interface FinancialService {
	
	public Map<String, Map<String,Set<String>>> checkDataAvailability(String customerId) throws ApplicationBusinessException;
	public String getFinancialData(String templateName,String companyId,String sourceName,List<PeriodRequest> periodRequest) throws ApplicationBusinessException;
}
