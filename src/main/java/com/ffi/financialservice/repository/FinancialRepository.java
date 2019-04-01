package com.ffi.financialservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ffi.financialservice.domain.BalanceSheet;
import com.ffi.financialservice.domain.Financial;
import com.ffi.financialservice.domain.IncomeStatement;

@Repository
public interface FinancialRepository extends JpaRepository<Financial, Integer>{
	
	@Query(value = "select f from Financial f where f.customerId like :filter")
	List<Financial> getFinancialOfCustomer(@Param("filter") UUID filter);
	
	@Query(value = "select b from BalanceSheet b where b.financialId like :filter")
	List<BalanceSheet> getBalanceSheetOfFinancial(@Param("filter") UUID filter);
	
	@Query(value = "select i from IncomeStatement i where i.financialId like :filter")
	List<IncomeStatement> getIncomeStatementOfFinancial(@Param("filter") UUID filter);

}
