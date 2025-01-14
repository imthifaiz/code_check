package com.track.service;

public interface ReportService {
	
	public Double GrossProfit(String plant, String fromDate,String  toDate) throws Exception;
	
	public Double NetProfit(String plant, String fromDate,String  toDate) throws Exception;
	
	public Double totalSalesAmount(String plant, String fromDate, String toDate) throws Exception;
	
	public Double totalCostOfSalesAmount(String plant, String fromDate, String toDate) throws Exception;
	
	public Double totalIncomeAmount(String plant, String fromDate, String toDate) throws Exception;
	
	public Double totalExpenseAmount(String plant, String fromDate, String toDate) throws Exception;
	
	public Double totalAsset(String plant, String fromDate, String toDate) throws Exception;
	
	public Double totalLiability(String plant, String fromDate, String toDate) throws Exception;
	
	public Double totalEquity(String plant, String fromDate, String toDate) throws Exception;
	
	public Double totalCashInHand(String plant, String fromDate, String toDate) throws Exception;
	
	public Double totalCashAtBank(String plant, String fromDate, String toDate) throws Exception;
	
	public Double totalCashInHandAll(String plant) throws Exception;
	
	public Double totalCashAtBankAll(String plant) throws Exception;
}
