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
@Table(name = "PERIOD")
public class Period {
	
	private UUID id;
	private PeriodType periodType;
	private String periodValue;
	private Date createdDate;
	private Date lastModifiedDate;
	
	@Id
	@Type(type = "org.hibernate.type.UUIDCharType")
	@Column(name = "Id")
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name = "periodType")
	public PeriodType getPeriodType() {
		return periodType;
	}
	public void setPeriodType(PeriodType periodType) {
		this.periodType = periodType;
	}
	
	@Column(name = "PERIOD_VALUE")
	public String getPeriodValue() {
		return periodValue;
	}
	public void setPeriodValue(String periodValue) {
		this.periodValue = periodValue;
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
}
