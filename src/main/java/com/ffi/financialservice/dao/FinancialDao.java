package com.ffi.financialservice.dao;

import java.util.List;
import java.util.UUID;

import com.ffi.financialservice.domain.BalanceSheet;
import com.ffi.financialservice.domain.Financial;
import com.ffi.financialservice.domain.IncomeStatement;
import com.ffi.financialservice.exception.ApplicationBusinessException;

public interface FinancialDao {
	
	public List<Financial> getFinancialOfCustomer(UUID customerId)  throws ApplicationBusinessException;;
	public List<BalanceSheet> getBalanceSheetOfFinancial(UUID financialId) throws ApplicationBusinessException;;
	public List<IncomeStatement> getIncomeStatementOfFinancial(UUID financialId) throws ApplicationBusinessException;;

}
