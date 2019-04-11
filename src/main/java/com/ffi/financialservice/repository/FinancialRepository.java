package com.ffi.financialservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

@Repository
public interface FinancialRepository extends JpaRepository<Financial, Integer>{
	
	@Query("select f from Financial f where f.customerId like :customerId")
	List<Financial> getFinancialOfCustomer(@Param("customerId") UUID customerId);
	
	@Query("select bs from BalanceSheet bs INNER JOIN bs.financial f where f.id like :financialId")
	BalanceSheet getBalanceSheetOfFinancial(@Param("financialId") UUID financialId);
	
	@Query("select ist from IncomeStatement ist INNER JOIN ist.financial f where f.id like :financialId")
	IncomeStatement getIncomeStatementOfFinancial(@Param("financialId") UUID financialId);
	
	@Query("select pt from PeriodType pt where pt.periodName like :periodType")
	PeriodType getPeriodType(@Param("periodType") String periodType);
	
	@Query("select p from Period p INNER JOIN p.periodType pt where pt.id like :periodTypeId AND p.periodValue like :period")
	Period getPeriod(@Param("periodTypeId") UUID periodTypeId,@Param("period") String period);
	
	@Query("select s from Source s where s.sourceName like :sourceName")
	Source getSource(@Param("sourceName") String sourceName);
	
	@Query("select fd from Financial fd INNER JOIN fd.source fsrc INNER JOIN fd.period fdp where fsrc.id like :sourceId AND fdp.id like :periodId AND fd.customerId like :customerId")
	Financial getFinancial(@Param("sourceId") UUID sourceId,@Param("periodId") UUID periodId,@Param("customerId") UUID customerId);
	
	@Query("select ca from CurrentAsset ca INNER JOIN ca.balanceSheet bs where bs.id like :balanceSheetId")
	List<CurrentAsset> getCurrentAssets(@Param("balanceSheetId") UUID balanceSheetId);
	
	@Query("select nca from NonCurrentAsset nca INNER JOIN nca.balanceSheet bs where bs.id like :balanceSheetId")
	List<NonCurrentAsset> getNonCurrentAssets(@Param("balanceSheetId") UUID balanceSheetId);
	
	@Query("select cl from CurrentLiability cl INNER JOIN cl.balanceSheet bs where bs.id like :balanceSheetId")
	List<CurrentLiability> getCurrentLiability(@Param("balanceSheetId") UUID balanceSheetId);
	
	
	@Query("select ncl from NonCurrentLiability ncl INNER JOIN ncl.balanceSheet bs where bs.id like :balanceSheetId")
	List<NonCurrentLiability> getNonCurrentLiability(@Param("balanceSheetId") UUID balanceSheetId);
	
	@Query("select eq from Equity eq INNER JOIN eq.balanceSheet bs where bs.id like :balanceSheetId")
	List<Equity> getEquity(@Param("balanceSheetId") UUID balanceSheetId);
	
	@Query("select r from Revenue r INNER JOIN r.incomeStatement ist where ist.id like :incomeStatementId")
	List<Revenue> getRevenues(@Param("incomeStatementId") UUID incomeStatementId);
	
	@Query("select dc from DirectCost dc INNER JOIN dc.incomeStatement ist where ist.id like :incomeStatementId")
	List<DirectCost> getDirectCosts(@Param("incomeStatementId") UUID incomeStatementId);
	
	@Query("select ic from IndirectCost ic INNER JOIN ic.incomeStatement ist where ist.id like :incomeStatementId")
	List<IndirectCost> getIndirectCosts(@Param("incomeStatementId") UUID incomeStatementId);
	
	@Query("select df from DebtFin df INNER JOIN df.incomeStatement ist where ist.id like :incomeStatementId")
	List<DebtFin> getDebtFins(@Param("incomeStatementId") UUID incomeStatementId);
	
	@Query("select t from Tax t INNER JOIN t.incomeStatement ist where ist.id like :incomeStatementId")
	List<Tax> getTax(@Param("incomeStatementId") UUID incomeStatementId);

}
