package com.ffi.financialservice.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import com.ffi.financialservice.domain.Financial;
import com.ffi.financialservice.domain.NonCurrentAsset;
import com.ffi.financialservice.domain.Period;
import com.ffi.financialservice.domain.PeriodType;
import com.ffi.financialservice.domain.Source;
import com.ffi.financialservice.endpoint.PeriodRequest;
import com.ffi.financialservice.exception.ApplicationBusinessException;
import com.ffi.financialservice.handler.AppProperities;
import com.ffi.financialservice.vo.FinancialDataVO;

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
	public List<FinancialDataVO> getFinancialData(String templateName, String companyId, String sourceName,
			List<PeriodRequest> periodRequest) throws ApplicationBusinessException {
		logger.info("Start of FinancialServiceImpl.getFinancialData()");
		List<FinancialDataVO> financialDataVO = new ArrayList<>();
		try {
			Source source = financialDao.getSource(sourceName);
			for (PeriodRequest pRequest : periodRequest) {
				PeriodType periodType = financialDao.getPeriodType(pRequest.getType());
				Period period = financialDao.getPeriod(periodType.getId(), pRequest.getPeriod());
				Financial financial = financialDao.getFinancial(source.getId(), period.getId(),
						UUID.fromString(companyId));
				financialDataVO.addAll(getBalanceSheet(financial.getId(), period.getPeriodValue()));
			}
			populateLineItemValueFromTemplate(financialDataVO, templateName);
			Collections.sort(financialDataVO, Collections.reverseOrder());
		} catch (Exception e) {
			logger.info("Error in FinancialServiceImpl.getFinancialData()" + e.getStackTrace());
			throw new ApplicationBusinessException(appProperities.getPropertyValue("error.msg"));
		}
		logger.info("End of FinancialServiceImpl.getFinancialData()");
		return financialDataVO;
	}

	private List<FinancialDataVO> getBalanceSheet(UUID financialId, String period) throws ApplicationBusinessException {
		logger.info("Start of FinancialServiceImpl.getBalanceSheet()");
		List<FinancialDataVO> financialDataVO = new ArrayList<>();
		try {
			BalanceSheet balanceSheet = financialDao.getBalanceSheetOfFinancial(financialId);
			List<CurrentAsset> currentAssets = financialDao.getCurrentAssetOfBalanceSheet(balanceSheet.getId());
			List<NonCurrentAsset> nonCurrentAssets = financialDao
					.getNonCurrentAssetOfBalanceSheet(balanceSheet.getId());
			for (CurrentAsset currentAsset : currentAssets) {
				FinancialDataVO financialData = new FinancialDataVO();
				financialData.setYear(period);
				financialData.setLineItem(currentAsset.getTemplateLabelId().toString());
				financialData.setLineItemValue(currentAsset.getValue());
				financialDataVO.add(financialData);
			}

			for (NonCurrentAsset nonCurrentAsset : nonCurrentAssets) {
				FinancialDataVO financialData = new FinancialDataVO();
				financialData.setYear(period);
				financialData.setLineItem(nonCurrentAsset.getTemplateLabelId().toString());
				financialData.setLineItemValue(nonCurrentAsset.getValue());
				financialDataVO.add(financialData);
			}
		} catch (ApplicationBusinessException e) {
			logger.info("Error in FinancialServiceImpl.getBalanceSheet()");
			throw new ApplicationBusinessException(appProperities.getPropertyValue("error.msg"));
		}
		logger.info("End of FinancialServiceImpl.getBalanceSheet()");
		return financialDataVO;
	}

	private List<FinancialDataVO> populateLineItemValueFromTemplate(List<FinancialDataVO> financialDataVO,
			String templateName) throws ApplicationBusinessException {
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
			
			List<FinancialDataVO> dockingFinancialDataVO = financialDataVO;
			
			Iterator<FinancialDataVO> itr = financialDataVO.iterator();
			while(itr.hasNext()){
				if(templateLineItemMap.containsKey(itr.next().getLineItem())){
					itr.next().setLineItem(templateLineItemMap.get(itr.next().getLineItem()));
				}
			}
			
			for(FinancialDataVO fv:financialDataVO){
				System.out.println("FV ="+fv);
			}
		} catch (Exception e) {
			logger.info("Error in FinancialServiceImpl.populateLineItemValueFromTemplate()" + e.getStackTrace());
			throw new ApplicationBusinessException(appProperities.getPropertyValue("error.msg"));
		}
		logger.info("End of FinancialServiceImpl.populateLineItemValueFromTemplate()");
		return null;
	}
}
