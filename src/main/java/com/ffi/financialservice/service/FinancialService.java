package com.ffi.financialservice.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ffi.financialservice.endpoint.PeriodRequest;
import com.ffi.financialservice.exception.ApplicationBusinessException;
import com.ffi.financialservice.vo.FinancialDataVO;

public interface FinancialService {
	
	public Map<String, Map<String,Set<String>>> checkDataAvailability(String customerId) throws ApplicationBusinessException;
	public List<FinancialDataVO> getFinancialData(String templateName,String companyId,String sourceName,List<PeriodRequest> periodRequest) throws ApplicationBusinessException;
}
