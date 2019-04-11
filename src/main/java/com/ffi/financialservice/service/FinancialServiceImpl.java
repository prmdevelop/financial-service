package com.ffi.financialservice.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ffi.financialservice.dao.FinancialDao;
import com.ffi.financialservice.domain.BalanceSheet;
import com.ffi.financialservice.domain.CurrentAsset;
import com.ffi.financialservice.domain.CurrentLiability;
import com.ffi.financialservice.domain.DebtFin;
import com.ffi.financialservice.domain.DirectCost;
import com.ffi.financialservice.domain.Equity;
import com.ffi.financialservice.domain.Financial;
import com.ffi.financialservice.domain.IncomeStatement;
import com.ffi.financialservice.domain.IndirectCost;
import com.ffi.financialservice.domain.NonCurrentAsset;
import com.ffi.financialservice.domain.NonCurrentLiability;
import com.ffi.financialservice.domain.Period;
import com.ffi.financialservice.domain.PeriodType;
import com.ffi.financialservice.domain.Revenue;
import com.ffi.financialservice.domain.Source;
import com.ffi.financialservice.domain.Tax;
import com.ffi.financialservice.dto.FinancialDTO;
import com.ffi.financialservice.dto.PeriodRequest;
import com.ffi.financialservice.exception.ApplicationBusinessException;
import com.ffi.financialservice.handler.AppProperities;

@Service
public class FinancialServiceImpl implements FinancialService {

	private static final Logger logger = LogManager.getLogger(FinancialServiceImpl.class);

	@Autowired
	FinancialDao financialDao;

	@Autowired
	AppProperities appProperities;

	public Map<String, Map<String, Set<String>>> checkDataAvailability(String customerId)
			throws ApplicationBusinessException {
		logger.info("Start of FinancialServiceImpl.checkDataAvailability()");
		Map<String, Map<String, Set<String>>> data = new HashMap<>();
		Map<String, Set<String>> period = new HashMap<>();
		Set<String> periodType = null;
		try {
			List<Financial> financials = financialDao.getFinancialOfCustomer(UUID.fromString(customerId));
			for (Financial financial : financials) {
				if (!data.containsKey(financial.getSource().getSourceName())) {
					period = new HashMap<>();
					periodType = new HashSet<>();
					periodType.add(financial.getPeriod().getPeriodType().getPeriodName());
				} else {
					if (!period.containsKey(financial.getPeriod().getPeriodValue())) {
						periodType = new HashSet<>();
						periodType.add(financial.getPeriod().getPeriodType().getPeriodName());
					} else {
						periodType = period.get(financial.getPeriod().getPeriodValue());
						periodType.add(financial.getPeriod().getPeriodType().getPeriodName());
					}
				}
				period.put(financial.getPeriod().getPeriodValue(), periodType);
				data.put(financial.getSource().getSourceName(), period);
			}
		} catch (Exception e) {
			logger.error("Error in FinancialServiceImpl.checkDataAvailability()" + e.getCause());
			throw new ApplicationBusinessException(appProperities.getPropertyValue("error.retrieved.msg"));
		}
		logger.info("End of FinancialServiceImpl.checkDataAvailability()");
		return data;
	}

	@Override
	public String getFinancialData(String templateName, String companyId, String sourceName,
			List<PeriodRequest> periodRequest) throws ApplicationBusinessException {
		logger.info("Start of FinancialServiceImpl.getFinancialData()");
		String templateURL = "";
		List<FinancialDTO> financialDTOList = new ArrayList<>();
		try {
			Source source = financialDao.getSource(sourceName);
			for (PeriodRequest pRequest : periodRequest) {
				PeriodType periodType = financialDao.getPeriodType(pRequest.getType());
				Period period = financialDao.getPeriod(periodType.getId(), pRequest.getPeriod());
				Financial financial = financialDao.getFinancial(source.getId(), period.getId(),UUID.fromString(companyId));
				financialDTOList.addAll(getBalanceSheet(financial.getId(), period.getPeriodValue()));
				financialDTOList.addAll(getIncomeStatement(financial.getId(), period.getPeriodValue()));
			}
			templateURL = uploadDataToTemplate(templateName, financialDTOList);
		} catch (Exception e) {
			logger.info("Error in FinancialServiceImpl.getFinancialData()" + e.getStackTrace());
			throw new ApplicationBusinessException(appProperities.getPropertyValue("error.msg"));
		}
		logger.info("End of FinancialServiceImpl.getFinancialData()");
		return templateURL;
	}

