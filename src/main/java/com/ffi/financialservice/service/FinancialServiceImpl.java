package com.ffi.financialservice.service;

import java.util.ArrayList;
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
import com.ffi.financialservice.dto.PeriodRequest;
import com.ffi.financialservice.exception.ApplicationBusinessException;
import com.ffi.financialservice.handler.FinancialServiceConfiguration;
import com.ffi.financialservice.vo.FinancialVO;

@Service
public class FinancialServiceImpl implements FinancialService {

	private static final Logger logger = LogManager.getLogger(FinancialServiceImpl.class);

	@Autowired
	FinancialDao financialDao;

	@Autowired
	FinancialServiceConfiguration configuration;

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
			logger.error("Error in FinancialServiceImpl.checkDataAvailability()" + e.getCause());
			throw new ApplicationBusinessException(configuration.getError().getRetrieve());
		}
		logger.info("End of FinancialServiceImpl.checkDataAvailability()");
		return data;
	}

	@Override
	public List<FinancialVO> getFinancialData(String companyId, String sourceName,List<PeriodRequest> periodRequest) throws ApplicationBusinessException {
		logger.info("Start of FinancialServiceImpl.getFinancialData()");
		List<FinancialVO> financialVO = new ArrayList<>();
		try {
			Source source = financialDao.getSource(sourceName);
			for (PeriodRequest pRequest : periodRequest) {
				PeriodType periodType = financialDao.getPeriodType(pRequest.getType());
				Period period = financialDao.getPeriod(periodType.getId(), pRequest.getPeriod());
				Financial financial = financialDao.getFinancial(source.getId(), period.getId(),UUID.fromString(companyId));
				financialVO.addAll(getBalanceSheet(financial.getId(), period.getPeriodValue()));
				financialVO.addAll(getIncomeStatement(financial.getId(), period.getPeriodValue()));
			}
		} catch (Exception e) {
			logger.info("Error in FinancialServiceImpl.getFinancialData()" + e.getStackTrace());
			throw new ApplicationBusinessException(configuration.getError().getRetrieve());
		}
		logger.info("End of FinancialServiceImpl.getFinancialData()");
		return financialVO;
	}

	private List<FinancialVO> getBalanceSheet(UUID financialId, String period) throws ApplicationBusinessException {
		logger.info("Start of FinancialServiceImpl.getBalanceSheet()");
		List<FinancialVO> financialDataList = new ArrayList<>();
		try {
			BalanceSheet balanceSheet = financialDao.getBalanceSheetOfFinancial(financialId);
			List<CurrentAsset> currentAssets = financialDao.getCurrentAssetOfBalanceSheet(balanceSheet.getId());
			List<NonCurrentAsset> nonCurrentAssets = financialDao
					.getNonCurrentAssetOfBalanceSheet(balanceSheet.getId());
			List<CurrentLiability> currentLiabilities = financialDao
					.getCurrentLiabilityOfBalanceSheet(balanceSheet.getId());
			List<NonCurrentLiability> nonCurrentLiabilities = financialDao
					.getNonCurrentLiabilityOfBalanceSheet(balanceSheet.getId());
			List<Equity> equities = financialDao.getEquityOfBalanceSheet(balanceSheet.getId());
			
			if (!currentAssets.isEmpty()) {
				for (CurrentAsset currentAsset : currentAssets) {
					FinancialVO financialVO = new FinancialVO();
					financialVO.setYear(period);
					financialVO.setLineItem(currentAsset.getTemplateLabelId().toString());
					financialVO.setLineItemValue(currentAsset.getValue());
					financialDataList.add(financialVO);
				}
			}

			if (!nonCurrentAssets.isEmpty()) {
				for (NonCurrentAsset nonCurrentAsset : nonCurrentAssets) {
					FinancialVO financialVO = new FinancialVO();
					financialVO.setYear(period);
					financialVO.setLineItem(nonCurrentAsset.getTemplateLabelId().toString());
					financialVO.setLineItemValue(nonCurrentAsset.getValue());
					financialDataList.add(financialVO);
				}
			}

			if (!currentLiabilities.isEmpty()) {
				for (CurrentLiability currentLiabilty : currentLiabilities) {
					FinancialVO financialVO = new FinancialVO();
					financialVO.setYear(period);
					financialVO.setLineItem(currentLiabilty.getTemplateLabelId().toString());
					financialVO.setLineItemValue(currentLiabilty.getValue());
					financialDataList.add(financialVO);
				}
			}

			if (!nonCurrentLiabilities.isEmpty()) {
				for (NonCurrentLiability currentLiability : nonCurrentLiabilities) {
					FinancialVO financialVO = new FinancialVO();
					financialVO.setYear(period);
					financialVO.setLineItem(currentLiability.getTemplateLabelId().toString());
					financialVO.setLineItemValue(currentLiability.getValue());
					financialDataList.add(financialVO);
				}
			}

			if (!equities.isEmpty()) {
				for (Equity equity : equities) {
					FinancialVO financialVO = new FinancialVO();
					financialVO.setYear(period);
					financialVO.setLineItem(equity.getTemplateLabelId().toString());
					financialVO.setLineItemValue(equity.getValue());
					financialDataList.add(financialVO);
				}
			}
		} catch (ApplicationBusinessException e) {
			logger.info("Error in FinancialServiceImpl.getBalanceSheet()");
			throw new ApplicationBusinessException(configuration.getError().getRetrieve());
		}
		logger.info("End of FinancialServiceImpl.getBalanceSheet()");
		return financialDataList;
	}
	
	private List<FinancialVO> getIncomeStatement(UUID financialId, String period) throws ApplicationBusinessException {
		logger.info("Start of FinancialServiceImpl.getIncomeStatement()");
		List<FinancialVO> financialDataList = new ArrayList<>();
		try {
			IncomeStatement incomeStatement = financialDao.getIncomeStatementOfFinancial(financialId);
			List<Revenue> revenues = financialDao.getRevenueOfIncomeStatement(incomeStatement.getId());
			List<DirectCost> directCosts = financialDao.getDirectCostOfIncomeStatement(incomeStatement.getId());
			List<IndirectCost> indirectCosts = financialDao.getIndirectCostOfIncomeStatement(incomeStatement.getId());
			List<DebtFin> debtFins = financialDao.getDebtFinOfIncomeStatement(incomeStatement.getId());
			List<Tax> taxes = financialDao.getTaxOfIncomeStatement(incomeStatement.getId());

			if (!revenues.isEmpty()) {
				for (Revenue revenue : revenues) {
					FinancialVO financialVO = new FinancialVO();
					financialVO.setYear(period);
					financialVO.setLineItem(revenue.getTemplateLabelId().toString());
					financialVO.setLineItemValue(revenue.getValue());
					financialDataList.add(financialVO);
				}
			}

			if (!directCosts.isEmpty()) {
				for (DirectCost directCost : directCosts) {
					FinancialVO financialVO = new FinancialVO();
					financialVO.setYear(period);
					financialVO.setLineItem(directCost.getTemplateLabelId().toString());
					financialVO.setLineItemValue(directCost.getValue());
					financialDataList.add(financialVO);
				}
			}

			if (!indirectCosts.isEmpty()) {
				for (IndirectCost indirectCost : indirectCosts) {
					FinancialVO financialVO = new FinancialVO();
					financialVO.setYear(period);
					financialVO.setLineItem(indirectCost.getTemplateLabelId().toString());
					financialVO.setLineItemValue(indirectCost.getValue());
					financialDataList.add(financialVO);
				}
			}

			if (!debtFins.isEmpty()) {
				for (DebtFin debtFin : debtFins) {
					FinancialVO financialVO = new FinancialVO();
					financialVO.setYear(period);
					financialVO.setLineItem(debtFin.getTemplateLabelId().toString());
					financialVO.setLineItemValue(debtFin.getValue());
					financialDataList.add(financialVO);
				}
			}

			if (!taxes.isEmpty()) {
				for (Tax tax : taxes) {
					FinancialVO financialVO = new FinancialVO();
					financialVO.setYear(period);
					financialVO.setLineItem(tax.getTemplateLabelId().toString());
					financialVO.setLineItemValue(tax.getValue());
					financialDataList.add(financialVO);
				}
			}
		} catch (ApplicationBusinessException e) {
			logger.info("Error in FinancialServiceImpl.getIncomeStatement()");
			throw new ApplicationBusinessException(configuration.getError().getRetrieve());
		}
		logger.info("End of FinancialServiceImpl.getBalancegetIncomeStatementSheet()");
		return financialDataList;
	}
}
