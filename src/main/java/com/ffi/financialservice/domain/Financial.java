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
@Table(name = "FINANCIAL_MASTER")
public class Financial {
	
	private UUID id;
	private UUID customerId;
	private Date reported;
	private String reportedType;
	private Country country;
	private Currency currencyReported;
	private Source source;
	private Period period;
	
	@Id
	@Type(type = "org.hibernate.type.UUIDCharType")
	@Column(name = "Id")
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
	@Type(type = "org.hibernate.type.UUIDCharType")
	@Column(name = "CUSTOMER_ID")
	public UUID getCustomerId() {
		return customerId;
	}
	public void setCustomerId(UUID customerId) {
		this.customerId = customerId;
	}
	
	@Column(name = "REPORTED")
	public Date getReported() {
		return reported;
	}
	public void setReported(Date reported) {
		this.reported = reported;
	}
	
	@Column(name = "REPORTED_TYPE")
	public String getReportedType() {
		return reportedType;
	}
	public void setReportedType(String reportedType) {
		this.reportedType = reportedType;
	}
	
	@ManyToOne
	@JoinColumn(name = "COUNTRY")
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
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
	@JoinColumn(name = "SOURCE")
	public Source getSource() {
		return source;
	}
	public void setSource(Source source) {
		this.source = source;
	}
	
	@ManyToOne
	@JoinColumn(name = "PERIOD")
	public Period getPeriod() {
		return period;
	}
	public void setPeriod(Period period) {
		this.period = period;
	}
	
	

}
