package com.ffi.financialservice.dao;

import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ffi.financialservice.domain.BalanceSheet;
import com.ffi.financialservice.domain.Financial;
import com.ffi.financialservice.domain.IncomeStatement;
import com.ffi.financialservice.exception.ApplicationBusinessException;
import com.ffi.financialservice.handler.AppProperities;
import com.ffi.financialservice.repository.FinancialRepository;

@Component
public class FinancialDaoImpl implements FinancialDao{
	
	private static final Logger logger = LogManager.getLogger(FinancialDaoImpl.class);
	
	@Autowired
	FinancialRepository financialRepository;
	
	@Autowired
	AppProperities appProperities;

	@Override
	public List<Financial> getFinancialOfCustomer(UUID customerId) throws ApplicationBusinessException {
		logger.info("Start of FinancialDaoImpl.getFinancialOfCustomer()");
		List<Financial> financials = null;
		try {
			financials = financialRepository.getFinancialOfCustomer(customerId);
		} catch (Exception e) {
			logger.info("Error in FinancialDaoImpl.getFinancialOfCustomer()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("sql.error"), e.getCause());
		}
		logger.info("End of FinancialDaoImpl.getFinancialOfCustomer()");
		return financials;
	}

	@Override
	public List<BalanceSheet> getBalanceSheetOfFinancial(UUID financialId)throws ApplicationBusinessException {
		logger.info("Start of FinancialDaoImpl.getBalanceSheetOfFinancial()");
		List<BalanceSheet> balanceSheets = null;
		try {
			balanceSheets = financialRepository.getBalanceSheetOfFinancial(financialId);
		} catch (Exception e) {
			logger.info("Error in FinancialDaoImpl.getBalanceSheetOfFinancial()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("sql.error"), e.getCause());
		}
		logger.info("End of FinancialDaoImpl.getBalanceSheetOfFinancial()");
		return balanceSheets;
	}

	@Override
	public List<IncomeStatement> getIncomeStatementOfFinancial(UUID financialId) throws ApplicationBusinessException {
		logger.info("Start of FinancialDaoImpl.getIncomeStatementOfFinancial()");
		List<IncomeStatement> incomeStatements = null;
		try {
			incomeStatements = financialRepository.getIncomeStatementOfFinancial(financialId);
		} catch (Exception e) {
			logger.info("Error in FinancialDaoImpl.getIncomeStatementOfFinancial()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("sql.error"), e.getCause());
		}
		logger.info("End of FinancialDaoImpl.getIncomeStatementOfFinancial()");
		return incomeStatements;
	}

}
