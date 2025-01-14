package com.track.service;

import java.util.List;

import com.track.db.object.LedgerDetails;
import com.track.db.object.LedgerDetailsWithId;

public interface LedgerService {
	
	public List<LedgerDetails> getLedgerDetailsByAccount(String plant,String account,String fromDate,String toDate) throws Exception;
	
	public Double openingBalance(String plant,String account,String fromDate,String toDate) throws Exception;
	
	public List<LedgerDetails> getLedgerDetailsByDate(String plant,String fromDate,String toDate) throws Exception;
	
	public List<LedgerDetails> getGeneralLedgerDetailsByDate(String plant,String fromDate,String toDate,String Search) throws Exception;
	
	public List<LedgerDetails> getJournalLedgerDetailsByDate(String plant,String fromDate,String toDate,String limit) throws Exception;

	public List<LedgerDetails> getLedgerDetailsByTransactionId(String plant,String transactionId) throws Exception;
	
	public List<LedgerDetails> getLedgerDetailsByJournalId(String plant,String journalId) throws Exception;
	
	public List<LedgerDetailsWithId> getGeneralLedgerIdDetailsByDate(String plant,String fromDate,String toDate,String Search) throws Exception;
}

