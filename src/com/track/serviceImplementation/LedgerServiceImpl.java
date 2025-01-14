package com.track.serviceImplementation;

import java.util.ArrayList;
import java.util.List;

import com.track.dao.JournalDAO;
import com.track.db.object.LedgerDetails;
import com.track.db.object.LedgerDetailsWithId;
import com.track.service.LedgerService;

public class LedgerServiceImpl implements LedgerService{
	
	private JournalDAO journalDAO=new JournalDAO();

	@Override
	public List<LedgerDetails> getLedgerDetailsByAccount(String plant, String account, String fromDate, String toDate)
			throws Exception {
		account=account.trim();
		List<LedgerDetails> ledgerDetails=journalDAO.getLedgerDetailsByAccount(plant, account, fromDate, toDate);
		return ledgerDetails;
	}
	

	@Override
	public Double openingBalance(String plant,String account,String fromDate,String toDate) throws Exception
	{
		List<LedgerDetails> ledgerDetails=new ArrayList<LedgerDetails>();
		ledgerDetails=getLedgerDetailsByAccount(plant, account, fromDate, toDate);
		Double debit=0.00;
		Double credit=0.00;
		for(LedgerDetails ledgerDet:ledgerDetails)
		{
			debit+=ledgerDet.getDEBIT();
			credit+=ledgerDet.getCREDIT();
		}
		Double openingBalance=debit-credit;	
		//openingBalance=Math.abs(openingBalance);
		return openingBalance;
	}



	@Override
	public List<LedgerDetails> getLedgerDetailsByDate(String plant, String fromDate, String toDate) throws Exception {
		List<LedgerDetails> ledgerDetails=journalDAO.getLedgerDetailsByDate(plant, fromDate, toDate);
		return ledgerDetails;
	}
		
	@Override
	public List<LedgerDetails> getJournalLedgerDetailsByDate(String plant, String fromDate, String toDate,String limit) throws Exception {
		List<LedgerDetails> ledgerDetails=journalDAO.getJournalLedgerDetailsByDate(plant, fromDate, toDate,limit);
		return ledgerDetails;
	}
	
	@Override
	public List<LedgerDetails> getGeneralLedgerDetailsByDate(String plant, String fromDate, String toDate,String Search) throws Exception {
		List<LedgerDetails> ledgerDetails=journalDAO.getGeneralLedgerDetailsByDate(plant, fromDate, toDate,Search);
		return ledgerDetails;
	}
	
	@Override
	public List<LedgerDetails> getLedgerDetailsByTransactionId(String plant, String transactionId) throws Exception {
		List<LedgerDetails> ledgerDetails=journalDAO.getLedgerDetailsByTransactionId(plant, transactionId);
		return ledgerDetails;
	}
	
	@Override
	public List<LedgerDetails> getLedgerDetailsByJournalId(String plant, String journalId) throws Exception {
		return journalDAO.getLedgerDetailsByJournalId(plant, journalId);
	}
	
	@Override
	public List<LedgerDetailsWithId> getGeneralLedgerIdDetailsByDate(String plant, String fromDate, String toDate,String Search) throws Exception {
		List<LedgerDetailsWithId> ledgerDetails=journalDAO.getGeneralLedgerIdDetailsByDate(plant, fromDate, toDate,Search);
		return ledgerDetails;
	}
	


}
