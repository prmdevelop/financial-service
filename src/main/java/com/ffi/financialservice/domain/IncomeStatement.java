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

@Entity
@Table(name = "INCOME_STATEMENT")
public class IncomeStatement {
	
	private UUID id;
	private UUID financialId;
	private String year;
	private Date endDate;
	private String auditStatus;
	private String version;
	private String grossProfit;
	private Currency currencyReported;
	private Currency currencyConverted;
	
	@Id
	@Type(type = "uuid-char")
	@Column(name = "Id")
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
	@Type(type = "org.hibernate.type.UUIDCharType")
	@Column(name = "FINANCIAL_ID")
	public UUID getFinancialId() {
		return financialId;
	}
	public void setFinancialId(UUID financialId) {
		this.financialId = financialId;
	}
	
	@Column(name = "YEAR")
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	
	@Column(name = "PERIOD_END_DATE")
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Column(name = "AUDIT_STATUS")
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	
	@Column(name = "VERSION")
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	@Column(name = "GROSS_PROFIT")
	public String getGrossProfit() {
		return grossProfit;
	}
	public void setGrossProfit(String grossProfit) {
		this.grossProfit = grossProfit;
	}
	
	@ManyToOne
	@JoinColumn(name = "CURRENCY_REPORTED")
	public Currency getCurrencyReported() {
		return currencyReported;
	}
	public void setCurrencyReported(Currency currencyReported) {
		this.currencyReported = currencyReported;
	}
	
	@ManyToOne
	@JoinColumn(name = "CURRENCY_CONVERTED")
	public Currency getCurrencyConverted() {
		return currencyConverted;
	}
	public void setCurrencyConverted(Currency currencyConverted) {
		this.currencyConverted = currencyConverted;
	}

}
