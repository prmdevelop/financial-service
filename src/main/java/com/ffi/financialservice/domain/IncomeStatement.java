package com.ffi.financialservice.domain;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "INCOME_STATEMENT")
public class IncomeStatement {
	
	private UUID id;
	private Financial financial;
	private Date periodEndDate;
	private boolean auditStatus;
	private int version;
	private Double grossProfit;
	private Double netSales;
	private Double opertingINcome;
	private Double profitBeforeTax;
	private Double netPrfoit;
	
	@Id
	@Type(type = "uuid-char")
	@Column(name = "Id")
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "FINANCIAL_ID")
	public Financial getFinancial() {
		return financial;
	}
	public void setFinancial(Financial financial) {
		this.financial = financial;
	}
	
	@Column(name = "PERIOD_END_DATE")
	public Date getPeriodEndDate() {
		return periodEndDate;
	}
	public void setPeriodEndDate(Date periodEndDate) {
		this.periodEndDate = periodEndDate;
	}
	
	@Column(name = "AUDIT_STATUS")
	public boolean isAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(boolean auditStatus) {
		this.auditStatus = auditStatus;
	}
	
	@Column(name = "VERSION")
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	@Column(name = "GROSS_PROFIT")
	public Double getGrossProfit() {
		return grossProfit;
	}
	public void setGrossProfit(Double grossProfit) {
		this.grossProfit = grossProfit;
	}
	
	@Column(name = "NET_SALES")
	public Double getNetSales() {
		return netSales;
	}
	public void setNetSales(Double netSales) {
		this.netSales = netSales;
	}
	
	@Column(name = "OPERTING_INCOME")
	public Double getOpertingINcome() {
		return opertingINcome;
	}
	public void setOpertingINcome(Double opertingINcome) {
		this.opertingINcome = opertingINcome;
	}
	
	@Column(name = "PROFIT_BEFORE_TAX")
	public Double getProfitBeforeTax() {
		return profitBeforeTax;
	}
	public void setProfitBeforeTax(Double profitBeforeTax) {
		this.profitBeforeTax = profitBeforeTax;
	}
	
	@Column(name = "NET_PROFIT")
	public Double getNetPrfoit() {
		return netPrfoit;
	}
	public void setNetPrfoit(Double netPrfoit) {
		this.netPrfoit = netPrfoit;
	}
}
