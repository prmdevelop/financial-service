package com.ffi.financialservice.controller;

import java.util.HashMap;
import java.util.List;
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

import com.ffi.financialservice.handler.FinancialServiceConfiguration;
import com.ffi.financialservice.service.FinancialService;
import com.ffi.financialservice.vo.FinancialVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "FinancialService End Point")
@RestController
@RequestMapping("/financial/service")
public class FinancialServiceController {

	private static final Logger logger = LogManager.getLogger(FinancialServiceController.class);

	@Autowired
	FinancialService financialService;

	@Autowired
	FinancialServiceConfiguration configuration;

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
			responseJson.setStatusMessage(configuration.getSuccess().getRetrieve());
			responseJson.setStatusCode(configuration.getSuccess().getCode());
			responseJson.setData(responseObject);
		} catch (Exception e) {
			logger.error("Exception in FinancialServiceEndPoint.getDataAvailability()");
			responseJson.setErrorMessage(configuration.getError().getRetrieve());
			responseJson.setErrorCode(configuration.getError().getCode());
		}
		logger.info("End of FinancialServiceEndPoint.getDataAvailability()");
		return responseJson;
	}

	@ApiOperation(value = "Load Financial Data")
	@PostMapping(value = "/loadData", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public FinancialServiceResponseJson<FinancialServiceResponseObject> loadData(
			@RequestBody FinancialServiceRequestJson fRequestJson) {
		logger.info("Start of FinancialServiceEndPoint.loadData()");
		FinancialServiceResponseJson<FinancialServiceResponseObject> responseJson = new FinancialServiceResponseJson<>();
		try {
			FinancialServiceResponseObject responseObject = new FinancialServiceResponseObject();
			Map<String, Object> data = new HashMap<>();
			List<FinancialVO> financialDTO = financialService.getFinancialData(
					fRequestJson.getCompanyId(), fRequestJson.getSourceName(), fRequestJson.getPeriodRequest());
			data.put("financial", financialDTO);
			responseObject.setFinancialServiceResponse(data);
			responseJson.setStatusMessage(configuration.getSuccess().getRetrieve());
			responseJson.setStatusCode(configuration.getSuccess().getCode());
			responseJson.setData(responseObject);
		} catch (Exception e) {
			logger.error("Exception in FinancialServiceEndPoint.getFinancialData()");
			responseJson.setErrorMessage(configuration.getError().getRetrieve());
			responseJson.setErrorCode(configuration.getError().getCode());
		}
		logger.info("End of FinancialServiceEndPoint.loadData()");
		return responseJson;
	}
}
