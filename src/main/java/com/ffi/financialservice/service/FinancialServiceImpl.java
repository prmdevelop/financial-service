package com.ffi.financialservice.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
import com.ffi.financialservice.domain.Equity;
import com.ffi.financialservice.domain.Financial;
import com.ffi.financialservice.domain.NonCurrentAsset;
import com.ffi.financialservice.domain.NonCurrentLiability;
import com.ffi.financialservice.domain.Period;
import com.ffi.financialservice.domain.PeriodType;
import com.ffi.financialservice.domain.Source;
import com.ffi.financialservice.endpoint.PeriodRequest;
import com.ffi.financialservice.exception.ApplicationBusinessException;
import com.ffi.financialservice.handler.AppProperities;
import com.ffi.financialservice.handler.FinancialDTO;

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
		Map<String, Map<String, Double>> financialData = new HashMap<>();
		try {
			Source source = financialDao.getSource(sourceName);
			for (PeriodRequest pRequest : periodRequest) {
				PeriodType periodType = financialDao.getPeriodType(pRequest.getType());
				Period period = financialDao.getPeriod(periodType.getId(), pRequest.getPeriod());
				Financial financial = financialDao.getFinancial(source.getId(), period.getId(),
						UUID.fromString(companyId));
				financialDTOList.addAll(getBalanceSheet(financial.getId(), period.getPeriodValue()));
			}
			populateLineItemValueFromTemplate(financialDTOList, templateName);
			for (int i = 0; i < financialDTOList.size(); i++) {
				Map<String, Double> lineItemValue;
				if (financialData.containsKey(financialDTOList.get(i).getLineItem())) {
					lineItemValue = financialData.get(financialDTOList.get(i).getLineItem());
					lineItemValue.put(financialDTOList.get(i).getYear(), financialDTOList.get(i).getLineItemValue());
				} else {
					lineItemValue = new HashMap<>();
					lineItemValue.put(financialDTOList.get(i).getYear(), financialDTOList.get(i).getLineItemValue());
				}
				financialData.put(financialDTOList.get(i).getLineItem(), lineItemValue);
			}
			templateURL = uploadDataToTemplate(templateName, financialData);
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

			for (CurrentAsset currentAsset : currentAssets) {
				FinancialDTO financialDto = new FinancialDTO();
				financialDto.setYear(period);
				financialDto.setLineItem(currentAsset.getTemplateLabelId().toString());
				financialDto.setLineItemValue(currentAsset.getValue());
				financialDataList.add(financialDto);
			}

			for (NonCurrentAsset nonCurrentAsset : nonCurrentAssets) {
				FinancialDTO financialDto = new FinancialDTO();
				financialDto.setYear(period);
				financialDto.setLineItem(nonCurrentAsset.getTemplateLabelId().toString());
				financialDto.setLineItemValue(nonCurrentAsset.getValue());
				financialDataList.add(financialDto);
			}

			for (CurrentLiability currentLiabilty : currentLiabilities) {
				FinancialDTO financialDto = new FinancialDTO();
				financialDto.setYear(period);
				financialDto.setLineItem(currentLiabilty.getTemplateLabelId().toString());
				financialDto.setLineItemValue(currentLiabilty.getValue());
				financialDataList.add(financialDto);
			}

			for (NonCurrentLiability currentLiability : nonCurrentLiabilities) {
				FinancialDTO financialDto = new FinancialDTO();
				financialDto.setYear(period);
				financialDto.setLineItem(currentLiability.getTemplateLabelId().toString());
				financialDto.setLineItemValue(currentLiability.getValue());
				financialDataList.add(financialDto);
			}

			for (Equity equity : equities) {
				FinancialDTO financialDto = new FinancialDTO();
				financialDto.setYear(period);
				financialDto.setLineItem(equity.getTemplateLabelId().toString());
				financialDto.setLineItemValue(equity.getValue());
				financialDataList.add(financialDto);
			}
		} catch (ApplicationBusinessException e) {
			logger.info("Error in FinancialServiceImpl.getBalanceSheet()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("error.msg"));
		}
		logger.info("End of FinancialServiceImpl.getBalanceSheet()");
		return financialDataList;
	}

	private void populateLineItemValueFromTemplate(List<FinancialDTO> financialDataList, String templateName)
			throws ApplicationBusinessException {
		logger.info("Start of FinancialServiceImpl.populateLineItemValueFromTemplate()");
		try {
			URL url = new URL(
					appProperities.getPropertyValue("template.rest.url") + URLEncoder.encode(templateName, "UTF-8"));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
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
			JSONArray jsonArray = new JSONObject(sb.toString()).getJSONObject("data").getJSONObject("templateResponse")
					.getJSONArray("template");
			Map<String, String> templateLineItemMap = new HashMap<>();
			if (jsonArray != null) {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					templateLineItemMap.put(jsonObject.getString("templateLabelId"),
							jsonObject.getString("templateLineItem"));
				}
			}

			for (int i = 0; i < financialDataList.size(); i++) {
				String lineItemId = financialDataList.get(i).getLineItem();
				if (templateLineItemMap.containsKey(lineItemId)) {
					financialDataList.get(i).setLineItem(templateLineItemMap.get(lineItemId));
				}
			}
		} catch (Exception e) {
			logger.info("Error in FinancialServiceImpl.populateLineItemValueFromTemplate()" + e.getStackTrace());
			throw new ApplicationBusinessException(appProperities.getPropertyValue("error.msg"));
		}
		logger.info("End of FinancialServiceImpl.populateLineItemValueFromTemplate()");
	}

	private String uploadDataToTemplate(String templateName, Map<String, Map<String, Double>> financialData)
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
			requestJson.put("data", financialData);
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
			JSONObject jsonObject = new JSONObject(sb.toString()).getJSONObject("data").getJSONObject("templateResponse");
			webUrl = jsonObject.getString("url");
		} catch (Exception e) {
			logger.info("Error in FinancialServiceImpl.uploadDataToTemplate()" + e.getStackTrace());
			throw new ApplicationBusinessException(appProperities.getPropertyValue("error.msg"));
		}
		logger.info("End of FinancialServiceImpl.uploadDataToTemplate()");
		return webUrl;
	}
}
