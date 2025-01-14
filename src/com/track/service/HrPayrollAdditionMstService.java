package com.track.service;

import java.util.List;

import com.track.db.object.HrPayrollAdditionMst;

public interface HrPayrollAdditionMstService {
	
	public int addpayrolladditionmst(HrPayrollAdditionMst payrolladditionmst) throws Exception;
	
	public List<HrPayrollAdditionMst> getAllpayrolladditionmst(String plant) throws Exception;
	
	public HrPayrollAdditionMst getpayrolladditionmstById(String plant,int id) throws Exception;
	
	public int updatepayrolladditionmst(HrPayrollAdditionMst payrolladditionmst,String user) throws Exception;
	
	public boolean Deletepayrolladditionmst(String plant,int id) throws Exception;
	
	public boolean ispayrolladditionmst(String plant,String addname) throws Exception;
	
	public List<HrPayrollAdditionMst> payrolladditionmstbyname(String plant,String addname) throws Exception;
	
	public List<HrPayrollAdditionMst> payrolladditionmstdeduct(String plant,String addname) throws Exception;
	
	public List<HrPayrollAdditionMst> payrolladditionmstclaim(String plant,String addname) throws Exception;
	
	public List<HrPayrollAdditionMst> payrolladditionmstadd(String plant,String addname) throws Exception;
	
	public List<HrPayrollAdditionMst> payrolladditionmstall(String plant,String addname) throws Exception;

}