	private List<FinancialDTO> getBalanceSheet(UUID financialId, String period) throws ApplicationBusinessException {
		logger.info("Start of FinancialServiceImpl.getBalanceSheet()");
		List<FinancialDTO> financialDataList = new ArrayList<>();
		try {
			BalanceSheet balanceSheet = financialDao.getBalanceSheetOfFinancial(financialId);
			List<CurrentAsset> currentAssets = financialDao.getCurrentAssetOfBalanceSheet(balanceSheet.getId());
			List<NonCurrentAsset> nonCurrentAssets = financialDao
					.getNonCurrentAssetOfBalanceSheet(balanceSheet.getId());
			List<CurrentLiability> currentLiabilities = financialDao
					.getCurrentLiabilityOfBalanceSheet(balanceSheet.getId());
			List<NonCurrentLiability> nonCurrentLiabilities = financialDao
					.getNonCurrentLiabilityOfBalanceSheet(balanceSheet.getId());
			List<Equity> equities = financialDao.getEquityOfBalanceSheet(balanceSheet.getId());
			
			if (!currentAssets.isEmpty()) {
				for (CurrentAsset currentAsset : currentAssets) {
					FinancialDTO financialDto = new FinancialDTO();
					financialDto.setYear(period);
					financialDto.setLineItem(currentAsset.getTemplateLabelId().toString());
					financialDto.setLineItemValue(currentAsset.getValue());
					financialDataList.add(financialDto);
				}
			}

			if (!nonCurrentAssets.isEmpty()) {
				for (NonCurrentAsset nonCurrentAsset : nonCurrentAssets) {
					FinancialDTO financialDto = new FinancialDTO();
					financialDto.setYear(period);
					financialDto.setLineItem(nonCurrentAsset.getTemplateLabelId().toString());
					financialDto.setLineItemValue(nonCurrentAsset.getValue());
					financialDataList.add(financialDto);
				}
			}

			if (!currentLiabilities.isEmpty()) {
				for (CurrentLiability currentLiabilty : currentLiabilities) {
					FinancialDTO financialDto = new FinancialDTO();
					financialDto.setYear(period);
					financialDto.setLineItem(currentLiabilty.getTemplateLabelId().toString());
					financialDto.setLineItemValue(currentLiabilty.getValue());
					financialDataList.add(financialDto);
				}
			}

			if (!nonCurrentLiabilities.isEmpty()) {
				for (NonCurrentLiability currentLiability : nonCurrentLiabilities) {
					FinancialDTO financialDto = new FinancialDTO();
					financialDto.setYear(period);
					financialDto.setLineItem(currentLiability.getTemplateLabelId().toString());
					financialDto.setLineItemValue(currentLiability.getValue());
					financialDataList.add(financialDto);
				}
			}

			if (!equities.isEmpty()) {
				for (Equity equity : equities) {
					FinancialDTO financialDto = new FinancialDTO();
					financialDto.setYear(period);
					financialDto.setLineItem(equity.getTemplateLabelId().toString());
					financialDto.setLineItemValue(equity.getValue());
					financialDataList.add(financialDto);
				}
			}
		} catch (ApplicationBusinessException e) {
			logger.info("Error in FinancialServiceImpl.getBalanceSheet()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("error.msg"));
		}
		logger.info("End of FinancialServiceImpl.getBalanceSheet()");
		return financialDataList;
	}
	
	private List<FinancialDTO> getIncomeStatement(UUID financialId, String period) throws ApplicationBusinessException {
		logger.info("Start of FinancialServiceImpl.getIncomeStatement()");
		List<FinancialDTO> financialDataList = new ArrayList<>();
		try {
			IncomeStatement incomeStatement = financialDao.getIncomeStatementOfFinancial(financialId);
			List<Revenue> revenues = financialDao.getRevenueOfIncomeStatement(incomeStatement.getId());
			List<DirectCost> directCosts = financialDao.getDirectCostOfIncomeStatement(incomeStatement.getId());
			List<IndirectCost> indirectCosts = financialDao.getIndirectCostOfIncomeStatement(incomeStatement.getId());
			List<DebtFin> debtFins = financialDao.getDebtFinOfIncomeStatement(incomeStatement.getId());
			List<Tax> taxes = financialDao.getTaxOfIncomeStatement(incomeStatement.getId());

			if (!revenues.isEmpty()) {
				for (Revenue revenue : revenues) {
					FinancialDTO financialDto = new FinancialDTO();
					financialDto.setYear(period);
					financialDto.setLineItem(revenue.getTemplateLabelId().toString());
					financialDto.setLineItemValue(revenue.getValue());
					financialDataList.add(financialDto);
				}
			}

			if (!directCosts.isEmpty()) {
				for (DirectCost directCost : directCosts) {
					FinancialDTO financialDto = new FinancialDTO();
					financialDto.setYear(period);
					financialDto.setLineItem(directCost.getTemplateLabelId().toString());
					financialDto.setLineItemValue(directCost.getValue());
					financialDataList.add(financialDto);
				}
			}

			if (!indirectCosts.isEmpty()) {
				for (IndirectCost indirectCost : indirectCosts) {
					FinancialDTO financialDto = new FinancialDTO();
					financialDto.setYear(period);
					financialDto.setLineItem(indirectCost.getTemplateLabelId().toString());
					financialDto.setLineItemValue(indirectCost.getValue());
					financialDataList.add(financialDto);
				}
			}

			if (!debtFins.isEmpty()) {
				for (DebtFin debtFin : debtFins) {
					FinancialDTO financialDto = new FinancialDTO();
					financialDto.setYear(period);
					financialDto.setLineItem(debtFin.getTemplateLabelId().toString());
					financialDto.setLineItemValue(debtFin.getValue());
					financialDataList.add(financialDto);
				}
			}

			if (!taxes.isEmpty()) {
				for (Tax tax : taxes) {
					FinancialDTO financialDto = new FinancialDTO();
					financialDto.setYear(period);
					financialDto.setLineItem(tax.getTemplateLabelId().toString());
					financialDto.setLineItemValue(tax.getValue());
					financialDataList.add(financialDto);
				}
			}
		} catch (ApplicationBusinessException e) {
			logger.info("Error in FinancialServiceImpl.getIncomeStatement()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("error.msg"));
		}
		logger.info("End of FinancialServiceImpl.getBalancegetIncomeStatementSheet()");
		return financialDataList;
	}

	private String uploadDataToTemplate(String templateName, List<FinancialDTO> financialDTOList)
			throws ApplicationBusinessException {
		logger.info("Start of FinancialServiceImpl.uploadDataToTemplate()");
		String webUrl = "";
		try {
			URL url = new URL(appProperities.getPropertyValue("template.upload.rest.url"));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestMethod("POST");
			JSONObject requestJson = new JSONObject();
			requestJson.put("templateName", templateName);
			JSONArray jsonArray = new JSONArray();
			for (int i = 0; i < financialDTOList.size(); i++) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("year", financialDTOList.get(i).getYear());
				jsonObject.put("lineItem", financialDTOList.get(i).getLineItem());
				jsonObject.put("lineItemValue", financialDTOList.get(i).getLineItemValue());
				jsonArray.put(i, jsonObject);
			}
			requestJson.put("data", jsonArray);
			byte[] data = requestJson.toString().getBytes("UTF-8");
			OutputStream outStream = conn.getOutputStream();
			outStream.write(data);
			outStream.flush();
			outStream.close();
			if (conn.getResponseCode() != 200) {
				throw new ApplicationBusinessException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			StringBuilder sb = new StringBuilder();
			String inputLine = "";
			while ((inputLine = br.readLine()) != null) {
				sb.append(inputLine);
			}
			br.close();
			conn.disconnect();
			JSONObject jsonObject = new JSONObject(sb.toString()).getJSONObject("data")
					.getJSONObject("templateResponse");
			webUrl = jsonObject.getString("url");
		} catch (Exception e) {
			logger.info("Error in FinancialServiceImpl.uploadDataToTemplate()" + e.getStackTrace());
			throw new ApplicationBusinessException(appProperities.getPropertyValue("error.msg"));
		}
		logger.info("End of FinancialServiceImpl.uploadDataToTemplate()");
		return webUrl;
	}
}
