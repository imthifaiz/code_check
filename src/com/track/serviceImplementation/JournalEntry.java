package com.track.serviceImplementation;

import java.util.ArrayList;
import java.util.List;

import com.track.dao.JournalDAO;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.object.TrialBalanceDetails;
import com.track.service.JournalService;
@SuppressWarnings({"rawtypes", "unchecked"})
public class JournalEntry implements JournalService{
	
	private JournalDAO journalDAO=new JournalDAO();
	
	public int addJournal(Journal journal,String user) throws Exception
	{
		int headerId=journalDAO.addJournal(journal, user);
		return headerId;
	}
	
	public List<Journal> getAllJournalHeader(String plant,String user,String type) throws Exception
	{
		List journal=journalDAO.getAllJournals(plant, user, type);
		return journal;
	}

	@Override
	public JournalHeader getJournalHeaderById(String plant, int id) throws Exception {
		
		JournalHeader journalHead=journalDAO.getJournalHeaderById(plant, id);
		return journalHead;
	}

	public int updateJournal(Journal journal, String user) throws Exception {
		int headerId=journalDAO.updateJournal(journal, user);
		return headerId;
	}
	
	public Journal getJournalByTransactionId(String plant,String transid,String transtype) throws Exception
	{
		return getJournalByTransactionId(plant, transid, transtype, false);
	}
	
	@Override
	public Journal getJournalByTransactionId(String plant,String transid,String transtype, boolean isTransactionTypeLike) throws Exception
	{
		return journalDAO.getJournalByTransactionId(plant, transid,transtype,isTransactionTypeLike);
	}
	
	
	@Override
	public List<Journal> getJournalsByDateWithoutAttach(String plant, String fromDate, String toDate) throws Exception {
		List<Journal> journals=journalDAO.getJournalsByDateWithoutAttach(plant, fromDate, toDate);
		return journals;
	}

	
	@Override
	public List<TrialBalanceDetails> getTrialBalanceDetailsByAccount(String plant, String account, String fromDate,
			String toDate) throws Exception {
		account=account.trim();
		List<TrialBalanceDetails> trialBalanceDetails=journalDAO.getTrialBalanceDetailsByAccount(plant, account, fromDate, toDate);
		return trialBalanceDetails;
	}

	@Override
	public boolean DeleteJournal(String plant, int id) throws Exception {
		return journalDAO.deleteJournal(plant, id);
	}

	@Override
	public List<JournalDetail> getJournalDetailsByAccountTypeAndDATE(String plant, String fromDate, String toDate,String accounttypes)
			throws Exception {
		return journalDAO.getJournalDetailsByAccountTypeAndDATE(plant, fromDate, toDate,accounttypes);
	}

	@Override
	public boolean DeleteJournal(String plant, String transId) throws Exception {
		return journalDAO.DeleteJournal(plant, transId);
	}
	@Override
	public List<TrialBalanceDetails> getTrialBalanceDetailsByAccountByType(String plant, String account, String fromDate,
			String toDate) throws Exception {
		account=account.trim();
		/*List trialBalanceQueryTypeDetails=journalDAO.getTrialBalanceQueryType(plant, account, fromDate, toDate);
		String type = (String) trialBalanceQueryTypeDetails.get(0);
		List<TrialBalanceDetails> trialBalanceDetails=new ArrayList<TrialBalanceDetails>();
		if(type.equalsIgnoreCase("Main")) {
			trialBalanceDetails = journalDAO.getTrialBalanceDetailsByAccountByMainType(plant, account, fromDate, toDate);
		}else {
			trialBalanceDetails = journalDAO.getTrialBalanceDetailsByAccountBySubType(plant, account, fromDate, toDate);
		}*/
		List<TrialBalanceDetails> trialBalanceDetails=new ArrayList<TrialBalanceDetails>();
		trialBalanceDetails = journalDAO.getTrialBalanceDetailsByAccountByType(plant, account, fromDate, toDate);
		return trialBalanceDetails;
	}

	@Override
	public List<JournalDetail> getJournalDetailsByChartOfAccountIdAndDATE(String plant, String fromDate, String toDate,
			String ChartAccountId) throws Exception {
		return journalDAO.getJournalDetailsByChartOfAccountIdAndDATE(plant, fromDate, toDate,ChartAccountId);
	}

	@Override
	public List<JournalDetail> getJournalDetailsByAccountDetailTypeAndDATE(String plant, String fromDate, String toDate,
			String accountdetailtypes) throws Exception {
		return journalDAO.getJournalDetailsByAccountDetailTypeAndDATE(plant, fromDate, toDate,accountdetailtypes);
	}
	
	
	
	@Override
	public List<Journal> getProjectJournalsByDateWithoutAttach(String plant, String fromDate, String toDate,
			String projId) throws Exception {
		List<Journal> journals = journalDAO.getProjectJournalsByDateWithoutAttach(plant, fromDate, toDate, projId);
		return journals;
	}

	@Override
	public List<JournalDetail> getJournalDetailsByChartOfAccountIdAndAll(String plant, String ChartAccountId)
			throws Exception {
		return journalDAO.getJournalDetailsByChartOfAccountIdAndAll(plant,ChartAccountId);
	}

	@Override
	public List<JournalDetail> getJournalDetailsByAccountDetailTypeAndAll(String plant, String accountdetailtypes)
			throws Exception {
		return journalDAO.getJournalDetailsByAccountDetailTypeAndAll(plant,accountdetailtypes);
	}
	
	public Journal getJournalByTransactionId(String plant,String transid) throws Exception
	{
		Journal journal=journalDAO.getJournalByTransactionId(plant, transid);
		return journal;
	}
	
	@Override
	public List<JournalDetail> getJournalDetailsByActTypeNoActDetAndDATE(String plant, String fromDate, String toDate,String accounttypes,
			String accountdetailtypes) throws Exception {
		return journalDAO.getJournalDetailsByActTypeNoActDetAndDATE(plant, fromDate, toDate,accounttypes,accountdetailtypes);
	}
	
	@Override
	public List<JournalDetail> getJournalDetailsBySalesRecipt(String plant, int sid)
			throws Exception {
		return journalDAO.getJournalDetailsBySalesRecipt(plant,sid);
	}
	
	@Override
	public List<JournalDetail> getJournalDetailsBySalesBankin(String plant, int sid)
			throws Exception {
		return journalDAO.getJournalDetailsBySalesBankin(plant,sid);
	}
	
	
	@Override
	public List<JournalDetail> getJournalDetailsByHdrId(String plant, int id)
			throws Exception {
		return journalDAO.getJournalDetailsByHdrId(plant,id);
	}

}
