package com.ffi.financialservice.endpoint;

public class PeriodRequest {
	
	private String period;
	
	private String type;

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "PeriodRequest [period=" + period + ", type=" + type + "]";
	}
	
	
	
}
