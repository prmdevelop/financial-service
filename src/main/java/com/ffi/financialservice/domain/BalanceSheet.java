package com.ffi.financialservice.domain;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "BALANCE_SHEET")
public class BalanceSheet {
	
	private UUID id;
	private Financial financial;
	private Date periodEndDate;
	private boolean auditStatus;
	private int version;
	private Double totalCurrentAsset;
	private Double totalNonCurrentAsset;
	private Double totalCurrentLiability;
	private Double totalNonCurrentLiability;
	private Double totalAsset;
	private Double totalLiability;
	private Double totalEquity;
	private Date createdDate;
	private Set<CurrentAsset> currentAssets;
	private Set<NonCurrentAsset> nonCurrentAssets;
	private Set<CurrentLiability> currentLiability;
	private Set<NonCurrentLiability> nonCurrentLiability;
	
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
	
	@Column(name = "TOTAL_CA")
	public Double getTotalCurrentAsset() {
		return totalCurrentAsset;
	}
	public void setTotalCurrentAsset(Double totalCurrentAsset) {
		this.totalCurrentAsset = totalCurrentAsset;
	}
	
	@Column(name = "TOTAL_NC_ASSET")
	public Double getTotalNonCurrentAsset() {
		return totalNonCurrentAsset;
	}
	public void setTotalNonCurrentAsset(Double totalNonCurrentAsset) {
		this.totalNonCurrentAsset = totalNonCurrentAsset;
	}
	
	@Column(name = "TOTAL_CL")
	public Double getTotalCurrentLiability() {
		return totalCurrentLiability;
	}
	public void setTotalCurrentLiability(Double totalCurrentLiability) {
		this.totalCurrentLiability = totalCurrentLiability;
	}
	
	@Column(name = " TOTAL_NC_LIA")
	public Double getTotalNonCurrentLiability() {
		return totalNonCurrentLiability;
	}
	public void setTotalNonCurrentLiability(Double totalNonCurrentLiability) {
		this.totalNonCurrentLiability = totalNonCurrentLiability;
	}
	
	@Column(name = "TOTAL_ASSET")
	public Double getTotalAsset() {
		return totalAsset;
	}
	public void setTotalAsset(Double totalAsset) {
		this.totalAsset = totalAsset;
	}
	
	@Column(name = "TOTAL_LIA")
	public Double getTotalLiability() {
		return totalLiability;
	}
	public void setTotalLiability(Double totalLiability) {
		this.totalLiability = totalLiability;
	}
	
	@Column(name = " TOTAL_EQUITY")
	public Double getTotalEquity() {
		return totalEquity;
	}
	public void setTotalEquity(Double totalEquity) {
		this.totalEquity = totalEquity;
	}
	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@JsonManagedReference
	@OneToMany(mappedBy = "balanceSheet", cascade = CascadeType.ALL)
	public Set<CurrentAsset> getCurrentAssets() {
		return currentAssets;
	}
	public void setCurrentAssets(Set<CurrentAsset> currentAssets) {
		this.currentAssets = currentAssets;
	}
	
	@JsonManagedReference
	@OneToMany(mappedBy = "balanceSheet", cascade = CascadeType.ALL)
	public Set<NonCurrentAsset> getNonCurrentAssets() {
		return nonCurrentAssets;
	}
	public void setNonCurrentAssets(Set<NonCurrentAsset> nonCurrentAssets) {
		this.nonCurrentAssets = nonCurrentAssets;
	}
	
	@JsonManagedReference
	@OneToMany(mappedBy = "balanceSheet", cascade = CascadeType.ALL)
	public Set<CurrentLiability> getCurrentLiability() {
		return currentLiability;
	}
	public void setCurrentLiability(Set<CurrentLiability> currentLiability) {
		this.currentLiability = currentLiability;
	}
	
	@JsonManagedReference
	@OneToMany(mappedBy = "balanceSheet", cascade = CascadeType.ALL)
	public Set<NonCurrentLiability> getNonCurrentLiability() {
		return nonCurrentLiability;
	}
	public void setNonCurrentLiability(Set<NonCurrentLiability> nonCurrentLiability) {
		this.nonCurrentLiability = nonCurrentLiability;
	}
	
	
	
}
