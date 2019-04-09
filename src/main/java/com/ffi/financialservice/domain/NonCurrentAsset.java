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
@Table(name = "NON_CURRENT_ASSETS")
public class NonCurrentAsset {

	private UUID id;
	private BalanceSheet balanceSheet;
	private String reportedLabel;
	private UUID templateLabelId;
	private Double value;
	private int version;
	private Date createdDate;
	private Date lastModifiedDate;
	private boolean isExpired;
	
	@Id
	@Type(type = "org.hibernate.type.UUIDCharType")
	@Column(name = "Id")
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "BALANCE_SHEET_ID")
	public BalanceSheet getBalanceSheet() {
		return balanceSheet;
	}
	public void setBalanceSheet(BalanceSheet balanceSheet) {
		this.balanceSheet = balanceSheet;
	}
	
	@Column(name = "REPORTED_LABEL")
	public String getReportedLabel() {
		return reportedLabel;
	}
	public void setReportedLabel(String reportedLabel) {
		this.reportedLabel = reportedLabel;
	}
	
	@Column(name = "TEMP_LABEL_ID")
	public UUID getTemplateLabelId() {
		return templateLabelId;
	}
	public void setTemplateLabelId(UUID templateLabelId) {
		this.templateLabelId = templateLabelId;
	}
	
	@Column(name = "VALUE")
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	
	@Column(name = "VERSION")
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name = "LAST_MODIFIED_DATE")
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	@Column(name = "ISEXPIRED")
	public boolean isExpired() {
		return isExpired;
	}
	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}
}
