package com.track.serviceImplementation;

import java.util.List;

import com.track.db.object.JournalDetail;
import com.track.service.JournalService;
import com.track.service.ReportService;

public class ReportServiceImpl implements ReportService{
	
//	private JournalDAO journalDAO=new JournalDAO();
	private JournalService journalService=new JournalEntry();
//	private LedgerService ledgerSerice=new LedgerServiceImpl();

	@Override
	public Double GrossProfit(String plant, String fromDate, String toDate) throws Exception {
		Double grossProfit=totalSalesAmount(plant, fromDate, toDate)-totalCostOfSalesAmount(plant, fromDate, toDate);
		return grossProfit;
	}
	
	public Double totalSalesAmount(String plant, String fromDate, String toDate) throws Exception
	{
		String accounttypes="(8)";
		List<JournalDetail> journalDetails=journalService.getJournalDetailsByAccountTypeAndDATE(plant, fromDate, toDate, accounttypes);
		Double debit=0.00;
		Double credit=0.00;
		for(JournalDetail journalDet:journalDetails)
		{
			debit+=journalDet.getDEBITS();
			credit+=journalDet.getCREDITS();
		}
		Double totalSalesAmount=credit-debit;
		return totalSalesAmount;
	}
	
	public Double totalCostOfSalesAmount(String plant, String fromDate, String toDate) throws Exception
	{
		String accounttypes="(10)";
		List<JournalDetail> journalDetails=journalService.getJournalDetailsByAccountTypeAndDATE(plant, fromDate, toDate, accounttypes);
		Double debit=0.00;
		Double credit=0.00;
		for(JournalDetail journalDet:journalDetails)
		{
			debit+=journalDet.getDEBITS();
			credit+=journalDet.getCREDITS();
		}
		Double totalCostOfSalesAmount=debit-credit;
		return totalCostOfSalesAmount;
	}

	@Override
	public Double NetProfit(String plant, String fromDate, String toDate) throws Exception {
		Double netProfit=GrossProfit(plant, fromDate, toDate)+((totalIncomeAmount(plant, fromDate, toDate)-totalExpenseAmount(plant, fromDate, toDate)));
		return netProfit;
	}

	public Double totalIncomeAmount(String plant, String fromDate, String toDate) throws Exception
	{
		String accounttypes="(9)";
		List<JournalDetail> journalDetails=journalService.getJournalDetailsByAccountTypeAndDATE(plant, fromDate, toDate, accounttypes);
		Double debit=0.00;
		Double credit=0.00;
		for(JournalDetail journalDet:journalDetails)
		{
			debit+=journalDet.getDEBITS();
			credit+=journalDet.getCREDITS();
		}
		Double totalIncomeAmount=credit-debit;
		return totalIncomeAmount;
	}

	public Double totalExpenseAmount(String plant, String fromDate, String toDate) throws Exception
	{
		String accounttypes="(11)";
		List<JournalDetail> journalDetails=journalService.getJournalDetailsByAccountTypeAndDATE(plant, fromDate, toDate, accounttypes);
		Double debit=0.00;
		Double credit=0.00;
		for(JournalDetail journalDet:journalDetails)
		{
			debit+=journalDet.getDEBITS();
			credit+=journalDet.getCREDITS();
		}
		Double totalExpenseAmount=debit-credit;
		return totalExpenseAmount;
	}

	@Override
	public Double totalAsset(String plant, String fromDate, String toDate) throws Exception {
		String accounttypes="(1,2,3)";
		List<JournalDetail> journalDetails=journalService.getJournalDetailsByAccountTypeAndDATE(plant, fromDate, toDate, accounttypes);
		Double debit=0.00;
		Double credit=0.00;
		for(JournalDetail journalDet:journalDetails)
		{
			debit+=journalDet.getDEBITS();
			credit+=journalDet.getCREDITS();
		}
		Double totalAssetAmount=debit-credit;
		return totalAssetAmount;
	}

	@Override
	public Double totalLiability(String plant, String fromDate, String toDate) throws Exception {
		String accounttypes="(4,5,6)";
		List<JournalDetail> journalDetails=journalService.getJournalDetailsByAccountTypeAndDATE(plant, fromDate, toDate, accounttypes);
		Double debit=0.00;
		Double credit=0.00;
		for(JournalDetail journalDet:journalDetails)
		{
			debit+=journalDet.getDEBITS();
			credit+=journalDet.getCREDITS();
		}
		Double totalLiabilityAmount=credit-debit;
		return totalLiabilityAmount;
	}

	@Override
	public Double totalEquity(String plant, String fromDate, String toDate) throws Exception {
		String accounttypes="(7,12)";
		List<JournalDetail> journalDetails=journalService.getJournalDetailsByAccountTypeAndDATE(plant, fromDate, toDate, accounttypes);
		Double debit=0.00;
		Double credit=0.00;
		for(JournalDetail journalDet:journalDetails)
		{
			debit+=journalDet.getDEBITS();
			credit+=journalDet.getCREDITS();
		}
		Double totalEquityAmount=NetProfit(plant, fromDate, toDate)+(credit-debit);
		return totalEquityAmount;
	}

	@Override
	public Double totalCashInHand(String plant, String fromDate, String toDate) throws Exception {
		String ChartAccountId="16";
		List<JournalDetail> journalDetails=journalService.getJournalDetailsByChartOfAccountIdAndDATE(plant, fromDate, toDate, ChartAccountId);
		Double debit=0.00;
		Double credit=0.00;
		for(JournalDetail journalDet:journalDetails)
		{
			debit+=journalDet.getDEBITS();
			credit+=journalDet.getCREDITS();
		}
		Double totalCashInHand=credit-debit;
		return totalCashInHand;
	}

	@Override
	public Double totalCashAtBank(String plant, String fromDate, String toDate) throws Exception {
		String accountdetailtypes="(8)";
		List<JournalDetail> journalDetails=journalService.getJournalDetailsByAccountDetailTypeAndDATE(plant, fromDate, toDate, accountdetailtypes);
		Double debit=0.00;
		Double credit=0.00;
		for(JournalDetail journalDet:journalDetails)
		{
			debit+=journalDet.getDEBITS();
			credit+=journalDet.getCREDITS();
		}
		Double totalCashAtBank=debit-credit;
		return totalCashAtBank;
	}

	@Override
	public Double totalCashInHandAll(String plant) throws Exception {
		String ChartAccountId="16";
		List<JournalDetail> journalDetails=journalService.getJournalDetailsByChartOfAccountIdAndAll(plant, ChartAccountId);
		Double debit=0.00;
		Double credit=0.00;
		for(JournalDetail journalDet:journalDetails)
		{
			debit+=journalDet.getDEBITS();
			credit+=journalDet.getCREDITS();
		}
		//Double totalCashInHand=credit-debit;
		Double totalCashInHand=debit-credit;
		return totalCashInHand;
	}

	@Override
	public Double totalCashAtBankAll(String plant) throws Exception {
		String accountdetailtypes="(8)";
		List<JournalDetail> journalDetails=journalService.getJournalDetailsByAccountDetailTypeAndAll(plant,accountdetailtypes);
		Double debit=0.00;
		Double credit=0.00;
		for(JournalDetail journalDet:journalDetails)
		{
			debit+=journalDet.getDEBITS();
			credit+=journalDet.getCREDITS();
		}
		Double totalCashAtBank=debit-credit;
		return totalCashAtBank;
	}
	
	
	

	


}
