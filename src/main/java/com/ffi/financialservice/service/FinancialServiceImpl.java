package com.ffi.financialservice.service;

import java.util.Calendar;
import java.util.Date;
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
import com.ffi.financialservice.domain.BalanceSheet;
import com.ffi.financialservice.domain.Financial;
import com.ffi.financialservice.domain.IncomeStatement;
import com.ffi.financialservice.exception.ApplicationBusinessException;
import com.ffi.financialservice.handler.AppProperities;

@Service
public class FinancialServiceImpl implements FinancialService {
	
	private static final Logger logger = LogManager.getLogger(FinancialServiceImpl.class);

	@Autowired
	FinancialDao financialDao;
	
	@Autowired
	AppProperities appProperities;

	public Map<String, Set<String>> checkDataAvailability1(String customerId) throws ApplicationBusinessException {
		logger.info("Start of FinancialServiceImpl.checkDataAvailability()");
		Map<String, Set<String>> map = new HashMap<>();
		List<Financial> financials = financialDao.getFinancialOfCustomer(UUID.fromString(customerId));
		for (Financial financial : financials) {
			Set<String> sourceList = null;
			List<BalanceSheet> balanceSheets = financialDao.getBalanceSheetOfFinancial(financial.getId());
			if (!balanceSheets.isEmpty()) {
				for (BalanceSheet balanceSheet : balanceSheets) {
					if (financial.getReportedType().equalsIgnoreCase("10-k")) {
						sourceList = new HashSet<>();
						sourceList.add(financial.getSource().getSourceName());
						map.put(balanceSheet.getYear(), sourceList);
					} else {
						Date date = balanceSheet.getEndDate();
						String getQuarter = getQuarter(date);
						sourceList = new HashSet<>();
						sourceList.add(financial.getSource().getSourceName());
						map.put(balanceSheet.getYear() + " " + getQuarter, sourceList);
					}
				}
			}

			List<IncomeStatement> incomeStatements = financialDao.getIncomeStatementOfFinancial(financial.getId());
			if (!incomeStatements.isEmpty()) {
				for (IncomeStatement incomeStatement : incomeStatements) {
					if (financial.getReportedType().equalsIgnoreCase("10-k")) {
						if (!map.containsKey(incomeStatement.getYear())) {
							sourceList = new HashSet<>();
							sourceList.add(financial.getSource().getSourceName());
							map.put(incomeStatement.getYear(), sourceList);
						} else {
							sourceList = map.get(incomeStatement.getYear());
							sourceList.add(financial.getSource().getSourceName());
							map.put(incomeStatement.getYear(), sourceList);
						}
					} else {
						Date date = incomeStatement.getEndDate();
						String getQuarter = getQuarter(date);
						if (!map.containsKey(incomeStatement.getYear() + " " + getQuarter)) {
							sourceList = new HashSet<>();
							sourceList.add(financial.getSource().getSourceName());
							map.put(incomeStatement.getYear() + " " + getQuarter, sourceList);
						} else {
							sourceList = map.get(incomeStatement.getYear());
							sourceList.add(financial.getSource().getSourceName());
							map.put(incomeStatement.getYear() + " " + getQuarter, sourceList);
						}
					}
				}
			}
		}
		logger.info("End of FinancialServiceImpl.checkDataAvailability()");
		return map;
	}
	
	@Override
	public Map<String, Map<String,Set<String>>> checkDataAvailability(String customerId) throws ApplicationBusinessException {
		logger.info("Start of FinancialServiceImpl.checkDataAvailability()");
		Map<String, Map<String,Set<String>>> data = new HashMap<>();
		Map<String,Set<String>> period = new HashMap<>();;
		Set<String> periodType = null;
		List<Financial> financials = financialDao.getFinancialOfCustomer(UUID.fromString(customerId));
		for (Financial financial : financials) {
			List<BalanceSheet> balanceSheets = financialDao.getBalanceSheetOfFinancial(financial.getId());
			if (!balanceSheets.isEmpty()) {
				for (BalanceSheet balanceSheet : balanceSheets) {
					if (financial.getReportedType().equalsIgnoreCase("10-k")) {
						if(!data.containsKey(financial.getSource().getSourceName()) ||
							 (data.containsKey(financial.getSource().getSourceName()) && !period.containsKey(balanceSheet.getYear()))){
							periodType = new HashSet<>();
							periodType.add("Annually");
						}else{
							periodType = period.get(balanceSheet.getYear());
							periodType.add("Annually");
						}
					} else {
						Date date = balanceSheet.getEndDate();
						String getQuarter = getQuarter(date);
						if(!data.containsKey(financial.getSource().getSourceName()) ||
								 (data.containsKey(financial.getSource().getSourceName()) && !period.containsKey(balanceSheet.getYear()))){
							periodType = new HashSet<>();
							periodType.add(getQuarter);
						}else{
							periodType = period.get(balanceSheet.getYear());
							periodType.add(getQuarter);
						}
					}
					period.put(balanceSheet.getYear(), periodType);
					data.put(financial.getSource().getSourceName(),period);
				}
			}

			List<IncomeStatement> incomeStatements = financialDao.getIncomeStatementOfFinancial(financial.getId());
			if (!incomeStatements.isEmpty()) {
				for (IncomeStatement incomeStatement : incomeStatements) {
					if (financial.getReportedType().equalsIgnoreCase("10-k")) {
						if(!data.containsKey(financial.getSource().getSourceName()) ||
							 (data.containsKey(financial.getSource().getSourceName()) && !period.containsKey(incomeStatement.getYear()))){
							periodType = new HashSet<>();
							periodType.add("Annually");
						}else{
							periodType = period.get(incomeStatement.getYear());
							periodType.add("Annually");
						}
					} else {
						Date date = incomeStatement.getEndDate();
						String getQuarter = getQuarter(date);
						if(!data.containsKey(financial.getSource().getSourceName()) ||
								 (data.containsKey(financial.getSource().getSourceName()) && !period.containsKey(incomeStatement.getYear()))){
							periodType = new HashSet<>();
							periodType.add(getQuarter);
						}else{
							periodType = period.get(incomeStatement.getYear());
							periodType.add(getQuarter);
						}
					}
					period.put(incomeStatement.getYear(), periodType);
					data.put(financial.getSource().getSourceName(),period);
				}
			}
		}
		logger.info("End of FinancialServiceImpl.checkDataAvailability()");
		return data;
	}

	public static String getQuarter(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH);
		return (month >= Calendar.JANUARY && month <= Calendar.MARCH) ? "Q1"
				: (month >= Calendar.APRIL && month <= Calendar.JUNE) ? "Q2"
						: (month >= Calendar.JULY && month <= Calendar.SEPTEMBER) ? "Q3" : "Q4";
	}

}
