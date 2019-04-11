package com.ffi.financialservice.dao;

import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ffi.financialservice.domain.BalanceSheet;
import com.ffi.financialservice.domain.CurrentAsset;
import com.ffi.financialservice.domain.CurrentLiability;
import com.ffi.financialservice.domain.DebtFin;
import com.ffi.financialservice.domain.DirectCost;
import com.ffi.financialservice.domain.Equity;
import com.ffi.financialservice.domain.Financial;
import com.ffi.financialservice.domain.IncomeStatement;
import com.ffi.financialservice.domain.IndirectCost;
import com.ffi.financialservice.domain.NonCurrentAsset;
import com.ffi.financialservice.domain.NonCurrentLiability;
import com.ffi.financialservice.domain.Period;
import com.ffi.financialservice.domain.PeriodType;
import com.ffi.financialservice.domain.Revenue;
import com.ffi.financialservice.domain.Source;
import com.ffi.financialservice.domain.Tax;
import com.ffi.financialservice.exception.ApplicationBusinessException;
import com.ffi.financialservice.handler.AppProperities;
import com.ffi.financialservice.repository.FinancialRepository;

@Component
public class FinancialDaoImpl implements FinancialDao {

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
	public BalanceSheet getBalanceSheetOfFinancial(UUID financialId) throws ApplicationBusinessException {
		logger.info("Start of FinancialDaoImpl.getBalanceSheetOfFinancial()");
		BalanceSheet balanceSheet = null;
		try {
			balanceSheet = financialRepository.getBalanceSheetOfFinancial(financialId);
		} catch (Exception e) {
			logger.info("Error in FinancialDaoImpl.getBalanceSheetOfFinancial()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("sql.error"), e.getCause());
		}
		logger.info("End of FinancialDaoImpl.getBalanceSheetOfFinancial()");
		return balanceSheet;
	}

	@Override
	public IncomeStatement getIncomeStatementOfFinancial(UUID financialId) throws ApplicationBusinessException {
		logger.info("Start of FinancialDaoImpl.getIncomeStatementOfFinancial()");
		IncomeStatement incomeStatement = null;
		try {
			incomeStatement = financialRepository.getIncomeStatementOfFinancial(financialId);
		} catch (Exception e) {
			logger.info("Error in FinancialDaoImpl.getIncomeStatementOfFinancial()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("sql.error"), e.getCause());
		}
		logger.info("End of FinancialDaoImpl.getIncomeStatementOfFinancial()");
		return incomeStatement;
	}

	@Override
	public PeriodType getPeriodType(String periodTypeName) throws ApplicationBusinessException {
		logger.info("Start of FinancialDaoImpl.getPeriodType()");
		PeriodType periodType = null;
		try {
			periodType = financialRepository.getPeriodType(periodTypeName);
		} catch (Exception e) {
			logger.info("Error in FinancialDaoImpl.getPeriodType()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("sql.error"), e.getCause());
		}
		logger.info("End of FinancialDaoImpl.getPeriodType()");
		return periodType;
	}

	@Override
	public Period getPeriod(UUID periodTypeID, String periodValue) throws ApplicationBusinessException {
		logger.info("Start of FinancialDaoImpl.getPeriod()");
		Period period = null;
		try {
			period = financialRepository.getPeriod(periodTypeID, periodValue);
		} catch (Exception e) {
			logger.info("Error in FinancialDaoImpl.getPeriod()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("sql.error"), e.getCause());
		}
		logger.info("End of FinancialDaoImpl.getPeriod()");
		return period;
	}

	@Override
	public Source getSource(String sourceName) throws ApplicationBusinessException {
		logger.info("Start of FinancialDaoImpl.getSource()");
		Source source = null;
		try {
			source = financialRepository.getSource(sourceName);
		} catch (Exception e) {
			logger.info("Error in FinancialDaoImpl.getSource()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("sql.error"), e.getCause());
		}
		logger.info("End of FinancialDaoImpl.getSource()");
		return source;
	}

	@Override
	public Financial getFinancial(UUID sourceId, UUID periodId,UUID companyId) throws ApplicationBusinessException {
		logger.info("Start of FinancialDaoImpl.getFinancial()");
		Financial financial = null;
		try {
			financial = financialRepository.getFinancial(sourceId, periodId,companyId);
		} catch (Exception e) {
			logger.info("Error in FinancialDaoImpl.getFinancial()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("sql.error"), e.getCause());
		}
		logger.info("End of FinancialDaoImpl.getFinancial()");
		return financial;
	}
	
	@Override
	public List<CurrentAsset> getCurrentAssetOfBalanceSheet(UUID balanceSheetId) throws ApplicationBusinessException {
		logger.info("Start of FinancialDaoImpl.getCurrentAssetOfBalanceSheet()");
		List<CurrentAsset> currentAssets = null;
		try {
			currentAssets = financialRepository.getCurrentAssets(balanceSheetId);
		} catch (Exception e) {
			logger.info("Error in FinancialDaoImpl.getCurrentAssetOfBalanceSheet()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("sql.error"), e.getCause());
		}
		logger.info("End of FinancialDaoImpl.getCurrentAssetOfBalanceSheet()");
		return currentAssets;
	}
	
	@Override
	public List<NonCurrentAsset> getNonCurrentAssetOfBalanceSheet(UUID balanceSheetId) throws ApplicationBusinessException {
		logger.info("Start of FinancialDaoImpl.getNonCurrentAssetOfBalanceSheet()");
		List<NonCurrentAsset> nonCurrentAssets = null;
		try {
			nonCurrentAssets = financialRepository.getNonCurrentAssets(balanceSheetId);
		} catch (Exception e) {
			logger.info("Error in FinancialDaoImpl.getNonCurrentAssetOfBalanceSheet()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("sql.error"), e.getCause());
		}
		logger.info("End of FinancialDaoImpl.getNonCurrentAssetOfBalanceSheet()");
		return nonCurrentAssets;
	}

	@Override
	public List<CurrentLiability> getCurrentLiabilityOfBalanceSheet(UUID balanceSheetId)
			throws ApplicationBusinessException {
		logger.info("Start of FinancialDaoImpl.getCurrentLiabilityOfBalanceSheet()");
		List<CurrentLiability> currentLiability = null;
		try {
			currentLiability = financialRepository.getCurrentLiability(balanceSheetId);
		} catch (Exception e) {
			logger.info("Error in FinancialDaoImpl.getCurrentLiabilityOfBalanceSheet()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("sql.error"), e.getCause());
		}
		logger.info("End of FinancialDaoImpl.getCurrentLiabilityOfBalanceSheet()");
		return currentLiability;
	}

	@Override
	public List<NonCurrentLiability> getNonCurrentLiabilityOfBalanceSheet(UUID balanceSheetId)
			throws ApplicationBusinessException {
		logger.info("Start of FinancialDaoImpl.getNonCurrentLiabilityOfBalanceSheet()");
		List<NonCurrentLiability> nonCurrentLiability = null;
		try {
			nonCurrentLiability = financialRepository.getNonCurrentLiability(balanceSheetId);
		} catch (Exception e) {
			logger.info("Error in FinancialDaoImpl.getNonCurrentLiabilityOfBalanceSheet()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("sql.error"), e.getCause());
		}
		logger.info("End of FinancialDaoImpl.getNonCurrentLiabilityOfBalanceSheet()");
		return nonCurrentLiability;
	}

	@Override
	public List<Equity> getEquityOfBalanceSheet(UUID balanceSheetId) throws ApplicationBusinessException {
		logger.info("Start of FinancialDaoImpl.getEquityOfBalanceSheet()");
		List<Equity> equity = null;
		try {
			equity = financialRepository.getEquity(balanceSheetId);
		} catch (Exception e) {
			logger.info("Error in FinancialDaoImpl.getEquityOfBalanceSheet()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("sql.error"), e.getCause());
		}
		logger.info("End of FinancialDaoImpl.getEquityOfBalanceSheet()");
		return equity;
	}

	@Override
	public List<Revenue> getRevenueOfIncomeStatement(UUID incomeStatementId) throws ApplicationBusinessException {
		logger.info("Start of FinancialDaoImpl.getRevenueOfIncomeStatement()");
		List<Revenue> revenues = null;
		try {
			revenues = financialRepository.getRevenues(incomeStatementId);
		} catch (Exception e) {
			logger.info("Error in FinancialDaoImpl.getRevenueOfIncomeStatement()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("sql.error"), e.getCause());
		}
		logger.info("End of FinancialDaoImpl.getRevenueOfIncomeStatement()");
		return revenues;
	}

	@Override
	public List<DirectCost> getDirectCostOfIncomeStatement(UUID incomeStatementId) throws ApplicationBusinessException {
		logger.info("Start of FinancialDaoImpl.getDirectCostOfIncomeStatement()");
		List<DirectCost> directCosts = null;
		try {
			directCosts = financialRepository.getDirectCosts(incomeStatementId);
		} catch (Exception e) {
			logger.info("Error in FinancialDaoImpl.getDirectCostOfIncomeStatement()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("sql.error"), e.getCause());
		}
		logger.info("End of FinancialDaoImpl.getDirectCostOfIncomeStatement()");
		return directCosts;
	}

	@Override
	public List<IndirectCost> getIndirectCostOfIncomeStatement(UUID incomeStatementId)
			throws ApplicationBusinessException {
		logger.info("Start of FinancialDaoImpl.getIndirectCostOfIncomeStatement()");
		List<IndirectCost> indirectCosts = null;
		try {
			indirectCosts = financialRepository.getIndirectCosts(incomeStatementId);
		} catch (Exception e) {
			logger.info("Error in FinancialDaoImpl.getIndirectCostOfIncomeStatement()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("sql.error"), e.getCause());
		}
		logger.info("End of FinancialDaoImpl.getIndirectCostOfIncomeStatement()");
		return indirectCosts;
	}

	@Override
	public List<DebtFin> getDebtFinOfIncomeStatement(UUID incomeStatementId) throws ApplicationBusinessException {
		logger.info("Start of FinancialDaoImpl.getDebtFinOfIncomeStatement()");
		List<DebtFin> debtFins = null;
		try {
			debtFins = financialRepository.getDebtFins(incomeStatementId);
		} catch (Exception e) {
			logger.info("Error in FinancialDaoImpl.getDebtFinOfIncomeStatement()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("sql.error"), e.getCause());
		}
		logger.info("End of FinancialDaoImpl.getDebtFinOfIncomeStatement()");
		return debtFins;
	}

	@Override
	public List<Tax> getTaxOfIncomeStatement(UUID incomeStatementId) throws ApplicationBusinessException {
		logger.info("Start of FinancialDaoImpl.getTaxOfIncomeStatement()");
		List<Tax> tax = null;
		try {
			tax = financialRepository.getTax(incomeStatementId);
		} catch (Exception e) {
			logger.info("Error in FinancialDaoImpl.getTaxOfIncomeStatement()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("sql.error"), e.getCause());
		}
		logger.info("End of FinancialDaoImpl.getTaxOfIncomeStatement()");
		return tax;
	}
}
