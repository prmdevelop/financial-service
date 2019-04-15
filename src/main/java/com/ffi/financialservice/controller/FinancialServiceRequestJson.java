package com.ffi.financialservice.controller;

import java.util.List;

import com.ffi.financialservice.dto.PeriodRequest;

public class FinancialServiceRequestJson {

	private String templateName;

	private String companyId;

	private String sourceName;

	private List<PeriodRequest> periodRequest;
	
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public List<PeriodRequest> getPeriodRequest() {
		return periodRequest;
	}

	public void setPeriodRequest(List<PeriodRequest> periodRequest) {
		this.periodRequest = periodRequest;
	}
	
	@Override
	public String toString() {
		return "FinancialServiceRequestJson [templateName=" + templateName + ", companyId=" + companyId
				+ ", sourceName=" + sourceName + ", periodRequest=" + periodRequest + "]";
	}
}
