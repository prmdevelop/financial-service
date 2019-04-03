package com.ffi.financialservice.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ffi.financialservice.dao.FinancialDao;
import com.ffi.financialservice.domain.Financial;
import com.ffi.financialservice.exception.ApplicationBusinessException;
import com.ffi.financialservice.handler.AppProperities;

@Service
public class FinancialServiceImpl implements FinancialService {

	private static final Logger logger = LogManager.getLogger(FinancialServiceImpl.class);

	@Autowired
	FinancialDao financialDao;

	@Autowired
	AppProperities appProperities;

	public Map<String, Map<String, Set<String>>> checkDataAvailability(String customerId)
			throws ApplicationBusinessException {
		logger.info("Start of FinancialServiceImpl.checkDataAvailability()");
		Map<String, Map<String, Set<String>>> data = new HashMap<>();
		Map<String, Set<String>> period = new HashMap<>();
		Set<String> periodType = null;
		try {
			List<Financial> financials = financialDao.getFinancialOfCustomer(UUID.fromString(customerId));
			for (Financial financial : financials) {
				if (!data.containsKey(financial.getSource().getSourceName())) {
					period = new HashMap<>();
					periodType = new HashSet<>();
					periodType.add(financial.getPeriod().getPeriodType().getPeriodName());
				} else {
					if (!period.containsKey(financial.getPeriod().getPeriodValue())) {
						periodType = new HashSet<>();
						periodType.add(financial.getPeriod().getPeriodType().getPeriodName());
					} else {
						periodType = period.get(financial.getPeriod().getPeriodValue());
						periodType.add(financial.getPeriod().getPeriodType().getPeriodName());
					}
				}
				period.put(financial.getPeriod().getPeriodValue(), periodType);
				data.put(financial.getSource().getSourceName(), period);
			}
		} catch (Exception e) {
			logger.error("Error in FinancialServiceImpl.checkDataAvailability()"+e.getCause());
			throw new ApplicationBusinessException(appProperities.getPropertyValue("error.retrieved.msg"));
		}
		logger.info("End of FinancialServiceImpl.checkDataAvailability()");
		return data;
	}
}
