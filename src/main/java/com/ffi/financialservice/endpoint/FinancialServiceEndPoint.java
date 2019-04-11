package com.ffi.financialservice.endpoint;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ffi.financialservice.handler.AppProperities;
import com.ffi.financialservice.service.FinancialService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "FinancialService End Point")
@RestController
@RequestMapping("/rest/FinancialService/")
public class FinancialServiceEndPoint {

	private static final Logger logger = LogManager.getLogger(FinancialServiceEndPoint.class);

	@Autowired
	FinancialService financialService;

	@Autowired
	AppProperities appProperities;

	@ApiOperation(value = "Data Availabilty of Financial Company")
	@GetMapping(value = "/getDataAvailability/{companyId}", produces = "application/json")
	@ResponseBody
	public FinancialServiceResponseJson<FinancialServiceResponseObject> getDataAvailability(
			@PathVariable final String companyId) {
		logger.info("Start of FinancialServiceEndPoint.getDataAvailability()");
		FinancialServiceResponseJson<FinancialServiceResponseObject> responseJson = new FinancialServiceResponseJson<>();
		try {
			FinancialServiceResponseObject responseObject = new FinancialServiceResponseObject();
			Map<String, Object> data = new HashMap<>();
			Map<String, Map<String, Set<String>>> financialSource = financialService.checkDataAvailability(companyId);
			data.put("data available", financialSource);
			responseObject.setFinancialServiceResponse(data);
			responseJson.setStatusMessage(appProperities.getPropertyValue("success.retrieved.msg"));
			responseJson.setStatusMessage(appProperities.getPropertyValue("success.retrieved.msg"));
			responseJson.setStatusCode(appProperities.getPropertyValue("success.code"));
			responseJson.setData(responseObject);
		} catch (Exception e) {
			logger.error("Exception in FinancialServiceEndPoint.getDataAvailability()");
			responseJson.setErrorMessage(appProperities.getPropertyValue("error.retrieved.msg"));
			responseJson.setErrorCode(appProperities.getPropertyValue("error.code"));
		}
		logger.info("End of FinancialServiceEndPoint.getDataAvailability()");
		return responseJson;
	}

	@ApiOperation(value = "Upload Template with Financial Data")
	@PostMapping(value = "/uploadTemplate", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public FinancialServiceResponseJson<FinancialServiceResponseObject> getFinancialData(
			@RequestBody FinancialServiceRequestJson fRequestJson) {
		logger.info("Start of FinancialServiceEndPoint.getFinancialData()");
		FinancialServiceResponseJson<FinancialServiceResponseObject> responseJson = new FinancialServiceResponseJson<>();
		try {
			FinancialServiceResponseObject responseObject = new FinancialServiceResponseObject();
			Map<String, Object> data = new HashMap<>();
			String templateURL = financialService.getFinancialData(fRequestJson.getTemplateName(),
					fRequestJson.getCompanyId(), fRequestJson.getSourceName(), fRequestJson.getPeriodRequest());
			data.put("templateURL", templateURL);
			responseObject.setFinancialServiceResponse(data);
			responseJson.setStatusMessage(appProperities.getPropertyValue("success.retrieved.msg"));
			responseJson.setStatusMessage(appProperities.getPropertyValue("success.retrieved.msg"));
			responseJson.setStatusCode(appProperities.getPropertyValue("success.code"));
			responseJson.setData(responseObject);
		} catch (Exception e) {
			logger.error("Exception in FinancialServiceEndPoint.getFinancialData()");
			responseJson.setErrorMessage(appProperities.getPropertyValue("error.retrieved.msg"));
			responseJson.setErrorCode(appProperities.getPropertyValue("error.code"));
		}
		logger.info("End of FinancialServiceEndPoint.getFinancialData()");
		return responseJson;
	}
}
