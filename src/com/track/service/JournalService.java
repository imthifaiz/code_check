package com.track.service;	
import java.util.List;

import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.object.TrialBalanceDetails;	
public interface JournalService {	
		
	public int addJournal(Journal journal,String user) throws Exception;	
		
	public List<Journal> getAllJournalHeader(String plant,String user,String type) throws Exception;	
		
	public JournalHeader getJournalHeaderById(String plant,int id) throws Exception;	
		
	public int updateJournal(Journal journal,String user) throws Exception;	
		
	public boolean DeleteJournal(String plant,int id) throws Exception;	
		
	public Journal getJournalByTransactionId(String plant,String transid,String transtype) throws Exception;
	
	public Journal getJournalByTransactionId(String plant,String transid,String transtype, boolean isTransactionTypeLike) throws Exception;
		
	public List<Journal> getJournalsByDateWithoutAttach(String plant,String fromDate,String toDate) throws Exception;	
		
	public List<TrialBalanceDetails> getTrialBalanceDetailsByAccount(String plant,String account,String fromDate,String toDate)throws Exception;	
		
	public List<JournalDetail> getJournalDetailsByAccountTypeAndDATE(String plant,String fromDate,String toDate,String accounttypes) throws Exception;
	
	public List<Journal> getProjectJournalsByDateWithoutAttach(String plant,String fromDate,String toDate,String projId) throws Exception;
		
	public boolean DeleteJournal(String plant,String transId) throws Exception;	
		
	public List<TrialBalanceDetails> getTrialBalanceDetailsByAccountByType(String plant,String account,String fromDate,String toDate)throws Exception;	
		
	public List<JournalDetail> getJournalDetailsByChartOfAccountIdAndDATE(String plant,String fromDate,String toDate,String ChartAccountId) throws Exception;	
		
	public List<JournalDetail> getJournalDetailsByAccountDetailTypeAndDATE(String plant,String fromDate,String toDate,String accountdetailtypes) throws Exception;	
	
	public List<JournalDetail> getJournalDetailsByChartOfAccountIdAndAll(String plant,String ChartAccountId) throws Exception;	
	
	public List<JournalDetail> getJournalDetailsByAccountDetailTypeAndAll(String plant,String accountdetailtypes) throws Exception;
		
	public Journal getJournalByTransactionId(String plant,String transid) throws Exception;	
	
	public List<JournalDetail> getJournalDetailsByActTypeNoActDetAndDATE(String plant,String fromDate,String toDate,String accounttypes,String accountdetailtypes) throws Exception;
	
	public List<JournalDetail> getJournalDetailsBySalesRecipt(String plant,int sid) throws Exception;	
	
	public List<JournalDetail> getJournalDetailsBySalesBankin(String plant,int sid) throws Exception;	
	
	public List<JournalDetail> getJournalDetailsByHdrId(String plant,int hid) throws Exception;	

}	
