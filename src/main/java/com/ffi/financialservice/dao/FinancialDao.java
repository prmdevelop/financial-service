package com.ffi.financialservice.dao;

import java.util.List;
import java.util.UUID;

import com.ffi.financialservice.domain.BalanceSheet;
import com.ffi.financialservice.domain.CurrentAsset;
import com.ffi.financialservice.domain.Financial;
import com.ffi.financialservice.domain.IncomeStatement;
import com.ffi.financialservice.domain.NonCurrentAsset;
import com.ffi.financialservice.domain.Period;
import com.ffi.financialservice.domain.PeriodType;
import com.ffi.financialservice.domain.Source;
import com.ffi.financialservice.exception.ApplicationBusinessException;

public interface FinancialDao {

	public List<Financial> getFinancialOfCustomer(UUID customerId) throws ApplicationBusinessException;;

	public BalanceSheet getBalanceSheetOfFinancial(UUID financialId) throws ApplicationBusinessException;;

	public IncomeStatement getIncomeStatementOfFinancial(UUID financialId) throws ApplicationBusinessException;

	public PeriodType getPeriodType(String periodType) throws ApplicationBusinessException;

	public Period getPeriod(UUID periodTypeID, String periodValue) throws ApplicationBusinessException;

	public Source getSource(String sourceName) throws ApplicationBusinessException;

	public Financial getFinancial(UUID sourceId, UUID periodId,UUID companyId)	throws ApplicationBusinessException;
	
	public List<CurrentAsset> getCurrentAssetOfBalanceSheet(UUID balanceSheetId) throws ApplicationBusinessException;
	
	public List<NonCurrentAsset> getNonCurrentAssetOfBalanceSheet(UUID balanceSheetId) throws ApplicationBusinessException;
}
